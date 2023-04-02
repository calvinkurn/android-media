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
import com.tokopedia.feedplus.presentation.adapter.FeedPostImageAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_SELECTED
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.uiview.FeedAsgcTagsView
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.feedplus.presentation.uiview.FeedProductButtonView
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.feedplus.presentation.util.animation.FeedLikeAnimationComponent
import com.tokopedia.feedplus.presentation.util.animation.FeedSmallLikeIconAnimationComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
@SuppressLint("ClickableViewAccessibility")
class FeedPostImageViewHolder(
    private val binding: ItemFeedPostBinding,
    private val parentToBeDisabled: ViewParent?,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardImageContentModel>(binding.root) {

    private val layoutManager =
        LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

    private val authorView = FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
    private val captionView = FeedCaptionView(binding.tvFeedCaption)
    private val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
    private val asgcTagsView = FeedAsgcTagsView(binding.rvFeedAsgcTags)
    private val campaignView = FeedCampaignRibbonView(binding.feedCampaignRibbon, listener)
    private val productTagView = FeedProductTagView(binding.productTagView, listener)
    private val likeAnimationView = FeedLikeAnimationComponent(
        binding.root
    )
    private val smallLikeAnimationView = FeedSmallLikeIconAnimationComponent(binding.root)

    init {
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
                }
            })
        }

        binding.scrollableHost.setTargetParent(parentToBeDisabled)
    }

    override fun bind(element: FeedCardImageContentModel?) {
        element?.let { data ->
            with(binding) {
                val postGestureDetector = GestureDetector(
                    root.context,
                    object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                            return true
                        }

                        override fun onDoubleTap(e: MotionEvent): Boolean {
                            if (element.like.isLiked.not()) {
                                listener.onLikePostCLicked(
                                    element.id,
                                    element.like.isLiked,
                                    absoluteAdapterPosition
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

                bindAuthor(data)
                bindCaption(data)
                bindImagesContent(data.media)
                bindIndicators(data.media.size)
                bindProductTag(data)
                bindLike(data)
                bindAsgcTags(data)
                bindCampaignRibbon(data)

                menuButton.setOnClickListener {
                    listener.onMenuClicked(data.id)
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
                        element.id,
                        element.like.isLiked,
                        absoluteAdapterPosition
                    )
                }

                btnDisableClearMode.setOnClickListener {
                    hideClearView()
                }

                rvFeedPostImageContent.setOnTouchListener { _, event ->
                    postGestureDetector.onTouchEvent(event)
                }
            }
        }
    }

    override fun bind(element: FeedCardImageContentModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        element?.let {
            if (payloads.contains(FEED_POST_LIKED_UNLIKED)) {
                setLikeUnlike(it.like)
            }
        }

        if (payloads.contains(FEED_POST_CLEAR_MODE)) {
            showClearView()
        }
        if (payloads.contains(FEED_POST_SELECTED)) {
            campaignView.startAnimation()
        }
        if (payloads.contains(FEED_POST_NOT_SELECTED)) {
            campaignView.resetView()
            hideClearView()
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
        authorView.bindData(model.author, false, !model.followers.isFollowed)
    }

    private fun bindCaption(model: FeedCardImageContentModel) {
        captionView.bind(model.text)
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
            totalProducts = model.totalProducts
        )

        productButtonView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            totalProducts = model.totalProducts
        )
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
            val adapter = FeedPostImageAdapter(media.map { it.mediaUrl })
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
            model.isTypeProductHighlight
        )
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
            btnDisableClearMode.hide()
        }
    }

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1

        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
