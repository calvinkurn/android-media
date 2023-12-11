package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemListViewHolder(
    itemView: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationItemDataModel>(
    itemView,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_list
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: HomeRecommendationItemDataModel) {
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        productCardView.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: HomeRecommendationItemDataModel) {
        productCardView.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    homeRecommendationListener.onProductImpression(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setOnClickListener {
            homeRecommendationListener.onProductClick(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setThreeDotsOnClickListener {
            homeRecommendationListener.onProductThreeDotsClick(
                element,
                bindingAdapterPosition
            )
        }
    }
}
