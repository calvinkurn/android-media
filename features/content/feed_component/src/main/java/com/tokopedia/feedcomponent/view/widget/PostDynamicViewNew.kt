package com.tokopedia.feedcomponent.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.util.util.*
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineListener
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.item_post_image_new.view.*
import kotlinx.android.synthetic.main.item_post_long_video_vod.view.*
import kotlinx.android.synthetic.main.item_post_video_new.view.*
import kotlinx.coroutines.*
import java.net.URLEncoder
import kotlin.collections.ArrayList
import kotlin.math.round

private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT: String = "FeedXCardProductsHighlight"
private const val TYPE_USE_ASGC_NEW_DESIGN: String = "use_new_design"
private const val TYPE_FEED_X_CARD_VOD: String = "FeedXCardPlay"
private const val TYPE_LONG_VIDEO: String = "long-video"
private const val SPAN_SIZE_FULL = 6
private const val SPAN_SIZE_HALF = 3
private const val SPAN_SIZE_SINGLE = 2
private const val MAX_FEED_SIZE = 6
private const val MAX_FEED_SIZE_SMALL = 3
private const val LAST_FEED_POSITION = 5
private const val TOPADS_TAGGING_CENTER_POS_X = 0.5f
private const val TOPADS_TAGGING_CENTER_POS_Y = 0.44f
private const val LAST_FEED_POSITION_SMALL = 2
private val scope = CoroutineScope(Dispatchers.Main)
private var productVideoJob: Job? = null
private const val TIME_THREE_SEC = 3000L
private const val TIME_THIRTY_SEC = 30000L
private const val TIME_FOUR_SEC = 4000L
private const val TIME_TWO_SEC = 2000L
private const val TIME_FIVE_SEC = 5000L
private const val MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL = 5
private const val ROUND_OFF_TO_ONE_DECIMAL_VALUE = 10


private const val TIME_SECOND = 1000L
private const val FOLLOW_SIZE = 7
private const val MINUTE_IN_HOUR = 60
private const val HOUR_IN_HOUR = 3600
private const val SPACE = 3
private const val DOT_SPACE = 2
private const val SHOW_MORE = "Lihat Lainnya"
private const val MAX_CHAR = 120
private const val CAPTION_END = 120
private const val VOD_VIDEO_RATIO = "4:5"
private const val MEDIA_RATIO_PORTRAIT_THRESHOLD_FLOAT = 0.8f
private const val MEDIA_RATIO_LANDSCAPE_THRESHOLD_FLOAT = 1.91f
private const val MEDIA_RATIO_SQUARE_VALUE_FLOAT = 1f
private const val SQUARE_RATIO = "1:1"
private const val LONG_VIDEO_RATIO = "1.91:1"
private const val FOLLOW_COUNT_THRESHOLD = 100
private const val TYPE_DISCOUNT = "discount"
private val handlerFeed = Handler(Looper.getMainLooper())
private var secondCountDownTimer: CountDownTimer? = null
private var isPaused = false
private const val FOLLOW_MARGIN = 6
private const val MARGIN_ZERO = 0
private const val ASGC_NEW_PRODUCTS = "asgc_new_products"
private const val ASGC_RESTOCK_PRODUCTS = "asgc_restock_products"


/**
 * LIHAT_PRODUK_EXPANDED_WIDTH, LIHAT_PRODUK_CONTRACTED_WIDTH Value is fixed width  for Lihat Produk (item_post_image_new ,id = tv_lihat_product)
 *Lihat Produk Value is static so we have fixed it width to Keep our animation intact
 *Do not manipulate this value unless Lihat Produk text change
 **/
private const val LIHAT_PRODUK_EXPANDED_WIDTH_MIN_INDP = 90
private const val LIHAT_PRODUK_CONTRACTED_WIDTH_INDP = 24
const val PORTRAIT = 1
const val LANDSCAPE = 2

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
    private var topAdsListener:TopAdsHeadlineListener? = null
    private var positionInFeed: Int = 0
    var isVODViewFrozen = true
    private var videoPlayer: FeedExoPlayer? = null
    private var handlerAnim: Handler? = null
    private var handlerHide: Handler? = null
    private var changeBgColorAnim: Handler? = null
    private var feedAddViewJob: Job? = null


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
        imagePostListener: ImagePostViewHolder.ImagePostListener,
        topAdsListener: TopAdsHeadlineListener?= null
    ) {
        this.listener = dynamicPostListener
        this.gridPostListener = gridItemListener
        this.videoListener = videoListener
        this.positionInFeed = adapterPosition
        this.imagePostListener = imagePostListener
        this.topAdsListener = topAdsListener
        bindFollow(feedXCard)
        bindItems(feedXCard)
        bindCaption(feedXCard)
        val isTypeNewASGC = feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && feedXCard.mods.contains(TYPE_USE_ASGC_NEW_DESIGN)
        val isTopadsOrAsgc = feedXCard.isTopAds || isTypeNewASGC
        bindPublishedAt(feedXCard.publishedAt, feedXCard.subTitle, isTopadsOrAsgc)
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
            feedXCard.media.firstOrNull()?.type ?: "",
            feedXCard.playChannelID
        )
        bindTracking(feedXCard)
        shareButton.setOnClickListener {
                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
            val desc = context.getString(R.string.feed_share_default_text)
            val url = if (feedXCard.isTopAds && feedXCard.media.size > feedXCard.lastCarouselIndex) {
                feedXCard.media[feedXCard.lastCarouselIndex].webLink
            } else {
                feedXCard.appLink
            }
            listener?.onShareClick(
                positionInFeed,
                feedXCard.id.toIntOrZero(),
                feedXCard.author.name + " `post",
                desc.replace("%s", feedXCard.author.name),
                url = url,
                feedXCard.media.firstOrNull()?.mediaUrl ?: "",
                feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                feedXCard.author.id,
                feedXCard.media.firstOrNull()?.type?:"",
                feedXCard.isTopAds,
                feedXCard.playChannelID
            )
        }
    }

    private fun bindTracking(feedXCard: FeedXCard) {
                addOnImpressionListener(feedXCard.impressHolder) {
                    val isTypeNewASGC = feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && feedXCard.mods.contains(TYPE_USE_ASGC_NEW_DESIGN)

                    if (feedXCard.typename == TYPE_FEED_X_CARD_POST || feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW || feedXCard.typename == TYPE_FEED_X_CARD_VOD || isTypeNewASGC) {
                        imagePostListener.userCarouselImpression(
                                feedXCard.id,
                                feedXCard.media.first(),
                                0,
                                feedXCard.typename,
                                feedXCard.followers.isFollowed,
                                feedXCard.author.id,
                                positionInFeed,
                                feedXCard.cpmData,
                                feedXCard.listProduct
                        )
                    }

                    if (feedXCard.typename == TYPE_FEED_X_CARD_POST || feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW || feedXCard.typename == TYPE_FEED_X_CARD_VOD || isTypeNewASGC ) {
                        listener?.onImpressionTracking(feedXCard, positionInFeed)
                    }

            }
        }

    fun bindLike(feedXCard: FeedXCard) {
        val isLongVideo = feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO

        if (feedXCard.typename == TYPE_FEED_X_CARD_VOD || isLongVideo ) {
            bindViews(feedXCard)
        } else {
            bindLikeData(feedXCard)
        }
    }

    fun bindFollow(feedXCard: FeedXCard) {
        bindHeader(feedXCard
        )
    }

    private fun bindHeader(
        feedXCard: FeedXCard
    ) {
        val activityId = feedXCard.id.toIntOrZero()
        val author = feedXCard.author
        val reportable = feedXCard.reportable
        val deletable = feedXCard.deletable
        val followers = feedXCard.followers
        val type = feedXCard.typename
        val mediaType = feedXCard.media.firstOrNull()?.type ?: ""
        val caption = feedXCard.text
        val isTopads = feedXCard.isTopAds
        val adId = feedXCard.adId
        val shopId = feedXCard.shopId
        val cpmData = feedXCard.cpmData
        val channelId = feedXCard.playChannelID.toIntOrZero()
        val isFollowed = followers.isFollowed
        val count = followers.count
        val isVideo = mediaType != TYPE_IMAGE

        if (count >= FOLLOW_COUNT_THRESHOLD) {
            followCount.text =
                String.format(
                    context.getString(R.string.feed_header_follow_count_text),
                    count.productThousandFormatted()
                )
        } else {
            followCount.text =
                context.getString(R.string.feed_header_follow_count_less_text)
        }
        if (isTopads){
            followCount.text = context.getString(R.string.feeds_ads_text)
        }

        followCount.showWithCondition(!isFollowed || followers.transitionFollow)
        if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT) {
            if (feedXCard.type == ASGC_NEW_PRODUCTS)
                followCount.text = context.getString(R.string.feeds_asgc_new_product_text)
            else if (feedXCard.type == ASGC_RESTOCK_PRODUCTS)
                followCount.text = context.getString(R.string.feeds_asgc_restock_text)
            followCount.show()
        }


        shopImage.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        if (shopBadge.visibility == GONE) {
            val layoutParams = (followCount.layoutParams as? MarginLayoutParams)
            layoutParams?.setMargins(FOLLOW_MARGIN, MARGIN_ZERO, MARGIN_ZERO, MARGIN_ZERO)
            followCount.layoutParams = layoutParams
        }
        val activityName = ""
        val authorType = if (author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val followCta =
            FollowCta(authorID = author.id, authorType = authorType, isFollow = isFollowed)
        val authorName = MethodChecker.fromHtml(author.name)
        val startIndex = authorName.length + DOT_SPACE
        var endIndex = startIndex + FOLLOW_SIZE

        val text = if (followers.transitionFollow) {
            endIndex += SPACE
            context.getString(R.string.kol_Action_following_color)
        } else if (followers.isFollowed && isTopads) {
            context.getString(
                R.string.kol_Action_following_color
            )
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
                    if (type == TYPE_FEED_X_CARD_VOD) channelId else activityId,
                    activityName,
                    followCta,
                    type,
                    isFollowed,
                    author.id,
                    mediaType,
                    false

                )
                sendHeaderTopadsEvent(positionInFeed,author.appLink,cpmData,true)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    unifyPrinciplesR.color.Unify_N600
                )
            }
        }

        if (startIndex < spannableString.length && endIndex <= spannableString.length) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    if (isTopads) {
                        listener?.onFollowClickAds(positionInFeed, shopId, adId)
                    } else {
                        listener?.onHeaderActionClick(
                            positionInFeed, author.id,
                            authorType, isFollowed, type, isVideo
                        )
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    if (endIndex == startIndex + FOLLOW_SIZE) {
                        ds.color = MethodChecker.getColor(
                            context,
                            unifyPrinciplesR.color.Unify_G500
                        )
                    } else {
                        ds.color = MethodChecker.getColor(
                            context,
                            unifyPrinciplesR.color.Unify_NN600
                        )
                    }
                }

            }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            try {
                spannableString.setSpan(
                    cs,
                    0,
                    authorName.length - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: Exception) {
            }
            shopName.text = spannableString

        } else {
            try {
                spannableString.setSpan(
                    cs,
                    0,
                    authorName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } catch (e: Exception) {
            }
            shopName.text = spannableString

        }

        shopName.movementMethod = LinkMovementMethod.getInstance()
        followers.transitionFollow = false

        shopBadge.setOnClickListener {
            sendHeaderTopadsEvent(positionInFeed, author.appLink, cpmData, true)
        }
        shopImage.setOnClickListener {
            listener?.onAvatarClick(
                positionInFeed,
                author.appLink,
                if (type == TYPE_FEED_X_CARD_VOD) channelId else activityId,
                activityName,
                followCta,
                type,
                isFollowed,
                author.id,
                mediaType,
                false
            )
            sendHeaderTopadsEvent(positionInFeed,author.appLink,cpmData,true)
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
                mediaType,
                caption,
                channelId.toString())
        }
    }
    private fun bindViews(feedXCard: FeedXCard){

        val view = feedXCard.views
        if (feedXCard.like.isLiked) {
            val colorGreen =
                    MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                    MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N700_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (view.count != 0) {
            likedText.show()

            likedText.text =
                    MethodChecker.fromHtml(
                            context.getString(
                                    R.string.feed_component_viewed_count_text,
                                    view.count.productThousandFormatted(1)
                            )
                    )
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            changeTopadsCekSekarangBtnColorToGreen(feedXCard)
            listener?.onLikeClick(
                    positionInFeed,
                    feedXCard.id.toIntOrZero(),
                    feedXCard.like.isLiked,
                    feedXCard.typename,
                    feedXCard.followers.isFollowed,
                    shopId = feedXCard.author.id,
                    mediaType = feedXCard.media.firstOrNull()?.type?:"",
                    playChannelId = feedXCard.playChannelID

            )
        }

    }

    private fun bindLikeData(feedXCard: FeedXCard) {
        val like: FeedXLike = feedXCard.like
        val id: Int = feedXCard.id.toIntOrZero()
        val mediaType: String = feedXCard.media.firstOrNull()?.type?:""

        if (like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N700_96)
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
            changeTopadsCekSekarangBtnColorToGreen(feedXCard)
            listener?.onLikeClick(
                positionInFeed,
                id,
                like.isLiked,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                shopId = feedXCard.author.id,
                mediaType = mediaType
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
                    if (caption.typename == TYPE_FEED_X_CARD_VOD) caption.playChannelID.toIntOrZero() else caption.id.toIntOrZero(),
                    "",
                    followCta,
                    caption.typename,
                    caption.followers.isFollowed,
                    caption.author.id,
                    caption.media.firstOrNull()?.type?:"",
                    true
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    unifyPrinciplesR.color.Unify_N600
                )
            }
        }
        captionText.shouldShowWithAction(caption.text.isNotEmpty()) {
            if (caption.text.length > MAX_CHAR ||
                hasSecondLine(caption.text)
            ) {
                val captionEnd =
                    if (findSubstringSecondLine(caption.text) < CAPTION_END)
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
                            .plus("<font color='#6D7588'>")
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
                            if (caption.typename == TYPE_FEED_X_CARD_VOD) caption.playChannelID else caption.id,
                            caption.author.id,
                            caption.typename,
                            caption.followers.isFollowed,
                            caption.media.firstOrNull()?.type?:""
                        )
                        val txt: String = buildString {
                            append("<b>" + caption.author.name + "</b>" + " - ").appendLine(
                                caption.text.replace("(\r\n|\n)".toRegex(), "<br />")
                            )
                        }
                        spannableString = tagConverter.convertToLinkifyHashtag(
                            SpannableString(MethodChecker.fromHtml(txt)),
                            colorLinkHashtag
                        ) { hashtag -> onHashtagClicked(hashtag, caption) }
                        spannableString.setSpan(
                            cs,
                            0,

                            MethodChecker.fromHtml(caption.author.name).length - 1 ,
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
                MethodChecker.fromHtml(caption.author.name).length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            captionText.text = spannableString
            captionText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private val colorLinkHashtag: Int
        get() = MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_G400)

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
        mediaType: String,
        playChannelId: String
    ) {

        setCommentCount(comments)
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
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, mediaType, playChannelId = playChannelId, isClickIcon = true)
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, mediaType, playChannelId = playChannelId, isClickIcon = false)
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClick(positionInFeed, id, authId, type, isFollowed, mediaType, playChannelId=playChannelId, isClickIcon = true)
        }
    }

    fun setCommentCount(comments: FeedXComments) {
        seeAllCommentText.showWithCondition(comments.count != 0)
        seeAllCommentText.text =
            context.getString(R.string.feed_component_see_all_comments, comments.countFmt)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindItems(
        feedXCard: FeedXCard,
    ) {
        val media = feedXCard.media
        val postId = feedXCard.id.toIntOrZero()
        if (feedXCard.typename != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && feedXCard.typename != TYPE_FEED_X_CARD_VOD) {
            if (media.isNotEmpty() && media.first().type == TYPE_LONG_VIDEO){
                setVODLayout(feedXCard)
            } else {
            val globalCardProductList = feedXCard.tags
            gridList.gone()
            carouselView.visible()
            commentButton.visible()
            carouselView.apply {
                stage.removeAllViews()
                indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
                if (media.size > 1) {
                    pageControl.show()
                    pageControl.setIndicator(media.size)
                    pageControl.indicatorCurrentPosition = feedXCard.lastCarouselIndex
                    pageControl.setCurrentIndicator(feedXCard.lastCarouselIndex)
                    carouselView.activeIndex = feedXCard.lastCarouselIndex
                } else {
                    pageControl.hide()
                }
                val orientation = getOrientation(feedXCard.mediaRatio)
                var ratio = if (orientation == PORTRAIT)
                    getRatioIfPortrait(feedXCard.mediaRatio)
                else
                    getRatioIfLandscape(feedXCard.mediaRatio)

                media.forEachIndexed {  index, feedMedia ->
                    val tags = feedMedia.tagging
                    val tagProducts = mutableListOf<FeedXProduct>()
                    tags.map {
                        if (!ifProductAlreadyPresent(globalCardProductList[it.tagIndex],
                                tagProducts))
                        tagProducts.add(globalCardProductList[it.tagIndex])
                    }

                        feedMedia.isImageImpressedFirst = true

                        if (feedMedia.type == TYPE_IMAGE) {
                            var imageWidth = 0
                            var imageHeight = 0

                            val imageItem = getImageView()
                            feedMedia.imageView = imageItem
                            imageItem?.run {
                                val postImage = findViewById<ImageUnify>(R.id.post_image)
                                postImage.setImageUrl(feedMedia.mediaUrl)
                                val layout = findViewById<ConstraintLayout>(R.id.post_image_layout)
                                val layoutLihatProdukParent = findViewById<TextView>(R.id.tv_lihat_product)
                                layoutLihatProdukParent.showWithCondition(tagProducts.isNotEmpty())

                                val constraintSetForMedia = ConstraintSet()
                                constraintSetForMedia.clone(layout)
                                constraintSetForMedia.setDimensionRatio(postImage.id, ratio)
                                constraintSetForMedia.applyTo(layout)

                                like_anim.setImageDrawable(
                                        MethodChecker.getDrawable(
                                                context,
                                                R.drawable.ic_thumb_filled
                                        )
                                )

                            if (feedXCard.isTopAds) {
                                likedText.hide()
                                captionText.hide()
                                commentButton.invisible()
                                likeButton.invisible()
                                seeAllCommentText.hide()
                                shopMenuIcon.hide()
                                val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
                                val topAdsProductName = findViewById<Typography>(R.id.top_ads_product_name)
                                val textViewPrice = findViewById<Typography>(R.id.top_ads_price)
                                val textViewSlashedPrice =
                                        findViewById<Typography>(R.id.top_ads_slashed_price)
                                val labelDiscount = findViewById<Label>(R.id.top_ads_label_discount)
                                val labelCashback = findViewById<Label>(R.id.top_ads_label_cashback)

                                topAdsCard.show()
                                topAdsCard.setOnClickListener {
                                    changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                    RouteManager.route(context,feedMedia.appLink)
                                    listener?.onClickSekSekarang(feedXCard.id,feedXCard.shopId, TYPE_TOPADS_HEADLINE_NEW,feedXCard.followers.isFollowed, positionInFeed, feedXCard)
                                }
                                    textViewPrice.hide()
                                    textViewSlashedPrice.hide()
                                    labelDiscount.hide()
                                    labelCashback.hide()

                                    topAdsProductName.text = context.getString(R.string.feeds_cek_sekarang)
                                    topAdsProductName.setTypeface(null,Typeface.BOLD)
                                    topAdsProductName.setTextColor(
                                            MethodChecker.getColor(
                                                    context,
                                                    unifyPrinciplesR.color.Unify_NN600
                                            )
                                    )
                                    topAdsProductName.show()
                                    val constraintSet = ConstraintSet()
                                    constraintSet.clone(topAdsCard)
                                    constraintSet.connect(
                                            topAdsProductName.id,
                                            ConstraintSet.TOP,
                                            topAdsCard.id,
                                            ConstraintSet.TOP
                                    )
                                    constraintSet.connect(
                                            topAdsProductName.id,
                                            ConstraintSet.BOTTOM,
                                            topAdsCard.id,
                                            ConstraintSet.BOTTOM
                                    )
                                    constraintSet.applyTo(topAdsCard)

                            }

                                doOnLayout {
                                    imageWidth = width
                                    imageHeight = height
                                    feedMedia.tagging.forEachIndexed { index, feedXMediaTagging ->
                                        val productTagView = PostTagView(context, feedXMediaTagging)
                                        productTagView.postDelayed({
                                            val bitmap = postImage?.drawable?.toBitmap()
                                            productTagView.bindData(listener,
                                                    globalCardProductList,
                                                    imageWidth,
                                                    imageHeight,
                                                    positionInFeed,
                                                    bitmap)

                                        }, TIME_SECOND)


                                        layout.addView(productTagView)
                                    }

                                }
                                imagePostListener.userImagePostImpression(
                                        positionInFeed,
                                        pageControl.indicatorCurrentPosition
                                )

                                val gd = GestureDetector(
                                        context,
                                        object : GestureDetector.SimpleOnGestureListener() {
                                            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                                var productTagBubbleShowing = false
                                                listener?.onImageClicked(
                                                        postId.toString(),
                                                        feedXCard.typename,
                                                        feedXCard.followers.isFollowed,
                                                        feedXCard.author.id
                                                )

                                               //show hide animations for tagging views
                                                for (i in 0 until layout.childCount) {
                                                    var view = layout.getChildAt(i)
                                                    if (view is PostTagView) {
                                                        val item = (view as PostTagView)
                                                        productTagBubbleShowing = item.showExpandedView()
                                                    }
                                                }
                                                if (tagProducts.isNotEmpty()) {
                                                    if (layoutLihatProdukParent.width.toDp() < LIHAT_PRODUK_EXPANDED_WIDTH_MIN_INDP && !productTagBubbleShowing) {
                                                        showViewWithAnimation(layoutLihatProdukParent, context)
                                                    } else if (!productTagBubbleShowing && layoutLihatProdukParent.width.toDp() >= LIHAT_PRODUK_CONTRACTED_WIDTH_INDP) {
                                                        hideViewWithoutAnimation(layoutLihatProdukParent, context)
                                                    } else if (productTagBubbleShowing) {
                                                        showViewWithAnimation(layoutLihatProdukParent, context)
                                                    }
                                                }
                                                return true
                                            }

                                            override fun onDown(e: MotionEvent): Boolean {
                                                return true
                                            }

                                            override fun onDoubleTap(e: MotionEvent): Boolean {

                                                var productTagBubbleShowing = false

                                                for (i in 0 until layout.childCount) {
                                                    var view = layout.getChildAt(i)
                                                    if (view is PostTagView) {
                                                        val item = (view as PostTagView)
                                                        productTagBubbleShowing = item.hideExpandedViewIfShown()
                                                    }
                                                }
                                                if (tagProducts.isNotEmpty()) {
                                                    if (!productTagBubbleShowing && layoutLihatProdukParent.width.toDp() > LIHAT_PRODUK_CONTRACTED_WIDTH_INDP) {
                                                        hideViewWithoutAnimation(layoutLihatProdukParent, context)
                                                    }
                                                }

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
                                                                feedXCard.like.isLiked,
                                                                feedXCard.typename,
                                                                feedXCard.followers.isFollowed,
                                                                type = true,
                                                                feedXCard.author.id,
                                                                feedMedia.type
                                                        )
                                                    }

                                                    override fun onAnimationEnd(animation: Animation) {
                                                        like_anim.gone()
                                                    }

                                                    override fun onAnimationRepeat(animation: Animation) {}
                                                })
                                                if (!feedXCard.isTopAds) {
                                                    like_anim.visible()
                                                    like_anim.startAnimation(pulseFade)
                                                }
                                                return true
                                            }

                                            override fun onLongPress(e: MotionEvent) {
                                                super.onLongPress(e)
                                                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                            }

                                            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                                                return true
                                            }
                                        })

                                layoutLihatProdukParent?.setOnClickListener {
                                    changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                    listener?.let { listener ->
                                        listener.onTagClicked(
                                                postId,
                                                tagProducts,
                                                listener,
                                                feedXCard.author.id,
                                                feedXCard.typename,
                                                feedXCard.followers.isFollowed,
                                                feedMedia.type,
                                                positionInFeed,
                                                feedXCard.author.name,
                                                shopName = feedXCard.author.name
                                        )
                                    }
                                }
                                setOnTouchListener { v, event ->
                                    gd.onTouchEvent(event)
                                    true
                                }
                            }
                            if (imageItem != null) {
                                addItem(imageItem)
                            }

                        } else {
                            setVideoCarouselView(
                                    feedMedia,
                                    feedXCard,
                                    tagProducts,
                                    feedXCard.author.id,
                                    feedXCard.typename,
                                    feedXCard.followers.isFollowed,
                                    feedXCard.author.name,
                                    ratio,
                                    index

                            )?.let {
                                addItem(
                                        it
                                )
                            }
                        }
                    }
                }
               resetCaraouselActiveListener(feedXCard)
            }

        } else if (feedXCard.typename == TYPE_FEED_X_CARD_VOD) {
            setVODLayout(feedXCard)
        } else {
            if (feedXCard.mods.contains(TYPE_USE_ASGC_NEW_DESIGN))
                setNewASGCLayout(feedXCard)
            else
                setGridASGCLayout(feedXCard)
        }
    }

    private fun setVODLayout(feedXCard: FeedXCard){
            val media = feedXCard.media
            val globalCardProductList = feedXCard.tags
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
                var ratio = VOD_VIDEO_RATIO

                 if (feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO) {
                    val orientation = getOrientation(feedXCard.mediaRatio)
                    ratio = if (orientation == PORTRAIT)
                        getRatioIfPortrait(feedXCard.mediaRatio)
                    else
                        getRatioIfLandscape(feedXCard.mediaRatio)
                }
                media.forEach { feedMedia ->
                    val tags = feedMedia.tagging
                    val tagProducts = mutableListOf<FeedXProduct>()
                    tags.map {
                        if (!ifProductAlreadyPresent(globalCardProductList[it.tagIndex],
                                        tagProducts))
                            tagProducts.add(globalCardProductList[it.tagIndex])
                    }
                    var finalId = if (feedXCard.typename == TYPE_FEED_X_CARD_PLAY) feedXCard.playChannelID else feedXCard.id

                    feedMedia.isImageImpressedFirst = true
                        setVODView(
                                feedXCard,
                                feedMedia,
                                tagProducts,
                                feedXCard.author.id,
                                feedXCard.typename,
                                feedXCard.followers.isFollowed,
                                feedXCard.author.name,
                                ratio
                        )?.let {
                            addItem(
                                    it
                            )
                        }
                }
            }


    }

    private fun setVideoCarouselView(
        feedMedia: FeedXMedia,
        feedXCard: FeedXCard,
        products: List<FeedXProduct>,
        id: String,
        type: String,
        isFollowed: Boolean,
        shopName: String,
        ratio: String,
        position: Int
    ): View? {
        val postId = feedXCard.id
        val videoItem = getVideoItem()
        feedMedia.canPlay = false
        feedMedia.videoView = videoItem
        videoItem?.run {

            val playButtonVideo = findViewById<ImageView>(R.id.ic_play)
            val layoutVideo = findViewById<ConstraintLayout>(R.id.layout_main)
            val videoPreviewImage = findViewById<ImageUnify>(R.id.videoPreviewImage)
            val videoView = findViewById<View>(R.id.video_view)
            val constraintSetForVideoCoveMedia = ConstraintSet()
            constraintSetForVideoCoveMedia.clone(layoutVideo)
            constraintSetForVideoCoveMedia.setDimensionRatio(videoPreviewImage.id, ratio)
            constraintSetForVideoCoveMedia.setDimensionRatio(videoView.id, ratio)
            constraintSetForVideoCoveMedia.applyTo(layoutVideo)

            val layoutFrameView = findViewById<ConstraintLayout>(R.id.frame_video)
            val layoutPlayerView = findViewById<PlayerView>(R.id.layout_video)
            val constraintSetForVideoLayout = ConstraintSet()
            constraintSetForVideoLayout.clone(layoutFrameView)
            constraintSetForVideoLayout.setDimensionRatio(layoutPlayerView.id, ratio)
            constraintSetForVideoLayout.applyTo(layoutFrameView)

            videoPreviewImage?.setImageUrl(feedMedia.coverUrl)
            playButtonVideo?.setOnClickListener {
                playVideo(feedXCard, position)
                }
                video_lihat_product?.setOnClickListener {
                    listener?.let { listener ->
                        listener.onTagClicked(
                                postId.toIntOrZero(),
                                products,
                                listener,
                                id,
                                type,
                                isFollowed,
                            feedMedia.type,
                            positionInFeed,
                            feedXCard.playChannelID,
                            shopName = shopName
                    )
                }
            }


            volumeIcon.setOnClickListener {
                changeMuteStateVideo(volumeIcon)
                setMuteUnmuteVOD(volumeIcon, feedXCard.playChannelID, isFollowed, id,false, true, feedMedia.type)
            }
        }
        return videoItem
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
        val tags = feedMedia.tagging
        val postProductList = feedXCard.tags
        val tagProducts = mutableListOf<FeedXProduct>()
        tags.map {
            if (!ifProductAlreadyPresent(postProductList[it.tagIndex], tagProducts))
            tagProducts.add(postProductList[it.tagIndex])
        }
        videoItem?.run {
            val layoutLihatProdukParent = findViewById<Typography>(R.id.video_lihat_product)
            if (tagProducts.isEmpty()) {
                layoutLihatProdukParent.gone()
            } else {
                layoutLihatProdukParent.visible()
                 hideViewWithAnimation(layoutLihatProdukParent, context)
            }


            if (handlerAnim == null) {
                handlerAnim = handlerFeed
            }
            handlerAnim?.postDelayed({
                if (tagProducts.isNotEmpty()) {
                    showViewWithAnimation(layoutLihatProdukParent, context)
                }
            }, TIME_SECOND)
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)
                layout_video?.player = videoPlayer?.getExoPlayer()
                layout_video?.videoSurfaceView?.setOnClickListener {
                    changeMuteStateVideo(volumeIcon)
                    setMuteUnmuteVOD(volumeIcon, postId, feedXCard.followers.isFollowed, authorId, true, false, feedMedia.type)

                }

                videoPlayer?.start(feedMedia.mediaUrl, GridPostAdapter.isMute)
                volumeIcon?.setImageResource(if (!GridPostAdapter.isMute) R.drawable.ic_feed_volume_up else R.drawable.ic_feed_volume_mute)
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showVideoLoading()
                    }

                    override fun onVideoReadyToPlay() {
                        hideVideoLoading()
                        timer_view.visible()
                        var time = (videoPlayer?.getExoPlayer()?.duration ?: 0L) / TIME_SECOND
                        object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
                            override fun onTick(millisUntilFinished: Long) {
                                time -= 1
                                timer_view.text =
                                    String.format(
                                        "%02d:%02d",
                                        (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                        time % MINUTE_IN_HOUR
                                    )
                            }

                            override fun onFinish() {
                                timer_view.gone()
                                volumeIcon?.gone()
                            }
                        }.start()
                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                        feedMedia.canPlay = false
                        videoListener?.onVideoStopTrack(
                            feedXCard,
                            (videoPlayer?.getExoPlayer()?.currentPosition ?: 0L) / TIME_SECOND
                        )
                    }
                })
            }
        }
    }

    private fun setVODView(
            feedXCard: FeedXCard,
            feedMedia: FeedXMedia,
            products: List<FeedXProduct>,
            id: String,
            type: String,
            isFollowed: Boolean,
            shopName: String,
            ratio: String
    ): View? {
        val postId = feedXCard.id
        val vodItem = getVODItem()
        feedMedia.canPlay = false
        feedMedia.videoView = vodItem

        vodItem?.run {
            var finalId = if (feedXCard.typename == TYPE_FEED_X_CARD_PLAY) feedXCard.playChannelID.toIntOrZero() else feedXCard.id.toIntOrZero()

            val layoutVideo = findViewById<ConstraintLayout>(R.id.vod_layout_main)
            val videoPreviewImage = findViewById<ImageUnify>(R.id.vod_videoPreviewImage)
            val videoView = findViewById<View>(R.id.vod_view)
            val constraintSetForVideoCoveMedia = ConstraintSet()
            constraintSetForVideoCoveMedia.clone(layoutVideo)
            constraintSetForVideoCoveMedia.setDimensionRatio(videoPreviewImage.id, ratio)
            constraintSetForVideoCoveMedia.setDimensionRatio(videoView.id, ratio)
            constraintSetForVideoCoveMedia.applyTo(layoutVideo)

            val layoutFrameView = findViewById<ConstraintLayout>(R.id.vod_frame_video)
            val layoutPlayerView = findViewById<PlayerView>(R.id.vod_layout_video)
            val constraintSetForVideoLayout = ConstraintSet()
            constraintSetForVideoLayout.clone(layoutFrameView)
            constraintSetForVideoLayout.setDimensionRatio(layoutPlayerView.id, ratio)
            constraintSetForVideoLayout.applyTo(layoutFrameView)

            vod_videoPreviewImage?.setImageUrl(feedMedia.coverUrl)
            vod_lihat_product?.setOnClickListener {
                listener?.let { listener ->
                    listener.onTagClicked(
                            finalId,
                            products,
                            listener,
                            id,
                            type,
                            isFollowed,
                            feedMedia.type,
                            positionInFeed,
                            playChannelId = feedXCard.playChannelID,
                            shopName = shopName
                    )
                }
            }
            ic_vod_play?.setOnClickListener {
                playVOD(feedXCard =  feedXCard, carouselView.activeIndex)
            }
            vod_full_screen_icon?.setOnClickListener {
                isPaused = true
                vod_lanjut_menonton_btn?.gone()
                vod_frozen_view?.gone()
                listener?.onFullScreenCLick(feedXCard, positionInFeed, feedXCard.appLink, 0L, shouldTrack = true, true)
            }

            vod_volumeIcon?.setOnClickListener {
                changeMuteStateVideoVOD(vod_volumeIcon)
                setMuteUnmuteVOD(vod_volumeIcon, finalId.toString(), isFollowed, id, isVideoTap = false, isVOD = true, feedMedia.type)

            }
        }
        return vodItem
    }

    private fun setMuteUnmuteVOD(volumeIcon: ImageView?, postId: String, isFollowed: Boolean, activityId: String, isVideoTap: Boolean, isVOD: Boolean, mediaType: String) {
        var countDownTimer = object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                volumeIcon?.gone()
            }
        }
        listener?.muteUnmuteVideo(postId, GridPostAdapter.isMute, activityId, isFollowed, isVOD, mediaType)
        if (!volumeIcon?.isVisible!!)
            volumeIcon.visible()
        if (isVideoTap){
            if (countDownTimer != null) {
                countDownTimer.cancel()
                countDownTimer.start()
            } else {
                countDownTimer.start()
            }

        }
    }

    private fun changeMuteStateVideo(volumeIcon: ImageView) {
        GridPostAdapter.isMute = !GridPostAdapter.isMute
        toggleVolume(GridPostAdapter.isMute)
        if (GridPostAdapter.isMute) {
            volumeIcon?.setImageResource(R.drawable.ic_feed_volume_mute)
        } else {
            volumeIcon?.setImageResource(R.drawable.ic_feed_volume_up)
        }
    }

    private fun changeMuteStateVideoVOD(volumeIcon: ImageView) {
        GridPostAdapter.isMute = !GridPostAdapter.isMute
        toggleVolume(GridPostAdapter.isMute)
        if (GridPostAdapter.isMute) {
            volumeIcon?.setImageResource(R.drawable.ic_feed_volume_mute_large)
        } else {
            volumeIcon?.setImageResource(R.drawable.ic_feed_volume_up_large)
        }
    }

    private fun setVODControl(
            feedMedia: FeedXMedia,
            postId: String,
            index: Int,
            authorId: String,
            type: Int,
            feedXCard: FeedXCard,
    ) {
        val vodItem = feedMedia.videoView
        val tags = feedMedia.tagging
        val postProductList = feedXCard.tags
        secondCountDownTimer = null
        val tagProducts = mutableListOf<FeedXProduct>()
        isVODViewFrozen = false
        var count1 = 0
        var shouldTrack = true
        isPaused = false
        vod_lanjut_menonton_btn?.gone()
        vod_frozen_view?.gone()

        tags.map {
            if (!ifProductAlreadyPresent(postProductList[it.tagIndex], tagProducts))
                tagProducts.add(postProductList[it.tagIndex])
        }
        vodItem?.run {
            val layoutLihatProdukParent = findViewById<Typography>(R.id.vod_lihat_product)
            if (tagProducts.isEmpty()) {
                layoutLihatProdukParent.gone()
            } else {
                layoutLihatProdukParent.visible()
                hideViewWithAnimationVod(layoutLihatProdukParent, context)
            }
            vod_frozen_view?.gone()
            vod_full_screen_icon?.visible()



            if (handlerAnim == null) {
                handlerAnim = Handler(Looper.getMainLooper())
            }
            handlerAnim?.postDelayed({
                if (tagProducts.isNotEmpty()) {
                    showViewWithAnimationVOD(layoutLihatProdukParent, context)
                }
            }, TIME_SECOND)
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)

                vod_layout_video?.player = videoPlayer?.getExoPlayer()
                vod_layout_video?.videoSurfaceView?.setOnClickListener {
                    if (feedMedia.mediaUrl.isNotEmpty() && !isVODViewFrozen) {
                        changeMuteStateVideoVOD(vod_volumeIcon)
                        var finalId = if (feedXCard.typename == TYPE_FEED_X_CARD_PLAY) feedXCard.playChannelID.toIntOrZero() else feedXCard.id.toIntOrZero()
                        setMuteUnmuteVOD(vod_volumeIcon, finalId.toString(), feedXCard.followers.isFollowed, authorId, isVideoTap = true, isVOD = true, feedMedia.type)
                    }
                }
                vod_full_screen_icon?.setOnClickListener {
                    isPaused = true
                    vod_lanjut_menonton_btn?.gone()
                    vod_frozen_view?.gone()
                    videoPlayer?.getExoPlayer()?.currentPosition?.let {
                        it1 -> listener?.onFullScreenCLick(feedXCard, positionInFeed, feedXCard.appLink, it1,shouldTrack, true) }
                }
                vod_lanjut_menonton_btn?.setOnClickListener {
                    vod_lanjut_menonton_btn?.gone()
                    vod_frozen_view?.gone()
                    videoPlayer?.getExoPlayer()?.currentPosition?.let { it2 -> listener?.onFullScreenCLick(feedXCard, positionInFeed, feedXCard.appLink,it2,false, false)}
                }
                videoPlayer?.start(feedMedia.mediaUrl, GridPostAdapter.isMute)
                vod_volumeIcon?.visible()
                if (GridPostAdapter.isMute) {
                    vod_volumeIcon?.setImageResource(R.drawable.ic_feed_volume_mute_large)
                } else {
                    vod_volumeIcon?.setImageResource(R.drawable.ic_feed_volume_up_large)
                }
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showVODLoading()
                        isPaused = false
                        isVODViewFrozen = false
                        layoutLihatProdukParent.visible()

                    }

                    override fun onVideoReadyToPlay() {
                        hideVODLoading()
                        vod_timer_view.visible()
                        vod_volumeIcon?.visible()
                        vod_full_screen_icon?.visible()
                        vod_lanjut_menonton_btn?.gone()
                        vod_frozen_view?.gone()

                        if(!isPaused) {
                            if (secondCountDownTimer != null) {
                                secondCountDownTimer?.cancel()
                                secondCountDownTimer?.start()
                            } else {
                                secondCountDownTimer = object : CountDownTimer(TIME_THIRTY_SEC, TIME_SECOND) {
                                    override fun onTick(millisUntilFinished: Long) {

                                    }

                                    override fun onFinish() {
                                        videoPlayer?.pause()
                                        isPaused = true

                                        vod_lanjut_menonton_btn?.visible()
                                        vod_frozen_view?.visible()
                                        vod_full_screen_icon?.gone()
                                        vod_lihat_product?.gone()
                                        vod_timer_view?.gone()
                                        isVODViewFrozen = true

                                    }
                                }.start()
                            }
                        }


                        feedAddViewJob?.cancel()
                        feedAddViewJob = scope.launch {
                                    delay(TIME_FIVE_SEC)
                                    if (!isPaused) {
                                        val view = feedXCard.views
                                        val count = view.count +1
                                        if (view.count != 0) {
                                            likedText.text =
                                                    MethodChecker.fromHtml(
                                                            context.getString(
                                                                    R.string.feed_component_viewed_count_text,
                                                                    count.productThousandFormatted(1)
                                                            )
                                                    )
                                        }
                                        listener?.addVODView(feedXCard, feedXCard.playChannelID, positionInFeed, TIME_FIVE_SEC,true)
                                        shouldTrack = false
                                        isPaused = true
                                    }
                                }


                       if(!isPaused) {
                           vod_timer_view.visible()
                           var time = (videoPlayer?.getExoPlayer()?.duration
                                   ?: 0L) / TIME_SECOND + 1
                           object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
                               override fun onTick(millisUntilFinished: Long) {
                                   if (time < HOUR_IN_HOUR) {
                                       vod_timer_view.text =
                                               String.format(
                                                       "%02d:%02d",
                                                       (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                                       time % MINUTE_IN_HOUR)
                                   } else {
                                       vod_timer_view.text =
                                               String.format(
                                                       "%02d:%02d:%02d",
                                                       (time / HOUR_IN_HOUR) % HOUR_IN_HOUR,
                                                       (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                                       time % MINUTE_IN_HOUR)
                                   }
                                   time -= 1
                               }

                               override fun onFinish() {
                                   vod_timer_view.gone()
                                   vod_volumeIcon.gone()
                               }
                           }.start()
                       }
                    }

                    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
                        feedMedia.canPlay = false
                        listener?.addVODView(feedXCard, feedXCard.playChannelID, positionInFeed, (videoPlayer?.getExoPlayer()?.currentPosition ?: 0L) / TIME_SECOND,false)

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
    private fun hideVODLoading() {
        vod_loader?.gone()
        ic_vod_play?.gone()
        vod_timer_view?.visible()
        vod_videoPreviewImage?.gone()
    }

    private fun showVODLoading() {
        vod_loader?.animate()
        vod_loader?.visible()
        ic_vod_play?.visible()
    }

    private fun toggleVolume(isMute: Boolean) {
        videoPlayer?.toggleVideoVolume(isMute)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setNewASGCLayout(feedXCard: FeedXCard){
        val postId = feedXCard.id.toIntOrZero()
        val products = feedXCard.products
        val totalProducts = feedXCard.products.size
        gridList.gone()
        carouselView.visible()
        commentButton.gone()
        carouselView.apply {
            stage.removeAllViews()
            indicatorPosition = CarouselUnify.INDICATOR_HIDDEN
            if (products.size > 1) {
                pageControl.show()
                pageControl.setIndicator(if (totalProducts <= MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL) totalProducts else MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL)
                pageControl.indicatorCurrentPosition = feedXCard.lastCarouselIndex
                pageControl.setCurrentIndicator(feedXCard.lastCarouselIndex)
                carouselView.activeIndex = feedXCard.lastCarouselIndex
            } else {
                pageControl.hide()
            }
            val mediaList = mutableListOf<FeedXMedia>()


            products.forEachIndexed { index, feedXProduct ->


                val feedXMedia = feedXProduct.run {
                    FeedXMedia(
                            id = id,
                            type = "image",
                            appLink = feedXCard.appLink,
                            mediaUrl = coverURL,
                            tagging = arrayListOf(FeedXMediaTagging(index, TOPADS_TAGGING_CENTER_POS_X, TOPADS_TAGGING_CENTER_POS_Y, mediaIndex = index)),
                            isImageImpressedFirst = true,
                            productName = name,
                            price = priceFmt,
                            slashedPrice = priceOriginalFmt,
                            discountPercentage = discount.toString(),
                            isCashback = !TextUtils.isEmpty(cashbackFmt),
                            variant = variant,
                            cashBackFmt = cashbackFmt,
                    )
                }
                mediaList.add(feedXMedia)
            }
            if (products.isNotEmpty()) {
                imagePostListener.userGridPostImpression(
                        positionInFeed, feedXCard.id,
                        feedXCard.typename,
                        feedXCard.author.id
                )
                val list = mutableListOf<FeedXProduct>()
                list.add(products[0])
                imagePostListener.userProductImpression(
                        positionInFeed,
                        feedXCard.id,
                        feedXCard.typename,
                        feedXCard.author.id,
                        list
                )
            }

            mediaList.forEachIndexed { index, feedXMedia ->
                val tagProducts = mutableListOf<FeedXProduct>()
                tagProducts.add(products[index])

                /** we need to add only upto 5 products in asgc carousel  */

                if (index >= MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL)
                    return@forEachIndexed

                    var imageWidth = 0
                    var imageHeight = 0

                    val imageItem = getImageView()
                    feedXMedia.imageView = imageItem
                    imageItem?.run {
                        val postImage = findViewById<ImageUnify>(R.id.post_image)
                        postImage.setImageUrl(feedXMedia.mediaUrl)
                        val layout = findViewById<ConstraintLayout>(R.id.post_image_layout)
                        val layoutLihatProdukParent = findViewById<TextView>(R.id.tv_lihat_product)
                        layoutLihatProdukParent.showWithCondition(tagProducts.isNotEmpty())

                        like_anim.setImageDrawable(
                                MethodChecker.getDrawable(
                                        context,
                                        R.drawable.ic_thumb_filled
                                )
                        )
                            commentButton.invisible()
                            timestampText.hide()
                            seeAllCommentText.hide()
                            val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
                            val topAdsProductName = findViewById<Typography>(R.id.top_ads_product_name)
                            val textViewPrice = findViewById<Typography>(R.id.top_ads_price)
                            val textViewSlashedPrice =
                                    findViewById<Typography>(R.id.top_ads_slashed_price)
                            val labelDiscount = findViewById<Label>(R.id.top_ads_label_discount)
                            val labelCashback = findViewById<Label>(R.id.top_ads_label_cashback)

                            topAdsCard.show()
                            topAdsCard.setOnClickListener {
                                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                listener?.onClickSekSekarang(feedXCard.id, feedXCard.author.id, feedXCard.typename, feedXCard.followers.isFollowed, positionInFeed, feedXCard)
                            }
                                textViewPrice.hide()
                                textViewSlashedPrice.hide()
                                labelDiscount.hide()
                                labelCashback.hide()

                                topAdsProductName.text = if (feedXCard.totalProducts > 1) {
                                    context.getString(R.string.feeds_check_x_products, feedXCard.totalProducts)
                                } else context.getString(R.string.feeds_cek_sekarang)
                                topAdsProductName.setTypeface(null,Typeface.BOLD)
                                topAdsProductName.setTextColor(
                                        MethodChecker.getColor(
                                                context,
                                                unifyPrinciplesR.color.Unify_NN600
                                        )
                                )
                                topAdsProductName.show()
                                val constraintSet = ConstraintSet()
                                constraintSet.clone(topAdsCard)
                                constraintSet.connect(
                                        topAdsProductName.id,
                                        ConstraintSet.TOP,
                                        topAdsCard.id,
                                        ConstraintSet.TOP
                                )
                                constraintSet.connect(
                                        topAdsProductName.id,
                                        ConstraintSet.BOTTOM,
                                        topAdsCard.id,
                                        ConstraintSet.BOTTOM
                                )
                                constraintSet.applyTo(topAdsCard)


                        doOnLayout {
                            imageWidth = width
                            imageHeight = height
                            feedXMedia.tagging.forEachIndexed { index, feedXMediaTagging ->
                                val productTagView = PostTagView(context, feedXMediaTagging)
                                productTagView.postDelayed({
                                    val bitmap = postImage?.drawable?.toBitmap()
                                    productTagView.bindData(listener,
                                            products,
                                            imageWidth,
                                            imageHeight,
                                            positionInFeed,
                                            bitmap)

                                }, TIME_SECOND)


                                layout.addView(productTagView)
                            }

                        }

                        val gd = GestureDetector(
                                context,
                                object : GestureDetector.SimpleOnGestureListener() {
                                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                        var productTagBubbleShowing = false
                                        changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                        listener?.onImageClicked(
                                                postId.toString(),
                                                feedXCard.typename,
                                                feedXCard.followers.isFollowed,
                                                feedXCard.author.id
                                        )

                                        for (i in 0 until layout.childCount) {
                                            var view = layout.getChildAt(i)
                                            if (view is PostTagView) {
                                                val item = (view as PostTagView)
                                                productTagBubbleShowing = item.showExpandedView()
                                            }
                                        }
                                        if (tagProducts.isNotEmpty()) {
                                            if (layoutLihatProdukParent.width.toDp() < LIHAT_PRODUK_EXPANDED_WIDTH_MIN_INDP && !productTagBubbleShowing  ) {
                                                showViewWithAnimation(layoutLihatProdukParent, context)
                                            } else if (!productTagBubbleShowing && layoutLihatProdukParent.width.toDp() >= LIHAT_PRODUK_CONTRACTED_WIDTH_INDP) {
                                                hideViewWithoutAnimation(layoutLihatProdukParent, context)
                                            } else if (productTagBubbleShowing){
                                                showViewWithAnimation(layoutLihatProdukParent, context)
                                            }
                                        }
                                        return true
                                    }

                                    override fun onDown(e: MotionEvent): Boolean {
                                        return true
                                    }

                                    override fun onDoubleTap(e: MotionEvent): Boolean {
                                        var productTagBubbleShowing = false
                                        for (i in 0 until layout.childCount) {
                                            var view = layout.getChildAt(i)
                                            if (view is PostTagView) {
                                                val item = (view as PostTagView)
                                                productTagBubbleShowing = item.hideExpandedViewIfShown()
                                            }
                                        }
                                        if (tagProducts.isNotEmpty()) {
                                             if (!productTagBubbleShowing && layoutLihatProdukParent.width.toDp() > LIHAT_PRODUK_CONTRACTED_WIDTH_INDP) {
                                                hideViewWithoutAnimation(layoutLihatProdukParent, context)
                                            }
                                        }
                                        val pulseFade: Animation =
                                                AnimationUtils.loadAnimation(
                                                        context,
                                                        android.R.anim.fade_in
                                                )
                                        pulseFade.setAnimationListener(object :
                                                Animation.AnimationListener {
                                            override fun onAnimationStart(animation: Animation) {
                                                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                                like_anim.visibility = VISIBLE
                                                listener?.onLikeClick(
                                                        positionInFeed, postId,
                                                        feedXCard.like.isLiked,
                                                        feedXCard.typename,
                                                        feedXCard.followers.isFollowed,
                                                        type = true,
                                                        feedXCard.author.id,
                                                        feedXMedia.type
                                                )
                                            }

                                            override fun onAnimationEnd(animation: Animation) {
                                                like_anim.gone()
                                            }

                                            override fun onAnimationRepeat(animation: Animation) {}
                                        })
                                        if (!feedXCard.isTopAds) {
                                            like_anim.visible()
                                            like_anim.startAnimation(pulseFade)
                                        }
                                        return true
                                    }

                                    override fun onLongPress(e: MotionEvent) {
                                        super.onLongPress(e)
                                        changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                                    }

                                    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                                        return true
                                    }
                                })

                        layoutLihatProdukParent?.setOnClickListener {
                            changeTopadsCekSekarangBtnColorToGreen(feedXCard)
                            listener?.let { listener ->
                                listener.onTagClicked(
                                        postId,
                                        tagProducts,
                                        listener,
                                        feedXCard.author.id,
                                        feedXCard.typename,
                                        feedXCard.followers.isFollowed,
                                        feedXCard.media.firstOrNull()?.type?:"",
                                        positionInFeed,
                                        feedXCard.playChannelID,
                                        feedXCard.author.name
                                )
                            }
                        }
                        setOnTouchListener { v, event ->
                            gd.onTouchEvent(event)
                            true
                        }
                    }
                    if (imageItem != null) {
                        addItem(imageItem)
                    }
            }
            feedXCard.media = mediaList
            feedXCard.tags = feedXCard.products
            resetCaraouselActiveListener(feedXCard)
        }
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
        val listToBeImpressed = feedXCard.products.subList(0, totalProductsImpressed)

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
                gridList.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_3),
                0,
                gridList.getDimens(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_3),
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
            SHOW_MORE,
            feedXCard.appLink,
            feedXCard.totalProducts,
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
                    id = it.id,
                    text = it.name,
                    price = if (it.isDiscount)
                        it.priceDiscountFmt
                    else
                        it.priceFmt,
                    priceOriginal = it.priceFmt,
                    redirectLink = it.appLink,
                    thumbnail = it.coverURL,
                    tagsList = getTagList(it),
                    trackingList = mutableListOf(),
                    index = products.indexOf(it)
                )
            )
        }
        return itemList
    }

    private fun getTagList(feedXProduct: FeedXProduct): MutableList<TagsItem> {
        return if (feedXProduct.isDiscount) {
            val item = TagsItem(
                linkType = "",
                text = feedXProduct.discountFmt,
                type = TYPE_DISCOUNT,
            )
            mutableListOf(item)
        } else {
            mutableListOf()
        }
    }
    private fun ifProductAlreadyPresent(
        product: FeedXProduct,
        tagList: List<FeedXProduct>,
    ): Boolean {
        tagList.forEachIndexed { index, feedXProduct ->
            if (feedXProduct.id == product.id)
                return true
        }
        return false
    }


    private fun bindPublishedAt(publishedAt: String, subTitle: String, isTopadsOrAsgc: Boolean) {
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
        if (isTopadsOrAsgc)
            timestampText.hide()
        else
            timestampText.show()
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

    fun attach(
             model: Visitable<*>? = null
    ) {
        if (model is DynamicPostUiModel) {
            resetCaraouselActiveListener(model?.feedXCard)
        }else if (model is TopadsHeadLineV2Model){
            resetCaraouselActiveListener(model?.feedXCard)
        }
    }

    fun detach(
        fromSlide: Boolean = false, model: Visitable<*>? = null
    ) {

        if (handlerAnim != null) {
            handlerAnim = null
        }
        if (handlerHide != null) {
            handlerHide = null
        }
        if (!fromSlide) {
            if (model is DynamicPostUiModel) {
                carouselView.onActiveIndexChangedListener = null
                model?.feedXCard?.media?.firstOrNull()?.canPlay = false
                model?.feedXCard?.media?.firstOrNull()?.isImageImpressedFirst = true
                model?.feedXCard?.let { hideTaggingOnDetach(it) }
            } else if (model is TopadsHeadLineV2Model) {
                carouselView.onActiveIndexChangedListener = null
                model?.feedXCard?.media?.firstOrNull()?.canPlay = false
                model?.feedXCard?.media?.firstOrNull()?.isImageImpressedFirst = true
                model?.feedXCard?.let { hideTaggingOnDetach(it) }
            }
        }
        if (videoPlayer != null) {
            if (model is DynamicPostUiModel)

            isPaused = true
            if (secondCountDownTimer != null) {
                secondCountDownTimer?.cancel()
                secondCountDownTimer = null
            }
            if (feedAddViewJob != null) {
                feedAddViewJob?.cancel()
                feedAddViewJob = null
            }
            videoPlayer?.pause()
            videoPlayer?.setVideoStateListener(null)
            videoPlayer?.destroy()
            videoPlayer = null
            layout_video?.player = null
        }
    }
    private fun hideTaggingOnDetach(feedXCard: FeedXCard) {
        val cardProducts: List<FeedXProduct> = feedXCard.tags
        val media = if (feedXCard.media.size > feedXCard.lastCarouselIndex) feedXCard.media[feedXCard.lastCarouselIndex] else null
        val imageItem = media?.imageView
        val tags = media?.tagging

        imageItem?.run {

            val layout = findViewById<ConstraintLayout>(R.id.post_image_layout)
            val layoutLihatProdukParent = findViewById<TextView>(R.id.tv_lihat_product)

            val tagProducts = mutableListOf<FeedXProduct>()
            changeTopadsCekSekarangBtnColorToDefaultWhite(feedXCard)

            tags?.map {
                if (!ifProductAlreadyPresent(cardProducts[it.tagIndex], tagProducts))
                    tagProducts.add(cardProducts[it.tagIndex])
            }
            for (i in 0 until layout.childCount) {
                val view = layout.getChildAt(i)
                if (view is PostTagView) {
                    val item = (view as PostTagView)
                    item.hideExpandedViewIfShown()
                }

                if (tagProducts.isNotEmpty()) {
                    if (layoutLihatProdukParent.width.toDp() > LIHAT_PRODUK_CONTRACTED_WIDTH_INDP) {
                        hideViewWithoutAnimation(layoutLihatProdukParent, context)
                    }
                }
            }
        }
    }
    private fun changeTopadsCekSekarangBtnColor(
        feedXCard: FeedXCard,
        ctaButtonBackgroundColor: Int,
        ctaTextColor: Int
    ) {
        val media =
            if (feedXCard.media.size > feedXCard.lastCarouselIndex) feedXCard.media[feedXCard.lastCarouselIndex] else null
        val imageItem = media?.imageView

        imageItem?.run {
            val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
            topAdsCard?.let {
                val topAdsProductName =
                    topAdsCard.findViewById<Typography>(R.id.top_ads_product_name)
                val topAdsChevron = topAdsCard.findViewById<IconUnify>(R.id.chevron)
                topAdsProductName.setTextColor(
                    ctaTextColor
                )
                topAdsChevron.setColorFilter(
                    ctaTextColor
                )
                topAdsCard.setBackgroundColor(
                    ctaButtonBackgroundColor
                )
            }
        }
    }
    private fun changeTopadsCekSekarangBtnColorToGreen(feedXCard: FeedXCard) {
        feedXCard.isAsgcColorChangedToGreen = true
        val backgroundWhiteColor = MethodChecker.getColor(
            context,
            unifyPrinciplesR.color.Unify_G500
        )
        val textColor = MethodChecker.getColor(
            context,
            unifyPrinciplesR.color.Unify_N0
        )
        changeTopadsCekSekarangBtnColor(feedXCard, backgroundWhiteColor, textColor)

    }


    fun changeTopadsCekSekarangBtnColorToDefaultWhite(feedXCard: FeedXCard) {
        feedXCard.isAsgcColorChangedToGreen = false
        val backgroundWhiteColor = MethodChecker.getColor(
            context,
            unifyPrinciplesR.color.Unify_NN50
        )
        val textColor = MethodChecker.getColor(
            context,
            unifyPrinciplesR.color.Unify_NN600
        )
        changeTopadsCekSekarangBtnColor(feedXCard, backgroundWhiteColor, textColor)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        videoPlayer?.destroy()
    }

    fun playVideo(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
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
    fun playVOD(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
        if (videoPlayer == null)
            feedXCard.media[position].canPlay = true
        if (feedXCard.media[position].canPlay) {
            setVODControl(
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

    fun setVideo(isFragmentVisible: Boolean) {
        if (isFragmentVisible)
            videoPlayer?.resume()
        else
            videoPlayer?.pause()
    }
    private fun resetCaraouselActiveListener(feedXCard: FeedXCard?){
        carouselView.apply {
            if (onActiveIndexChangedListener == null) {
                onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                    override fun onActiveIndexChanged(prev: Int, current: Int) {
                        pageControl.setCurrentIndicator(current)
                        feedXCard?.lastCarouselIndex = current
                        if (feedXCard?.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT) {
                            val list = mutableListOf<FeedXProduct>()
                            list.add(feedXCard.products[current])
                            if (list.isNotEmpty())
                            imagePostListener.userProductImpression(
                                    positionInFeed,
                                    feedXCard.id,
                                    feedXCard.typename,
                                    feedXCard.author.id,
                                    list
                            )

                            changeTopadsCekSekarangBtnColorToGreen(feedXCard)

                            if (feedXCard.media.isNotEmpty() && feedXCard.media.size > current)
                            bindImage(feedXCard.products, feedXCard.media[current], feedXCard)
                        } else if (feedXCard != null) {
                            if (feedXCard.media.isNotEmpty() && feedXCard.media.size > current) {
                                imagePostListener.userCarouselImpression(
                                        feedXCard.id,
                                        feedXCard.media[current],
                                        current,
                                        feedXCard.typename,
                                        feedXCard.followers.isFollowed,
                                        feedXCard.author.id,
                                        positionInFeed,
                                        feedXCard.cpmData,
                                        feedXCard.listProduct
                                )

                                if (feedXCard.media[current].type == TYPE_IMAGE) {
                                    videoPlayer?.pause()
                                    bindImage(feedXCard.tags, feedXCard.media[current], feedXCard)
                                } else {
                                    detach(true)
                                    feedXCard.media[current].canPlay = true
                                    playVideo(feedXCard, current)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun bindImage(cardProducts: List<FeedXProduct>, media: FeedXMedia, feedXCard: FeedXCard) {
        val imageItem = media.imageView
        val tags = media.tagging
        val tagProducts = mutableListOf<FeedXProduct>()

        tags.map {
            if (!ifProductAlreadyPresent(cardProducts[it.tagIndex], tagProducts))
                tagProducts.add(cardProducts[it.tagIndex])
        }
        imageItem?.run {
            val layout = findViewById<ConstraintLayout>(R.id.post_image_layout)
            val layoutLihatProdukParent = findViewById<TextView>(R.id.tv_lihat_product)
            val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
            val startColor = MethodChecker.getColor(
                context,
                unifyPrinciplesR.color.Unify_NN50
            )
            val endColor = MethodChecker.getColor(
                context,
                unifyPrinciplesR.color.Unify_G500
            )
            val isTypeNewASGC = feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && feedXCard.mods.contains(TYPE_USE_ASGC_NEW_DESIGN)

            if ((isTypeNewASGC || feedXCard.isTopAds) && !feedXCard.isAsgcColorChangedToGreen) {
                topAdsCard?.let {
                    if (changeBgColorAnim == null)
                        changeBgColorAnim = handlerFeed
                    changeBgColorAnim?.postDelayed({
                        topAdsCard.findViewById<Typography>(R.id.top_ads_product_name).setTextColor(
                            MethodChecker.getColor(
                                context,
                                unifyPrinciplesR.color.Unify_N0
                            )
                        )
                        topAdsCard.findViewById<IconUnify>(R.id.chevron).setColorFilter(
                            MethodChecker.getColor(
                                context,
                                unifyPrinciplesR.color.Unify_N0
                            )
                        )
                           if(!feedXCard.isAsgcColorChangedToGreen) {
                               changeBackgroundColorAnimation(startColor, endColor, topAdsCard)
                               feedXCard.isAsgcColorChangedToGreen = true
                           }

                    }, TIME_TWO_SEC)
                }
            } else {
                changeTopadsCekSekarangBtnColorToGreen(feedXCard)
            }
            for (i in 0 until layout.childCount) {
                val view = layout.getChildAt(i)
                if (view is PostTagView) {
                    val item = (view as PostTagView)
                    item.resetView()
                }
            }
            if (tagProducts.isEmpty()) {
                layoutLihatProdukParent.gone()
            } else {
                hideViewWithAnimation(layoutLihatProdukParent, context)
            }

            if (handlerAnim == null) {
                handlerAnim = handlerFeed
            }
            handlerAnim?.postDelayed({
                if (tagProducts.isNotEmpty()) {
                    showViewWithAnimation(layoutLihatProdukParent, context)
                }
            }, TIME_SECOND)

            if (handlerHide == null) {
                handlerHide = handlerFeed
            }
            handlerHide?.postDelayed({
                if (!shouldContinueToShowLihatProduct(layout) && tagProducts.isNotEmpty()) {
                    hideViewWithoutAnimation(layoutLihatProdukParent, context)
                }
                },TIME_FOUR_SEC)
        }
    }

    private fun getImageView(): View? {
        val imageItem = View.inflate(context, R.layout.item_post_image_new, null)
        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imageItem.layoutParams = param
        return imageItem
    }

    private fun getVideoItem(): View? {
        val videoItem = View.inflate(context, R.layout.item_post_video_new, null)
        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        videoItem?.layoutParams = param
        return videoItem
    }
    private fun getVODItem(): View? {
        val videoItem = View.inflate(context, R.layout.item_post_long_video_vod, null)
        val param = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        videoItem?.layoutParams = param
        return videoItem
    }

    private fun shouldContinueToShowLihatProduct(layout: ConstraintLayout) : Boolean{
        var isInflatedBubbleShowing = false
        for (i in 0 until layout.childCount) {
            var view = layout.getChildAt(i)
            if (view is PostTagView) {
                val item = (view as PostTagView)
                isInflatedBubbleShowing = item.getExpandedViewVisibility()
            }
        }
        return isInflatedBubbleShowing
    }

    private fun sendHeaderTopadsEvent(positionInFeed: Int, appLink: String, cpmData: CpmData, isNewVariant: Boolean) {
        topAdsListener?.onTopAdsHeadlineAdsClick(positionInFeed, appLink, cpmData, isNewVariant)
    }
    private fun getOrientation(mediaRatio: FeedXMediaRatio): Int {
        if (mediaRatio.width > mediaRatio.height)
            return LANDSCAPE
        return PORTRAIT
    }
    private fun getRatioIfPortrait(mediaRatio: FeedXMediaRatio):String{
        val ratio = round((mediaRatio.width.toFloat() / mediaRatio.height) * ROUND_OFF_TO_ONE_DECIMAL_VALUE) / ROUND_OFF_TO_ONE_DECIMAL_VALUE
        return if (ratio <= MEDIA_RATIO_PORTRAIT_THRESHOLD_FLOAT)
            VOD_VIDEO_RATIO
        else if (ratio > MEDIA_RATIO_PORTRAIT_THRESHOLD_FLOAT && ratio < MEDIA_RATIO_SQUARE_VALUE_FLOAT)
            ratio.toString() //original ratio
        else
            SQUARE_RATIO

    }
    private fun getRatioIfLandscape(mediaRatio: FeedXMediaRatio):String{

        val ratio = round((mediaRatio.width.toFloat() / mediaRatio.height) * ROUND_OFF_TO_ONE_DECIMAL_VALUE) / ROUND_OFF_TO_ONE_DECIMAL_VALUE
        return if (ratio >= MEDIA_RATIO_LANDSCAPE_THRESHOLD_FLOAT)
            LONG_VIDEO_RATIO
        else if (ratio > MEDIA_RATIO_SQUARE_VALUE_FLOAT && ratio < MEDIA_RATIO_LANDSCAPE_THRESHOLD_FLOAT)
            ratio.toString() //original ratio
        else
            SQUARE_RATIO
    }

}