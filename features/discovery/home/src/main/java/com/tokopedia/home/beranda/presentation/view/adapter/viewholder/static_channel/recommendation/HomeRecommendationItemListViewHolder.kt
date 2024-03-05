package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.listener.ImpressionRecommendationItemListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemListViewHolder(
    itemView: View,
    private val listener: ImpressionRecommendationItemListener,
    private val homeRecommendationListener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationItemDataModel>(
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
                    listener.onProductCardImpressed(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setOnClickListener {
            homeRecommendationListener.onProductCardClicked(
                element.toModel(),
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setThreeDotsOnClickListener {
            homeRecommendationListener.onProductCardThreeDotsClicked(
                element.toModel(),
                bindingAdapterPosition
            )
        }
    }
}
