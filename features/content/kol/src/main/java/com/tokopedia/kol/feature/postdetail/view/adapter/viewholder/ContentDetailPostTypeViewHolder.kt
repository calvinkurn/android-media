package com.tokopedia.kol.feature.postdetail.view.adapter.viewholder

import android.content.Context
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.feedcomponent.domain.mapper.TYPE_TOPADS_HEADLINE_NEW
import com.tokopedia.feedcomponent.util.ColorUtil
import com.tokopedia.feedcomponent.util.NestedScrollableHost
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedcomponent.util.util.productThousandFormatted
import com.tokopedia.feedcomponent.view.adapter.post.FeedPostCarouselAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.CarouselImageViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.CarouselVideoViewHolder
import com.tokopedia.feedcomponent.view.widget.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.URLEncoder
import com.tokopedia.feedcomponent.R as feedComponentR
import com.tokopedia.kol.R as kolR

class ContentDetailPostTypeViewHolder  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private val view = LayoutInflater.from(context).inflate(
        R.layout.content_detail_revamped_viewholder_container_item,
        this,
        true
    )
    private val shopImage: ImageUnify = findViewById(R.id.shop_image)
    private val shopBadge: ImageUnify = findViewById(R.id.shop_badge)
    private val shopName: Typography = findViewById(R.id.shop_name)
    private val shopMenuIcon: IconUnify = findViewById(R.id.menu_button)
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
    private val followCount: Typography = findViewById(R.id.follow_count)
    private val scrollHostCarousel: NestedScrollableHost = findViewById(R.id.scroll_host_carousel)
    private var listener: ContentDetailPostViewHolder.CDPListener? = null

    private var mData = FeedXCard()
    private var positionInFeed: Int = 0


    private val adapter = FeedPostCarouselAdapter(
        dataSource = object : FeedPostCarouselAdapter.DataSource {
            override fun getFeedXCard(): FeedXCard {
                return mData
            }

            override fun getTagBubbleListener(): PostTagView.TagBubbleListener? {
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
                return positionInFeed
            }
        },
        imageListener = object : CarouselImageViewHolder.Listener {
            override fun onTopAdsCardClicked(
                viewHolder: CarouselImageViewHolder,
                media: FeedXMedia,
            ) {

                if (mData.isTypeProductHighlight && mData.useASGCNewDesign) {
                    listener?.onCekSekarangButtonClicked(mData, positionInFeed)
                }

            }

            override fun onCTAColorChangedAsPerWidgetColor(viewHolder: CarouselImageViewHolder) {
                (rvCarousel.adapter as FeedPostCarouselAdapter).updateNeighbourTopAdsColor(
                    viewHolder.adapterPosition
                )
            }

            override fun onImageClicked(viewHolder: CarouselImageViewHolder) {
                listener?.onImageClicked(mData)

            }

            override fun onLiked(viewHolder: CarouselImageViewHolder) {
                listener?.onLikeClicked(
                    mData,
                    positionInFeed
                )
            }

            override fun onImpressed(viewHolder: CarouselImageViewHolder) {
                val data = mData
                listener?.onCarouselItemImpressed(data, positionInFeed)
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
                listener?.onLihatProdukClicked(
                    mData,
                    positionInFeed,
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
                    positionInFeed,
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
        }
    )
    private val snapHelper = PagerSnapHelper()
    private val pageControlListener = object : RecyclerView.OnScrollListener() {

        private val layoutManager = rvCarousel.layoutManager as LinearLayoutManager

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val snappedView = snapHelper.findSnapView(layoutManager) ?: return

            val position = layoutManager.getPosition(snappedView)
            pageControl.setCurrentIndicator(position)
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
        this.positionInFeed = adapterPosition
        bindHeader(feedXCard)
        bindItems(feedXCard)
        bindCaption(feedXCard)
        bindPublishedAt(feedXCard.publishedAt, feedXCard.subTitle)
        bindLike(feedXCard)
        bindComment(
            feedXCard.comments,
            userSession.profilePicture,
            userSession.name
        )
        bindTracking(feedXCard)
        bindShare(feedXCard)

    }
    private fun bindTracking(feedXCard: FeedXCard) {
        addOnImpressionListener(feedXCard.impressHolder) {

            if (feedXCard.typename == TYPE_FEED_X_CARD_POST) {
                listener?.onPostImpressed(feedXCard, positionInFeed)
            }
        }
    }

    fun bindLike(feedXCard: FeedXCard) {
        if (feedXCard.isTypeVOD || feedXCard.isTypeLongVideo) {
            bindViews(feedXCard)
        } else {
            bindLikeData(feedXCard)
        }
    }
    private fun bindPublishedAt(publishedAt: String, subTitle: String) {
        val avatarDate = TimeConverter.generateTimeNew(context, publishedAt)
        val spannableString: SpannableString =
            if (subTitle.isNotEmpty()) {
                SpannableString(
                    String.format(
                        context.getString(com.tokopedia.feedcomponent.R.string.feed_header_time_new),
                        avatarDate
                    )
                )
            } else {
                SpannableString(avatarDate)
            }
        timestampText.text = spannableString
        timestampText.show()
    }

    fun bindHeader(
        feedXCard: FeedXCard
    ) {
        val author = feedXCard.author
        val followers = feedXCard.followers
        val isFollowed = followers.isFollowed
        val count = followers.count

        if (count >= FOLLOW_COUNT_THRESHOLD) {
            followCount.text =
                String.format(
                    context.getString(kolR.string.feed_header_follow_count_text),
                    count.productThousandFormatted()
                )
        }

        if (feedXCard.isTypeProductHighlight) {
            if (feedXCard.type == ASGC_NEW_PRODUCTS)
                followCount.text =
                    context.getString(feedComponentR.string.feeds_asgc_new_product_text)
            else if (feedXCard.type == ASGC_RESTOCK_PRODUCTS)
                followCount.text = context.getString(feedComponentR.string.feeds_asgc_restock_text)
            followCount.show()
        } else {
            followCount.showWithCondition((!isFollowed || followers.transitionFollow) && count >= FOLLOW_COUNT_THRESHOLD)
        }

        shopImage.setImageUrl(author.logoURL)
        shopBadge.setImageUrl(author.badgeURL)
        shopBadge.showWithCondition(author.badgeURL.isNotEmpty())
        if (shopBadge.visibility == GONE) {
            val layoutParams = (followCount.layoutParams as? MarginLayoutParams)
            layoutParams?.setMargins(FOLLOW_MARGIN, MARGIN_ZERO, MARGIN_ZERO, MARGIN_ZERO)
            followCount.layoutParams = layoutParams
        }
        val authorName = MethodChecker.fromHtml(author.name)
        val startIndex = authorName.length + DOT_SPACE
        var endIndex = startIndex + FOLLOW_SIZE

        val text = if (followers.transitionFollow) {
            endIndex += SPACE
            context.getString(feedComponentR.string.kol_Action_following_color)
        } else {
            context.getString(
                feedComponentR.string.feed_component_follow
            )
        }
        val textToShow = MethodChecker.fromHtml(
            context.getString(feedComponentR.string.feed_header_separator) + text
        )
        val spannableString = SpannableStringBuilder("")
        spannableString.append(authorName)
        if (!isFollowed || followers.transitionFollow) {
            spannableString.append(" $textToShow")
        }

        val cs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.onShopHeaderItemClicked(
                    feedXCard
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N600
                )
            }
        }

        if (startIndex < spannableString.length && endIndex <= spannableString.length) {
            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                        listener?.onFollowUnfollowClicked(
                            feedXCard,
                            positionInFeed
                        )

                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    if (endIndex == startIndex + FOLLOW_SIZE) {
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

        shopImage.setOnClickListener {
            listener?.onShopHeaderItemClicked(
                    feedXCard
                )
        }
        shopMenuIcon.setOnClickListener {
            listener?.onClickOnThreeDots(
                feedXCard,
                positionInFeed)
        }
    }
    private fun bindShare(feedXCard: FeedXCard){
        shareButton.setOnClickListener {
            listener?.onSharePostClicked(feedXCard, positionInFeed)
        }
    }
    fun bindViews(feedXCard: FeedXCard){

        val view = feedXCard.views
        if (feedXCard.like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
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
            likeButton.setOnClickListener {
            adapter.updateCTAasperWidgetColor(pageControl.indicatorCurrentPosition)

            listener?.onLikeClicked(
                feedXCard,
                positionInFeed
            )
        }
        }

    }

    private fun bindLikeData(feedXCard: FeedXCard) {
        val like: FeedXLike = feedXCard.like

        if (like.isLiked) {
            val colorGreen =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            likeButton.setImage(IconUnify.THUMB_FILLED, colorGreen, colorGreen)
        } else {
            val colorGrey =
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
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
            adapter.updateCTAasperWidgetColor(pageControl.indicatorCurrentPosition)

            listener?.onLikeClicked(
                feedXCard,
                positionInFeed
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
        addCommentHint.hint = context.getString(com.tokopedia.feedcomponent.R.string.feed_component_add_comment, name)

        commentButton.setOnClickListener {
            listener?.onCommentClicked(mData, positionInFeed)
        }
        seeAllCommentText.setOnClickListener {
            listener?.onCommentClicked(mData, positionInFeed, isSeeMoreComment = true)
        }
        addCommentHint.setOnClickListener {
            listener?.onCommentClicked(mData, positionInFeed)
        }
    }
    fun bindImageOnImpress(){
        adapter.focusItemAt(mData.lastCarouselIndex)
    }

    private fun bindItems(
        feedXCard: FeedXCard,
    ) {
        val media = feedXCard.media
        if (!feedXCard.isTypeProductHighlight && !feedXCard.isTypeVOD) {
            if (feedXCard.isTypeLongVideo) {
                setVODLayout(feedXCard)
            } else {
                val globalCardProductList = feedXCard.tags
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
                    shopMenuIcon.hide()
                }
            }
        } else if (feedXCard.isTypeVOD) {
            setVODLayout(feedXCard)
        } else {
            setNewASGCLayout(feedXCard)
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
            positionInFeed = positionInFeed
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

        })

        feedVODViewHolder.updateLikedText {
            likedText.text = it
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
            context.getString(com.tokopedia.feedcomponent.R.string.feed_component_see_all_comments, comments.countFmt)
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
        val tagConverter = TagConverter()
        var spannableString: SpannableString
        val authorType =
            if (card.author.type == 1) FollowCta.AUTHOR_USER else FollowCta.AUTHOR_SHOP
        val followCta =
            FollowCta(
                authorID = card.author.id,
                authorType = authorType,
                isFollow = card.followers.isFollowed
            )
        val cs: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                listener?.onShopHeaderItemClicked(
                    card,
                    true
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N600
                )
            }
        }
        captionText.shouldShowWithAction(card.text.isNotEmpty()) {
            if (card.text.length > MAX_CHAR ||
                hasSecondLine(card.text)
            ) {
                val captionEnd =
                    if (findSubstringSecondLine(card.text) < CAPTION_END)
                        findSubstringSecondLine(card.text)
                    else
                        DynamicPostViewHolder.CAPTION_END

                val captionTxt: String = buildString {
                    append(
                        ("<b>" + card.author.name + "</b>" + " - ")
                            .plus(card.text.substring(0, captionEnd))
                            .replace("\n", "<br/>")
                            .replace(DynamicPostViewHolder.NEWLINE, "<br/>")
                            .plus("... ")
                            .plus("<font color='${ColorUtil.getColorFromResToString(context, com.tokopedia.unifyprinciples.R.color.Unify_N400)}'>" + "<b>")
                            .plus(context.getString(feedComponentR.string.feed_component_read_more_button))
                            .plus("</b></font>")
                    )
                }
                spannableString = tagConverter.convertToLinkifyHashtag(
                    SpannableString(MethodChecker.fromHtml(captionTxt)), colorLinkHashtag
                ) {
                        hashtag -> onHashtagClicked(hashtag, mData)
                }

                captionText.setOnClickListener {
                    listener?.onReadMoreClicked(
                        card
                    )
                    if (captionText.text.contains(context.getString(feedComponentR.string.feed_component_read_more_button))) {
                        val txt: String = buildString {
                            append("<b>" + card.author.name + "</b>" + " - ").appendLine(
                                card.text.replace("(\r\n|\n)".toRegex(), "<br />")
                            )
                        }
                        spannableString = tagConverter.convertToLinkifyHashtag(
                            SpannableString(MethodChecker.fromHtml(txt)),
                            colorLinkHashtag
                        ) {
                                hashtag -> onHashtagClicked(hashtag, card)
                        }
                        spannableString.setSpan(
                            cs,
                            0,

                            MethodChecker.fromHtml(card.author.name).length - 1 ,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        captionText.text = spannableString
                        captionText.movementMethod = LinkMovementMethod.getInstance()
                    }
                }

            } else {

                val captionTxt: String = buildString {
                    append(
                        ("<b>" + card.author.name + "</b>" + " - ").plus(
                            card.text.replace(DynamicPostViewHolder.NEWLINE, " ")
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
                    ) {
                            hashtag -> onHashtagClicked(hashtag, card)
                    }
            }
            spannableString.setSpan(
                cs,
                0,
                MethodChecker.fromHtml(card.author.name).length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            captionText.text = spannableString
            captionText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private val colorLinkHashtag: Int
        get() = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)


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

    private fun onHashtagClicked(hashtag: String, feed: FeedXCard) {
        listener?.onHashtagClicked(hashtag, feed)
        val encodeHashtag = URLEncoder.encode(hashtag, "UTF-8")
        RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
    }

    fun playVOD(feedXCard: FeedXCard, position: Int = feedXCard.lastCarouselIndex) {
        if (feedXCard.media[position].canPlay) {
            val feedMedia = feedXCard.media[position]
            val vodItem = feedMedia.vodView
            vodItem?.setVODControl(GridPostAdapter.isMute)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter.removeAllFocus(pageControl.indicatorCurrentPosition)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        var parent = this.parent
        while (parent !is ViewPager && parent !is ViewPager2 && parent != null) {
            parent = parent.parent
        }
        scrollHostCarousel.setTargetParent(parent)
        adapter.focusItemAt(mData.lastCarouselIndex)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    internal fun onResume() {
        adapter.focusItemAt(pageControl.indicatorCurrentPosition)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        adapter.onPause()
    }

    companion object {

        private const val ASGC_NEW_PRODUCTS = "asgc_new_products"
        private const val ASGC_RESTOCK_PRODUCTS = "asgc_restock_products"
        private const val MAX_PRODUCT_TO_SHOW_IN_ASGC_CAROUSEL = 5
        private const val TOPADS_TAGGING_CENTER_POS_X = 0.5f
        private const val TOPADS_TAGGING_CENTER_POS_Y = 0.44f
        private const val VOD_VIDEO_RATIO = "4:5"

        private const val FOLLOW_COUNT_THRESHOLD = 100
        private const val FOLLOW_MARGIN = 6
        private const val MARGIN_ZERO = 0
        private const val FOLLOW_SIZE = 7
        private const val SPACE = 3
        private const val DOT_SPACE = 2
        private const val MAX_CHAR = 120
        private const val CAPTION_END = 120

    }

}