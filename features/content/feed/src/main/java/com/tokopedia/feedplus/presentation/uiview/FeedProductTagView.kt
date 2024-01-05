package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.FeedProductTagViewBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedTopAdsTrackerDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedProductTagView(
    private val binding: FeedProductTagViewBinding,
    private val listener: FeedListener
) {
    private var postId: String = ""
    private var author: FeedAuthorModel? = null
    private var postType: String = ""
    private var isFollowing: Boolean = false
    private var campaign: FeedCardCampaignModel? = null
    private var hasVoucher: Boolean = false
    private var totalProducts: Int = 0
    private var trackerData: FeedTrackerDataModel? = null
    private var topAdsTrackerData: FeedTopAdsTrackerDataModel? = null
    private var products: List<FeedCardProductModel> = emptyList()
    private var positionInFeed: Int = -1

    fun bindData(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        products: List<FeedCardProductModel>,
        totalProducts: Int,
        trackerData: FeedTrackerDataModel?,
        topAdsTrackerData: FeedTopAdsTrackerDataModel?,
        positionInFeed: Int
    ) {
        this.postId = postId
        this.author = author
        this.postType = postType
        this.isFollowing = isFollowing
        this.campaign = campaign
        this.trackerData = trackerData
        this.topAdsTrackerData = topAdsTrackerData
        this.totalProducts = totalProducts
        this.products = products
        this.positionInFeed = positionInFeed

        bindText(products, totalProducts)
    }

    fun bindText(
        products: List<FeedCardProductModel>,
        totalProducts: Int
    ) {
        this.totalProducts = totalProducts
        with(binding) {
            when {
                products.size == PRODUCT_COUNT_ZERO && totalProducts == PRODUCT_COUNT_ZERO -> {
                    root.hide()
                }
                products.size == PRODUCT_COUNT_ONE -> {
                    tvTagProduct.text = products.firstOrNull()?.name
                    root.show()
                }
                totalProducts > PRODUCT_COUNT_NINETY_NINE -> {
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_99_more_text)
                    root.show()
                }
                else -> {
                    val total =
                        if (totalProducts > PRODUCT_COUNT_ZERO) totalProducts else products.size
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_text, total)
                    root.show()
                }
            }

            root.setOnClickListener {
                topAdsTrackerData?.let {
                    listener.onTopAdsClick(it)
                }

                author?.let { mAuthor ->
                    campaign?.let { mCampaign ->
                        listener.onProductTagViewClicked(
                            postId,
                            mAuthor,
                            postType,
                            isFollowing,
                            mCampaign,
                            hasVoucher,
                            products,
                            totalProducts,
                            trackerData,
                            positionInFeed
                        )
                    }
                }
            }
        }
    }

    fun showClearView() {
        binding.root.hide()
    }

    fun showIfPossible() {
        bindText(this.products, this.totalProducts)
    }

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1
        const val PRODUCT_COUNT_NINETY_NINE = 99
    }
}
