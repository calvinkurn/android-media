package com.tokopedia.feedcomponent.view.adapter.viewholder.post.image

import android.annotation.SuppressLint
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.feedcomponent.view.adapter.post.FeedPostCarouselAdapter
import com.tokopedia.feedcomponent.view.widget.FlashSaleRilisanCampaignOngoingView
import com.tokopedia.feedcomponent.view.widget.FlashSaleRilisanCampaignUpcomingView
import com.tokopedia.feedcomponent.view.widget.PostTagView
import com.tokopedia.feedcomponent.view.widget.listener.FeedCampaignListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by kenny.hadisaputra on 29/06/22
 */
@SuppressLint("ClickableViewAccessibility")
class CarouselImageViewHolder(
    itemView: View,
    private val dataSource: FeedPostCarouselAdapter.DataSource,
    private val listener: Listener,
    private val fstListener: FeedCampaignListener?
) : BaseViewHolder(itemView) {

    private val postImage = itemView.findViewById<ImageUnify>(R.id.post_image)
    private val postImageLayout = itemView.findViewById<ConstraintLayout>(R.id.post_image_layout)
    private val llLihatProduct = itemView.findViewById<LinearLayout>(R.id.ll_lihat_product)
    private val tvLihatProduct = itemView.findViewById<TextView>(R.id.tv_lihat_product)
    private val flashSaleViewCardUpcoming = itemView.findViewById<FlashSaleRilisanCampaignUpcomingView>(R.id.feed_fst_upcoming)
    private val flashSaleViewCardOngoing = itemView.findViewById<FlashSaleRilisanCampaignOngoingView>(R.id.feed_fst_ongoing)
    private val likeAnim = itemView.findViewById<ImageUnify>(R.id.like_anim)

    private val animationLike = AnimationUtils.loadAnimation(
        itemView.context,
        android.R.anim.fade_in
    )

    private val postGestureDetector = GestureDetector(
        itemView.context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                listener.onImageClicked(this@CarouselImageViewHolder)

                animateLihatProduct(
                    toggleAllPostTagViews()
                )
                itemView.removeCallbacks(hideLihatProduct)

                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                onPostTagViews {
                    it.hideExpandedViewIfShown()
                }
                animateLihatProduct(false)

                if (!dataSource.getFeedXCard().isTopAds) {
                    likeAnim.startAnimation(animationLike)
                }

                listener.onImageDoubleClicked(this@CarouselImageViewHolder)

                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                listener.onImageLongClicked(this@CarouselImageViewHolder)
            }
        }
    )

    private val showLihatProduct = Runnable {
        animateLihatProduct(true)
    }

    private val hideLihatProduct = Runnable {
        animateLihatProduct(false)
    }

    init {
        postImage.setOnTouchListener { _, event ->
            postGestureDetector.onTouchEvent(event)
            true
        }

        likeAnim.setImageDrawable(
            MethodChecker.getDrawable(
                itemView.context,
                R.drawable.ic_thumb_filled
            )
        )

        animationLike.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                likeAnim.visible()
                listener.onLiked(this@CarouselImageViewHolder)
            }

            override fun onAnimationEnd(animation: Animation) {
                likeAnim.gone()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                removeAllPendingCallbacks()
            }
        })
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        removeExistingPostTags()
        postImage.setImageDrawable(null)
    }

    fun focusMedia() {
        removeAllPendingCallbacks()

        val isExpanded = tvLihatProduct.isVisible
        if (isExpanded) return

        itemView.postDelayed(showLihatProduct, FOCUS_SHOW_LIHAT_PRODUK_DELAY)
        itemView.postDelayed(
            hideLihatProduct,
            FOCUS_SHOW_LIHAT_PRODUK_DELAY + FOCUS_HIDE_LIHAT_PRODUK_DELAY
        )

        onPostTagViews { it.resetView() }
    }

    fun removeFocus() {
        removeAllPendingCallbacks()
        resetUi()
    }

    fun bind(item: FeedXMedia) {
        val card = dataSource.getFeedXCard()

        postImage.setImageUrl(item.mediaUrl)
        llLihatProduct.showWithCondition(item.tagProducts.isNotEmpty())
        when {
            card.campaign.isUpcoming -> {
                updateAsgcButton()
                showHideFlashSaleRsUpcomingCampaignCard(card)
            }
            card.campaign.isOngoing -> {
                showHideFlashSaleRsOngoingCampaignCard(card, item)
            }
            else -> {
                flashSaleViewCardUpcoming.hide()
                flashSaleViewCardOngoing.hide()
            }
        }

        itemView.doOnLayout {
            removeExistingPostTags()
            item.tagging.forEach { tagging ->
                val tagView = PostTagView(itemView.context, tagging)
                tagView.bindData(
                    tagBubbleListener  = dataSource.getTagBubbleListener(),
                    products = if (card.isTypeProductHighlight && card.useASGCNewDesign || card.isTypeUGC) {
                        card.products
                    } else card.tags,
                    width = it.width,
                    height = it.height,
                    positionInFeed = dataSource.getPositionInFeed(),
                    bitmap = postImage?.drawable?.toBitmap(),
                    campaign = dataSource.getFeedXCard().campaign
                )
                postImageLayout.addView(tagView)
            }
        }

        llLihatProduct.setOnClickListener {
            listener.onLihatProductClicked(this, item)
        }

        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onImpressed(this)
        }
    }

    private fun showHideFlashSaleRsUpcomingCampaignCard(feedXCard: FeedXCard){
        flashSaleViewCardUpcoming.setupTimer(feedXCard.campaign.endTime) {
            fstListener?.onTimerFinishUpcoming()
        }
        flashSaleViewCardUpcoming.setData(
            feedXCard = feedXCard,
            positionInFeed = dataSource.getPositionInFeed()
        )
        fstListener?.let {
            flashSaleViewCardUpcoming.setListener(fstListener)
        }
        flashSaleViewCardUpcoming.showWithCondition(feedXCard.isTypeProductHighlight)
        flashSaleViewCardOngoing.hide()
    }

    private fun showHideFlashSaleRsOngoingCampaignCard(feedXCard: FeedXCard,  media: FeedXMedia){
        flashSaleViewCardOngoing.setupTimer(feedXCard.campaign.endTime) {
            fstListener?.onTimerFinishOngoing()
        }
        flashSaleViewCardOngoing.setData(
            feedXCard = feedXCard,
            positionInFeed = dataSource.getPositionInFeed(),
            media = media
        )
        flashSaleViewCardOngoing.showWithCondition(feedXCard.isTypeProductHighlight)
        flashSaleViewCardUpcoming.hide()

    }

    fun updateAsgcButton(){
        flashSaleViewCardUpcoming.setReminderBtnState(dataSource.getFeedXCard().campaign.reminder, dataSource.getPositionInFeed())
    }

    private fun removeExistingPostTags() {
        (0 until postImageLayout.childCount).mapNotNull {
            postImageLayout.getChildAt(it) as? PostTagView
        }.forEach { postImageLayout.removeView(it) }

        tvLihatProduct.gone()
    }

    private fun onPostTagViews(onTag: (PostTagView) -> Unit) {
        (0 until postImageLayout.childCount).forEach {
            val view = postImageLayout.getChildAt(it)
            if (view is PostTagView) onTag(view)
        }
    }

    private fun toggleAllPostTagViews(): Boolean {
        var isAnyGoingToVisible = false
        onPostTagViews {
            val isGoingToVisible = it.toggleExpandedView()
            if (!isAnyGoingToVisible) isAnyGoingToVisible = isGoingToVisible
        }
        return isAnyGoingToVisible
    }

    private fun animateLihatProduct(shouldShow: Boolean) {
        TransitionManager.beginDelayedTransition(
            llLihatProduct,
            AutoTransition()
                .setDuration(ANIMATION_LIHAT_PRODUCT_DURATION)
        )

        tvLihatProduct.showWithCondition(shouldShow)
    }

    private fun removeAllPendingCallbacks(resetTopAds: Boolean = true) {
        itemView.removeCallbacks(showLihatProduct)
        itemView.removeCallbacks(hideLihatProduct)
    }

    private fun resetUi(resetTopAds: Boolean = true) {
        onPostTagViews { it.hideExpandedViewIfShown() }
        tvLihatProduct.gone()
    }

    companion object {
        private const val FOCUS_SHOW_LIHAT_PRODUK_DELAY = 1000L
        private const val FOCUS_HIDE_LIHAT_PRODUK_DELAY = 3000L

        private const val ANIMATION_LIHAT_PRODUCT_DURATION = 250L

        fun create(
            parent: ViewGroup,
            dataSource: FeedPostCarouselAdapter.DataSource,
            listener: Listener,
            fstListener: FeedCampaignListener?
        ) = CarouselImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_post_image_new,
                    parent,
                    false,
                ),
            dataSource,
            listener,
            fstListener
        )
    }

    interface Listener {
        fun onTopAdsCardClicked(viewHolder: CarouselImageViewHolder, media: FeedXMedia)
        fun onImageClicked(viewHolder: CarouselImageViewHolder)
        fun onImageDoubleClicked(viewHolder: CarouselImageViewHolder) {}
        fun onImageLongClicked(viewHolder: CarouselImageViewHolder) {}
        fun onLiked(viewHolder: CarouselImageViewHolder)
        fun onImpressed(viewHolder: CarouselImageViewHolder)
        fun onLihatProductClicked(viewHolder: CarouselImageViewHolder, media: FeedXMedia)
    }
}
