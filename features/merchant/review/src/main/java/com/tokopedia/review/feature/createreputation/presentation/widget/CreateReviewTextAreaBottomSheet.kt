package com.tokopedia.review.feature.createreputation.presentation.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(textAreaListener: TextAreaListener, text: String, incentiveHelper: String): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
                this.incentiveHelper = incentiveHelper
            }
        }
        const val ORIGINAL_UNIFY_MARGIN = 16
        const val HEADER_BOTTOM_MARGIN = 8
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null
    private var incentiveHelper = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        clearContentPadding = true
        context?.let {
            val view = View.inflate(context, R.layout.widget_create_review_text_area_bottom_sheet, null)
            val editText: EditText = view.findViewById(R.id.createReviewBottomSheetEditText)
            editText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    activity?.run {
                        if (hasFocus) {
                            showKeyboard()
                        } else {
                            hideKeyboard()
                        }
                    }
                }
                setBackgroundColor(Color.TRANSPARENT)
            }
            val incentiveHelper: Typography = view.findViewById(R.id.incentiveHelperTypography)
            incentiveHelper.text = this.incentiveHelper
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
                (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(ORIGINAL_UNIFY_MARGIN, ORIGINAL_UNIFY_MARGIN, ORIGINAL_UNIFY_MARGIN, HEADER_BOTTOM_MARGIN)
                Handler().postDelayed( {
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