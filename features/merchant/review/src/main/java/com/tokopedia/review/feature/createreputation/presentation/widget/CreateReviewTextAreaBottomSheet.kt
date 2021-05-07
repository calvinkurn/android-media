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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(textAreaListener: TextAreaListener, text: String, incentiveHelper: String = "", isUserEligible: Boolean = false): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
                this.incentiveHelper = incentiveHelper
                this.isUserEligible = isUserEligible
            }
        }
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null
    private var incentiveHelper = ""
    private var isUserEligible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        clearContentPadding = true
        context?.let {
            val view = View.inflate(context, R.layout.widget_create_review_text_area_bottom_sheet, null)
            val editText: EditText = view.findViewById(R.id.createReviewBottomSheetEditText)
            val incentiveHelperText: Typography = view.findViewById(R.id.incentiveHelperTypography)
            editText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    activity?.run {
                        if (hasFocus) {
                            editText.setSelection(editText.text.length)
                            showKeyboard()
                        } else {
                            hideKeyboard()
                        }
                    }
                }
                setBackgroundColor(Color.TRANSPARENT)
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // No Op
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // No Op
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if(!isUserEligible) {
                            return
                        }
                        val textLength = s?.length ?: 0
                        with(incentiveHelperText) {
                            incentiveHelper = when {
                                textLength >= CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD -> {
                                    context?.getString(R.string.review_create_text_area_eligible) ?: ""
                                }
                                textLength < CreateReviewFragment.REVIEW_INCENTIVE_MINIMUM_THRESHOLD && textLength != 0 -> {
                                    context?.getString(R.string.review_create_text_area_partial) ?: ""
                                }
                                else -> {
                                    context?.getString(R.string.review_create_text_area_empty) ?: ""
                                }
                            }
                            text = incentiveHelper
                            show()
                        }
                    }

                })
            }
            incentiveHelperText.text = this.incentiveHelper
            setChild(view)
            showCloseIcon = false
            isFullpage = true
            setAction(ContextCompat.getDrawable(it, R.drawable.ic_collapse)) {
                textAreaListener?.onCollapseButtonClicked(editText.text.toString())
            }
            setOnDismissListener {
                editText.onFocusChangeListener = null
                textAreaListener?.onDismissBottomSheet(editText.text.toString())
            }
            isKeyboardOverlap = false
            setShowListener {
                Handler().postDelayed({
                    editText.requestFocus()
                }, 100)
                editText.setText(this@CreateReviewTextAreaBottomSheet.text)
            }
        }
        super.onCreate(savedInstanceState)
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}