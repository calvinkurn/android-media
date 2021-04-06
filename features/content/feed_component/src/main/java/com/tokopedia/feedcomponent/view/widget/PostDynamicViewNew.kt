package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXAuthor
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXComments
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXLike
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography

private const val TYPE_IMAGE = "image"

class PostDynamicViewNew @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var shopImage: ImageUnify
    private var shopBadge: ImageUnify
    private var shopName: Typography
    private var shopNameSeparator: Typography
    private var followText: Typography
    private var shopMenuIcon: IconUnify
    private var carouselView: CarouselUnify
    private var pageControl: PageControl
    private var likeButton: IconUnify
    private var commentButton: IconUnify
    private var shareButton: IconUnify
    private var likedText: Typography
    private var captionText: Typography
    private var timestampText: Typography
    private var commentUserImage1: ImageUnify
    private var commentUserImage2: ImageUnify
    private var commentText1: Typography
    private var commentText2: Typography
    private var likeButton1: IconUnify
    private var likeButton2: IconUnify
    private var seeAllCommentText: Typography
    private var userImage: ImageUnify
    private var addCommentHint: Typography

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_dynamic_new_content, this, true)
        view.run {
            shopImage = findViewById(R.id.shop_image)
            shopBadge = findViewById(R.id.shop_badge)
            shopName = findViewById(R.id.shop_name)
            shopNameSeparator = findViewById(R.id.shop_name_separator)
            followText = findViewById(R.id.follow_text)
            shopMenuIcon = findViewById(R.id.menu_button)
            carouselView = findViewById(R.id.feed_carousel)
            pageControl = findViewById(R.id.page_indicator)
            likeButton = findViewById(R.id.like_button)
            commentButton = findViewById(R.id.comment_button)
            shareButton = findViewById(R.id.share_button)
            likedText = findViewById(R.id.liked_text)
            captionText = findViewById(R.id.caption_text)
            timestampText = findViewById(R.id.timestamp_text)
            seeAllCommentText = findViewById(R.id.see_all_comment_text)
            commentUserImage1 = findViewById(R.id.comment_user_image1)
            commentUserImage2 = findViewById(R.id.comment_user_image2)
            commentText1 = findViewById(R.id.comment_text1)
            commentText2 = findViewById(R.id.comment_text2)
            likeButton1 = findViewById(R.id.like_button1)
            likeButton2 = findViewById(R.id.like_button2)
            userImage = findViewById(R.id.user_image)
            addCommentHint = findViewById(R.id.comment_hint)
        }
    }

    fun bindHeader(author: FeedXAuthor) {
        shopImage.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        shopName.text = author.name
        //handle 3 dots click listener
    }

    fun bindLike(like: FeedXLike) {
        if (like.isLiked) {
            likeButton.setImage(IconUnify.THUMB_FILLED)
        } else {
            likeButton.setImage(IconUnify.THUMB)
        }
        if (like.likedBy.isNotEmpty() || like.count != 0) {
            likedText.show()
            if (like.likedBy.isEmpty()) {
                likedText.text = context.getString(R.string.feed_component_liked_count_text, like.countFmt)
            } else {
                likedText.text = context.getString(R.string.feed_component_liked_by_text, getLikedByText(like.likedBy), like.countFmt)
            }
        } else {
            likedText.hide()
        }
        //handle like click listener
    }

    private fun getLikedByText(likedBy: List<String>): String {
        var text = ""
        likedBy.forEachIndexed { index, str ->
            if (index == 0) {
                text += str
            } else {
                text = "$text,$str"
            }
        }
        return text
    }

    fun bindComment(comments: FeedXComments, profilePicture: String, name: String) {
        seeAllCommentText.showWithCondition(comments.count != 0)
        seeAllCommentText.text = context.getString(R.string.feed_component_see_all_comments, comments.countFmt)
        comments.commentItems.firstOrNull()?.let {
            commentUserImage1.setImageUrl(it.author.badgeURL)
            commentUserImage1.showWithCondition(it.author.badgeURL.isNotEmpty())
            commentText1.text = it.text
            commentText1.show()
            likeButton1.show()
        } ?: run {
            commentText1.hide()
            commentUserImage1.hide()
            likeButton1.hide()
        }
        comments.commentItems.getOrNull(1)?.let {
            commentUserImage2.setImageUrl(it.author.badgeURL)
            commentUserImage2.showWithCondition(it.author.badgeURL.isNotEmpty())
            commentText2.text = it.text
            commentText2.show()
            likeButton2.show()
        } ?: run {
            commentText2.hide()
            commentUserImage2.hide()
            likeButton2.hide()
        }
        userImage.setImageUrl(profilePicture)
        addCommentHint.hint = context.getString(R.string.feed_component_add_comment, name)
        //find out how to show whether the comment is liked or not and handle click event for see all comment
    }

    fun bindItems(media: List<FeedXMedia>) {
        carouselView.apply {
            stage.removeAllViews()
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            if (media.size > 1) {
                pageControl.show()
                pageControl.setIndicator(media.size)
            } else {
                pageControl.hide()
            }
            media.forEach {
                if (it.type == TYPE_IMAGE) {
                    val imageItem = View.inflate(context, R.layout.item_post_image_new, null)
                    val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    imageItem.layoutParams = param
                    imageItem.run {
                        findViewById<ImageUnify>(R.id.post_image).setImageUrl(it.mediaUrl)
                        findViewById<CardView>(R.id.product_tagging_parent).showWithCondition(it.tagging.isNotEmpty())
                        //set on click listener for the image item
                    }
                    addItem(imageItem)
                }
                //write else part for the video type
            }
            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    pageControl.setCurrentIndicator(current)
                }
            }
        }
    }


}