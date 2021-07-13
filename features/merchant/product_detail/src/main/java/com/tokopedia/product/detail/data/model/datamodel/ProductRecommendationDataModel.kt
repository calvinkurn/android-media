package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class ProductRecommendationDataModel(
        val type: String = "",
        val name: String = "",
        var recomWidgetData: RecommendationWidget? = null,

        //UI Data
        var filterData: List<AnnotationChip>? = null,
        var cardModel: List<ProductCardModel>? = null,
        var position: Int = -1,
        var updatedPosList: List<Int> = listOf()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    val isRecomenDataEmpty: Boolean
        get() = recomWidgetData?.recommendationItemList?.isEmpty() == true

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductRecommendationDataModel) {
            recomWidgetData?.title == newData.recomWidgetData?.title
                    && recomWidgetData?.recommendationFilterChips == newData.recomWidgetData?.recommendationFilterChips
                    && areRecomItemTheSame(newData.recomWidgetData)
                    && areRecomQtyItemTheSame(newData.recomWidgetData)
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductRecommendationDataModel) {
            if (cardModel?.size != newData.cardModel?.size) {
                return null
            }

            if (!areFilterTheSame(newData.recomWidgetData)) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_UPDATE_FILTER_RECOM)
                return bundle
            }
            if (!areRecomQtyItemTheSame(newData.recomWidgetData)) {
                val posList = getRecomPosListNeedToBeUpdated(newData.recomWidgetData)
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_UPDATE_QTY_RECOM_TOKONOW)
                newData.updatedPosList = posList
                return bundle
            }
            null
        } else {
            null
        }
    }

    private fun areRecomItemTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        if (recomWidgetData?.recommendationItemList == null && newRecomWidgetData?.recommendationItemList == null) {
            return true
        } else if (recomWidgetData?.recommendationItemList?.size != newRecomWidgetData?.recommendationItemList?.size) {
            return false
        }

        return recomWidgetData?.recommendationItemList?.hashCode() == newRecomWidgetData?.recommendationItemList?.hashCode()
    }

    private fun areRecomQtyItemTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        if (recomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
                && newRecomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
                && recomWidgetData?.recommendationItemList?.size == newRecomWidgetData?.recommendationItemList?.size) {
            val itemSize =recomWidgetData?.recommendationItemList?.size ?: 0
            for (i in 0 until itemSize) {
                if (recomWidgetData?.recommendationItemList?.get(i)?.quantity != newRecomWidgetData?.recommendationItemList[i].quantity) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRecomPosListNeedToBeUpdated(newRecomWidgetData: RecommendationWidget?): MutableList<Int> {
        val dataList = mutableListOf<Int>()
        if (recomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
                && newRecomWidgetData?.layoutType == LAYOUTTYPE_HORIZONTAL_ATC
                && recomWidgetData?.recommendationItemList?.size == newRecomWidgetData?.recommendationItemList?.size) {
            val itemSize =recomWidgetData?.recommendationItemList?.size ?: 0
            for (i in 0 until itemSize) {
                if (recomWidgetData?.recommendationItemList?.get(i)?.quantity != newRecomWidgetData?.recommendationItemList[i].quantity) {
                    dataList.add(i)
                }
            }
        }
        return dataList
    }

    private fun areFilterTheSame(newRecomWidgetData: RecommendationWidget?): Boolean {
        var areSame = false
        if (recomWidgetData?.recommendationFilterChips == null && newRecomWidgetData?.recommendationFilterChips == null ||
                recomWidgetData?.recommendationFilterChips?.isEmpty() == true && newRecomWidgetData?.recommendationFilterChips?.isEmpty() == true) {
            return true
        }
        val currentFilterData = recomWidgetData?.recommendationFilterChips?.zip(newRecomWidgetData?.recommendationFilterChips
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
}