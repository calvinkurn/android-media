package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedPostImageAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_COMMENT_COUNT
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_REMINDER_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED_CHANGED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloads
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.adapter.util.animateAlpha
import com.tokopedia.feedplus.presentation.model.*
import com.tokopedia.feedplus.presentation.uiview.*
import com.tokopedia.feedplus.presentation.util.animation.FeedLikeAnimationComponent
import com.tokopedia.feedplus.presentation.util.animation.FeedSmallLikeIconAnimationComponent
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.util.extension.changeConstraint
import kotlinx.coroutines.*
import kotlin.math.abs

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
@SuppressLint("ClickableViewAccessibility")
class FeedPostImageViewHolder(
    private val binding: ItemFeedPostBinding,
    parentToBeDisabled: ViewParent?,
    private val listener: FeedListener,
    private val trackerMapper: MapperFeedModelToTrackerDataModel
) : AbstractViewHolder<FeedCardImageContentModel>(binding.root) {

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val layoutManager =
        LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
    private var adapter: FeedPostImageAdapter? = null

    private val captionViewListener = object : FeedCaptionView.Listener {
        override fun onExpanded(view: FeedCaptionView) {
            val screenHeight = getScreenHeight()
            val maxHeight = screenHeight * 0.45f
            binding.root.changeConstraint {
                constrainMaxHeight(binding.layoutFeedCaption.id, maxHeight.toInt())
            }
        }
    }

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(
        binding.tvFeedCaption,
        binding.layoutFeedCaption,
        listener,
        captionViewListener
    )
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView = FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(binding.root)
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)
    private val commentButtonView = FeedCommentButtonView(binding.feedCommentButton, listener)

    private var firstX = 0f
    private var secondX = 0f
    private var isAutoSwipeOn = true

    private var trackerDataModel: FeedTrackerDataModel? = null

    private var isInClearView: Boolean = false

    private var mData: FeedCardImageContentModel? = null

    private val opacityViewList = listOf(
        binding.layoutAuthorInfo.root,
        binding.tvFeedCaption,
        binding.menuButton,
        binding.shareButton,
        binding.overlayTop.root,
        binding.overlayBottom.root,
        binding.overlayRight.root,
        binding.btnDisableClearMode,
        binding.postLikeButton.root,
        binding.feedCommentButton.root,
        binding.productTagView.root,
        binding.productTagButton.root,
        binding.rvFeedAsgcTags,
        binding.feedCampaignRibbon.root,
    )

    init {
        binding.root.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
            }

            override fun onViewDetachedFromWindow(p0: View) {
                onNotSelected()
            }
        })

        with(binding) {
            indicatorFeedContent.activeColor = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
            indicatorFeedContent.inactiveColor = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White_44
            )

            rvFeedPostImageContent.layoutManager = layoutManager
            PagerSnapHelper().attachToRecyclerView(rvFeedPostImageContent)
            rvFeedPostImageContent.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    indicatorFeedContent.setCurrentIndicator(layoutManager.findFirstVisibleItemPosition())

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mData?.let {
                            updateProductTagText(it)
                            updateCampaignAvailability(it)
                        }
                    }
                }
            })

            postLikeButton.likeButton.setOnClickListener {
                val data = mData ?: return@setOnClickListener
                listener.onLikePostCLicked(
                    data.id,
                    absoluteAdapterPosition,
                    trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(
                        data
                    ),
                    false
                )
            }

            val postGestureDetector = GestureDetector(
                root.context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        val data = mData ?: return true
                        if (data.like.isLiked.not() && !data.isTopAds) {
                            listener.onLikePostCLicked(
                                data.id,
                                absoluteAdapterPosition,
                                trackerDataModel
                                    ?: trackerMapper.transformImageContentToTrackerModel(
                                        data
                                    ),
                                true
                            )
                        }
                        return true
                    }

                    override fun onDown(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent) {
                    }
                }
            )

            rvFeedPostImageContent.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        firstX = event.x
                    }
                    MotionEvent.ACTION_UP -> {
                        secondX = event.x
                        val deltaX = secondX - firstX
                        if (abs(deltaX) > MINIMUM_DISTANCE_SWIPE) {
                            isAutoSwipeOn = false
                            val data = mData
                            if (data != null) {
                                listener.onSwipeMultiplePost(
                                    trackerDataModel
                                        ?: trackerMapper.transformImageContentToTrackerModel(data)
                                )
                            }
                        }
                    }
                }
                postGestureDetector.onTouchEvent(event)
            }
        }

        binding.scrollableHost.setTargetParent(parentToBeDisabled)
    }

    fun bind(item: FeedContentAdapter.Item) {
        val data = item.data as FeedCardImageContentModel
        bind(data)
        if (item.isSelected) onSelected(data)
    }

    override fun bind(element: FeedCardImageContentModel?) {
        mData = element
        element?.let { data ->
            trackerDataModel = trackerMapper.transformImageContentToTrackerModel(data)

            with(binding) {
                if (data.isTopAds) {
                    postLikeButton.root.hide()
                    commentButtonView.hide()
                } else {
                    postLikeButton.root.show()
                    commentButtonView.show()

                    bindLike(data)
                    bindComments(data)
                }

                bindAuthor(data)
                bindCaption(data)
                bindImagesContent(data.media)
                bindIndicators(data.media.size)
                bindProductTag(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)

                val trackerData =
                    trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(
                        data.id,
                        data.menuItems.map {
                            it.copy(
                                contentData = it.contentData?.copy(rowNumber = absoluteAdapterPosition)
                            )
                        },
                        trackerData
                    )
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(getShareModel(data), trackerData)
                }

                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }
            }
        }
    }

    fun bind(item: FeedContentAdapter.Item, payloads: MutableList<Any>) {
        val selectedPayload = if (item.isSelected) FEED_POST_SELECTED else FEED_POST_NOT_SELECTED
        val feedPayloads =
            payloads.firstOrNull { it is FeedViewHolderPayloads } as? FeedViewHolderPayloads
        val newPayloads =
            if (feedPayloads != null && feedPayloads.payloads.contains(FEED_POST_SELECTED_CHANGED)) {
                payloads.toMutableList().also { it.add(selectedPayload) }
            } else {
                payloads
            }
        bind(item.data as FeedCardImageContentModel, newPayloads)
    }

    override fun bind(element: FeedCardImageContentModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        mData = element
        element?.let {
            trackerDataModel = trackerMapper.transformImageContentToTrackerModel(it)

            if (payloads.contains(FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(it.like)
            }

            if (payloads.contains(FEED_POST_CLEAR_MODE)) {
                showClearView()
            }

            if (payloads.contains(FEED_POST_COMMENT_COUNT)) {
                bindComments(it)
            }

            if (payloads.contains(FEED_POST_REMINDER_CHANGED)) {
                campaignView.bindCampaignReminder(element.campaign.isReminderActive)
            }

            if (payloads.contains(FEED_POST_SELECTED)) {
                onSelected(element)
            }

            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                onNotSelected()
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_FOLLOW_CHANGED)) {
                bindAuthor(element)
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_SCROLLING)) {
                onScrolling()
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_DONE_SCROLL)) {
                onDoneScroll()
            }

            payloads.forEach { payload ->
                if (payload is FeedViewHolderPayloads) {
                    bind(
                        element,
                        payload.payloads.toMutableList()
                    )
                }
            }
        }
    }

    private fun onSelected(data: FeedCardImageContentModel) {
        campaignView.resetView()
        campaignView.startAnimation()
        sendImpressionTracker(data)
        updateProductTagText(data)

        isAutoSwipeOn = true
        runAutoSwipe()
    }

    private fun onNotSelected() {
        job?.cancel()
        campaignView.resetView()
        hideClearView()

        isAutoSwipeOn = false
    }

    private fun updateProductTagText(element: FeedCardImageContentModel) {
        val index = layoutManager.findFirstVisibleItemPosition()

        if (index in PRODUCT_COUNT_ZERO until element.media.size) {
            element.media[index].let {
                if (it.tagging.isNotEmpty()) {
                    if (it.tagging.size == PRODUCT_COUNT_ONE && it.tagging[PRODUCT_COUNT_ZERO].tagIndex in PRODUCT_COUNT_ZERO until element.products.size) {
                        productTagView.bindText(
                            listOf(element.products[it.tagging[PRODUCT_COUNT_ZERO].tagIndex]),
                            element.totalProducts
                        )
                    } else {
                        productTagView.bindText(
                            element.products,
                            element.totalProducts
                        )
                    }
                } else {
                    productTagView.showClearView()
                }
            }
        }

        if (isInClearView) {
            productTagView.showClearView()
        }
    }

    private fun updateCampaignAvailability(element: FeedCardImageContentModel) {
        val index = layoutManager.findFirstVisibleItemPosition()
        if (index in PRODUCT_COUNT_ZERO until element.media.size) {
            element.media[index].let {
                if (it.tagging.isNotEmpty()) {
                    if (it.tagging.size == PRODUCT_COUNT_ONE && it.tagging[PRODUCT_COUNT_ZERO].tagIndex in PRODUCT_COUNT_ZERO until element.products.size) {
                        campaignView.bindProduct(
                            element.products[it.tagging[PRODUCT_COUNT_ZERO].tagIndex]
                        )
                    }
                }
            }
        }
    }

    private fun sendImpressionTracker(element: FeedCardImageContentModel) {
        listener.onPostImpression(
            trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(
                element
            ),
            element.id,
            absoluteAdapterPosition
        )
        if (element.isTopAds) {
            listener.onTopAdsImpression(
                adViewUrl = element.adViewUrl,
                id = element.id,
                shopId = element.author.id,
                uri = element.adViewUri,
                fullEcs = element.author.logoUrl,
                position = absoluteAdapterPosition
            )
        }
    }

    private fun renderLikeView(
        like: FeedLikeModel
    ) {
        val isLiked = like.isLiked
        likeAnimationView.setEnabled(isEnabled = true)
        smallLikeAnimationView.setEnabled(isEnabled = true)

        likeAnimationView.setIsLiked(true)
        binding.postLikeButton.likedText.text = like.countFmt
        if (isLiked) {
            likeAnimationView.show()
        } else {
            likeAnimationView.hide()
        }
    }

    private fun setLikeUnlike(like: FeedLikeModel) {
        val isLiked = like.isLiked
        renderLikeView(like)
        if (isLiked) {
            likeAnimationView.playLikeAnimation()
            smallLikeAnimationView.playLikeAnimation()
        } else {
            smallLikeAnimationView.playUnLikeAnimation()
        }
    }

    private fun bindAuthor(model: FeedCardImageContentModel) {
        authorView.bindData(model.author, false, !model.followers.isFollowed, trackerDataModel)
    }

    private fun bindCaption(model: FeedCardImageContentModel) {
        captionView.bind(model.text, trackerDataModel)
    }

    private fun bindProductTag(model: FeedCardImageContentModel) {
        val products =
            if (model.media.isNotEmpty() && model.media.first().tagging.size == PRODUCT_COUNT_ONE) {
                listOf(
                    model.products[model.media.first().tagging.first().tagIndex]
                )
            } else {
                model.products
            }

        productTagView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            products = products,
            totalProducts = model.totalProducts,
            trackerData = trackerDataModel,
            positionInFeed = absoluteAdapterPosition
        )
        productButtonView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            products = model.products,
            totalProducts = model.totalProducts,
            trackerData = trackerDataModel,
            positionInFeed = absoluteAdapterPosition
        )
        updateProductTagText(model)
    }

    private fun bindIndicators(imageSize: Int) {
        with(binding) {
            indicatorFeedContent.setIndicator(imageSize)
            indicatorFeedContent.showWithCondition(imageSize > 1)
        }
    }

    private fun bindLike(feedCardModel: FeedCardImageContentModel) {
        val like = feedCardModel.like
        binding.postLikeButton.likedText.text = like.countFmt
        smallLikeAnimationView.setIsLiked(like.isLiked)
    }

    private fun bindImagesContent(media: List<FeedMediaModel>) {
        with(binding) {
            adapter = FeedPostImageAdapter(media.map { it.mediaUrl })
            rvFeedPostImageContent.adapter = adapter
        }
    }

    private fun bindAsgcTags(model: FeedCardImageContentModel) {
        asgcTagsView.bindData(model.type, model.campaign)
    }

    private fun bindCampaignRibbon(model: FeedCardImageContentModel) {
        campaignView.bindData(
            model.type,
            model.campaign,
            model.cta,
            model.products.firstOrNull(),
            model.products,
            model.hasVoucher,
            model.isTypeProductHighlight,
            trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(model),
            model.id,
            model.author,
            model.typename,
            model.followers.isFollowed,
            absoluteAdapterPosition
        )
    }

    private fun bindComments(model: FeedCardImageContentModel) {
        commentButtonView.bind(
            model.id,
            false,
            model.comments.countFmt,
            trackerDataModel,
            absoluteAdapterPosition
        )

        if (model.isTypeProductHighlight) {
            commentButtonView.hide()
        } else {
            commentButtonView.show()
        }
    }

    private fun showClearView() {
        isInClearView = true
        with(binding) {
            layoutAuthorInfo.root.hide()
            tvFeedCaption.hide()
            postLikeButton.root.hide()
            commentButtonView.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.root.hide()
            productTagView.root.hide()
            overlayTop.root.hide()
            overlayBottom.root.hide()
            overlayRight.root.hide()
            btnDisableClearMode.show()
        }
    }

    private fun hideClearView() {
        isInClearView = false
        with(binding) {
            layoutAuthorInfo.root.show()
            tvFeedCaption.show()
            menuButton.show()
            shareButton.show()
            overlayTop.root.show()
            overlayBottom.root.show()
            overlayRight.root.show()
            btnDisableClearMode.hide()

            mData?.let {
                if (!it.isTopAds) {
                    postLikeButton.root.show()
                }
                if (!it.isTopAds && !it.isTypeProductHighlight) {
                    commentButtonView.show()
                }
            }
        }

        productTagView.showIfPossible()
        productButtonView.showIfPossible()
    }

    private fun onScrolling() {
        opacityViewList.forEach {
            it.animateAlpha(SCROLL_OPACITY, OPACITY_ANIMATE_DURATION)
        }
    }

    private fun onDoneScroll() {
        opacityViewList.forEach {
            it.alpha = FULL_OPACITY
        }
    }

    private fun runAutoSwipe() {
        job?.cancel()
        job = scope.launch {
            while (isAutoSwipeOn && isActive) {
                delay(THREE_SECONDS)
                val index = layoutManager.findFirstVisibleItemPosition()
                if ((index + PRODUCT_COUNT_ONE) < adapter?.data?.size.orZero()) {
                    binding.rvFeedPostImageContent.smoothScrollToPosition(index + PRODUCT_COUNT_ONE)
                } else {
                    binding.rvFeedPostImageContent.smoothScrollToPosition(PRODUCT_COUNT_ZERO)
                }
            }
        }
    }

    private fun getShareModel(data: FeedCardImageContentModel): FeedShareModel {
        return if (data.isTopAds) {
            val selectedImagePosition = layoutManager.findFirstVisibleItemPosition()
            // intentionally, get data from 'products' instead of 'medias'
            // as long as products value === medias value (please check the MapperTopAdsFeed.kt)
            // todo: differentiate TopAds model with the rest
            val productLink = if (data.products.size > selectedImagePosition) {
                val productItem = data.products[selectedImagePosition]
                Pair(productItem.applink, productItem.weblink)
            } else {
                Pair(data.share.appLink, data.share.webLink)
            }
            return data.share.copy(
                appLink = productLink.first,
                webLink = productLink.second
            )
        } else {
            data.share
        }
    }

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1

        const val SCROLL_OPACITY = .3f
        const val FULL_OPACITY = 1f
        const val OPACITY_ANIMATE_DURATION = 300L

        private const val MINIMUM_DISTANCE_SWIPE = 100

        private const val THREE_SECONDS = 3000L

        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
