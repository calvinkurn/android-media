package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemGridViewHolder(
    view: View,
    private val listener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationItemDataModel>(
    view,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_grid
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: HomeRecommendationItemDataModel) {
        setLayout(element)

        val model = element.toModel()
        productCardImpressionListener(model)
        setItemProductCardClickListener(model)
        setItemThreeDotsClickListener(model)
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it.toModel())
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        productCardView.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: RecommendationCardModel) {
        productCardView.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductCardImpressed(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: RecommendationCardModel) {
        productCardView.setOnClickListener {
            listener.onProductCardClicked(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(productCardItem: RecommendationCardModel) {
        productCardView.setThreeDotsOnClickListener {
            listener.onProductCardThreeDotsClicked(
                productCardItem,
                bindingAdapterPosition
            )
        }
    }
}
