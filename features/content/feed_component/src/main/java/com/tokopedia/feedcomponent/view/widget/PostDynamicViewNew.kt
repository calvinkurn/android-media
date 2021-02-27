package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class PostDynamicViewNew @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    private var shopImage: ImageUnify
    private var shopBadge: ImageUnify
    private var shopName: Typography
    private var shopMenuIcon: IconUnify
    private var carouselView: CarouselUnify
    private var likeButton: IconUnify
    private var commentButton: IconUnify
    private var shareButton: IconUnify
    private var likedText: Typography
    private var captionText: Typography
    private var timestampText: Typography
    private var commentText1: Typography
    private var commentText2: Typography
    private var likeButton1: IconUnify
    private var likeButton2: IconUnify
    private var seeAllCommentText: Typography
    private var userImage: ImageUnify
    private var addCommentHint: Typography

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_dynamic_new, this, true)
        view.run {
            shopImage = findViewById(R.id.shop_image)
            shopBadge = findViewById(R.id.shop_badge)
            shopName = findViewById(R.id.shop_name)
            shopMenuIcon = findViewById(R.id.menu_button)
            carouselView = findViewById(R.id.feed_carousel)
            likeButton = findViewById(R.id.like_button)
            commentButton = findViewById(R.id.comment_button)
            shareButton = findViewById(R.id.share_button)
            likedText = findViewById(R.id.liked_text)
            captionText = findViewById(R.id.caption_text)
            timestampText = findViewById(R.id.timestamp_text)
            commentText1 = findViewById(R.id.comment_text1)
            commentText2 = findViewById(R.id.comment_text2)
            seeAllCommentText = findViewById(R.id.see_all_comment_text)
            likeButton1 = findViewById(R.id.like_button1)
            likeButton2 = findViewById(R.id.like_button2)
            userImage = findViewById(R.id.user_image)
            addCommentHint = findViewById(R.id.comment_hint)
        }
    }
}