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

    private var item: HomeRecommendationItemDataModel? = null

    override fun bind(element: HomeRecommendationItemDataModel) {
        this.item = element
        setLayout(element)
        productCardImpressionListener()
        setItemProductCardClickListener()
        setItemThreeDotsClickListener()
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        productCardView.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener() {
        item?.let { productCardItem ->
            productCardView.setImageProductViewHintListener(
                productCardItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        homeRecommendationListener.onProductImpression(
                            productCardItem,
                            bindingAdapterPosition
                        )
                    }
                }
            )
        }
    }

    private fun setItemProductCardClickListener() {
        item?.let { productCardItem ->
            productCardView.setOnClickListener {
                homeRecommendationListener.onProductClick(
                    productCardItem,
                    bindingAdapterPosition
                )
            }
        }
    }

    private fun setItemThreeDotsClickListener() {
        item?.let { productCardItem ->
            productCardView.setThreeDotsOnClickListener {
                homeRecommendationListener.onProductThreeDotsClick(
                    productCardItem,
                    bindingAdapterPosition
                )
            }
        }
    }
}
