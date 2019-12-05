package com.tokopedia.product.detail.view.adapter.dynamicadapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl

class DynamicProductDetailAdapter(
        private val adapterTypeFactory: DynamicProductDetailAdapterFactoryImpl
) : BaseListAdapter<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(adapterTypeFactory) {

    fun notifySnapshotWithPayloads(snapshotData: ProductSnapshotDataModel, payload: Int) {
        val indexOfSnapshot = list.indexOf(snapshotData)
        notifyItemChanged(indexOfSnapshot, payload)
    }

    fun notifyShopInfo(shopInfoData: ProductShopInfoDataModel, payload: Int) {
        val indexOfShopInfo = list.indexOf(shopInfoData)
        notifyItemChanged(indexOfShopInfo, payload)
    }

    fun notifyShipingInfo(shipingInfo: ProductGeneralInfoDataModel) {
        val indexOfShipingInfo = list.indexOf(shipingInfo)
        notifyItemChanged(indexOfShipingInfo)
    }

    fun notifyRecomAdapter(listOfData: List<ProductRecommendationDataModel>?) {
        listOfData?.run {
            forEach {
                if (it.recomWidgetData == null) {
                    clearElement(it)
                } else {
                    notifyItemChanged(list.indexOf(it))
                }
            }
        }
    }

    fun removeRecommendation(listOfData: List<ProductRecommendationDataModel>?) {
        listOfData?.run {
            listOfData.forEach {
                clearElement(it)
            }
        }
    }

    fun removeDiscussionSection(data: ProductDiscussionDataModel?) {
        clearElement(data)
    }

    fun removeInstallmentSection(data: ProductGeneralInfoDataModel?) {
        clearElement(data)
    }

    fun removeImageReviewSection(data: ProductImageReviewDataModel?) {
        clearElement(data)
    }

    fun removeMostHelpfulReviewSection(data: ProductMostHelpfulReviewDataModel?) {
        clearElement(data)
    }

    fun removeTradeinSection(data: ProductTradeinDataModel?) {
        clearElement(data)
    }

}