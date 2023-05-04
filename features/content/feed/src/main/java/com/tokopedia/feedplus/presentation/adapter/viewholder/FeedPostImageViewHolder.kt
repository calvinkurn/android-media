package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
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
import com.tokopedia.feedplus.presentation.adapter.FeedPostImageAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedAsgcTagsView
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.feedplus.presentation.uiview.FeedProductButtonView
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.animation.FeedLikeAnimationComponent
import com.tokopedia.feedplus.presentation.util.animation.FeedSmallLikeIconAnimationComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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

    private val scope = CoroutineScope(Dispatchers.Main)

    private val layoutManager =
        LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
    private var adapter: FeedPostImageAdapter? = null

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption, listener)
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView =
        FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(
        binding.root
    )
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)

    private var firstX = 0f
    private var secondX = 0f
    private var isAutoSwipeOn = true

    private var trackerDataModel: FeedTrackerDataModel? = null

    init {
        with(binding) {
            indicatorFeedContent.activeColor = ContextCompat.getColor(
                binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
            indicatorFeedContent.inactiveColor = ContextCompat.getColor(
                binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White_44
            )

            rvFeedPostImageContent.layoutManager = layoutManager
            PagerSnapHelper().attachToRecyclerView(rvFeedPostImageContent)
            rvFeedPostImageContent.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    indicatorFeedContent.setCurrentIndicator(layoutManager.findFirstVisibleItemPosition())
                }
            })
        }

        binding.scrollableHost.setTargetParent(parentToBeDisabled)
    }

    override fun bind(element: FeedCardImageContentModel?) {
        element?.let { data ->
            trackerDataModel = trackerMapper.transformImageContentToTrackerModel(data)

            with(binding) {
                val postGestureDetector = GestureDetector(root.context,
                    object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                            return true
                        }

                        override fun onDoubleTap(e: MotionEvent): Boolean {
                            if (data.like.isLiked.not()) {
                                listener.onLikePostCLicked(
                                    data.id,
                                    data.like.isLiked,
                                    absoluteAdapterPosition,
                                    trackerDataModel
                                        ?: trackerMapper.transformImageContentToTrackerModel(data),
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
                    })

                bindAuthor(data)
                bindCaption(data)
                bindImagesContent(data.media)
                bindIndicators(data.media.size)
                bindProductTag(data)
                bindLike(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)
                bindComments(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(
                        data.id,
                        trackerDataModel
                            ?: trackerMapper.transformImageContentToTrackerModel(data)
                    )
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(
                        id = data.id,
                        authorName = data.author.name,
                        applink = data.applink,
                        weblink = data.weblink,
                        imageUrl = data.media.firstOrNull()?.mediaUrl ?: ""
                    )
                }
                postLikeButton.likeButton.setOnClickListener {
                    listener.onLikePostCLicked(
                        data.id,
                        data.like.isLiked,
                        absoluteAdapterPosition,
                        trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(data),
                        false
                    )
                }

                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }

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
                                listener.onSwipeMultiplePost(
                                    trackerDataModel
                                        ?: trackerMapper.transformImageContentToTrackerModel(data)
                                )
                            }
                        }
                    }
                    postGestureDetector.onTouchEvent(event)
                }
                rvFeedPostImageContent.addOnScrollListener(object : OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            updateProductTagText(data)
                            updateCampaignAvailability(data)
                        }
                    }
                })
            }
        }
    }

    override fun bind(element: FeedCardImageContentModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element?.let {
            trackerDataModel = trackerMapper.transformImageContentToTrackerModel(it)

            if (payloads.contains(FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(it.like)
            }

            if (payloads.contains(FEED_POST_CLEAR_MODE)) {
                showClearView()
            }
            if (payloads.contains(FEED_POST_SELECTED)) {
                listener.onPostImpression(
                    trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(
                        it
                    ),
                    it.id,
                    absoluteAdapterPosition
                )

                campaignView.startAnimation()
                sendImpressionTracker(it)
                updateProductTagText(it)

                isAutoSwipeOn = true
                runAutoSwipe()
            }
            if (payloads.contains(FEED_POST_NOT_SELECTED)) {
                campaignView.resetView()
                hideClearView()

                isAutoSwipeOn = false
            }
        }
    }

    private fun updateProductTagText(element: FeedCardImageContentModel) {
        val index = layoutManager.findFirstVisibleItemPosition()
        if (index in PRODUCT_COUNT_ZERO until element.media.size) {
            element.media[index].let {
                if (it.tagging.isNotEmpty()) {
                    if (it.tagging.size == PRODUCT_COUNT_ONE && it.tagging[PRODUCT_COUNT_ZERO].tagIndex in PRODUCT_COUNT_ZERO until element.products.size) {
                        productTagView.bindText(
                            listOf(element.products[it.tagging[PRODUCT_COUNT_ZERO].tagIndex])
                        )
                    } else {
                        productTagView.bindText(
                            element.products
                        )
                    }
                }
            }
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
        listener.onTopAdsImpression(
            adViewUrl = element.adViewUrl,
            id = element.id,
            shopId = element.author.id,
            uri = element.adViewUri,
            fullEcs = element.author.logoUrl,
            position = absoluteAdapterPosition
        )
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
        productTagView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            products = model.products,
            totalProducts = model.totalProducts,
            trackerData = trackerDataModel
        )
        productButtonView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            products = model.products,
            trackerData = trackerDataModel
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
        likeAnimationView.setIsLiked(like.isLiked)
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
            model.hasVoucher,
            model.isTypeProductHighlight,
            trackerDataModel ?: trackerMapper.transformImageContentToTrackerModel(model)
        )
    }

    private fun bindComments(model: FeedCardImageContentModel) {
        if (model.isTypeProductHighlight) {
            binding.commentButton.hide()
        } else {
            binding.commentButton.show()
        }
    }

    private fun showClearView() {
        with(binding) {
            layoutAuthorInfo.root.hide()
            tvFeedCaption.hide()
            postLikeButton.root.hide()
            commentButton.hide()
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
        with(binding) {
            layoutAuthorInfo.root.show()
            tvFeedCaption.show()
            postLikeButton.root.show()
            commentButton.show()
            menuButton.show()
            shareButton.show()
            productTagButton.root.show()
            productTagView.root.show()
            overlayTop.root.show()
            overlayBottom.root.show()
            overlayRight.root.show()
            btnDisableClearMode.hide()
        }
    }

    private fun runAutoSwipe() {
        scope.launch {
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

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1

        private const val MINIMUM_DISTANCE_SWIPE = 100

        private const val THREE_SECONDS = 3000L

        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
