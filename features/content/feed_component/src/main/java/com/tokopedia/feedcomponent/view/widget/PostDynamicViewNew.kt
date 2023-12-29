package com.tokopedia.feedcomponent.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXComments
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXLike
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMediaRatio
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_IMAGE
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.feedcomponent.presentation.utils.FeedXCardSubtitlesAnimationHandler
import com.tokopedia.feedcomponent.util.FeedNestedScrollableHost
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.util.bold
import com.tokopedia.feedcomponent.util.buildSpannedString
import com.tokopedia.feedcomponent.util.caption.FeedCaption
import com.tokopedia.feedcomponent.util.util.hideViewWithAnimation
import com.tokopedia.feedcomponent.util.util.hideViewWithoutAnimation
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedcomponent.util.util.showViewWithAnimation
import com.tokopedia.feedcomponent.view.adapter.post.FeedPostCarouselAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.CarouselImageViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.CarouselVideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineListener
import com.tokopedia.feedcomponent.view.transition.BackgroundColorTransition
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridItemModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.net.URLEncoder
import kotlin.math.round
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

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
private var productVideoJob: Job? = null
private const val TIME_THREE_SEC = 3000L
private const val MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL = 5
private const val ROUND_OFF_TO_ONE_DECIMAL_VALUE = 10
private const val TIME_FIVE_SEC = 5000L


private const val TIME_SECOND = 1000L
private const val MINUTE_IN_HOUR = 60
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
private const val ASGC_NEW_PRODUCTS = "asgc_new_products"
private const val ASGC_RESTOCK_PRODUCTS = "asgc_restock_products"
private const val ASGC_DISCOUNT_TOKO = "asgc_discount_toko"
private const val ASGC_FLASH_SALE_TOKO = "asgc_flash_sale_toko"
private const val ASGC_RILISAN_SPECIAL = "asgc_rilisan_spesial"

private const val FOCUS_CTA_DELAY = 2000L
private const val VIEWS_START_VALUE = 14


/**
 * LIHAT_PRODUK_EXPANDED_WIDTH, LIHAT_PRODUK_CONTRACTED_WIDTH Value is fixed width  for Lihat Produk (item_post_image_new ,id = tv_lihat_product)
 *Lihat Produk Value is static so we have fixed it width to Keep our animation intact
 *Do not manipulate this value unless Lihat Produk text change
 **/
private const val LIHAT_PRODUK_CONTRACTED_WIDTH_INDP = 32
const val PORTRAIT = 1
const val LANDSCAPE = 2

class PostDynamicViewNew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main.immediate + job)

    private val view = LayoutInflater.from(context).inflate(
        R.layout.item_post_dynamic_new_content,
        this,
        true
    )
    private val authorAvatar: ImageUnify = findViewById(R.id.iv_author_avatar)
    private val shopBadge: ImageUnify = findViewById(R.id.iv_shop_badge)
    private val authorName: Typography = findViewById(R.id.tv_author_name)
    private val authorFollowAction: Typography = findViewById(R.id.tv_author_follow_action)
    private val contentSubInfo: Typography = findViewById(R.id.tv_content_sub_info)
    private val headerMenu: IconUnify = findViewById(R.id.menu_button)
    private val rvCarousel: RecyclerView = findViewById(R.id.rv_carousel)
    private val feedVODViewHolder: FeedVODViewHolder = findViewById(R.id.feed_vod_viewholder)
    private val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
    private val asgcCtaProductName = findViewById<Typography>(R.id.top_ads_product_name)
    private val asgcProductCampaignCopywritingContainer =
        findViewById<FrameLayout>(R.id.top_ads_campaign_copywriting_container)
    private val asgcProductCampaignCopywritingFirst =
        findViewById<Typography>(R.id.top_ads_campaign_copywriting_first)
    private val asgcProductCampaignCopywritingSecond =
        findViewById<Typography>(R.id.top_ads_campaign_copywriting_second)
    private val topAdsChevron = topAdsCard.findViewById<IconUnify>(R.id.chevron)
    private val pageControl: PageControl = findViewById(R.id.page_indicator)
    private val likeButton: IconUnify = findViewById(R.id.like_button)
    private val commentButton: IconUnify = findViewById(R.id.comment_button)
    private val shareButton: IconUnify = findViewById(R.id.share_button)
    private val likedText: Typography = findViewById(R.id.liked_text)
    private val captionText: Typography = findViewById(R.id.caption_text)
    private val timestampText: Typography = findViewById(R.id.timestamp_text)
    private val commentUserImage1: ImageUnify = findViewById(R.id.comment_user_image1)
    private val commentUserImage2: ImageUnify = findViewById(R.id.comment_user_image2)
    private val commentText1: Typography = findViewById(R.id.comment_text1)
    private val commentText2: Typography = findViewById(R.id.comment_text2)
    private val likeButton1: IconUnify = findViewById(R.id.like_button1)
    private val likeButton2: IconUnify = findViewById(R.id.like_button2)
    private val seeAllCommentText: Typography = findViewById(R.id.see_all_comment_text)
    private val userImage: ImageUnify = findViewById(R.id.user_image)
    private val addCommentHint: Typography = findViewById(R.id.comment_hint)
    private val gridList: RecyclerView = findViewById(R.id.gridList)
    private val scrollHostCarousel: FeedNestedScrollableHost = findViewById(R.id.scroll_host_carousel)

    private var listener: DynamicPostViewHolder.DynamicPostListener? = null
    private var videoListener: VideoViewHolder.VideoViewListener? = null
    private lateinit var gridPostListener: GridPostAdapter.GridItemListener
    private lateinit var imagePostListener: ImagePostViewHolder.ImagePostListener
    private var topAdsListener: TopAdsHeadlineListener? = null
    private var positionInFeed: Int = 0
    private var videoPlayer: FeedExoPlayer? = null
    private var handlerAnim: Handler? = null
    private var handlerHide: Handler? = null
    private var changeBgColorAnim: Handler? = null
    private var feedAddViewJob: Job? = null

    private var shouldResumeVideoPLayerOnBack = true

    private var mData = FeedXCard()

    private var topAdsJob: Job? = null

    private var animationHandler: FeedXCardSubtitlesAnimationHandler? = null

    private val adapter = FeedPostCarouselAdapter(
        dataSource = object : FeedPostCarouselAdapter.DataSource {
            override fun getFeedXCard(): FeedXCard {
                return mData
            }

            override fun getTagBubbleListener(): PostTagView.TagBubbleListener? {
                return object : PostTagView.TagBubbleListener {
                    override fun onPostTagBubbleClick(
                        positionInFeed: Int,
                        redirectUrl: String,
                        postTagItem: FeedXProduct,
                        adClickUrl: String
                    ) {
                        listener?.onPostTagBubbleClick(
                            positionInFeed,
                            redirectUrl,
                            postTagItem,
                            adClickUrl
                        )
                    }

                }
            }

            override fun getPositionInFeed(): Int {
                return positionInFeed
            }
        },
        imageListener = object : CarouselImageViewHolder.Listener {
            override fun onTopAdsCardClicked(
                viewHolder: CarouselImageViewHolder,
                media: FeedXMedia,
            ) {
                if (mData.isTopAds) {
                    RouteManager.route(
                        context,
                        media.appLink,
                    )
                }

                if (mData.isTypeProductHighlight && mData.useASGCNewDesign) {
                    listener?.onClickSekSekarang(
                        mData.id,
                        mData.author.id,
                        mData.typename,
                        mData.followers.isFollowed,
                        mData.hasVoucher,
                        positionInFeed,
                        mData,
                    )
                } else {
                    listener?.onClickSekSekarang(
                        mData.id,
                        mData.shopId,
                        TYPE_TOPADS_HEADLINE_NEW,
                        mData.followers.isFollowed,
                        mData.hasVoucher,
                        positionInFeed,
                        mData,
                    )
                }

            }

            override fun onImageClicked(viewHolder: CarouselImageViewHolder) {
                changeCTABtnColorAsPerWidget(mData)
                listener?.onImageClicked(
                    mData
                )
            }

            override fun onImageDoubleClicked(viewHolder: CarouselImageViewHolder) {
                val card = mData
                if (!card.isTopAds) changeCTABtnColorAsPerWidget(card)
            }

            override fun onImageLongClicked(viewHolder: CarouselImageViewHolder) {
                changeCTABtnColorAsPerWidget(mData)
            }

            override fun onLiked(viewHolder: CarouselImageViewHolder) {
                listener?.onLikeClick(
                    positionInFeed,
                    mData.id.toLongOrZero(),
                    mData.like.isLiked,
                    mData.typename,
                    mData.followers.isFollowed,
                    type = true,
                    mData.author.id,
                    mData.type,
                    authorType = mData.author.type.toString()
                )
            }

            override fun onImpressed(viewHolder: CarouselImageViewHolder) {
                imagePostListener.userImagePostImpression(
                    positionInFeed,
                    pageControl.indicatorCurrentPosition,
                )

                val data = mData
                val position = viewHolder.adapterPosition

                if (position == RecyclerView.NO_POSITION) return

                if (data.isTypeProductHighlight) {

                    if (data.products.isEmpty() ||
                        data.products.size <= position
                    ) return

                    imagePostListener.userProductImpression(
                        positionInFeed,
                        data.id,
                        data.typename,
                        data.author.id,
                        data.followers.isFollowed,
                        listOf(data.products[position])
                    )
                } else {
                    if (data.media.isEmpty() ||
                        data.media.size <= position
                    ) return

                    imagePostListener.userCarouselImpression(
                        data,
                        positionInFeed
                    )
                }
            }

            override fun onLihatProductClicked(
                viewHolder: CarouselImageViewHolder,
                media: FeedXMedia,
            ) {
                changeCTABtnColorAsPerWidget(mData)

                val listener = this@PostDynamicViewNew.listener ?: return

                listener.onTagClicked(
                    mData,
                    media.tagProducts,
                    listener,
                    media.type,
                    positionInFeed,
                )
            }
        },
        videoListener = object : CarouselVideoViewHolder.Listener {
            override fun onLihatProductClicked(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia
            ) {
                val listener = this@PostDynamicViewNew.listener ?: return

                listener.onTagClicked(
                    mData,
                    media.tagProducts,
                    listener,
                    media.type,
                    positionInFeed,
                )
            }

            override fun onMuteChanged(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia,
                isMuted: Boolean
            ) {
                listener?.muteUnmuteVideo(
                    mData,
                    isMuted,
                    positionInFeed,
                    media.type,
                )
            }

            override fun onVideoSurfaceTapped(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia,
                isMuted: Boolean
            ) {
                listener?.muteUnmuteVideo(
                    mData,
                    isMuted,
                    positionInFeed,
                    media.type,
                )

            }

            override fun onVideoStopTrack(viewHolder: CarouselVideoViewHolder, lastPosition: Long) {
                videoListener?.onVideoStopTrack(
                    mData,
                    lastPosition,
                )
            }
        },
        listener = object : FeedCampaignListener {
            override fun onTimerFinishUpcoming() {
                listener?.changeUpcomingWidgetToOngoing(mData, positionInFeed)
            }

            override fun onTimerFinishOngoing() {
                listener?.removeOngoingCampaignSaleWidget(mData, positionInFeed)

            }

            override fun onReminderBtnClick(isReminderSet: Boolean, positionInFeed: Int) {
                listener?.onIngatkanSayaBtnClicked(mData, positionInFeed)
            }

        }
    )
    private val snapHelper = PagerSnapHelper()
    private val pageControlListener = object : RecyclerView.OnScrollListener() {

        private val layoutManager = rvCarousel.layoutManager as LinearLayoutManager

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val snappedView = snapHelper.findSnapView(layoutManager) ?: return

            val position = layoutManager.getPosition(snappedView)
            pageControl.setCurrentIndicator(position)

            if (mData.lastCarouselIndex != position) changeCTABtnColorAsPerWidget(mData)
            mData.lastCarouselIndex = position
        }
    }

    private val onMediaFocusedListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState != RecyclerView.SCROLL_STATE_IDLE) return
            val layoutManager = recyclerView.layoutManager ?: return
            val snappedView = snapHelper.findSnapView(layoutManager) ?: return

            adapter.focusItemAt(
                layoutManager.getPosition(snappedView)
            )
        }
    }

    init {
        (context as LifecycleOwner).lifecycle.addObserver(this)

        snapHelper.attachToRecyclerView(rvCarousel)
        rvCarousel.addOnScrollListener(pageControlListener)
        rvCarousel.addOnScrollListener(onMediaFocusedListener)
        rvCarousel.adapter = adapter
    }

    fun bindData(
        dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
        gridItemListener: GridPostAdapter.GridItemListener,
        videoListener: VideoViewHolder.VideoViewListener,
        adapterPosition: Int,
        userSession: UserSessionInterface,
        feedXCard: FeedXCard,
        imagePostListener: ImagePostViewHolder.ImagePostListener,
        topAdsListener: TopAdsHeadlineListener? = null
    ) {
        mData = feedXCard
        this.listener = dynamicPostListener
        this.gridPostListener = gridItemListener
        this.videoListener = videoListener
        this.positionInFeed = adapterPosition
        this.imagePostListener = imagePostListener
        this.topAdsListener = topAdsListener
        bindFollow(feedXCard)
        bindItems(feedXCard)
        bindCaption(feedXCard)
        bindPublishedAt(feedXCard.publishedAt)
        bindTopAds(feedXCard)
        bindLike(feedXCard)
        bindComment(
            feedXCard.comments,
            userSession.profilePicture,
            userSession.name,
            feedXCard.id,
            feedXCard.author.type,
            feedXCard.author.id,
            feedXCard.typename,
            feedXCard.followers.isFollowed,
            feedXCard.media.firstOrNull()?.type ?: "",
            feedXCard.playChannelID
        )
        bindTracking(feedXCard)
        shareButton.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)

            val desc = context.getString(contentCommonR.string.feed_share_default_text)
            val url =
                if (feedXCard.isTopAds && feedXCard.media.size > feedXCard.lastCarouselIndex) {
                    feedXCard.media[feedXCard.lastCarouselIndex].webLink
                } else {
                    feedXCard.appLink
                }
            val mediaUrl =
                if (feedXCard.isTypeProductHighlight) feedXCard.products.firstOrNull()?.coverURL
                    ?: ""
                else feedXCard.media.firstOrNull()?.mediaUrl ?: ""

            listener?.onShareClick(
                positionInFeed,
                feedXCard.id,
                feedXCard.author.name + " post",
                desc.replace("%s", feedXCard.author.name),
                url = url,
                mediaUrl,
                feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                feedXCard.author.id,
                feedXCard.media.firstOrNull()?.type ?: "",
                feedXCard.isTopAds,
                feedXCard.playChannelID,
                feedXCard.webLink
            )
        }
    }

    private fun bindTracking(feedXCard: FeedXCard) {
        addOnImpressionListener(feedXCard.impressHolder) {
            val isTypeNewASGC =
                feedXCard.typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && feedXCard.mods.contains(
                    TYPE_USE_ASGC_NEW_DESIGN
                )

            if (feedXCard.typename == TYPE_FEED_X_CARD_POST || feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW || feedXCard.typename == TYPE_FEED_X_CARD_VOD || isTypeNewASGC) {
                imagePostListener.userCarouselImpression(
                    feedXCard,
                    positionInFeed
                )
            }

            if (feedXCard.typename == TYPE_FEED_X_CARD_POST || feedXCard.typename == TYPE_TOPADS_HEADLINE_NEW || feedXCard.typename == TYPE_FEED_X_CARD_VOD || isTypeNewASGC) {
                listener?.onImpressionTracking(feedXCard, positionInFeed)
            }

        }
    }

    private fun bindTopAds(feedXCard: FeedXCard) {
        asgcCtaProductName.text = getCTAButtonText(feedXCard)
        val ctaSubtitle = getCTAButtonSubtitle(feedXCard)

        ctaSubtitle.mapIndexed { index, item ->
            if (index == ZERO) {
                asgcProductCampaignCopywritingFirst.text = item
            } else if (index == ONE) {
                asgcProductCampaignCopywritingSecond.text = item
            }
        }

        if (ctaSubtitle.size >= TWO && shouldShowCtaSubtitile(ctaSubtitle)) {
            animationHandler = FeedXCardSubtitlesAnimationHandler(
                WeakReference(asgcProductCampaignCopywritingFirst),
                WeakReference(asgcProductCampaignCopywritingSecond)
            )
            animationHandler?.subtitles = ctaSubtitle
            animationHandler?.checkToCancelTimer()
            animationHandler?.startTimer()
        } else if (animationHandler != null) {
            animationHandler?.stopAnimation()
        }
        asgcProductCampaignCopywritingContainer.showWithCondition(shouldShowCtaSubtitile(ctaSubtitle))

        topAdsCard.showWithCondition(
            shouldShow = (feedXCard.isTypeProductHighlight || feedXCard.isTopAds) &&
                feedXCard.media.any { it.isImage }
        )

        topAdsCard.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)

            val appLink = try {
                feedXCard.media[pageControl.indicatorCurrentPosition].appLink
            } catch (e: IndexOutOfBoundsException) {
                null
            }

            if (feedXCard.isTopAds && appLink != null) {
                RouteManager.route(context, appLink)
            }

            if (feedXCard.isTypeProductHighlight && feedXCard.useASGCNewDesign) {
                listener?.onClickSekSekarang(
                    feedXCard.id,
                    feedXCard.author.id,
                    feedXCard.typename,
                    feedXCard.followers.isFollowed,
                    feedXCard.hasVoucher,
                    positionInFeed,
                    feedXCard,
                )
            } else {
                listener?.onClickSekSekarang(
                    feedXCard.id,
                    feedXCard.shopId,
                    TYPE_TOPADS_HEADLINE_NEW,
                    feedXCard.followers.isFollowed,
                    feedXCard.hasVoucher,
                    positionInFeed,
                    feedXCard,
                )
            }
        }
    }

    private fun shouldShowCtaSubtitile(subtitle: List<String>) =
        subtitle.isNotEmpty()

    fun bindLike(feedXCard: FeedXCard) {
        val isLongVideo =
            feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO

        if (feedXCard.typename == TYPE_FEED_X_CARD_VOD || isLongVideo) {
            bindViews(feedXCard)
        } else {
            bindLikeData(feedXCard)
        }
    }

    fun bindFollow(feedXCard: FeedXCard) {
        bindHeader(feedXCard)
    }

    private fun bindContentSubInfo(shouldShow: Boolean, value: String) {
        contentSubInfo.showWithCondition(shouldShow)
        contentSubInfo.text = value
    }

    private fun bindHeader(
        feedXCard: FeedXCard
    ) {
        val activityId = feedXCard.id
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
        val channelId = feedXCard.playChannelID
        val isFollowed = followers.isFollowed
        val count = followers.count
        val isVideo = mediaType != TYPE_IMAGE

        /**
         * todo: create simpler logic & do it in mapper class instead
         */

        //region bind content sub info
        val contentSubInfoValue = if (isTopads) {
            context.getString(contentCommonR.string.feeds_ads_text)
        } else if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT) {
            when (feedXCard.type) {
                ASGC_NEW_PRODUCTS -> context.getString(contentCommonR.string.feeds_asgc_new_product_text)
                ASGC_RESTOCK_PRODUCTS -> context.getString(contentCommonR.string.feeds_asgc_restock_text)
                ASGC_DISCOUNT_TOKO -> context.getString(contentCommonR.string.feed_asgc_diskon_toko)
                ASGC_FLASH_SALE_TOKO -> context.getString(contentCommonR.string.feed_asgc_flash_sale_toko)
                ASGC_RILISAN_SPECIAL -> context.getString(contentCommonR.string.feed_asgc_rilisan_special)
                else -> String.EMPTY
            }
        } else {
            if (count >= FOLLOW_COUNT_THRESHOLD) {
                String.format(
                    context.getString(contentCommonR.string.feed_header_follow_count_text),
                    count.productThousandFormatted()
                )
            } else context.getString(contentCommonR.string.feed_header_follow_count_less_text)
        }
        bindContentSubInfo(
            shouldShow = type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT
                || ((!isFollowed || followers.transitionFollow)
                && !(type == TYPE_FEED_X_CARD_POST && author.type == 3)),
            value = contentSubInfoValue
        )
        //endregion

        authorAvatar.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())

        //region author info
        val activityName = ""
        val authorType = if (author.type == 3) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val authorId =
            if (authorType == FollowCta.AUTHOR_USER) author.encryptedUserId else author.id

        val followCta = FollowCta(
            authorID = authorId,
            authorType = authorType,
            isFollow = isFollowed
        )
        this.authorName.text = MethodChecker.fromHtml(author.name)
        this.authorName.setOnClickListener {
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
            sendHeaderTopadsEvent(positionInFeed, author.appLink, cpmData, true)
        }
        val textFollowAction = if (followers.transitionFollow || followers.isFollowed) {
            context.getString(contentCommonR.string.kol_action_following_color)
        } else {
            context.getString(contentCommonR.string.kol_action_follow_color)
        }
        if (!isFollowed || followers.transitionFollow) {
            this.authorFollowAction.text = MethodChecker.fromHtml(
                "${context.getString(contentCommonR.string.feed_header_separator)}$textFollowAction"

            )
            this.authorFollowAction.setOnClickListener {
                if (isTopads) {
                    listener?.onFollowClickAds(positionInFeed, shopId, adId)
                } else {
                    listener?.onHeaderActionClick(
                        positionInFeed, authorId,
                        authorType, isFollowed, type, isVideo
                    )
                }
            }
            this.authorFollowAction.show()
        } else {
            this.authorFollowAction.hide()
        }
        followers.transitionFollow = false
        //endregion

        shopBadge.setOnClickListener {
            sendHeaderTopadsEvent(positionInFeed, author.appLink, cpmData, true)
        }
        authorAvatar.setOnClickListener {
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
            sendHeaderTopadsEvent(positionInFeed, author.appLink, cpmData, true)
        }
        headerMenu.setOnClickListener {
            listener?.onMenuClick(
                positionInFeed,
                activityId,
                reportable,
                deletable,
                true,
                isFollowed,
                authorId,
                authorType,
                type,
                mediaType,
                caption,
                channelId
            )
        }
    }

    private fun bindViews(feedXCard: FeedXCard) {

        val view = feedXCard.views
        if (feedXCard.like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (view.count != 0) {
            likedText.show()

            likedText.text =
                MethodChecker.fromHtml(
                    context.getString(
                        contentCommonR.string.feed_component_viewed_count_text,
                        view.count.productThousandFormatted(1)
                    )
                )
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)
            listener?.onLikeClick(
                positionInFeed,
                feedXCard.id.toLongOrZero(),
                feedXCard.like.isLiked,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                shopId = feedXCard.author.id,
                mediaType = feedXCard.media.firstOrNull()?.type ?: "",
                playChannelId = feedXCard.playChannelID,
                authorType = feedXCard.author.type.toString()
            )
        }

    }

    private fun bindLikeData(feedXCard: FeedXCard) {
        val like: FeedXLike = feedXCard.like
        val id = feedXCard.id.toLongOrZero()
        val mediaType: String = feedXCard.media.firstOrNull()?.type ?: ""

        if (like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (like.likedBy.isNotEmpty() || like.count != 0) {
            likedText.show()
            if (like.likedBy.isEmpty()) {
                if (like.isLiked) {
                    if (like.count == 1) {
                        likedText.text =
                            context.getString(contentCommonR.string.feed_component_liked_count_text_only_me)
                    } else
                        likedText.text =
                            MethodChecker.fromHtml(
                                context.getString(
                                    contentCommonR.string.feed_component_liked_by_text_me,
                                    (like.count - 1).productThousandFormatted(1)
                                )
                            )
                } else
                    likedText.text =
                        MethodChecker.fromHtml(
                            context.getString(
                                contentCommonR.string.feed_component_liked_count_text,
                                like.count.productThousandFormatted(1)
                            )
                        )
            } else {
                likedText.text = MethodChecker.fromHtml(
                    context.getString(
                        contentCommonR.string.feed_component_liked_by_text,
                        getLikedByText(like.likedBy),
                        like.count.productThousandFormatted(1)
                    )
                )
            }
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)

            listener?.onLikeClick(
                positionInFeed,
                id,
                like.isLiked,
                feedXCard.typename,
                feedXCard.followers.isFollowed,
                shopId = feedXCard.author.id,
                mediaType = mediaType,
                authorType = feedXCard.author.type.toString()
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
        captionText.shouldShowWithAction(caption.text.isNotEmpty()) {
            val authorType = if (caption.author.type == 1) {
                FollowCta.AUTHOR_USER
            } else FollowCta.AUTHOR_SHOP
            val followCta = FollowCta(
                authorID = caption.author.id,
                authorType = authorType,
                isFollow = caption.followers.isFollowed
            )

            val authorCaption = FeedCaption.Author(
                name = caption.author.name,
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN800
                ),
                typeface = getTypeface(
                    context,
                    FeedCaption.ROBOTO_BOLD
                ),
                clickListener = {
                    listener?.onAvatarClick(
                        positionInFeed,
                        caption.author.appLink,
                        if (caption.typename == TYPE_FEED_X_CARD_VOD) caption.playChannelID else caption.id,
                        "",
                        followCta,
                        caption.typename,
                        caption.followers.isFollowed,
                        caption.author.id,
                        caption.media.firstOrNull()?.type ?: "",
                        true
                    )
                }
            )
            val tagCaption = FeedCaption.Tag(
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                ),
                clickListener = {
                    onHashtagClicked(it, caption)
                }
            )

            val captionBody = FeedCaption.Builder(caption.text)
                .withAuthor(authorCaption)
                .withTag(tagCaption)
                .build()

            val readMoreCaption = FeedCaption.ReadMore(
                maxTrimChar = MAX_CHAR,
                label = context.getString(contentCommonR.string.feed_component_read_more_button),
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                ),
                clickListener = {
                    captionText.setText(captionBody, TextView.BufferType.SPANNABLE)
                }
            )
            val trimmedCaption = FeedCaption.Builder(caption.text)
                .withAuthor(authorCaption)
                .withTag(tagCaption)
                .trimCaption(readMoreCaption)
                .build()

            captionText.setText(trimmedCaption, TextView.BufferType.SPANNABLE)
            captionText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onHashtagClicked(hashtag: String, feed: FeedXCard) {
        listener?.onHashtagClickedFeed(hashtag, feed)
        val encodeHashtag = URLEncoder.encode(hashtag, "UTF-8")
        RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
    }

    private fun bindComment(
        comments: FeedXComments,
        profilePicture: String,
        name: String,
        id: String,
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
        addCommentHint.hint = context.getString(contentCommonR.string.feed_component_add_comment, name)

        var authId = ""
        if (authorType != 1)
            authId = authorId
        commentButton.setOnClickListener {
            listener?.onCommentClick(
                positionInFeed,
                id,
                authId,
                type,
                isFollowed,
                mediaType,
                playChannelId = playChannelId,
                isClickIcon = true
            )
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClick(
                positionInFeed,
                id,
                authId,
                type,
                isFollowed,
                mediaType,
                playChannelId = playChannelId,
                isClickIcon = false
            )
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClick(
                positionInFeed,
                id,
                authId,
                type,
                isFollowed,
                mediaType,
                playChannelId = playChannelId,
                isClickIcon = true
            )
        }
    }

    fun setCommentCount(comments: FeedXComments) {
        seeAllCommentText.showWithCondition(comments.count != 0)
        seeAllCommentText.text =
            context.getString(contentCommonR.string.feed_component_see_all_comments, comments.countFmt)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindItems(
        feedXCard: FeedXCard,
    ) {
        val media = feedXCard.media
        if (!feedXCard.isTypeProductHighlight && !feedXCard.isTypeVOD) {
            if (media.isNotEmpty() && media.first().type == TYPE_LONG_VIDEO) {
                setVODLayout(feedXCard)
            } else {
                val globalCardProductList = feedXCard.tags
                gridList.gone()
                feedVODViewHolder.gone()
                rvCarousel.visible()
                commentButton.visible()
                pageControl.apply {
                    setIndicator(media.size)
                    setCurrentIndicator(feedXCard.lastCarouselIndex)
                }.showWithCondition(media.size > 1)

                media.forEach { feedMedia ->
                    val tags = feedMedia.tagging
                    feedMedia.tagProducts = tags.map { globalCardProductList[it.tagIndex] }
                        .distinctBy { it.id }
                    feedMedia.isImageImpressedFirst = true

                    if (!feedMedia.isImage) feedMedia.canPlay = false
                }
                adapter.setItemsAndAnimateChanges(media)
                rvCarousel.addOneTimeGlobalLayoutListener {
                    rvCarousel.scrollToPosition(feedXCard.lastCarouselIndex)
                }

                if (feedXCard.isTopAds) {
                    likedText.hide()
                    captionText.hide()
                    commentButton.invisible()
                    likeButton.invisible()
                    seeAllCommentText.hide()
                    headerMenu.hide()
                }
            }
        } else if (feedXCard.isTypeVOD) {
            setVODLayout(feedXCard)
        } else {
            if (feedXCard.useASGCNewDesign)
                setNewASGCLayout(feedXCard)
            else
                setGridASGCLayout(feedXCard)
        }
    }

    private fun setVODLayout(feedXCard: FeedXCard) {
        val media = feedXCard.media
        val globalCardProductList = feedXCard.tags
        gridList.gone()
        commentButton.visible()
        rvCarousel.gone()
        val mediaList = emptyList<FeedXMedia>()
        adapter.setItemsAndAnimateChanges(mediaList)
        pageControl.setIndicator(mediaList.size)
        pageControl.hide()
        var ratio = VOD_VIDEO_RATIO

        if (feedXCard.media.isNotEmpty() && feedXCard.media.first().type == TYPE_LONG_VIDEO) {
            val orientation = getOrientation(feedXCard.mediaRatio)
            ratio = if (orientation == PORTRAIT)
                getRatioIfPortrait(feedXCard.mediaRatio)
            else
                getRatioIfLandscape(feedXCard.mediaRatio)
        }
        val feedMedia = media.first()
        val tags = feedMedia.tagging
        val tagProducts = mutableListOf<FeedXProduct>()
        tags.map {
            if (!ifProductAlreadyPresent(
                    globalCardProductList[it.tagIndex],
                    tagProducts
                )
            )
                tagProducts.add(globalCardProductList[it.tagIndex])
        }
        feedMedia.isImageImpressedFirst = true
        setVODView(
            feedXCard,
            feedMedia,
            tagProducts,
            ratio
        )
        feedVODViewHolder.visible()

    }

    private fun setVideoControl(
        feedMedia: FeedXMedia,
        postId: String,
        authorId: String,
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
            val layoutLihatProdukParent = findViewById<LinearLayout>(R.id.ll_lihat_product)
            val layoutVideo: PlayerView = findViewById(R.id.layout_video)
            val layoutIconVolume: ImageView = findViewById(R.id.volume_icon)
            val layoutTimerView: Typography = findViewById(R.id.timer_view)
            val layoutLoaderView: LoaderUnify = findViewById(R.id.loader)
            val layoutIcPlay: ImageUnify = findViewById(R.id.ic_play)
            val videoPreviewImage: ImageUnify = findViewById(R.id.videoPreviewImage)

            if (tagProducts.isEmpty()) {
                layoutLihatProdukParent?.gone()
            } else {
                layoutLihatProdukParent?.let {
                    it.visible()
                    hideViewWithAnimation(it, context)
                }
            }


            if (handlerAnim == null) {
                handlerAnim = handlerFeed
            }
            handlerAnim?.postDelayed({
                if (tagProducts.isNotEmpty()) {
                    layoutLihatProdukParent?.let {
                        showViewWithAnimation(layoutLihatProdukParent, context)
                    }
                }
            }, TIME_SECOND)
            productVideoJob?.cancel()
            productVideoJob = scope.launch {
                if (videoPlayer == null)
                    videoPlayer = FeedExoPlayer(context)
                layoutVideo.player = videoPlayer?.getExoPlayer()
                layoutVideo.videoSurfaceView?.setOnClickListener {
                    changeMuteStateVideo(layoutIconVolume)
                    setMuteUnmuteSgcVideo(layoutIconVolume, true, feedMedia.type)

                }

                videoPlayer?.start(feedMedia.mediaUrl, GridPostAdapter.isMute)
                layoutIconVolume.setImageResource(
                    if (!GridPostAdapter.isMute) com.tokopedia.iconunify.R.drawable.iconunify_volume_up
                    else com.tokopedia.iconunify.R.drawable.iconunify_volume_mute
                )
                videoPlayer?.setVideoStateListener(object : VideoStateListener {
                    override fun onInitialStateLoading() {
                        showVideoLoading(
                            viewLoader = layoutLoaderView,
                            viewIcPlay = layoutIcPlay,
                        )
                    }

                    override fun onVideoReadyToPlay(isPlaying: Boolean) {
                        hideVideoLoading(
                            viewLoader = layoutLoaderView,
                            viewIcPlay = layoutIcPlay,
                            viewTimer = layoutTimerView,
                            viewVideoPreview = videoPreviewImage,
                        )
                        layoutTimerView.visible()
                        var time = (videoPlayer?.getExoPlayer()?.duration ?: 0L) / TIME_SECOND
                        object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
                            override fun onTick(millisUntilFinished: Long) {
                                time -= 1
                                layoutTimerView.text =
                                    String.format(
                                        "%02d:%02d",
                                        (time / MINUTE_IN_HOUR) % MINUTE_IN_HOUR,
                                        time % MINUTE_IN_HOUR
                                    )
                            }

                            override fun onFinish() {
                                layoutTimerView.gone()
                                layoutIconVolume.gone()
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
        ratio: String
    ) {
        feedMedia.canPlay = false
        feedVODViewHolder.setData(
            feedXCard,
            feedMedia,
            ratio,
            products,
            positionInFeed = positionInFeed
        )
        feedMedia.vodView = feedVODViewHolder
        feedVODViewHolder.bindData(GridPostAdapter.isMute)
        listener?.let {
            feedVODViewHolder.setListener(listener = object : FeedVODViewHolder.VODListener {
                override fun onLihatProdukClicked(
                    feedXCard: FeedXCard,
                    positionInFeed: Int,
                    products: List<FeedXProduct>
                ) {
                    it.onTagClicked(
                        feedXCard,
                        products,
                        it,
                        feedMedia.type,
                        positionInFeed
                    )
                }

                override fun onFullScreenBtnClicked(
                    feedXCard: FeedXCard,
                    positionInFeed: Int,
                    redirectUrl: String,
                    currentTime: Long,
                    shouldTrack: Boolean,
                    isFullScreenButton: Boolean
                ) {
                    it.onFullScreenCLick(
                        feedXCard,
                        positionInFeed,
                        feedXCard.appLink,
                        currentTime,
                        shouldTrack,
                        isFullScreenButton
                    )
                }

                override fun onVolumeBtnClicked(
                    feedXCard: FeedXCard,
                    mute: Boolean,
                    mediaType: String
                ) {
                    it.muteUnmuteVideo(
                        feedXCard,
                        mute,
                        positionInFeed,
                        mediaType
                    )
                }

                override fun addViewsToVOD(
                    feedXCard: FeedXCard,
                    rowNumber: Int,
                    time: Long,
                    hitTrackerApi: Boolean
                ) {
                    it.addVODView(
                        feedXCard,
                        feedXCard.playChannelID,
                        positionInFeed,
                        TIME_FIVE_SEC,
                        true
                    )
                }

                override fun onVODStopTrack(
                    viewHolder: FeedVODViewHolder,
                    lastPosition: Long
                ) {
                    it.sendWatchVODTracker(
                        feedXCard,
                        feedXCard.playChannelID,
                        positionInFeed,
                        TIME_FIVE_SEC
                    )
                }


            })
        }
        feedVODViewHolder.updateLikedText {
            likedText.text = buildSpannedString {
                append(it, 0, VIEWS_START_VALUE)
                bold {
                    append(it, VIEWS_START_VALUE, it.length)
                }
            }
        }
        feedVODViewHolder.setChangeVolumeStateCallback {
            GridPostAdapter.isMute = !GridPostAdapter.isMute
        }
    }

    private fun setMuteUnmuteSgcVideo(
        volumeIcon: ImageView?,
        isVideoTap: Boolean,
        mediaType: String
    ) {
        val countDownTimer = object : CountDownTimer(TIME_THREE_SEC, TIME_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                volumeIcon?.gone()
            }
        }
        listener?.muteUnmuteVideo(mData, GridPostAdapter.isMute, positionInFeed, mediaType)
        if (!volumeIcon?.isVisible!!)
            volumeIcon.visible()
        if (isVideoTap) {
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
            volumeIcon.setImageResource(R.drawable.ic_feed_volume_mute_large)
        } else {
            volumeIcon.setImageResource(R.drawable.ic_feed_volume_up_large)
        }
    }


    private fun hideVideoLoading(
        viewLoader: View,
        viewIcPlay: View,
        viewTimer: View,
        viewVideoPreview: View,
    ) {
        viewLoader.gone()
        viewIcPlay.gone()
        viewTimer.visible()
        viewVideoPreview.gone()
    }

    private fun showVideoLoading(
        viewLoader: View,
        viewIcPlay: View,
    ) {
        viewLoader.animate()
        viewLoader.visible()
        viewIcPlay.visible()
    }

    private fun toggleVolume(isMute: Boolean) {
        videoPlayer?.toggleVideoVolume(isMute)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setNewASGCLayout(feedXCard: FeedXCard) {
        val products = feedXCard.products
        val totalProducts = feedXCard.products.size
        gridList.gone()
        rvCarousel.visible()
        feedVODViewHolder.gone()
        pageControl.apply {
            setIndicator(
                if (totalProducts <= MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL) totalProducts
                else MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL
            )
            setCurrentIndicator(feedXCard.lastCarouselIndex)
        }.showWithCondition(products.size > 1)

        val mediaList = products
            .take(MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL)
            .mapIndexed { index, product ->
                FeedXMedia(
                    id = product.id,
                    type = "image",
                    appLink = feedXCard.appLink,
                    mediaUrl = product.coverURL,
                    tagging = arrayListOf(
                        FeedXMediaTagging(
                            index,
                            TOPADS_TAGGING_CENTER_POS_X,
                            TOPADS_TAGGING_CENTER_POS_Y,
                            mediaIndex = index
                        )
                    ),
                    isImageImpressedFirst = true,
                    productName = product.name,
                    price = product.priceFmt,
                    slashedPrice = product.priceOriginalFmt,
                    discountPercentage = product.discount.toString(),
                    isCashback = !TextUtils.isEmpty(product.cashbackFmt),
                    variant = product.variant,
                    cashBackFmt = product.cashbackFmt,
                    tagProducts = listOf(product),
                )
            }

        commentButton.invisible()
        seeAllCommentText.hide()
        if (feedXCard.isTypeProductHighlight && feedXCard.campaign.isUpcoming)
            listener?.onIngatkanSayaBtnImpressed(mData, positionInFeed)

        adapter.setItemsAndAnimateChanges(mediaList)
        rvCarousel.addOneTimeGlobalLayoutListener {
            rvCarousel.scrollToPosition(feedXCard.lastCarouselIndex)
        }

        feedXCard.media = mediaList
        feedXCard.tags = feedXCard.products
    }

    private fun setGridASGCLayout(feedXCard: FeedXCard) {
        gridList.visible()
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
            feedXCard.author.id,
            feedXCard.hasVoucher
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
            feedXCard.followers.isFollowed,
            listToBeImpressed
        )
    }

    private fun setGridListPadding(listSize: Int) {
        if (listSize == 1) {
            gridList.setPadding(0, 0, 0, 0)
        } else {
            gridList.setPadding(
                gridList.getDimens(contentCommonR.dimen.content_common_dp_3),
                0,
                gridList.getDimens(contentCommonR.dimen.content_common_dp_3),
                0
            )
        }
    }

    @SuppressLint("Method Call Prohibited")
    private fun getGridPostModel(
        feedXCard: FeedXCard,
        products: List<FeedXProduct>
    ): GridPostModel {
        return GridPostModel(
            getGridItemViewModel(products),
            SHOW_MORE,
            feedXCard.appLink,
            feedXCard.totalProducts,
            true,
            mutableListOf(),
            feedXCard.id, // just replace to String instead of return null
            positionInFeed,
            feedXCard.typename,
            feedXCard.followers.isFollowed,
            feedXCard.hasVoucher,
            feedXCard.author.id,
            feedXCard.products
        )
    }

    private fun getGridItemViewModel(products: List<FeedXProduct>): MutableList<GridItemModel> {
        val itemList: MutableList<GridItemModel> = ArrayList()
        products.forEach {
            itemList.add(
                GridItemModel(
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


    private fun bindPublishedAt(publishedAt: String) {
        val avatarDate = TimeConverter.generateTimeNew(context, publishedAt)
        val spannableString =
            SpannableString(
                String.format(
                    context.getString(contentCommonR.string.feed_header_time_new),
                    avatarDate
                )
            )
        timestampText.text = spannableString
        timestampText.show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun onResume() {
        if (shouldResumeVideoPLayerOnBack)
            videoPlayer?.resume()
        adapter.focusItemAt(pageControl.indicatorCurrentPosition)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        videoPlayer?.pause()
        adapter.onPause()
        job.cancelChildren()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var parent = this.parent
        while (parent !is ViewPager && parent !is ViewPager2 && parent != null) {
            parent = parent.parent
        }
        scrollHostCarousel.setTargetParent(parent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        detach()
    }

    fun attach(
        model: Visitable<*>? = null
    ) {
        if (model is DynamicPostUiModel) {
            model?.feedXCard?.media?.firstOrNull()?.canPlay = false
            model?.feedXCard?.let { hideTaggingOnDetach(it) }
            if (model.feedXCard.typename == TYPE_FEED_X_CARD_PLAY || (model.feedXCard.typename == TYPE_FEED_X_CARD_POST && model.feedXCard.media.first().type == TYPE_LONG_VIDEO)) {
                val feedXCard = model.feedXCard
                val media = feedXCard.media.first()
                media.vodView?.onViewAttached()
            }
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
        if (animationHandler != null) {
            animationHandler?.stopAnimation()
            animationHandler = null
        }
        if (!fromSlide) {
            if (model is DynamicPostUiModel) {
                model?.feedXCard?.media?.firstOrNull()?.canPlay = false
                model?.feedXCard?.media?.firstOrNull()?.isImageImpressedFirst = true
                model?.feedXCard?.let { hideTaggingOnDetach(it) }
                if (model.feedXCard.typename == TYPE_FEED_X_CARD_PLAY || (model.feedXCard.typename == TYPE_FEED_X_CARD_POST && model.feedXCard.media.first().type == TYPE_LONG_VIDEO)) {
                    val feedXCard = model.feedXCard
                    val media = feedXCard.media.get(feedXCard.lastCarouselIndex)
                    media.vodView?.onViewDetached()
                }
            } else if (model is TopadsHeadLineV2Model) {
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
        }

        adapter.removeAllFocus(pageControl.indicatorCurrentPosition)
        changeCTABtnColorToWhite(mData)
    }

    private fun hideTaggingOnDetach(feedXCard: FeedXCard) {
        val cardProducts: List<FeedXProduct> = feedXCard.tags
        val media =
            if (feedXCard.media.size > feedXCard.lastCarouselIndex) feedXCard.media[feedXCard.lastCarouselIndex] else null
        val imageItem = media?.imageView
        val tags = media?.tagging

        imageItem?.run {

            val layout = findViewById<ConstraintLayout>(R.id.post_image_layout)
            val layoutLihatProdukParent = findViewById<TextView>(R.id.tv_lihat_product)

            val tagProducts = mutableListOf<FeedXProduct>()

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

    private fun changeCTABtnColorAsPerColorCodeFromBE(color: String) {
        changeCTABtnColor(
            primaryColor = Color.parseColor(color),
            secondaryColor = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            ),
        )
    }

    private fun changeCTABtnColorAsPerColorGradientFromBE(colorArray: ArrayList<String>?) {
        colorArray?.let {
            changeCTABtnColorGradient(
                colorArray = it,
                secondaryColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                ),
            )
        }
    }


    private fun changeCTABtnColorToGreen() {
        changeCTABtnColor(
            primaryColor = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
            ),
            secondaryColor = MethodChecker.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            ),
        )
    }

    private fun changeCTABtnColorToWhite(card: FeedXCard) {
        card.isAsgcColorChangedAsPerWidgetColor = false

        changeCTABtnColor(
            primaryColor = MethodChecker.getColor(
                context,
                unifyPrinciplesR.color.Unify_NN50
            ),
            secondaryColor = MethodChecker.getColor(
                context,
                unifyPrinciplesR.color.Unify_NN600
            ),
        )
    }

    private fun changeCTABtnColorAsPerWidget(card: FeedXCard, delayInMs: Long? = null) {
        val colorGradient = card.cta.colorGradient
        topAdsJob?.cancel()

        topAdsJob = scope.launch {
            if (delayInMs != null) delay(delayInMs)

            card.isAsgcColorChangedAsPerWidgetColor = true
            if (card.isTypeProductHighlight) {
                if ((card.campaign.isRilisanSpl || card.campaign.isFlashSaleToko) && colorGradient.isNotEmpty()) {
                    changeCTABtnColorAsPerColorGradientFromBE(colorGradient.map { colorGradient ->
                        colorGradient.color
                    } as? ArrayList<String>)
                } else if (card.cta.color.isNotEmpty()) {
                    changeCTABtnColorAsPerColorCodeFromBE(card.cta.color)
                } else {
                    changeCTABtnColorToGreen()
                }
            } else
                changeCTABtnColorToGreen()
        }
    }

    private fun changeCTABtnColor(
        primaryColor: Int,
        secondaryColor: Int,
    ) {
        TransitionManager.beginDelayedTransition(
            this,
            BackgroundColorTransition()
                .addTarget(topAdsCard)
        )
        asgcCtaProductName.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingFirst.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingSecond.setTextColor(secondaryColor)
        topAdsChevron.setColorFilter(secondaryColor)
        topAdsCard.setBackgroundColor(primaryColor)
    }

    private fun changeCTABtnColorGradient(
        colorArray: ArrayList<String>,
        secondaryColor: Int,
    ) {
        asgcCtaProductName.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingFirst.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingSecond.setTextColor(secondaryColor)
        topAdsChevron.setColorFilter(secondaryColor)
        topAdsCard.setGradientBackground(colorArray)
    }

    private fun View.setGradientBackground(colorArray: ArrayList<String>) {
        try {
            if (colorArray.size > 1) {
                val colors = IntArray(colorArray.size)
                for (i in 0 until colorArray.size) {
                    colors[i] = Color.parseColor(colorArray[i])
                }
                val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
                gradient.cornerRadius = 0f
                this.background = gradient
            } else {
                this.setBackgroundColor(Color.parseColor(colorArray[0]))
            }
        } catch (e: Exception) {
            changeCTABtnColorToGreen()
        }
    }

    private fun getCTAButtonText(card: FeedXCard) =
        if (card.isTypeProductHighlight) card.cta.text
        else context.getString(contentCommonR.string.feeds_cek_sekarang)

    private fun getCTAButtonSubtitle(card: FeedXCard) = card.cta.subtitle

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        videoPlayer?.destroy()
        job.cancelChildren()
    }

    fun playVideo(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
        if (videoPlayer == null)
            feedXCard.media[position].canPlay = true
        if (feedXCard.media[position].canPlay) {
            setVideoControl(
                feedXCard.media[position],
                feedXCard.id,
                feedXCard.author.id,
                feedXCard
            )
        }

        adapter.focusItemAt(pageControl.indicatorCurrentPosition)
    }

    fun playVOD(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
        if (videoPlayer == null) {
            feedXCard.media[position].canPlay = true
        }
        if (feedXCard.media[position].canPlay) {
            val feedMedia = feedXCard.media[position]
            val vodItem = feedMedia.vodView
            vodItem?.setVODControl(GridPostAdapter.isMute)
        }
    }

    fun setVideo(isFragmentVisible: Boolean) {
        if (isFragmentVisible) {
            feedVODViewHolder.onResume()
            videoPlayer?.resume()
        } else {
            feedVODViewHolder.onPause()
            videoPlayer?.pause()
        }
    }

    fun bindImage(cardProducts: List<FeedXProduct>, media: FeedXMedia, feedXCard: FeedXCard) {
        adapter.focusItemAt(feedXCard.lastCarouselIndex)
        changeCTABtnColorAsPerWidget(feedXCard, FOCUS_CTA_DELAY)
    }

    fun onCTAVisible(feedXCard: FeedXCard) {
        changeCTABtnColorAsPerWidget(feedXCard, FOCUS_CTA_DELAY)
    }

    fun onFSTReminderStatusUpdated() {
        adapter.updateReminderStatusForAllButtonsInCarousel()
    }

    private fun sendHeaderTopadsEvent(
        positionInFeed: Int,
        appLink: String,
        cpmData: CpmData,
        isNewVariant: Boolean
    ) {
        topAdsListener?.onTopAdsHeadlineAdsClick(positionInFeed, appLink, cpmData, isNewVariant)
    }

    private fun getOrientation(mediaRatio: FeedXMediaRatio): Int {
        if (mediaRatio.width > mediaRatio.height)
            return LANDSCAPE
        return PORTRAIT
    }

    private fun getRatioIfPortrait(mediaRatio: FeedXMediaRatio): String {
        val ratio =
            round((mediaRatio.width.toFloat() / mediaRatio.height) * ROUND_OFF_TO_ONE_DECIMAL_VALUE) / ROUND_OFF_TO_ONE_DECIMAL_VALUE
        return if (ratio <= MEDIA_RATIO_PORTRAIT_THRESHOLD_FLOAT)
            VOD_VIDEO_RATIO
        else if (ratio > MEDIA_RATIO_PORTRAIT_THRESHOLD_FLOAT && ratio < MEDIA_RATIO_SQUARE_VALUE_FLOAT)
            ratio.toString() //original ratio
        else
            SQUARE_RATIO

    }

    private fun getRatioIfLandscape(mediaRatio: FeedXMediaRatio): String {

        val ratio =
            round((mediaRatio.width.toFloat() / mediaRatio.height) * ROUND_OFF_TO_ONE_DECIMAL_VALUE) / ROUND_OFF_TO_ONE_DECIMAL_VALUE
        return if (ratio >= MEDIA_RATIO_LANDSCAPE_THRESHOLD_FLOAT)
            LONG_VIDEO_RATIO
        else if (ratio > MEDIA_RATIO_SQUARE_VALUE_FLOAT && ratio < MEDIA_RATIO_LANDSCAPE_THRESHOLD_FLOAT)
            ratio.toString() //original ratio
        else
            SQUARE_RATIO
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
    }

}
