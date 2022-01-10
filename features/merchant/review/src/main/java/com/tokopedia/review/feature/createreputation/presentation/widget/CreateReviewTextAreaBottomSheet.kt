package com.tokopedia.review.feature.createreputation.presentation.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.adapter.ReviewTemplatesAdapter
import com.tokopedia.review.feature.createreputation.presentation.bottomsheet.CreateReviewBottomSheet
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.listener.ReviewTemplateListener
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class CreateReviewTextAreaBottomSheet : BottomSheetUnify(), ReviewTemplateListener {

    companion object {
        fun createNewInstance(
            textAreaListener: TextAreaListener,
            text: String,
            incentiveHelper: String = "",
            hasIncentive: Boolean = false,
            hasOngoingChallenge: Boolean = false,
            templates: List<String> = listOf(),
            placeholder: String = ""
        ): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
                this.incentiveHelper = incentiveHelper
                this.hasIncentive = hasIncentive
                this.hasOngoingChallenge = hasOngoingChallenge
                this.templates = templates
                this.placeholder = placeholder
            }
        }
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null
    private var incentiveHelper = ""
    private var hasIncentive = false
    private var hasOngoingChallenge = false
    private var templates: List<String> = listOf()
    private var placeholder: String = ""

    private var editText: EditText? = null
    private var incentiveHelperText: Typography? = null
    private var templatesRecyclerView: RecyclerView? = null
    private val templatesAdapter: ReviewTemplatesAdapter by lazy {
        ReviewTemplatesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        clearContentPadding = true
        context?.let {
            val view =
                View.inflate(context, R.layout.widget_create_review_text_area_bottom_sheet, null)
            editText = view.findViewById(R.id.createReviewBottomSheetEditText)
            incentiveHelperText = view.findViewById(R.id.incentiveHelperTypography)
            templatesRecyclerView =
                view.findViewById(R.id.review_text_area_bottomsheet_templates_rv)
            editText?.apply {
                hint = placeholder
                setOnFocusChangeListener { _, hasFocus ->
                    activity?.run {
                        if (hasFocus) {
                            editText?.setSelection(editText?.text?.length ?: 0)
                            showKeyboard()
                        } else {
                            hideKeyboard()
                        }
                    }
                }
                setBackgroundColor(Color.TRANSPARENT)
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // No Op
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // No Op
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val textLength = s?.length ?: 0
                        incentiveHelper = when {
                            textLength >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                                if (hasIncentive) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_eligible_for_incentive) ?: ""
                                } else if (hasOngoingChallenge) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_eligible_for_challenge) ?: ""
                                } else {
                                    ""
                                }
                            }
                            textLength < CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && textLength != 0 -> {
                                if (hasIncentive) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_partial_incentive) ?: ""
                                } else if (hasOngoingChallenge) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_partial_challenge) ?: ""
                                } else {
                                    ""
                                }
                            }
                            else -> {
                                if (hasIncentive) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_empty_incentive) ?: ""
                                } else if (hasOngoingChallenge) {
                                    context?.getString(R.string.review_create_bottom_sheet_text_area_empty_challenge) ?: ""
                                } else {
                                    ""
                                }
                            }
                        }
                        incentiveHelperText?.text = incentiveHelper
                    }

                })
            }
            if (hasIncentive || hasOngoingChallenge) {
                incentiveHelperText?.apply {
                    text = incentiveHelper
                    show()
                }
            }
            setChild(view)
            showCloseIcon = false
            isFullpage = true
            setAction(ContextCompat.getDrawable(it, R.drawable.ic_collapse)) {
                textAreaListener?.onCollapseButtonClicked(editText?.text.toString())
            }
            setOnDismissListener {
                editText?.onFocusChangeListener = null
                textAreaListener?.onDismissBottomSheet(
                    editText?.text.toString(),
                    templatesAdapter.getTemplates()
                )
            }
            isKeyboardOverlap = false
            setShowListener {
                Handler().postDelayed({
                    editText?.requestFocus()
                }, 100)
                editText?.setText(this@CreateReviewTextAreaBottomSheet.text)
            }
        }
        setTemplates()
        super.onCreate(savedInstanceState)
    }

    override fun onTemplateSelected(template: String) {
        editText?.append(
            context?.getString(R.string.review_form_templates_formatting, template)
                ?: template
        )
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun Context.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun setTemplates() {
        if (templates.isEmpty() || hasIncentive) {
            hideTemplates()
            return
        }
        templatesRecyclerView?.apply {
            adapter = templatesAdapter
            layoutManager = StaggeredGridLayoutManager(
                CreateReviewBottomSheet.TEMPLATES_ROW_COUNT,
                RecyclerView.HORIZONTAL
            )
            showTemplates()
        }
        templatesAdapter.setData(templates)
    }

    private fun showTemplates() {
        templatesRecyclerView?.show()
    }

    private fun hideTemplates() {
        templatesRecyclerView?.show()
    }
}