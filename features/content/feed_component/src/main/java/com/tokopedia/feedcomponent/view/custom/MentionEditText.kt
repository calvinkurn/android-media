package com.tokopedia.feedcomponent.view.custom

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.MentionTextHelper
import com.tokopedia.feedcomponent.view.span.MentionSpan
import kotlin.math.max

/**
 * Created by jegul on 2019-08-05.
 */

//{@user_id|display name@}
class MentionEditText : AppCompatMultiAutoCompleteTextView {

    companion object {
        @ColorInt
        fun getMentionColor(context: Context): Int {
            return ContextCompat.getColor(context, R.color.Unify_G500)
        }
    }

    interface MentionEditTextListener {
        fun onTextChanged(spannedText: CharSequence, fullText: String)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var listener: MentionEditTextListener? = null

    val color = getMentionColor(context)

    private val onMentionClickedListener = object : MentionSpan.OnClickListener {
        override fun onClick(userId: String) {

        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            if (text == null) return
            removeTextChangedListener(this)

            val prevLength = length()
            val selEnd = selectionEnd
            val distanceFromEnd = prevLength - selEnd //calculate cursor distance from text end

            val spannedText = MentionTextHelper.spanText(text, color, onMentionClickedListener, true)
            val allMentionSpans = MentionTextHelper.getRenewedMentionSpans(spannedText).toList()
            val newText = MentionTextHelper.stripInvalidMentionFromText(spannedText, allMentionSpans)

            text.replace(0, text.length, newText)

            /**
             * Cursor positioning
             */
            val currentLength = length()
            if (currentLength < distanceFromEnd) setSelection(selEnd)
            else {
                val expectedCursorFromEnd = max(currentLength - distanceFromEnd, 0)
                setSelection(expectedCursorFromEnd) //move cursor to distance from text end to mimic previous cursor place
            }

            addTextChangedListener(this)

            listener?.onTextChanged(newText, MentionTextHelper.deSpanMentionTag(newText))
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        var start = selStart
        var end = selEnd

        val mentionSpanList = MentionTextHelper.getRenewedMentionSpans(text, selStart, selEnd)
        for (span in mentionSpanList) {
            val spanStart = text.getSpanStart(span)
            val spanEnd = text.getSpanEnd(span)

            if (selStart != selEnd) {
                if (selStart in (spanStart + 1) until spanEnd) {
                    start = spanStart
                }
                if (selEnd in (spanStart + 1) until spanEnd) {
                    end = spanEnd
                }
            } else if (spanStart <= selStart && spanEnd > selEnd) {
                start = spanStart
                end = spanEnd
            }
        }

        if (selStart != start || selEnd != end) {
            setSelection(start, end)
        }

        super.onSelectionChanged(selStart, selEnd)
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