package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.content.common.databinding.ViewProductSeeMoreBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedTopAdsTrackerDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_NINETY_NINE
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedProductButtonView(
    private val binding: ViewProductSeeMoreBinding,
    private val listener: FeedListener
) {

    private var products: List<FeedCardProductModel> = emptyList()
    private var totalProducts: Int = 0

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
        positionInFeed: Int
    ) = with(binding) {

        bind(products, totalProducts)
        bindProductIcon(hasVoucher, isContainsDiscountProduct(products)) {
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

    private fun bindProductIcon(hasVoucher: Boolean, hasDiscount: Boolean, onClick: () -> Unit) {
        if (hasVoucher || hasDiscount) {
            binding.icPlayProductSeeMore.hide()
            binding.lottieProductSeeMore.show()
            binding.lottieProductSeeMore.setAnimationFromUrl(binding.root.context.getString(contentcommonR.string.feed_product_icon_anim))
            binding.lottieProductSeeMore.setOnClickListener { onClick() }
        } else {
            binding.icPlayProductSeeMore.show()
            binding.lottieProductSeeMore.hide()
            binding.icPlayProductSeeMore.setOnClickListener { onClick() }
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

    companion object {
        private const val NINETY_NINE_PLUS = "99+"
    }
}
