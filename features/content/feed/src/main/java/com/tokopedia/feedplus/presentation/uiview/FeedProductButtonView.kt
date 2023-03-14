package com.tokopedia.feedplus.presentation.uiview

import com.tokopedia.content.common.databinding.ViewProductSeeMoreBinding
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
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
        postId: String, author: FeedAuthorModel, postType: String,
        isFollowing: Boolean, campaign: FeedCardCampaignModel,
        hasVoucher: Boolean, totalProducts: Int
    ) {
        with(binding) {
            when {
                totalProducts > PRODUCT_COUNT_NINETY_NINE -> {
                    tvPlayProductCount.text =
                        root.context.getString(R.string.feeds_tag_product_99_more_text)
                }
                else -> {
                    tvPlayProductCount.text =
                        root.context.getString(R.string.feeds_tag_product_text, totalProducts)
                }
            }

            if (totalProducts == PRODUCT_COUNT_ZERO) {
                root.hide()
            } else {
                root.show()
            }

            root.setOnClickListener {
                listener.onProductTagButtonClicked(
                    postId,
                    author,
                    postType,
                    isFollowing,
                    campaign,
                    hasVoucher,
                )
            }
        }
    }

}
