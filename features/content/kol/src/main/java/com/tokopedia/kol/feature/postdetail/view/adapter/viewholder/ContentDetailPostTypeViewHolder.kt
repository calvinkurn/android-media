package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.*
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.presentation.utils.FeedXCardSubtitlesAnimationHandler
import com.tokopedia.feedcomponent.util.*
import com.tokopedia.feedcomponent.util.caption.FeedCaption
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedcomponent.view.adapter.post.FeedPostCarouselAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.CarouselImageViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.CarouselVideoViewHolder
import com.tokopedia.feedcomponent.view.transition.BackgroundColorTransition
import com.tokopedia.feedcomponent.view.widget.*
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import com.tokopedia.feedcomponent.R as feedComponentR
import com.tokopedia.unifyprinciples.R as unifyR

class ContentDetailPostTypeViewHolder  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val view = LayoutInflater.from(context).inflate(
        R.layout.content_detail_viewholder_container_item,
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
    private val scrollHostCarousel: NestedScrollableHost = findViewById(R.id.scroll_host_carousel)
    private var listener: ContentDetailPostViewHolder.CDPListener? = null
    private val topAdsCard = findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
    private val topAdsProductName = findViewById<Typography>(R.id.top_ads_product_name)
    private val asgcProductCampaignCopywritingContainer =
        findViewById<FrameLayout>(R.id.top_ads_campaign_copywriting_container)
    private val asgcProductCampaignCopywritingFirst =
        findViewById<Typography>(R.id.top_ads_campaign_copywriting_first)
    private val asgcProductCampaignCopywritingSecond =
        findViewById<Typography>(R.id.top_ads_campaign_copywriting_second)
    private val topAdsChevron = topAdsCard.findViewById<IconUnify>(R.id.chevron)
    private var animationHandler: FeedXCardSubtitlesAnimationHandler? = null


    private var mData = FeedXCard()
    private var positionInCdp: Int = 0
    private var isFirstImpressedItemAfterEnteringCDP = true

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main.immediate + job)
    private var topAdsJob: Job? = null



    private val adapter = FeedPostCarouselAdapter(
        dataSource = object : FeedPostCarouselAdapter.DataSource {
            override fun getFeedXCard(): FeedXCard {
                return mData
            }

            override fun getTagBubbleListener(): PostTagView.TagBubbleListener {
                return object : PostTagView.TagBubbleListener{
                    override fun onPostTagBubbleClick(
                        positionInFeed: Int,
                        redirectUrl: String,
                        postTagItem: FeedXProduct,
                        adClickUrl: String
                    ) {
                        listener?.onPostTagBubbleClicked(
                            positionInFeed,
                            redirectUrl,
                            postTagItem
                        )
                    }
                }
            }

            override fun getPositionInFeed(): Int {
                return positionInCdp
            }
        },

        imageListener = object : CarouselImageViewHolder.Listener {
            override fun onTopAdsCardClicked(
                viewHolder: CarouselImageViewHolder,
                media: FeedXMedia,
            ) {

                if (mData.isTypeProductHighlight && mData.useASGCNewDesign) {
                    listener?.onCekSekarangButtonClicked(mData, positionInCdp)
                }
            }

            override fun onImageClicked(viewHolder: CarouselImageViewHolder) {
                listener?.onImageClicked(mData)
                changeCTABtnColorAsPerWidget(mData)

            }

            override fun onImageDoubleClicked(viewHolder: CarouselImageViewHolder) {
                val card = mData
                if (card.isTypeProductHighlight) changeCTABtnColorAsPerWidget(card)
            }

            override fun onImageLongClicked(viewHolder: CarouselImageViewHolder) {
                changeCTABtnColorAsPerWidget(mData)
            }


            override fun onLiked(viewHolder: CarouselImageViewHolder) {
                changeCTABtnColorAsPerWidget(mData)
                listener?.onLikeClicked(
                    mData,
                    positionInCdp,
                    true
                )
            }

            override fun onImpressed(viewHolder: CarouselImageViewHolder) {
                val data = mData
                listener?.onCarouselItemImpressed(data, positionInCdp)
                val position = viewHolder.adapterPosition

                if (position == RecyclerView.NO_POSITION) return

                if (data.isTypeProductHighlight) {

                    if (data.products.isEmpty() ||
                        data.products.size <= position) return

                } else {
                    if (data.media.isEmpty() ||
                        data.media.size <= position) return

                }
            }

            override fun onLihatProductClicked(
                viewHolder: CarouselImageViewHolder,
                media: FeedXMedia,
            ) {
                changeCTABtnColorAsPerWidget(mData)
                listener?.onLihatProdukClicked(
                    mData,
                    positionInCdp,
                    media.tagProducts
                )
            }
        },
        videoListener = object : CarouselVideoViewHolder.Listener {
            override fun onLihatProductClicked(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia
            ) {
                listener?.onLihatProdukClicked(
                    mData,
                    positionInCdp,
                    media.tagProducts
                )

            }

            override fun onMuteChanged(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia,
                isMuted: Boolean
            ) {
                //TODO analytics
//                listener?.muteUnmuteVideo(
//                    mData.playChannelID,
//                    isMuted,
//                    mData.author.id,
//                    mData.followers.isFollowed,
//                    true,
//                    media.type,
//                )
            }

            override fun onVideoSurfaceTapped(
                viewHolder: CarouselVideoViewHolder,
                media: FeedXMedia,
                isMuted: Boolean
            ) {
                listener?.onSgcVideoTapped(mData)
            }

            override fun onVideoStopTrack(viewHolder: CarouselVideoViewHolder, lastPosition: Long) {
                listener?.onVideoStopTrack(
                    mData,
                    lastPosition,
                )
            }
        },
        listener = object : FeedCampaignListener {
            override fun onTimerFinishUpcoming() {
                listener?.changeUpcomingWidgetToOngoing(mData, positionInCdp)
            }

            override fun onTimerFinishOngoing() {
                listener?.removeOngoingCampaignSaleWidget(mData, positionInCdp)

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
        ContentDetailListener: ContentDetailPostViewHolder.CDPListener,
        adapterPosition: Int,
        feedXCard: FeedXCard,
        userSession: UserSessionInterface
    ) {
        mData = feedXCard
        this.listener = ContentDetailListener
        this.positionInCdp = adapterPosition
        bindHeader(feedXCard)
        bindItems(feedXCard)
        bindCaption(feedXCard)
        bindTopAds(feedXCard)
        bindPublishedAt(feedXCard.publishedAt)
        bindLike(feedXCard)
        bindComment(
            feedXCard.comments,
            userSession.profilePicture,
            userSession.name
        )
        addOnImpressionListener(feedXCard.impressHolder) {
            bindTracking(feedXCard)
            bindFirstItemForAnimations(feedXCard)
        }
        bindShare(feedXCard)

    }

    private fun bindFirstItemForAnimations(feedXCard: FeedXCard) {
        if (positionInCdp == 0 && isFirstImpressedItemAfterEnteringCDP) {
            isFirstImpressedItemAfterEnteringCDP = false
            if (feedXCard.isTypeVOD || feedXCard.isTypeLongVideo)
                playVOD(feedXCard)
            else
                bindImageOnImpress()
            if (feedXCard.isTypeProductHighlight)
                onCTAVisible(mData)
        }
    }

    private fun bindTracking(feedXCard: FeedXCard) {
                listener?.onPostImpressed(feedXCard, positionInCdp)
    }

    fun bindLike(feedXCard: FeedXCard) {
        if (feedXCard.isTypeVOD || feedXCard.isTypeLongVideo) {
            bindViews(feedXCard)
        } else {
            bindLikeData(feedXCard)
        }
    }

    private fun bindPublishedAt(publishedAt: String) {
        val avatarDate = TimeConverter.generateTimeNew(context, publishedAt)
        val spannableString = SpannableString(
                String.format(
                    context.getString(feedComponentR.string.feed_header_time_new),
                    avatarDate
                )
            )
        timestampText.text = spannableString
        timestampText.show()
    }

    private fun bindContentSubInfo(shouldShow: Boolean, value: String) {
        contentSubInfo.showWithCondition(shouldShow)
        contentSubInfo.text = value
    }


    fun bindHeader(
        feedXCard: FeedXCard
    ) {
        val author = feedXCard.author
        val followers = feedXCard.followers
        val isFollowed = followers.isFollowed
        val count = followers.count

        //region bind content sub info
        val contentSubInfoValue =  if (feedXCard.isTypeProductHighlight) {
            when (feedXCard.type) {
                ASGC_NEW_PRODUCTS -> context.getString(feedComponentR.string.feeds_asgc_new_product_text)
                ASGC_RESTOCK_PRODUCTS -> context.getString(
                    feedComponentR.string.feeds_asgc_restock_text)
                ASGC_DISCOUNT_TOKO -> context.getString(feedComponentR.string.feed_asgc_diskon_toko)
                ASGC_FLASH_SALE_TOKO -> context.getString(feedComponentR.string.feed_asgc_flash_sale_toko)
                ASGC_RILISAN_SPECIAL -> context.getString(feedComponentR.string.feed_asgc_rilisan_special)
                else -> String.EMPTY
            }
        } else {
            if (count >= FOLLOW_COUNT_THRESHOLD) {
                String.format(
                    context.getString(feedComponentR.string.feed_header_follow_count_text),
                    count.productThousandFormatted()
                )
            } else context.getString(feedComponentR.string.feed_header_follow_count_less_text)
        }
        bindContentSubInfo(
            shouldShow = (feedXCard.isTypeProductHighlight
                    || (!isFollowed || followers.transitionFollow)) && !feedXCard.isTypeUGC,
            value = contentSubInfoValue
        )
        //endregion

        authorAvatar.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        this.authorName.text = MethodChecker.fromHtml(author.name)
        this.authorName.setOnClickListener {
            listener?.onShopHeaderItemClicked(
                feedXCard
            )
        }
        val textFollowAction = if (followers.transitionFollow || followers.isFollowed) {
            context.getString(feedComponentR.string.kol_action_following_color)
        } else {
            context.getString(feedComponentR.string.kol_action_follow_color)
        }
        if (!isFollowed || followers.transitionFollow) {
            this.authorFollowAction.text = MethodChecker.fromHtml(
                "${context.getString(feedComponentR.string.feed_header_separator)}$textFollowAction"

            )
            this.authorFollowAction.setOnClickListener {
                listener?.onFollowUnfollowClicked(
                        feedXCard,
                        positionInCdp
                    )
            }
            this.authorFollowAction.show()
        } else {
            this.authorFollowAction.hide()
        }
        followers.transitionFollow = false
        //endregion

        authorAvatar.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)
            listener?.onShopHeaderItemClicked(
                feedXCard
            )
        }
        val shouldNotShowMenuIcon = (!feedXCard.reportable && !feedXCard.deletable && !feedXCard.followers.isFollowed)
        headerMenu.showWithCondition(!shouldNotShowMenuIcon)
        headerMenu.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)
            listener?.onClickOnThreeDots(
                feedXCard,
                positionInCdp)
        }
    }

    private fun bindShare(feedXCard: FeedXCard){
        changeCTABtnColorAsPerWidget(mData)
        shareButton.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)
            listener?.onSharePostClicked(feedXCard, positionInCdp)
        }
    }

    private fun bindTopAds(feedXCard: FeedXCard) {
        topAdsProductName.text = getCTAButtonText(feedXCard)
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

            if (feedXCard.isTypeProductHighlight && feedXCard.useASGCNewDesign) {
                listener?.onCekSekarangButtonClicked(
                    feedXCard,
                    positionInCdp
                )
            }
        }
    }
    private fun shouldShowCtaSubtitile(subtitle: List<String>) =
        subtitle.isNotEmpty()


    private fun bindViews(feedXCard: FeedXCard){

        val view = feedXCard.views
        if (feedXCard.like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, unifyR.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, unifyR.color.Unify_N700_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (view.count != 0) {
            likedText.show()

            likedText.text =
                MethodChecker.fromHtml(
                    context.getString(
                        feedComponentR.string.feed_component_viewed_count_text,
                        view.count.productThousandFormatted(1)
                    )
                )
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            changeCTABtnColorAsPerWidget(feedXCard)
            listener?.onLikeClicked(
                feedXCard,
                positionInCdp
            )
        }

    }

    private fun bindLikeData(feedXCard: FeedXCard) {
        val like: FeedXLike = feedXCard.like

        if (like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, unifyR.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, unifyR.color.Unify_N700_96)
            likeButton.setImage(IconUnify.THUMB, colorGrey, colorGrey)
        }
        if (like.likedBy.isNotEmpty() || like.count != 0) {
            likedText.show()
            if (like.likedBy.isEmpty()) {
                if (like.isLiked) {
                    if (like.count == 1) {
                        likedText.text =
                            context.getString(feedComponentR.string.feed_component_liked_count_text_only_me)
                    } else
                        likedText.text =
                            MethodChecker.fromHtml(
                                context.getString(
                                    feedComponentR.string.feed_component_liked_by_text_me,
                                    (like.count - 1).productThousandFormatted(1)
                                )
                            )
                } else
                    likedText.text =
                        MethodChecker.fromHtml(
                            context.getString(
                                feedComponentR.string.feed_component_liked_count_text,
                                like.count.productThousandFormatted(1)
                            )
                        )
            } else {
                likedText.text = MethodChecker.fromHtml(
                    context.getString(
                        feedComponentR.string.feed_component_liked_by_text,
                        getLikedByText(like.likedBy),
                        like.count.productThousandFormatted(1)
                    )
                )
            }
        } else {
            likedText.hide()
        }
        likeButton.setOnClickListener {
            listener?.onLikeClicked(
                feedXCard,
                positionInCdp
            )
        }
    }
    private fun bindComment(
        comments: FeedXComments,
        profilePicture: String,
        name: String
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
        addCommentHint.hint = context.getString(feedComponentR.string.feed_component_add_comment, name)

        commentButton.setOnClickListener {
            listener?.onCommentClicked(mData, positionInCdp)
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClicked(mData, positionInCdp, isSeeMoreComment = true)
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClicked(mData, positionInCdp)
        }
    }
    fun bindImageOnImpress(){
        adapter.focusItemAt(mData.lastCarouselIndex)
    }

    private fun bindItems(
        feedXCard: FeedXCard,
    ) {
        when {
            feedXCard.isTypeUGC -> setTypeUGC(feedXCard)
            feedXCard.isTypeSGC -> setTypeSGC(feedXCard)
            feedXCard.isTypeLongVideo || feedXCard.isTypeVOD -> setVODLayout(feedXCard)
            feedXCard.isTypeProductHighlight -> setNewASGCLayout(feedXCard)
        }
    }

    private fun setTypeUGC(feedXCard: FeedXCard) {
        val media = feedXCard.media
        val globalCardProductList = feedXCard.tags
        feedVODViewHolder.gone()
        rvCarousel.visible()
        commentButton.visible()
        pageControl.apply {
            setIndicator(media.size)
            setCurrentIndicator(feedXCard.lastCarouselIndex)
        }.showWithCondition(media.size > 1)

        media.forEach { feedMedia ->
            if (globalCardProductList.isEmpty()) return@forEach
            val tags = feedMedia.tagging
            feedMedia.tagProducts = tags.map { globalCardProductList[it.tagIndex] }.distinctBy { it.id }
            feedMedia.isImageImpressedFirst = true

            if (!feedMedia.isImage) feedMedia.canPlay = false
        }

        adapter.setItemsAndAnimateChanges(media)
        rvCarousel.addOneTimeGlobalLayoutListener {
            rvCarousel.scrollToPosition(feedXCard.lastCarouselIndex)
        }
    }

    private fun setTypeSGC(feedXCard: FeedXCard) {
        val media = feedXCard.media
        val globalCardProductList = feedXCard.tags
        feedVODViewHolder.gone()
        rvCarousel.visible()
        commentButton.visible()
        pageControl.apply {
            setIndicator(media.size)
            setCurrentIndicator(feedXCard.lastCarouselIndex)
        }.showWithCondition(media.size > 1)

        media.forEach { feedMedia ->
            if (globalCardProductList.isEmpty()) return@forEach
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
    }

    private fun setVODLayout(feedXCard: FeedXCard) {
        val media = feedXCard.media
        val globalCardProductList = feedXCard.tags
        commentButton.visible()
        rvCarousel.gone()
        val mediaList = emptyList<FeedXMedia>()
        adapter.setItemsAndAnimateChanges(mediaList)
        pageControl.setIndicator(mediaList.size)
        pageControl.hide()

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
            VOD_VIDEO_RATIO
        )
        feedVODViewHolder.visible()

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
            positionInFeed = positionInCdp
        )
        feedMedia.vodView = feedVODViewHolder
        feedVODViewHolder.bindData(GridPostAdapter.isMute)
        feedVODViewHolder.setListener(listener = object : FeedVODViewHolder.VODListener {
            override fun onLihatProdukClicked(
                feedXCard: FeedXCard,
                positionInFeed: Int,
                products: List<FeedXProduct>
            ) {
                listener?.onLihatProdukClicked(feedXCard, positionInFeed, products)
            }

            override fun onFullScreenBtnClicked(
                feedXCard: FeedXCard,
                positionInFeed: Int,
                redirectUrl: String,
                currentTime: Long,
                shouldTrack: Boolean,
                isFullScreenButton: Boolean
            ) {
                listener?.onFullScreenButtonClicked(
                    feedXCard,
                    positionInFeed,
                    currentTime,
                    shouldTrack,
                    isFullScreenButton
                )
            }

            override fun onVolumeBtnClicked(feedXCard: FeedXCard, mute: Boolean, mediaType: String) {
                listener?.onVolumeClicked(feedXCard, mute, mediaType)

            }

            override fun addViewsToVOD(
                feedXCard: FeedXCard,
                rowNumber: Int,
                time: Long,
                hitTrackerApi: Boolean
            ) {
                listener?.addViewsToVOD(feedXCard, rowNumber, time, hitTrackerApi)
            }

            override fun onVODStopTrack(viewHolder: FeedVODViewHolder, lastPosition: Long) {
                listener?.sendWatchVODTracker(mData, lastPosition)
            }

        })

        feedVODViewHolder.updateLikedText {
            likedText.text = buildSpannedString {
                append(it, 0, VIEWS_START_VALUE)
                bold {
                    append(it, VIEWS_START_VALUE,it.length)
                }
            }
        }
        feedVODViewHolder.setChangeVolumeStateCallback {
            GridPostAdapter.isMute = !GridPostAdapter.isMute
        }
    }

    private fun setNewASGCLayout(feedXCard: FeedXCard){
        val products = feedXCard.products
        val totalProducts = feedXCard.products.size
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
            listener?.onIngatkanSayaBtnImpressed(mData, positionInCdp)

        adapter.setItemsAndAnimateChanges(mediaList)
        rvCarousel.addOneTimeGlobalLayoutListener {
            rvCarousel.scrollToPosition(feedXCard.lastCarouselIndex)
        }

        feedXCard.media = mediaList
        feedXCard.tags = feedXCard.products
    }



    fun setCommentCount(comments: FeedXComments) {
        seeAllCommentText.showWithCondition(comments.count != 0)
        seeAllCommentText.text =
            context.getString(feedComponentR.string.feed_component_see_all_comments, comments.countFmt)
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

    private fun bindCaption(card: FeedXCard) {
        captionText.shouldShowWithAction(card.text.isNotEmpty()) {
            val authorCaption = FeedCaption.Author(
                name = card.author.name,
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N600
                ),
                typeface = getTypeface(
                    context,
                    FeedCaption.ROBOTO_BOLD
                ),
                clickListener = {
                    listener?.onShopHeaderItemClicked(
                        card,
                        true
                    )
                }
            )

            val tagCaption = FeedCaption.Tag(
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                ),
                clickListener = {
                    onHashtagClicked(it, card)
                }
            )
            val captionBody = FeedCaption.Builder(card.text)
                .withAuthor(authorCaption)
                .withTag(tagCaption)
                .build()

            val readMoreCaption = FeedCaption.ReadMore(
                maxTrimChar = MAX_CHAR,
                label = context.getString(com.tokopedia.feedcomponent.R.string.feed_component_read_more_button),
                colorRes = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N400
                ),
                clickListener = {
                    captionText.setText(captionBody, TextView.BufferType.SPANNABLE)
                }
            )
            val trimmedCaption = FeedCaption.Builder(card.text)
                .withAuthor(authorCaption)
                .withTag(tagCaption)
                .trimCaption(readMoreCaption)
                .build()

            captionText.setText(trimmedCaption, TextView.BufferType.SPANNABLE)
            captionText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onHashtagClicked(hashtag: String, feed: FeedXCard) {
        listener?.onHashtagClicked(hashtag, feed)
    }

    fun playVOD(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
            val feedMedia = feedXCard.media[position]
            val vodItem = feedMedia.vodView
            vodItem?.setVODControl(GridPostAdapter.isMute)
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
                } else{
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
        topAdsProductName.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingFirst.setTextColor(secondaryColor)
        asgcProductCampaignCopywritingSecond.setTextColor(secondaryColor)
        topAdsChevron.setColorFilter(secondaryColor)
        topAdsCard.setBackgroundColor(primaryColor)
    }

    private fun changeCTABtnColorAsPerColorCodeFromBE(color: String) {
        changeCTABtnColor(
            primaryColor = Color.parseColor(color),
            secondaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_N0
            ),
        )
    }

    private fun changeCTABtnColorAsPerColorGradientFromBE(colorArray: ArrayList<String>?) {
        colorArray?.let {
            changeCTABtnColorGradient(
                colorArray = it,
                secondaryColor = MethodChecker.getColor(
                    context,
                    unifyR.color.Unify_N0
                ),
            )
        }
    }


    private fun changeCTABtnColorGradient(
        colorArray: ArrayList<String>,
        secondaryColor: Int,
    ) {
        topAdsProductName.setTextColor(secondaryColor)
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
        else context.getString(feedComponentR.string.feeds_cek_sekarang)

    private fun getCTAButtonSubtitle(card: FeedXCard) = card.cta.subtitle



    fun onCTAVisible(feedXCard: FeedXCard) {
        changeCTABtnColorAsPerWidget(feedXCard,
            FOCUS_CTA_DELAY
        )
    }

    fun onFSTReminderStatusUpdated() {
        adapter.updateReminderStatusForAllButtonsInCarousel()
    }

    private fun changeCTABtnColorToRed() {
        changeCTABtnColor(
            primaryColor = MethodChecker.getColor(
                context,
                feedComponentR.color.feed_dms_asgc_discount_toko_btn_bg_color
            ),
            secondaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_N0
            ),
        )
    }

    private fun changeCTABtnColorToGreen() {
        changeCTABtnColor(
            primaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_G500
            ),
            secondaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_N0
            ),
        )
    }

    private fun changeCTABtnColorToWhite(card: FeedXCard) {
        card.isAsgcColorChangedAsPerWidgetColor = false

        changeCTABtnColor(
            primaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_NN50
            ),
            secondaryColor = MethodChecker.getColor(
                context,
                unifyR.color.Unify_NN600
            ),
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter.removeAllFocus(pageControl.indicatorCurrentPosition)
        feedVODViewHolder.onPause()
        if (animationHandler != null) {
            animationHandler?.stopAnimation()
            animationHandler = null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var parent = this.parent
        while (parent !is ViewPager && parent !is ViewPager2 && parent != null) {
            parent = parent.parent
        }
        changeCTABtnColorToWhite(mData)
        scrollHostCarousel.setTargetParent(parent)
        adapter.focusItemAt(mData.lastCarouselIndex)
        feedVODViewHolder.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun onResume() {
        adapter.focusItemAt(pageControl.indicatorCurrentPosition)
        if (mData.isTypeVOD || mData.isTypeLongVideo)
            feedVODViewHolder.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        adapter.onPause()
        if (mData.isTypeVOD || mData.isTypeLongVideo)
        feedVODViewHolder.onPause()
        job.cancelChildren()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        if (mData.isTypeVOD || mData.isTypeLongVideo)
        feedVODViewHolder.onDestroy()
    }

    companion object {

        private const val ASGC_NEW_PRODUCTS = "asgc_new_products"
        private const val ASGC_RESTOCK_PRODUCTS = "asgc_restock_products"
        private const val ASGC_DISCOUNT_TOKO = "asgc_discount_toko"
        private const val ASGC_FLASH_SALE_TOKO = "asgc_flash_sale_toko"
        private const val ASGC_RILISAN_SPECIAL = "asgc_rilisan_spesial"
        private const val MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL = 5
        private const val TOPADS_TAGGING_CENTER_POS_X = 0.5f
        private const val TOPADS_TAGGING_CENTER_POS_Y = 0.44f
        private const val VOD_VIDEO_RATIO = "4:5"
        private const val FOCUS_CTA_DELAY = 2000L

        private const val FOLLOW_COUNT_THRESHOLD = 100
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2

        private const val MAX_CHAR = 120
        private const val VIEWS_START_VALUE = 14

    }

}
