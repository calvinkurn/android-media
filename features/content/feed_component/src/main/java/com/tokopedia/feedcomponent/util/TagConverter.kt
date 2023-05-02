package com.tokopedia.feedcomponent.util

import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import java.util.regex.Pattern

class TagConverter {

    private val tagPattern = Pattern.compile(HASHTAG_PATTERN)

    fun convertToLinkifyHashtag(spanString: SpannableString, hashtagColor: Int, onClick: (String)->Unit): SpannableString{
        val matcher = tagPattern.matcher(spanString)
        while (matcher.find()){
            val text = matcher.group()

            spanString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClick.invoke(text)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    ds.color = hashtagColor
                }
            }, matcher.start(), matcher.end(), 0)
        }
        return spanString
    }

    companion object{
        private const val HASHTAG_PATTERN = "[#]+[A-Za-z0-9-_]+\\b"
    }
}
