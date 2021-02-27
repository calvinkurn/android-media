package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.iconunify.IconUnify

class PostCarouselView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var carouselView: CarouselUnify
    private var likeButton: IconUnify
    private var commentButton: IconUnify
    private var shareButton: IconUnify

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_carousel_view, this, true)
        view.run {
            carouselView = findViewById(R.id.feed_carousel)
            likeButton = findViewById(R.id.like_button)
            commentButton = findViewById(R.id.comment_button)
            shareButton = findViewById(R.id.share_button)
        }
    }
}