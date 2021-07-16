package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_post_image_new.view.*
import kotlinx.android.synthetic.main.item_post_video_new.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URLEncoder


private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT: String = "FeedXCardProductsHighlight"
private const val SPAN_SIZE_FULL = 6
private const val SPAN_SIZE_HALF = 3
private const val SPAN_SIZE_SINGLE = 2
private const val MAX_FEED_SIZE = 6
private const val MAX_FEED_SIZE_SMALL = 3
private const val LAST_FEED_POSITION = 5
private const val LAST_FEED_POSITION_SMALL = 2
private val scope = CoroutineScope(Dispatchers.Main)
private val scopeDef = CoroutineScope(Dispatchers.Default)
private var productVideoJob: Job? = null
private const val TIMER_TO_BE_SHOWN = 3000L
private const val TIME_SECOND = 1000L

class PostDynamicViewNew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private var shopImage: ImageUnify
    private var shopBadge: ImageUnify
    private var shopName: Typography
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
    private var followCount: Typography
    private var gridList: RecyclerView
    private var listener: DynamicPostViewHolder.DynamicPostListener? = null
    private var videoListener: VideoViewHolder.VideoViewListener? = null
    private lateinit var gridPostListener: GridPostAdapter.GridItemListener
    private lateinit var imagePostListener: ImagePostViewHolder.ImagePostListener
    private var positionInFeed: Int = 0
    var isMute = true
    private var videoPlayer: FeedExoPlayer? = null
    var totalRunnTime = 0L

    init {
        (context as LifecycleOwner).lifecycle.addObserver(this)
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_post_dynamic_new_content, this, true)
        view.run {
            shopImage = findViewById(R.id.shop_image)
            shopBadge = findViewById(R.id.shop_badge)
            shopName = findViewById(R.id.shop_name)
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
            gridList = findViewById(R.id.gridList)
            followCount = findViewById(R.id.follow_count)
        }
    }

    fun bindData(
        dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
        gridItemListener: GridPostAdapter.GridItemListener,
        videoListener: VideoViewHolder.VideoViewListener,
        adapterPosition: Int,
        userSession: UserSessionInterface,
        feedXCard: FeedXCard,
        imagePostListener: ImagePostViewHolder.ImagePostListener
    ) {
        this.listener = dynamicPostListener
        this.gridPostListener = gridItemListener
        this.videoListener = videoListener
        this.positionInFeed = adapterPosition
        this.imagePostListener = imagePostListener
        bindFollow(feedXCard)
        bindItems(feedXCard)
        bindCaption(feedXCard)
        bindPublishedAt(feedXCard.publishedAt, feedXCard.subTitle)
        bindLike(feedXCard)
        bindComment(
            feedXCard.comments,
            userSession.profilePicture,
            userSession.name,
            feedXCard.id.toIntOrZero(),
            feedXCard.author.type,
            feedXCard.author.id,
            feedXCard.typename,
            feedXCard.followers.isFollowed,
            feedXCard.media.firstOrNull()?.type ?: ""
        )
        bindTracking(feedXCard)
        shareButton.setOnClickListener {
            val desc = context.getString(R.string.feed_share_default_text)
            listener?.onShareClick(
                positionInFeed,
                feedXCard.id.toIntOrZero(),
                feedXCard.author.name + " `post",
                desc.replace("%s", feedXCard.author.name),
                feedXCard.appLink,
                feedXCard.media.firstOrNull()?.mediaUrl ?: "",
                feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                feedXCard.author.id,
                isVideo(feedXCard.media.firstOrNull())
            )
        }
    }

    private fun bindTracking(feedXCard: FeedXCard) {
        if (feedXCard.typename == TYPE_FEED_X_CARD_POST) {
            addOnImpressionListener(feedXCard.impressHolder) {
                listener?.onImpressionTracking(feedXCard, positionInFeed)
            }
        }
    }

    fun bindLike(feedXCard: FeedXCard) {
        bindLike(
            feedXCard.like,
            feedXCard.id.toIntOrZero(),
            feedXCard.typename,
            feedXCard.followers.isFollowed,
            feedXCard.author.id,
            isVideo(feedXCard.media.firstOrNull())
        )
    }

    fun bindFollow(feedXCard: FeedXCard) {
        bindHeader(
            feedXCard.id.toIntOrZero(),
            feedXCard.author,
            feedXCard.reportable,
            feedXCard.deletable,
            feedXCard.followers,
            feedXCard.typename,
            feedXCard.media.firstOrNull()?.type ?: ""
        )
    }

    private fun bindHeader(
        activityId: Int,
        author: FeedXAuthor,
        reportable: Boolean,
        deletable: Boolean,
        followers: FeedXFollowers,
        type: String,
        mediaType: String
    ) {
        val isFollowed = followers.isFollowed
        val count = followers.count
        val isVideo = mediaType != TYPE_IMAGE

        if (count >= 100) {
            followCount.text =
                String.format(
                    context.getString(R.string.feed_header_follow_count_text),
                    count.productThousandFormatted()
                )
        } else {
            followCount.text =
                context.getString(R.string.feed_header_follow_count_less_text)
        }
        followCount.showWithCondition(!isFollowed || followers.transitionFollow)
        shopImage.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        val activityName = ""
        val authorType = if (author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val followCta =
            FollowCta(authorID = author.id, authorType = authorType, isFollow = isFollowed)
        val authorName = MethodChecker.fromHtml(author.name)
        val startIndex = authorName.length + 2
        var endIndex = startIndex + 7

        val text = if (followers.transitionFollow) {
            endIndex += 3
            context.getString(R.string.kol_Action_following_color)
        } else {
            context.getString(
                R.string.feed_component_follow
            )
        }
        val textToShow = MethodChecker.fromHtml(
            context.getString(R.string.feed_header_separator) + text
        )
        val spannableString = SpannableStringBuilder("")
        spannableString.append(authorName)
        if (!isFollowed || followers.transitionFollow) {
            spannableString.append(" $textToShow")
        }

        val cs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.onAvatarClick(
                    positionInFeed,
                    author.appLink,
                    activityId,
                    activityName,
                    followCta,
                    type,
                    isFollowed,
                    author.id,
                    isVideo,
                    false

                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Neutral_N600
                )
            }
        }

        if (startIndex < spannableString.length && endIndex <= spannableString.length) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    listener?.onHeaderActionClick(
                        positionInFeed, author.id,
                        authorType, isFollowed, type, isVideo
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    if (endIndex == startIndex + 7) {
                        ds.color = MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    } else {
                        ds.color = MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    }
                }

            }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        spannableString.setSpan(cs, 0, authorName.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        shopName.text = spannableString
        shopName.movementMethod = LinkMovementMethod.getInstance()
        followers.transitionFollow = false
        shopImage.setOnClickListener {
            listener?.onAvatarClick(
                positionInFeed,
                author.appLink,
                activityId,
                activityName,
                followCta,
                type,
                isFollowed,
                author.id,
                isVideo,
                false
            )
        }
        shopMenuIcon.setOnClickListener {
            listener?.onMenuClick(
                positionInFeed,
                activityId,
                reportable,
                deletable,
                true,
                isFollowed,
                author.id,
                authorType,
                type,
                isVideo
            )
        }
    }

    private fun bindLike(
        like: FeedXLike,
        id: Int,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        isVideo: Boolean
    ) {
        if (like.isLiked) {
            val colorGreen =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (like.likedBy.isNotEmpty() || like.count != 0) {
            likedText.show()
            if (like.likedBy.isEmpty()) {
                if (like.isLiked) {
                    if (like.count == 1) {
                        likedText.text =
                            context.getString(R.string.feed_component_liked_count_text_only_me)
                    } else
                        likedText.text =
                            MethodChecker.fromHtml(
                                context.getString(
                                    R.string.feed_component_liked_by_text_me,
                                    (like.count - 1).productThousandFormatted(1)
                                )
                            )
                } else
                    likedText.text =
                        MethodChecker.fromHtml(
                            context.getString(
                                R.string.feed_component_liked_count_text,
                                like.count.productThousandFormatted(1)
                            )
                        )
            } else {
                likedText.text = MethodChecker.fromHtml(
                    context.getString(
                        R.string.feed_component_liked_by_text,
                        getLikedByText(like.likedBy),
                        like.count.productThousandFormatted(1)
                    )
                )
            }
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            listener?.onLikeClick(
                positionInFeed,
                id,
                like.isLiked,
                type,
                isFollowed,
                shopId = shopId,
                isVideo = isVideo
            )
        }
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

    private fun bindCaption(caption: FeedXCard) {
        val tagConverter = TagConverter()
        var spannableString: SpannableString
        val authorType =
            if (caption.author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val followCta =
            FollowCta(
                authorID = caption.author.id,
                authorType = authorType,
                isFollow = caption.followers.isFollowed
            )
        val cs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.onAvatarClick(
                    positionInFeed,
                    caption.author.appLink,
                    caption.id.toIntOrZero(),
                    "",
                    followCta,
                    caption.typename,
                    caption.followers.isFollowed,
                    caption.author.id,
                    isVideo(caption.media.firstOrNull()),
                    true
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Neutral_N600
                )
            }
        }
        captionText.shouldShowWithAction(caption.text.isNotEmpty()) {
            if (caption.text.length > DynamicPostViewHolder.MAX_CHAR ||
                hasSecondLine(caption.text)
            ) {
                val captionEnd =
                    if (findSubstringSecondLine(caption.text) < DynamicPostViewHolder.CAPTION_END)
                        findSubstringSecondLine(caption.text)
                    else
                        DynamicPostViewHolder.CAPTION_END

                val captionTxt: String = buildString {
                    append(
                        ("<b>" + caption.author.name + "</b>" + " - ")
                            .plus(caption.text.substring(0, captionEnd))
                            .replace("\n", "<br/>")
                            .replace(DynamicPostViewHolder.NEWLINE, "<br/>")
                            .plus("... ")
                            .plus("<font color='#6D7588'><b>")
                            .plus(context.getString(R.string.feed_component_read_more_button))
                            .plus("</b></font>")
                    )
                }
                spannableString = tagConverter.convertToLinkifyHashtag(
                    SpannableString(MethodChecker.fromHtml(captionTxt)), colorLinkHashtag
                ) { hashtag -> onHashtagClicked(hashtag, caption) }

                captionText.setOnClickListener {
                    if (captionText.text.contains(context.getString(R.string.feed_component_read_more_button))) {
                        listener?.onReadMoreClicked(
                            caption.id,
                            caption.author.id,
                            caption.typename,
                            caption.followers.isFollowed,
                            isVideo(caption.media.firstOrNull())
                        )
                        val txt: String = buildString {
                            append(("<b>" + caption.author.name + "</b>" + " - " + caption.text))
                        }
                        spannableString = tagConverter.convertToLinkifyHashtag(
                            SpannableString(MethodChecker.fromHtml(txt)),
                            colorLinkHashtag
                        ) { hashtag -> onHashtagClicked(hashtag, caption) }
                        spannableString.setSpan(
                            cs,
                            0,
                            caption.author.name.length - 1,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        captionText.text = spannableString
                        captionText.movementMethod = LinkMovementMethod.getInstance()
                    }
                }

            } else {

                val captionTxt: String = buildString {
                    append(
                        ("<b>" + caption.author.name + "</b>" + " - ").plus(
                            caption.text.replace(DynamicPostViewHolder.NEWLINE, " ")
                        )
                    )
                }
                spannableString = tagConverter
                    .convertToLinkifyHashtag(
                        SpannableString(
                            MethodChecker.fromHtml(
                                captionTxt
                            )
                        ),
                        colorLinkHashtag
                    ) { hashtag -> onHashtagClicked(hashtag, caption) }
            }
            spannableString.setSpan(
                cs,
                0,
                caption.author.name.length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            captionText.text = spannableString
            captionText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private val colorLinkHashtag: Int
        get() = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)

    private fun onHashtagClicked(hashtag: String, feed: FeedXCard) {
        listener?.onHashtagClickedFeed(hashtag, feed)
        val encodeHashtag = URLEncoder.encode(hashtag, "UTF-8")
        RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
    }

    private fun hasSecondLine(caption: String): Boolean {
        val firstIndex = caption.indexOf("\n", 0)
        return caption.indexOf("\n", firstIndex + 1) != -1
    }

    private fun findSubstringSecondLine(caption: String): Int {
        val firstIndex = caption.indexOf("\n", 0)
        return if (hasSecondLine(caption)) caption.indexOf(
            "\n",
            firstIndex + 1
        ) else caption.length
    }

    private fun bindComment(
        comments: FeedXComments,
        profilePicture: String,
        name: String,
        id: Int,
        authorType: Int,
        authorId: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String
    ) {
        seeAllCommentText.showWithCondition(comments.count != 0)
        seeAllCommentText.text =
            context.getString(R.string.feed_component_see_all_comments, comments.countFmt)
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

        var authId = ""
        if (authorType != 1)
            authId = authorId
        val isVideo = mediaType != TYPE_IMAGE
        commentButton.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, isVideo)
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, isVideo)
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, isVideo)
        }
    }

    private fun bindItems(
        feedXCard: FeedXCard,
    ) {
        val media = feedXCard.media
        val postId = feedXCard.id.toIntOrZero()
        if (feedXCard.typename != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT) {
            val products = feedXCard.tags
            gridList.gone()
            carouselView.visible()
            commentButton.visible()
            carouselView.apply {
                stage.removeAllViews()
                indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
                if (media.size > 1) {
                    pageControl.show()
                    pageControl.setIndicator(media.size)
                    pageControl.indicatorCurrentPosition = activeIndex
                } else {
                    pageControl.hide()
                }
                imagePostListener.userCarouselImpression(
                    feedXCard.id,
                    media[0],
                    0,
                    feedXCard.typename,
                    feedXCard.followers.isFollowed,
                    feedXCard.author.id,
                    positionInFeed
                )
                media.forEachIndexed { index, feedMedia ->

                    if (feedMedia.type == TYPE_IMAGE) {
                        val imageItem = View.inflate(context, R.layout.item_post_image_new, null)
                        val param = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        imageItem.layoutParams = param
                        imageItem.run {
                            findViewById<ImageUnify>(R.id.post_image).setImageUrl(feedMedia.mediaUrl)
                            findViewById<IconUnify>(R.id.product_tag_button).showWithCondition(
                                products.isNotEmpty()
                            )
                            val productTag = findViewById<IconUnify>(R.id.product_tag_button)
                            val productTagText = findViewById<Typography>(R.id.product_tag_text)
                            like_anim.setImageDrawable(
                                MethodChecker.getDrawable(
                                    context,
                                    R.drawable.ic_thumb_filled
                                )
                            )
                            productTagText.gone()
                            imagePostListener.userImagePostImpression(
                                positionInFeed,
                                pageControl.indicatorCurrentPosition
                            )
                            productTagText.postDelayed({
                                if (products.isNotEmpty()) {
                                    productTagText.apply {
                                        visible()
                                        animate().alpha(1f).start()
                                    }
                                }

                            }, TIME_SECOND)

                            val gd = GestureDetector(
                                context,
                                object : GestureDetector.SimpleOnGestureListener() {
                                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                        listener?.onImageClicked(
                                            postId.toString(),
                                            feedXCard.typename,
                                            feedXCard.followers.isFollowed,
                                            feedXCard.author.id
                                        )
                                        if (!productTagText.isVisible) {
                                            productTagText.apply {
                                                visible()
                                                animate().alpha(1f).start()
                                            }
                                        } else {
                                            productTagText.gone()
                                            productTagText.animate().alpha(0f)
                                        }
                                        return true
                                    }

                                    override fun onDown(e: MotionEvent): Boolean {
                                        return true
                                    }

                                    override fun onDoubleTap(e: MotionEvent): Boolean {
                                        val pulseFade: Animation =
                                            AnimationUtils.loadAnimation(
                                                context,
                                                android.R.anim.fade_in
                                            )
                                        pulseFade.setAnimationListener(object :
                                            Animation.AnimationListener {
                                            override fun onAnimationStart(animation: Animation) {
                                                like_anim.visibility = VISIBLE
                                                listener?.onLikeClick(
                                                    positionInFeed, postId,
                                                    !feedXCard.like.isLiked,
                                                    feedXCard.typename,
                                                    feedXCard.followers.isFollowed,
                                                    type = true,
                                                    feedXCard.author.id,
                                                    isVideo(feedMedia)
                                                )
                                            }

                                            override fun onAnimationEnd(animation: Animation) {
                                                like_anim.gone()
                                            }

                                            override fun onAnimationRepeat(animation: Animation) {}
                                        })
                                        like_anim.visible()
                                        like_anim.startAnimation(pulseFade)
                                        return true
                                    }

                                    override fun onLongPress(e: MotionEvent) {
                                        super.onLongPress(e)
                                    }

                                    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                                        return true
                                    }
                                })

                            productTagText?.setOnClickListener {
                                listener?.let { listener ->
                                    listener.onTagClicked(
                                        postId,
                                        products,
                                        listener,
                                        feedXCard.author.id,
                                        feedXCard.typename,
                                        feedXCard.followers.isFollowed,
                                        false,
                                        positionInFeed
                                    )
                                }
                            }
                            productTag?.setOnClickListener {
                                listener?.let { listener ->
                                    listener.onTagClicked(
                                        postId,
                                        products,
                                        listener,
                                        feedXCard.author.id,
                                        feedXCard.typename,
                                        feedXCard.followers.isFollowed,
                                        false,
                                        positionInFeed
                                    )
                                }
                            }
                            setOnTouchListener { v, event ->
                                gd.onTouchEvent(event)
                                true
                            }
                        }
                        addItem(imageItem)

                    } else {
                        addItem(
                            setVideoCarouselView(
                                feedMedia,
                                feedXCard.id,
                                products,
                                feedXCard.author.id,
                                feedXCard.typename,
                                feedXCard.followers.isFollowed
                            )
                        )
                    }
                }
                onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                    override fun onActiveIndexChanged(prev: Int, current: Int) {
                        imagePostListener.userCarouselImpression(
                            feedXCard.id,
                            media[current],
                            current,
                            feedXCard.typename,
                            feedXCard.followers.isFollowed,
                            feedXCard.author.id,
                            positionInFeed
                        )
                        pageControl.setCurrentIndicator(current)
                        if (media[current].type == TYPE_IMAGE)
                            videoPlayer?.pause()
                        else {
                            detach()
                            media[current].canPlay = true
                            playVideo(feedXCard, current)
                        }
                    }
                }
            }

        } else {
            setGridASGCLayout(feedXCard)
        }
    }

    private fun setVideoCarouselView(
        feedMedia: FeedXMedia,
        postId: String,
        products: List<FeedXProduct>,
        id: String,
        type: String,
        isFollowed: Boolean
    ): View {
        val videoItem = View.inflate(context, R.layout.item_post_video_new, null)
        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        feedMedia.canPlay = false
        videoItem?.layoutParams = param
        feedMedia.videoView = videoItem
        videoItem?.run {
            videoPreviewImage?.setImageUrl(feedMedia.coverUrl)
            video_tag_text?.setOnClickListener {
                listener?.let { listener ->
                    listener.onTagClicked(
                        postId.toIntOrZero(),
                        products,
                        listener,
                        id,
                        type,
                        isFollowed,
                        true,
                        positionInFeed
                    )
                }
            }

            volumeIcon.setOnClickListener {
                if(isMute)
                listener?.muteUnmuteVideo(postId, isMute, id, isFollowed)
                isMute = !isMute
                volumeIcon?.setImage(if (!isMute) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE)
                toggleVolume(videoPlayer?.isMute() != true)
            }
        }
        return (videoItem)
    }

    private fun setVideoControl(
        feedMedia: FeedXMedia,
        postId: String,
        index: Int,
        authorId: String,
        type: Int,
        feedXCard: FeedXCard,
    ) {
        val videoItem = feedMedia.videoView
        videoItem?.run {
            if (feedMedia.videoTime == 0L) {
                scopeDef.launch {
                    try {
                        feedMedia.videoTime = getVideoDuration(feedMedia) / TIME_SECOND
                    } catch (e: Exception) {

                    }
                }
            }
            video_tag_text.postDelayed({
                video_tag_text.visible()
                video_tag_text.animate().alpha(1F).start()
            }, TIME_SECOND)
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)
                layout_video?.player = videoPlayer?.getExoPlayer()
                layout_video?.videoSurfaceView?.setOnClickListener {
                    if (feedMedia.mediaUrl.isNotEmpty()) {
                        val authorType = if (type != 1)
                            authorId else ""
                        videoListener?.onVideoPlayerClicked(
                            positionInFeed,
                            index,
                            postId,
                            feedMedia.appLink,
                            authorId,
                            authorType,
                            feedXCard.followers.isFollowed

                        )
                    }
                }
                videoPlayer?.start(feedMedia.mediaUrl, isMute)
                volumeIcon?.setImage(if (!isMute) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE)
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showVideoLoading()
                    }

                    override fun onVideoReadyToPlay() {
                        hideVideoLoading()
                        timer_view.visible()
                        var time = if (feedMedia.videoTime == 0L) 10L else feedMedia.videoTime
                        object : CountDownTimer(TIMER_TO_BE_SHOWN, TIME_SECOND) {
                            override fun onTick(millisUntilFinished: Long) {
                                time -= 1
                                timer_view.text =
                                    String.format("%02d:%02d", (time / 60) % 60, time % 60)
                            }

                            override fun onFinish() {
                                timer_view.gone()
                            }
                        }.start()
                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                        feedMedia.canPlay = false
                        videoListener?.onVideoStopTrack(feedXCard, videoDuration / 1000)
                    }
                })
            }
        }
    }

    private fun hideVideoLoading() {
        loader?.gone()
        ic_play?.gone()
        timer_view?.visible()
        videoPreviewImage?.gone()
    }

    private fun showVideoLoading() {
        loader?.animate()
        loader?.visible()
        ic_play?.visible()
    }

    private fun getVideoDuration(feedMedia: FeedXMedia): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(feedMedia.mediaUrl, HashMap())
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return time.toLong()
    }

    private fun toggleVolume(isMute: Boolean) {
        videoPlayer?.toggleVideoVolume(isMute)
    }

    private fun setGridASGCLayout(feedXCard: FeedXCard) {
        gridList.visible()
        carouselView.gone()
        commentButton.gone()
        val layoutManager = GridLayoutManager(
            gridList.context,
            SPAN_SIZE_FULL,
            LinearLayoutManager.VERTICAL,
            false
        )
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (feedXCard.products.size) {
                    1 -> SPAN_SIZE_FULL
                    2 -> SPAN_SIZE_HALF
                    else -> SPAN_SIZE_SINGLE
                }
            }
        }
        gridList.layoutManager = layoutManager

        imagePostListener.userGridPostImpression(
            positionInFeed, feedXCard.id,
            feedXCard.typename,
            feedXCard.author.id
        )
        val adapter = GridPostAdapter(
            0,
            getGridPostModel(feedXCard, feedXCard.products),
            gridPostListener
        )

        gridList.adapter = adapter
        setGridListPadding(feedXCard.products.size)

        val totalProducts = feedXCard.products.size
        var totalProductsImpressed = totalProducts
        if (totalProducts > MAX_FEED_SIZE) {
            totalProductsImpressed = LAST_FEED_POSITION

        } else if (totalProducts in (MAX_FEED_SIZE_SMALL + 1) until MAX_FEED_SIZE) {
            totalProductsImpressed = LAST_FEED_POSITION_SMALL
        }
        var listToBeImpressed = feedXCard.products.subList(0, totalProductsImpressed)

        imagePostListener.userProductImpression(
            positionInFeed,
            feedXCard.id,
            feedXCard.typename,
            feedXCard.author.id,
            listToBeImpressed
        )
    }

    private fun setGridListPadding(listSize: Int) {
        if (listSize == 1) {
            gridList.setPadding(0, 0, 0, 0)
        } else {
            gridList.setPadding(
                gridList.getDimens(R.dimen.dp_3),
                0,
                gridList.getDimens(R.dimen.dp_3),
                0
            )
        }
    }

    private fun getGridPostModel(
        feedXCard: FeedXCard,
        products: List<FeedXProduct>
    ): GridPostViewModel {
        return GridPostViewModel(
            getGridItemViewModel(products),
            "Lihat Lainnya",
            feedXCard.appLink,
            products.size,
            true,
            mutableListOf(),
            feedXCard.id.toInt(),
            positionInFeed,
            feedXCard.typename,
            feedXCard.followers.isFollowed,
            feedXCard.author.id,
            feedXCard.products
        )
    }

    private fun getGridItemViewModel(products: List<FeedXProduct>): MutableList<GridItemViewModel> {
        val itemList: MutableList<GridItemViewModel> = ArrayList()
        products.forEach {
            itemList.add(
                GridItemViewModel(
                    it.id,
                    it.name,
                    it.priceFmt,
                    it.priceOriginalFmt,
                    it.appLink,
                    it.coverURL,
                    mutableListOf(),
                    mutableListOf(),
                    index = products.indexOf(it)
                )
            )
        }
        return itemList
    }

    private fun bindPublishedAt(publishedAt: String, subTitle: String) {
        val avatarDate = TimeConverter.generateTimeNew(context, publishedAt)
        val spannableString: SpannableString =
            if (subTitle.isNotEmpty()) {
                SpannableString(
                    String.format(
                        context.getString(R.string.feed_header_time_new),
                        avatarDate
                    )
                )
            } else {
                SpannableString(avatarDate)
            }
        timestampText.text = spannableString
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun onResume() {
        videoPlayer?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        videoPlayer?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        detach()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun onStop() {
        videoPlayer?.pause()
    }

    fun detach() {
        if (videoPlayer != null) {
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.destroy()
            videoPlayer = null
            layout_video?.player = null
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        videoPlayer?.destroy()
    }

    fun playVideo(feedXCard: FeedXCard, position: Int = carouselView.activeIndex) {
        if (videoPlayer == null)
            feedXCard.media[position].canPlay = true
        if (feedXCard.media[position].canPlay) {
            setVideoControl(
                feedXCard.media[position],
                feedXCard.id,
                position,
                feedXCard.author.id,
                feedXCard.author.type,
                feedXCard
            )
        }
    }

    private fun isVideo(media: FeedXMedia?): Boolean {
        return media?.type != TYPE_IMAGE
    }
}