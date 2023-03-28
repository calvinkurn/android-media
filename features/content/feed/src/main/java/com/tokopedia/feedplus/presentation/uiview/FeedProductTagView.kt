package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.FeedProductTagViewBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 14/03/23
 */
class FeedProductTagView(
    private val binding: FeedProductTagViewBinding, private val listener: FeedListener
) {

    fun bindData(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int
    ) {
        with(binding) {
            when {
                totalProducts == PRODUCT_COUNT_ZERO -> {
                    root.hide()
                }
                totalProducts == PRODUCT_COUNT_ONE -> {
                    tvTagProduct.text = products.firstOrNull()?.name
                    root.show()
                }
                totalProducts > PRODUCT_COUNT_NINETY_NINE -> {
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_99_more_text)
                    root.show()
                }
                else -> {
                    tvTagProduct.text =
                        root.context.getString(R.string.feeds_tag_product_text, totalProducts)
                    root.show()
                }
            }

            root.setOnClickListener {
                listener.onProductTagViewClicked(
                    postId,
                    author,
                    postType,
                    isFollowing,
                    campaign,
                    hasVoucher,
                    products,
                    totalProducts
                )
            }
        }
    }

    companion object {
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1
        const val PRODUCT_COUNT_NINETY_NINE = 99
    }

}
