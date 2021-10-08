package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationCarouselItemViewHolder]
 * @param productItem the pojo of product recommendation from the network
 * @param parentPosition the parent position [RecommendationCarouselViewHolder]
 * @param listener the default listener for recommendation widget, it will handling on impression, click, wishlist tracker
 */
data class RecommendationCarouselItemDataModel(
        val productItem: RecommendationItem,
        val parentPosition: Int
) : HomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_another_product_item
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    override fun name(): String = productItem.name

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        return if (newData is RecommendationCarouselItemDataModel) {
            productItem == newData.productItem &&
                    parentPosition == newData.parentPosition &&
                    productItem.hashCode() == newData.productItem.hashCode() &&
                    areRecomQtyItemTheSame(newData.productItem)

        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null


    private fun areRecomQtyItemTheSame(recomItem: RecommendationItem): Boolean {
        if (this.productItem.quantity != recomItem.quantity || this.productItem.currentQuantity != recomItem.currentQuantity) {
            return false
        }
        return true
    }
}