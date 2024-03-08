package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationItemViewHolder]
 * @param productItem the pojo of product recommendation from the network
 */
data class RecommendationItemDataModel(
    var productItem: RecommendationItem
) : HomeRecommendationDataModel {

    companion object {
        fun layout(isReimagine: Boolean) =
            if (isReimagine) R.layout.fragment_recommendation_reimagine_item
            else R.layout.fragment_recommendation_item
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    override fun name(): String = productItem.name

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        return if (newData is RecommendationItemDataModel) {
            productItem == newData.productItem &&
                areRecomQtyItemTheSame(newData.productItem)
        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null

    private fun areRecomQtyItemTheSame(recomItem: RecommendationItem): Boolean {
        if (this.productItem.quantity != recomItem.quantity || this.productItem.currentQuantity != recomItem.currentQuantity) {
            return false
        }
        return true
    }
}
