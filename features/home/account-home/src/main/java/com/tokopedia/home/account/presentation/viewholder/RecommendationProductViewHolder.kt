package com.tokopedia.home.account.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel

/**
 * @author devarafikry on 24/07/19.
 */
class RecommendationProductViewHolder(itemView: View, val accountItemListener: AccountItemListener) : AbstractViewHolder<RecommendationProductViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_account_product_recommendation
    }
    private val productCardView: ProductCardGridView by lazy { itemView.findViewById<ProductCardGridView>(R.id.account_product_recommendation) }

    override fun bind(element: RecommendationProductViewModel) {
        productCardView.run {
            setProductModel(
                    element.product.toProductCardModel(hasThreeDots = true)
            )
            setImageProductViewHintListener(element.product, object : ViewHintListener {
                override fun onViewHint() {
                    accountItemListener.onProductRecommendationImpression(element.product, adapterPosition)
                }
            })

            setOnClickListener {
                accountItemListener.onProductRecommendationClicked(element.product, adapterPosition, element.widgetTitle)
            }

            setThreeDotsOnClickListener {
                accountItemListener.onProductRecommendationThreeDotsClicked(element.product, adapterPosition)
            }
        }
    }
}
