package com.tokopedia.feedcomponent.view.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.MultiAutoCompleteTextView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.MentionTextHelper

/**
 * Created by jegul on 2019-08-05.
 */

//{@user_id|display name@}
class MentionEditText : MultiAutoCompleteTextView {

    interface MentionEditTextListener {
        fun onTextChanged(spannedText: CharSequence, fullText: String)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var listener: MentionEditTextListener? = null

    val color = ContextCompat.getColor(context, R.color.Green_G500)

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            if (text == null) return
            removeTextChangedListener(this)

            val distanceFromEnd = length() - selectionEnd //calculate cursor distance from text end

            val spannedText = MentionTextHelper.spanText(text, color)
            val allMentionSpan = MentionTextHelper.getAllMentionSpansFromText(spannedText).toList()
            val newText = MentionTextHelper.stripInvalidMentionFromText(spannedText, allMentionSpan)

            text.replace(0, text.length, newText)

            setSelection(length() - distanceFromEnd) //move cursor to distance from text end to mimic previous cursor place

            addTextChangedListener(this)

            listener?.onTextChanged(newText, MentionTextHelper.deSpanMentionTag(newText))
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    init {
        threshold = 3
        addTextChangedListener(textWatcher)
        setTokenizer(MentionTokenizer())
    }

    fun setListener(listener: MentionEditTextListener) {
        this.listener = listener
    }

    fun getRawText(): String {
        return MentionTextHelper.deSpanMentionTag(text)
    }
}