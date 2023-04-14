package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.content.common.databinding.ViewProductSeeMoreBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_NINETY_NINE
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView.Companion.PRODUCT_COUNT_ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedProductButtonView(
    private val binding: ViewProductSeeMoreBinding,
    private val listener: FeedListener
) {
    fun bindData(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>
    ) {
        with(binding) {
            when {
                products.size == PRODUCT_COUNT_ZERO -> {
                    root.hide()
                }
                products.size > PRODUCT_COUNT_NINETY_NINE -> {
                    tvPlayProductCount.text = NINETY_NINE_PLUS
                    root.show()
                }
                else -> {
                    tvPlayProductCount.text = products.size.toString()
                    root.show()
                }
            }

            icPlayProductSeeMore.setOnClickListener {
                listener.onProductTagButtonClicked(
                    postId,
                    author,
                    postType,
                    isFollowing,
                    campaign,
                    hasVoucher,
                    products
                )
            }
            tvPlayProductCount.setOnClickListener {
                listener.onProductTagButtonClicked(
                    postId,
                    author,
                    postType,
                    isFollowing,
                    campaign,
                    hasVoucher,
                    products
                )
            }
        }
    }

    companion object {
        private const val NINETY_NINE_PLUS = "99+"
    }
}
