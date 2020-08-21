package com.tokopedia.review.feature.createreputation.presentation.widget

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_create_review_expanded_text_area_bottomsheet.*

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(textAreaListener: TextAreaListener, text: String): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
            }
        }
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val view = View.inflate(it, R.layout.widget_create_review_expanded_text_area_bottomsheet,null)
            setChild(view)
            showCloseIcon = false
            isFullpage = true
            setAction(ContextCompat.getDrawable(it, R.drawable.ic_collapse)) {
                textAreaListener?.onCollapseButtonClicked(reviewCreateTextAreaBottomSheet.text.toString())
            }
            setOnDismissListener {
                reviewCreateTextAreaBottomSheet.onFocusChangeListener = null
                textAreaListener?.onDismissBottomSheet(reviewCreateTextAreaBottomSheet.text.toString())
            }
            isKeyboardOverlap = false
            setShowListener {
                Handler().postDelayed( {
                    reviewCreateTextAreaBottomSheet.requestFocus()
                }, 100)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewCreateTextAreaBottomSheet.apply {
            setOnFocusChangeListener { _, hasFocus ->
                activity?.run {
                    if (hasFocus) {
                        showKeyboard()
                    } else {
                        hideKeyboard()
                    }
                }
            }
            setText(this@CreateReviewTextAreaBottomSheet.text)
        }
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