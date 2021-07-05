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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT: String = "FeedXCardProductsHighlight"
private const val SPAN_SIZE_FULL = 6
private const val SPAN_SIZE_HALF = 3
private const val SPAN_SIZE_SINGLE = 2
private val scope = CoroutineScope(Dispatchers.Main)
private var productVideoJob: Job? = null

class PostDynamicViewNew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

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
    var isPlaying = false
    private var videoPlayer: FeedExoPlayer? = null

    init {
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
            feedXCard.followers.isFollowed
        )
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
                feedXCard.followers.isFollowed
            )
        }
    }

    fun bindLike(feedXCard: FeedXCard) {
        bindLike(
            feedXCard.like,
            feedXCard.id.toIntOrZero(),
            feedXCard.typename,
            feedXCard.followers.isFollowed
        )
    }

    fun bindFollow(feedXCard: FeedXCard) {
        bindHeader(
            feedXCard.id.toIntOrZero(),
            feedXCard.author,
            feedXCard.reportable,
            feedXCard.deletable,
            feedXCard.followers,
            feedXCard.typename
        )
    }

    private fun bindHeader(
        activityId: Int,
        author: FeedXAuthor,
        reportable: Boolean,
        deletable: Boolean,
        followers: FeedXFollowers,
        type: String
    ) {
        val isFollowed = followers.isFollowed
        val count = followers.count
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
                    isFollowed
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
                        authorType, isFollowed, type
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
                isFollowed
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
                type
            )
        }
    }

    private fun bindLike(like: FeedXLike, id: Int, type: String, isFollowed: Boolean) {
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
            listener?.onLikeClick(positionInFeed, id, like.isLiked, type, isFollowed)
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
                captionText.text = tagConverter.convertToLinkifyHashtag(
                    SpannableString(MethodChecker.fromHtml(captionTxt)), colorLinkHashtag
                ) { hashtag -> onHashtagClicked(hashtag) }

                spannableString = SpannableString(MethodChecker.fromHtml(captionTxt))
                captionText.setOnClickListener {
                    listener?.onReadMoreClicked(caption.id)
                    val txt: String = buildString {
                        append(("<b>" + caption.author.name + "</b>" + " - " + caption.text))
                    }
                    captionText.text = tagConverter.convertToLinkifyHashtag(
                        SpannableString(MethodChecker.fromHtml(txt)),
                        colorLinkHashtag
                    ) { hashtag -> onHashtagClicked(hashtag) }
                    spannableString = SpannableString(MethodChecker.fromHtml(txt))

                }
                captionText.movementMethod = LinkMovementMethod.getInstance()

            } else {

                val captionTxt: String = buildString {
                    append(
                        ("<b>" + caption.author.name + "</b>" + " - ").plus(
                            caption.text.replace(DynamicPostViewHolder.NEWLINE, " ")
                        )
                    )
                }
                captionText.text = tagConverter
                    .convertToLinkifyHashtag(
                        SpannableString(
                            MethodChecker.fromHtml(
                                captionTxt
                            )
                        ),
                        colorLinkHashtag
                    ) { hashtag -> onHashtagClicked(hashtag) }
                spannableString = SpannableString(
                    MethodChecker.fromHtml(
                        captionTxt
                    )
                )
                captionText.movementMethod = LinkMovementMethod.getInstance()
            }

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    listener?.onAvatarClick(
                        positionInFeed,
                        caption.appLink,
                        caption.id.toIntOrZero(),
                        "",
                        followCta,
                        caption.typename,
                        caption.followers.isFollowed
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
            }, 0, caption.author.name.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
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
        isFollowed: Boolean
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
        commentButton.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed)
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed)
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed)
        }
    }

    fun bindItems(
        feedXCard: FeedXCard
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
                    pageControl.indicatorCurrentPosition = 0
                } else {
                    pageControl.hide()
                }
                media.forEach { feedMedia ->
                    imagePostListener.userCarouselImpression(
                        positionInFeed,
                        media,
                        feedXCard.typename,
                        feedXCard.followers.isFollowed
                    )

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
                                    productTagText.visible()
                                }

                            }, 1000)
                            val gd = GestureDetector(
                                context,
                                object : GestureDetector.SimpleOnGestureListener() {
                                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                        listener?.onImageClicked(
                                            postId.toString(),
                                            feedXCard.typename,
                                            feedXCard.followers.isFollowed
                                        )
                                        if (!productTagText.isVisible)
                                            productTagText.visible()
                                        else
                                            productTagText.gone()
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
                                                    isLiked = false,
                                                    feedXCard.typename,
                                                    feedXCard.followers.isFollowed,
                                                    type = true
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
                                        feedXCard.followers.isFollowed
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
                                        feedXCard.followers.isFollowed
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
                        pageControl.setCurrentIndicator(current)
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
        videoItem?.layoutParams = param
        setVideoControl(videoItem,feedMedia)
        videoItem?.run {
            video_tag_text.visible()
            setOnClickListener {
                if (feedMedia.mediaUrl.isNotEmpty()) {
                    videoListener?.onVideoPlayerClicked(
                        positionInFeed,
                        0,
                        postId,
                        feedMedia.appLink
                    )
                }
            }
            video_tag_text?.setOnClickListener {
                listener?.let { listener ->
                    listener.onTagClicked(
                        postId.toIntOrZero(),
                        products,
                        listener,
                        id,
                        type,
                        isFollowed
                    )
                }
            }
            volumeIcon.setOnClickListener {
                toggleVolume(videoPlayer?.isMute() != true)
            }
        }
        return (videoItem)
    }

    private fun setVideoControl(videoItem: View?, feedMedia: FeedXMedia) {
        videoItem?.run {
            layout_video?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            videoPreviewImage.setImageUrl(feedMedia.coverUrl)
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)
                var time = getVideoDuration(feedMedia)
                layout_video?.player = videoPlayer?.getExoPlayer()
                videoPlayer?.start(feedMedia.mediaUrl, feedMedia.isMute)
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {}

                    override fun onVideoReadyToPlay() {
                        hideVideoLoading(videoItem)
                        object : CountDownTimer(3000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                time = time/1000 -1
                                timer_view.text = String.format("%02d:%02d", (time / 60) % 60, time % 60);
                            }
                            override fun onFinish() {
                                timer_view.gone()
                            }
                        }.start()
                    }

                    override fun configureVolume(isMute: Boolean) {
                        setupVolume(isMute)
                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                    }

                })

            }

        }

    }

    private fun hideVideoLoading(videoItem: View) {
        videoItem.run {
            loader?.gone()
            ic_play?.gone()
            timer_view?.visible()
            videoPreviewImage?.gone()
        }

    }

    private fun showVideoLoading(videoItem: View) {
        videoItem.run {
            loader?.animate()
            loader?.visible()
            ic_play?.visible()
        }
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

    private fun setupVolume(isMute: Boolean) {
        volumeIcon?.setImage(if (!isMute) IconUnify.VOLUME_UP else IconUnify.VOLUME_MUTE)
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
            feedXCard.typename
        )
        val adapter = GridPostAdapter(
            0,
            getGridPostModel(feedXCard, feedXCard.products),
            gridPostListener
        )
        gridList.adapter = adapter
        setGridListPadding(feedXCard.products.size)
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
            feedXCard.followers.isFollowed
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
                    mutableListOf()
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

    fun showAnim() {
        carouselView.getChildAt(0)
    }
}