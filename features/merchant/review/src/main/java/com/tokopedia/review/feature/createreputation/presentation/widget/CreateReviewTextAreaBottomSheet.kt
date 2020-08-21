package com.tokopedia.review.feature.createreputation.presentation.widget

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.listener.TextAreaListener
import com.tokopedia.unifycomponents.BottomSheetUnify

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
            val editText = EditText(it)
            editText.setBackgroundColor(Color.TRANSPARENT)
            setChild(editText)
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
                Handler().postDelayed( {
                    editText.requestFocus()
                }, 100)
                this.dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                editText.setText(this@CreateReviewTextAreaBottomSheet.text)
            }
        }
    }
}