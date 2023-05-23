package com.tokopedia.review.feature.reviewreminder.view.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.UnifyButton

class EditMessageBottomSheet(
        private val onSave: (String) -> Unit
) : BottomSheetUnify() {

    companion object {
        const val TAG_BUYER = "@Nama Pembeli"
        const val MAX_LENGTH = 200
    }

    var message: String? = null

    private var isAddBuyerEnabled = true

    private var textAreaEditMessage: TextAreaUnify? = null
    private var textAreaInput: EditText? = null
    private var layoutAddBuyer: LinearLayout? = null
    private var buttonSave: UnifyButton? = null

    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        isKeyboardOverlap = false
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(View.inflate(context, R.layout.bottom_sheet_review_reminder_edit_message, null))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textAreaEditMessage = view.findViewById(R.id.textarea_edit_message)
        layoutAddBuyer = view.findViewById(R.id.layout_add_buyer)
        buttonSave = view.findViewById(R.id.button_save)

        textAreaInput = textAreaEditMessage?.textAreaInput

        initView()
        setupViewInteraction()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        setTitle(getString(R.string.review_reminder_edit_message))
    }

    private fun setupViewInteraction() {

        initTextWatcher()

        textAreaInput?.apply {
            addTextChangedListener(textWatcher)
            setText(message ?: "")
        }

        layoutAddBuyer?.setOnClickListener {
            if (isAddBuyerEnabled) {
                textAreaInput?.apply {
                    text.replace(selectionStart, selectionEnd, TAG_BUYER)
                }
            }
        }

        buttonSave?.setOnClickListener {
            textAreaInput?.text?.let {
                onSave(it.toString())
            }
            dismiss()
        }
    }

    private fun enableAddLayout(isEnabled: Boolean) {
        val childCount = layoutAddBuyer?.childCount ?: 0
        for (i in 0 until childCount) {
            layoutAddBuyer?.getChildAt(i)?.apply {
                this.isEnabled = isEnabled
            }
        }
        isAddBuyerEnabled = isEnabled
    }

    private fun initTextWatcher() {
        textWatcher = object : TextWatcher {
            private var currentTagIndex = -1
            private var haveSpan = true

            private val greenSpan = ForegroundColorSpan(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500))
            private val errorMessage = getString(R.string.review_reminder_bottom_sheet_edit_message_error_message)

            override fun afterTextChanged(p0: Editable?) {
                val tagIndex = p0?.indexOf(TAG_BUYER) ?: -1
                if (currentTagIndex != tagIndex && tagIndex != -1) {
                    p0?.setSpan(greenSpan, tagIndex, tagIndex + TAG_BUYER.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    currentTagIndex = tagIndex
                    haveSpan = true
                    enableAddLayout(false)
                }
                if (haveSpan && tagIndex == -1) {
                    clearSpans(p0)
                    p0?.removeSpan(greenSpan)
                    currentTagIndex = tagIndex
                    haveSpan = false
                    enableAddLayout(true)
                }

                val isMaxLength = p0?.length == MAX_LENGTH

                if (isMaxLength) {
                    textAreaEditMessage?.textAreaMessage = errorMessage
                    textAreaEditMessage?.isError = true
                } else {
                    textAreaEditMessage?.textAreaMessage = ""
                    textAreaEditMessage?.isError = false
                }

                buttonSave?.isEnabled = !p0.isNullOrBlank() && !isMaxLength
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            private fun clearSpans(editable: Editable?) {
                editable?.getSpans(0, editable.length, ForegroundColorSpan::class.java)?.let {
                    for (span in it) {
                        editable.removeSpan(span)
                    }
                }
            }
        }
    }
}