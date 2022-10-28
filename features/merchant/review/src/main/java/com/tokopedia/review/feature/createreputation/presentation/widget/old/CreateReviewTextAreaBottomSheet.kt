package com.tokopedia.review.feature.createreputation.presentation.widget.old

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class CreateReviewTextAreaBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(
            textAreaListener: TextAreaListener, text: String
        ): CreateReviewTextAreaBottomSheet {
            return CreateReviewTextAreaBottomSheet().apply {
                this.text = text
                this.textAreaListener = textAreaListener
            }
        }
    }

    private var text: String = ""
    private var textAreaListener: TextAreaListener? = null

    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        clearContentPadding = true
        context?.let {
            val view = View.inflate(
                context,
                R.layout.old_widget_create_review_text_area_bottom_sheet,
                null
            )
            editText = view.findViewById(R.id.createReviewBottomSheetEditText)
            editText?.apply {
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
            }
            setChild(view)
            showCloseIcon = false
            isFullpage = true
            setAction(ContextCompat.getDrawable(it, R.drawable.ic_collapse)) {
                textAreaListener?.onCollapseButtonClicked(editText?.text.toString())
            }
            setOnDismissListener {
                editText?.onFocusChangeListener = null
                textAreaListener?.onDismissBottomSheet(editText?.text.toString())
            }
            isKeyboardOverlap = false
            setShowListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    editText?.requestFocus()
                }, 100)
                editText?.setText(this@CreateReviewTextAreaBottomSheet.text)
            }
        }
        super.onCreate(savedInstanceState)
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
}