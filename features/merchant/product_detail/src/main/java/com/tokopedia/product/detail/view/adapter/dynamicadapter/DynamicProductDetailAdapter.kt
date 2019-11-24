package com.tokopedia.product.detail.view.adapter.dynamicadapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl

class DynamicProductDetailAdapter(
        private val adapterTypeFactory: DynamicProductDetailAdapterFactoryImpl
) : BaseListAdapter<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(adapterTypeFactory) {

    fun notifySnapshotWithPayloads(snapshotData: ProductSnapshotDataModel, payload: Int? = null) {
        val indexOfSnapshot = list.indexOf(snapshotData)
        notifyItemChanged(indexOfSnapshot, payload)
    }

    fun notifyShopInfo(shopInfoData: ProductShopInfoDataModel, payload: Int) {
        val indexOfShopInfo = list.indexOf(shopInfoData)
        notifyItemChanged(indexOfShopInfo, payload)
    }

    fun notifyRecomAdapter(listOfData: List<ProductRecommendationDataModel>?) {
        listOfData?.run {
            forEach {
                notifyItemChanged(list.indexOf(it))
            }
        }
    }

    fun getRecommendationIndex(): Int {
        val position = -1
        list.forEachIndexed { index, visitable ->
            if (visitable is ProductRecommendationDataModel) {
                return@getRecommendationIndex index
            }
        }
        return position
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