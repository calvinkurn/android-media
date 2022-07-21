package com.tokopedia.feedcomponent.view.adapter.viewholder.post.image

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.feedcomponent.view.transition.BackgroundColorTransition
import com.tokopedia.feedcomponent.view.widget.PostTagView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by kenny.hadisaputra on 29/06/22
 */
@SuppressLint("ClickableViewAccessibility")
class CarouselImageViewHolder(
    itemView: View,
    private val dataSource: FeedPostCarouselAdapter.DataSource,
    private val listener: Listener,
) : BaseViewHolder(itemView) {

    private val postImage = itemView.findViewById<ImageUnify>(R.id.post_image)
    private val postImageLayout = itemView.findViewById<ConstraintLayout>(R.id.post_image_layout)
    private val llLihatProduct = itemView.findViewById<LinearLayout>(R.id.ll_lihat_product)
    private val tvLihatProduct = itemView.findViewById<TextView>(R.id.tv_lihat_product)
    private val likeAnim = itemView.findViewById<ImageUnify>(R.id.like_anim)

    private val topAdsCard = itemView.findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
    private val topAdsProductName = itemView.findViewById<Typography>(R.id.top_ads_product_name)
    private val topAdsChevron = topAdsCard.findViewById<IconUnify>(R.id.chevron)

    private val animationLike = AnimationUtils.loadAnimation(
        itemView.context,
        android.R.anim.fade_in
    )

    private val postGestureDetector = GestureDetector(
        itemView.context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                changeCTABtnColorAsPerWidget(dataSource.getFeedXCard())
                listener.onImageClicked(this@CarouselImageViewHolder)

                animateLihatProduct(
                    toggleAllPostTagViews()
                )
                itemView.removeCallbacks(hideLihatProduct)

                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                onPostTagViews {
                    it.hideExpandedViewIfShown()
                }
                animateLihatProduct(false)

                if (!dataSource.getFeedXCard().isTopAds) {
                    likeAnim.startAnimation(animationLike)
                    changeCTABtnColorAsPerWidget(
                        dataSource.getFeedXCard(),
                        shouldNotify = true,
                    )
                }

                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                changeCTABtnColorAsPerWidget(dataSource.getFeedXCard())
            }
        }
    )

    private val showLihatProduct = Runnable {
        animateLihatProduct(true)
    }

    private val hideLihatProduct = Runnable {
        animateLihatProduct(false)
    }

    private val focusTopAds = Runnable {
        changeCTABtnColorAsPerWidget(dataSource.getFeedXCard())
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
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View?) {
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
        itemView.postDelayed(focusTopAds, FOCUS_TOP_ADS_DELAY)

        onPostTagViews { it.resetView() }
    }

    fun changeTopAds(isColorChangedAsPerAsgcWidget: Boolean) {
        val card = dataSource.getFeedXCard()
        if (isColorChangedAsPerAsgcWidget) changeCTABtnColorAsPerWidget(card, shouldNotify = false)
        else changeTopAdsColorToWhite(card)
    }

    fun removeFocus(resetTopAds: Boolean) {
        removeAllPendingCallbacks(resetTopAds = resetTopAds)
        resetUi(resetTopAds = resetTopAds)
    }

    fun bind(item: FeedXMedia) {
        val card = dataSource.getFeedXCard()

        postImage.setImageUrl(item.mediaUrl)
        llLihatProduct.showWithCondition(item.tagProducts.isNotEmpty())

        setupTopAds(card, item)

        itemView.doOnLayout {
            removeExistingPostTags()
            item.tagging.forEach { tagging ->
                val tagView = PostTagView(itemView.context, tagging)
                tagView.bindData(
                    dynamicPostListener = dataSource.getDynamicPostListener(),
                    products = if (card.isTypeProductHighlight && card.useASGCNewDesign) {
                        card.products
                    } else card.tags,
                    width = it.width,
                    height = it.height,
                    positionInFeed = dataSource.getPositionInFeed(),
                    bitmap = postImage?.drawable?.toBitmap(),
                )
                postImageLayout.addView(tagView)
            }
        }

        llLihatProduct.setOnClickListener {
            changeCTABtnColorAsPerWidget(dataSource.getFeedXCard())
            listener.onLihatProductClicked(this, item)
        }

        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onImpressed(this)
        }
    }

    private fun setupTopAds(card: FeedXCard, media: FeedXMedia) {
        topAdsProductName.text = getCTAButtonText(card)
        topAdsCard.showWithCondition(
            shouldShow = card.isTypeProductHighlight || card.isTopAds
        )

        topAdsCard.setOnClickListener {
            changeCTABtnColorAsPerWidget(card)
            listener.onTopAdsCardClicked(this, media)
        }
        if (!card.isAsgcColorChangedAsPerWidgetColor) changeTopAdsColorToWhite(card)
        else changeCTABtnColorAsPerWidget(card, shouldNotify = false)
    }

    private fun getCTAButtonText(card: FeedXCard) =
         if (card.isTypeProductHighlight && !card.isASGCDiscountToko && card.totalProducts > 1)
            itemView.context.getString(R.string.feeds_check_x_products, card.totalProducts)
        else if (card.isASGCDiscountToko && card.totalProducts > 1)
            itemView.context.getString(
                R.string.feeds_asgc_disc_x_products,
                card.totalProducts,
                card.maximumDisPercentFmt
            )
        else if (card.isASGCDiscountToko && card.totalProducts == 1)
             itemView.context.getString(
                 R.string.feeds_asgc_disc_one_products,
                 card.maximumDisPercentFmt
             )
        else itemView.context.getString(R.string.feeds_cek_sekarang)

    private fun changeTopAdsColorToGreen(card: FeedXCard, shouldNotify: Boolean = true) {
        changeTopAdsColor(
            primaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_G500
            ),
            secondaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            ),
        )
    }

    private fun changeTopAdsColorToWhite(card: FeedXCard) {
        card.isAsgcColorChangedAsPerWidgetColor = false

        changeTopAdsColor(
            primaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN50
            ),
            secondaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN600
            ),
        )
    }

    private fun changeTopAdsColorToRed(card: FeedXCard) {
        changeTopAdsColor(
            primaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.feedcomponent.R.color.feed_dms_asgc_discount_toko_btn_bg_color
            ),
            secondaryColor = MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            ),
        )
    }
    private fun changeCTABtnColorAsPerWidget(card: FeedXCard, shouldNotify: Boolean = true) {
        card.isAsgcColorChangedAsPerWidgetColor = true

        if (shouldNotify) listener.onCTAColorChangedAsPerWidgetColor(this)

        if (card.isASGCDiscountToko)
            changeTopAdsColorToRed(card)
        else
            changeTopAdsColorToGreen(card)
    }

    private fun changeTopAdsColor(
        primaryColor: Int,
        secondaryColor: Int,
    ) {
        itemView.removeCallbacks(focusTopAds)

        TransitionManager.beginDelayedTransition(
            itemView as ViewGroup,
            BackgroundColorTransition()
                .addTarget(topAdsCard)
        )
        topAdsProductName.setTextColor(secondaryColor)
        topAdsChevron.setColorFilter(secondaryColor)
        topAdsCard.setBackgroundColor(primaryColor)
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
        if (resetTopAds) itemView.removeCallbacks(focusTopAds)
    }

    private fun resetUi(resetTopAds: Boolean = true) {
        onPostTagViews { it.hideExpandedViewIfShown() }
        tvLihatProduct.gone()
        if (resetTopAds) changeTopAdsColorToWhite(dataSource.getFeedXCard())
    }

    companion object {
        private const val FOCUS_SHOW_LIHAT_PRODUK_DELAY = 1000L
        private const val FOCUS_HIDE_LIHAT_PRODUK_DELAY = 3000L
        private const val FOCUS_TOP_ADS_DELAY = 2000L

        private const val ANIMATION_LIHAT_PRODUCT_DURATION = 250L

        fun create(
            parent: ViewGroup,
            dataSource: FeedPostCarouselAdapter.DataSource,
            listener: Listener,
        ) = CarouselImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_post_image_new,
                    parent,
                    false,
                ),
            dataSource,
            listener,
        )
    }

    interface Listener {
        fun onTopAdsCardClicked(viewHolder: CarouselImageViewHolder, media: FeedXMedia)
        fun onCTAColorChangedAsPerWidgetColor(viewHolder: CarouselImageViewHolder)
        fun onImageClicked(viewHolder: CarouselImageViewHolder)
        fun onLiked(viewHolder: CarouselImageViewHolder)
        fun onImpressed(viewHolder: CarouselImageViewHolder)
        fun onLihatProductClicked(viewHolder: CarouselImageViewHolder, media: FeedXMedia)
    }
}