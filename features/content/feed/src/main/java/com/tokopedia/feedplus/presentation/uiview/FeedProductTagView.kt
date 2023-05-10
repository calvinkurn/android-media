package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.FeedProductTagViewBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
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

    fun bindData(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int,
        trackerData: FeedTrackerDataModel?
    ) {
        this.postId = postId
        this.author = author
        this.postType = postType
        this.isFollowing = isFollowing
        this.campaign = campaign
        this.trackerData = trackerData
        this.totalProducts = totalProducts

        bindText(products)
    }

    fun bindText(
        products: List<FeedCardProductModel>
    ) {
        with(binding) {
            when {
                products.size == PRODUCT_COUNT_ZERO -> {
                    root.hide()
                }
                products.size == PRODUCT_COUNT_ONE -> {
                    tvTagProduct.text = products.firstOrNull()?.name
                    root.show()
                }
                products.size > PRODUCT_COUNT_NINETY_NINE -> {
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_99_more_text)
                    root.show()
                }
                else -> {
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_text, products.size)
                    root.show()
                }
            }

            root.setOnClickListener {
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
                            trackerData
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1
        const val PRODUCT_COUNT_NINETY_NINE = 99
    }

}
