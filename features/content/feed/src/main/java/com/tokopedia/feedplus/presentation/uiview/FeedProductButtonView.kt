package com.tokopedia.feedplus.presentation.uiview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.CountDownTimer
import com.tokopedia.content.common.databinding.ViewProductSeeMoreBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedTopAdsTrackerDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_NINETY_NINE
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedProductButtonView(
    private val binding: ViewProductSeeMoreBinding,
    private val listener: FeedListener
) {
    private val context = binding.root.context
    private var products: List<FeedCardProductModel> = emptyList()
    private var totalProducts: Int = 0

    private var animationOn = false
    private val animationAssets = context.getString(contentcommonR.string.feed_product_icon_anim)
    private val animationTimerDelay by lazyThreadSafetyNone {
        object: CountDownTimer(PRODUCT_ICON_ANIM_REPEAT_DELAY, DELAY_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                binding.lottieProductSeeMore.playAnimation()
            }
        }
    }
    private val animatorListener by lazyThreadSafetyNone {
        object : AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}
            override fun onAnimationEnd(p0: Animator) {
                animationTimerDelay.start()
            }
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        }
    }

    fun bindData(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int,
        trackerData: FeedTrackerDataModel?,
        topAdsTrackerData: FeedTopAdsTrackerDataModel?,
        positionInFeed: Int,
        enableProductIconAnim: Boolean
    ) = with(binding) {

        bind(products, totalProducts)
        bindProductIcon(
            enableProductIconAnim,
            hasVoucher,
            isContainsDiscountProduct(products)
        ) {
            onClick(
                postId,
                author,
                postType,
                isFollowing,
                campaign,
                hasVoucher,
                products,
                trackerData,
                topAdsTrackerData,
                positionInFeed
            )
        }

        tvPlayProductCount.setOnClickListener {
            onClick(
                postId,
                author,
                postType,
                isFollowing,
                campaign,
                hasVoucher,
                products,
                trackerData,
                topAdsTrackerData,
                positionInFeed
            )
        }
    }

    private fun onClick(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerData: FeedTrackerDataModel?,
        topAdsTrackerData: FeedTopAdsTrackerDataModel?,
        positionInFeed: Int
    ) {
        topAdsTrackerData?.let {
            listener.onTopAdsClick(it)
        }
        listener.onProductTagButtonClicked(
            postId,
            author,
            postType,
            isFollowing,
            campaign,
            hasVoucher,
            products,
            trackerData,
            positionInFeed
        )
    }

    private fun bind(products: List<FeedCardProductModel>, totalProducts: Int) {
        this.products = products
        this.totalProducts = totalProducts
        with(binding) {
            when {
                totalProducts == PRODUCT_COUNT_ZERO && products.size == PRODUCT_COUNT_ZERO -> {
                    root.hide()
                }
                totalProducts > PRODUCT_COUNT_NINETY_NINE -> {
                    tvPlayProductCount.text = NINETY_NINE_PLUS
                    root.show()
                }
                else -> {
                    val total =
                        if (totalProducts > PRODUCT_COUNT_ZERO) totalProducts else products.size
                    tvPlayProductCount.text = total.toString()
                    root.show()
                }
            }
        }
    }

    private fun bindProductIcon(
        enableProductIconAnim: Boolean,
        hasVoucher: Boolean,
        hasDiscount: Boolean,
        onClick: () -> Unit,
    ) = with(binding) {
        animationOn = enableProductIconAnim && (hasVoucher || hasDiscount)
        if (animationOn) {
            binding.lottieProductSeeMore.apply {
                setAnimationFromUrl(animationAssets)
                repeatCount = PRODUCT_ICON_ANIM_REPEAT_COUNT
            }
            lottieProductSeeMore.setOnClickListener { onClick() }
            icPlayProductSeeMore.hide()
            lottieProductSeeMore.show()
        } else {
            icPlayProductSeeMore.show()
            lottieProductSeeMore.hide()
        }
    }

    private fun isContainsDiscountProduct(products: List<FeedCardProductModel>): Boolean {
        val discountProduct = products.filter { it.isDiscount }
        return discountProduct.isNotEmpty()
    }

    fun showClearView() {
        binding.root.hide()
    }

    fun showIfPossible() {
        bind(this.products, totalProducts)
    }

    fun playProductIconAnimation() {
        if (!animationOn) return
        binding.root.addOneTimeGlobalLayoutListener {
            binding.lottieProductSeeMore.removeAnimatorListener(animatorListener)
            binding.lottieProductSeeMore.addAnimatorListener(animatorListener)
            binding.lottieProductSeeMore.playAnimation()
        }
    }

    fun pauseProductIconAnimation() {
        if (!animationOn) return
        animationTimerDelay.cancel()
        binding.lottieProductSeeMore.pauseAnimation()
    }

    companion object {
        private const val NINETY_NINE_PLUS = "99+"
        private const val PRODUCT_ICON_ANIM_REPEAT_COUNT = 2
        private const val PRODUCT_ICON_ANIM_REPEAT_DELAY = 10000L
        private const val DELAY_MILLIS = 1000L
    }
}
