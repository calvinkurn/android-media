package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifyprinciples.Typography

class PostCaptionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var likedText: Typography
    private var captionText: Typography
    private var timestampText: Typography

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_caption, this, true)
        view.run {
            likedText = findViewById(R.id.liked_text)
            captionText = findViewById(R.id.caption_text)
            timestampText = findViewById(R.id.timestamp_text)
        }
    }
}
