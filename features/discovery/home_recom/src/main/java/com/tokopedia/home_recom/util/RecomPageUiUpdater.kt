package com.tokopedia.home_recom.util

import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst

/**
 * Created by yfsx on 01/09/21.
 */
class RecomPageUiUpdater(var dataList: MutableList<HomeRecommendationDataModel>) {

    fun appendFirstData(listOfData: List<HomeRecommendationDataModel>) {
        dataList = mutableListOf()
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }

    fun appendNextData(listOfData: List<HomeRecommendationDataModel>) {
        listOfData.forEach {
            when (it) {
                is RecommendationItemDataModel -> dataList.add(it)
                is ProductInfoDataModel -> dataList.add(it)
            }
        }
    }


//    fun updateRecomWithMinicartData(miniCartMap: MutableMap<String, MiniCartItem>, dataList: List<HomeRecommendationDataModel>): List<HomeRecommendationDataModel> {
//
//    }
}