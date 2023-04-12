package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.RecomComparisonBeautyModel
import com.tokopedia.recommendation_widget_common.widget.global.RecomVisitable

/**
 * Created by frenzel on 27/03/23
 */
data class PdpRecomWidgetDataModel(
    var recomWidgetModel: RecomVisitable,
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(): String = recomWidgetModel.type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = recomWidgetModel.name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is PdpRecomWidgetDataModel) {
            recomWidgetModel.recomWidgetData?.title == newData.recomWidgetModel.recomWidgetData?.title
                    && recomWidgetModel.recomWidgetData?.recommendationFilterChips == newData.recomWidgetModel.recomWidgetData?.recommendationFilterChips
                    && areRecomItemTheSame(newData.recomWidgetModel.recomWidgetData)
                    && areRecomQtyItemTheSame(newData.recomWidgetModel.recomWidgetData)
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is PdpRecomWidgetDataModel) {
            if (recomWidgetModel.recomWidgetData?.recommendationItemList?.size != newData.recomWidgetModel.recomWidgetData?.recommendationItemList?.size) {
                return null
            }

            if (!areFilterTheSame(newData.recomWidgetModel.recomWidgetData)) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_UPDATE_FILTER_RECOM)
                return bundle
            }
            if (!areRecomQtyItemTheSame(newData.recomWidgetModel.recomWidgetData)) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_UPDATE_QTY_RECOM_TOKONOW)
                return bundle
            }
            null
        } else {
            null
        }
    }

    private fun areRecomItemTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        if (recomWidgetModel.recomWidgetData?.recommendationItemList == null && newRecomWidgetData?.recommendationItemList == null) {
            return true
        } else if (recomWidgetModel.recomWidgetData?.recommendationItemList?.size != newRecomWidgetData?.recommendationItemList?.size) {
            return false
        }

        return recomWidgetModel.recomWidgetData?.recommendationItemList?.hashCode() == newRecomWidgetData?.recommendationItemList?.hashCode()
    }

    private fun areRecomQtyItemTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        if (recomWidgetModel.recomWidgetData?.recommendationItemList?.size == newRecomWidgetData?.recommendationItemList?.size && isOldAndItemIsTokonow(newRecomWidgetData)) {
            val itemSize = recomWidgetModel.recomWidgetData?.recommendationItemList?.size ?: 0
            for (i in 0 until itemSize) {
                if (recomWidgetModel.recomWidgetData?.recommendationItemList?.get(i)?.quantity != newRecomWidgetData?.recommendationItemList?.get(i)?.quantity
                        || recomWidgetModel.recomWidgetData?.recommendationItemList?.get(i)?.currentQuantity != newRecomWidgetData?.recommendationItemList?.get(i)?.currentQuantity) {
                    return false
                }
            }
        }
        return true
    }

    private fun isOldAndItemIsTokonow(newRecomWidgetData: RecommendationWidget?): Boolean {
        return recomWidgetModel.recomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
                && newRecomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
    }

    private fun areFilterTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        var areSame = false
        if (recomWidgetModel.recomWidgetData?.recommendationFilterChips == null && newRecomWidgetData?.recommendationFilterChips == null ||
            recomWidgetModel.recomWidgetData?.recommendationFilterChips?.isEmpty() == true && newRecomWidgetData?.recommendationFilterChips?.isEmpty() == true) {
            return true
        }
        val currentFilterData = recomWidgetModel.recomWidgetData?.recommendationFilterChips?.zip(newRecomWidgetData?.recommendationFilterChips
                ?: listOf())

        if (currentFilterData != null) {
            for ((oldItem, newItem) in currentFilterData) {
                if (newItem.isActivated == oldItem.isActivated) {
                    areSame = true
                    break
                }
            }
        }
        return areSame
    }

    fun cloneData(data : PdpRecomWidgetDataModel) : PdpRecomWidgetDataModel {
        return PdpRecomWidgetDataModel(
            RecomComparisonBeautyModel(
            )
        )
    }
}
