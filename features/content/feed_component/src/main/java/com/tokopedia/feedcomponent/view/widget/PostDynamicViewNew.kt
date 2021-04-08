package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import java.net.URLEncoder

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

    fun bindHeader(author: FeedXAuthor, isFollowed: Boolean) {
        shopImage.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        shopName.text = author.name
        followText.showWithCondition(!isFollowed)
        //handle follow click listener here
        //handle 3 dots click listener here
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

    fun bindCaption(caption: FeedXCard) {
        val tagConverter = TagConverter()
        captionText.shouldShowWithAction(caption.text.isNotEmpty()) {
            if (caption.text.length > DynamicPostViewHolder.MAX_CHAR ||
                    hasSecondLine(caption.text)) {
                val captionEnd = if (findSubstringSecondLine(caption.text) < DynamicPostViewHolder.CAPTION_END)
                    findSubstringSecondLine(caption.text)
                else
                    DynamicPostViewHolder.CAPTION_END
                val captionTxt = caption.text.substring(0, captionEnd)
                        .replace("\n", "<br/>")
                        .replace(DynamicPostViewHolder.NEWLINE, "<br/>")
                        .plus("... ")
                        .plus("<font color='#42b549'><b>")
                        .plus(context.getString(R.string.feed_component_read_more_button))
                        .plus("</b></font>")

                captionText.text = tagConverter.convertToLinkifyHashtag(
                        SpannableString(MethodChecker.fromHtml(captionTxt)), colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag) }
                captionText.setOnClickListener {
                    if (caption.appLink.isNotEmpty()) {
//                        handle caption click here
                    } else {
                        captionText.text = tagConverter.convertToLinkifyHashtag(SpannableString(caption.text),
                                colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag) }
                    }
                }
                captionText.movementMethod = LinkMovementMethod.getInstance()
            } else {
                captionText.text = tagConverter
                        .convertToLinkifyHashtag(SpannableString(caption.text.replace(DynamicPostViewHolder.NEWLINE, " ")),
                                colorLinkHashtag) { hashtag -> onHashtagClicked(hashtag) }
                captionText.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private val colorLinkHashtag: Int
        get() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)

    private fun onHashtagClicked(hashtag: String) {
        val encodeHashtag = URLEncoder.encode(hashtag, "UTF-8")
        RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
    }

    private fun hasSecondLine(caption: String): Boolean {
        val firstIndex = caption.indexOf("\n", 0)
        return caption.indexOf("\n", firstIndex + 1) != -1
    }

    private fun findSubstringSecondLine(caption: String): Int {
        val firstIndex = caption.indexOf("\n", 0)
        return if (hasSecondLine(caption)) caption.indexOf("\n",
                firstIndex + 1) else caption.length
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

    fun bindPublishedAt(publishedAt: String, subTitle: String) {
        val avatarDate = TimeConverter.generateTime(context, publishedAt)
        val spannableString: SpannableString =
                if (subTitle.isNotEmpty()) {
                    SpannableString(String.format(
                            context.getString(R.string.feed_header_time_format),
                            avatarDate,
                            subTitle))
                } else {
                    SpannableString(avatarDate)
                }
        timestampText.text = spannableString
    }

}