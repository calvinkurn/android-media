package com.tokopedia.product.detail.view.adapter.dynamicadapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationViewHolder

class DynamicProductDetailAdapter(
        adapterTypeFactory: DynamicProductDetailAdapterFactoryImpl,
        val listener: DynamicProductDetailListener
) : BaseListAdapter<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl>(adapterTypeFactory) {

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
                if (it.isRecomenDataEmpty) {
                    clearElement(it)
                } else {
                    notifyItemChanged(list.indexOf(it))
                }
            }
        }
    }

    fun removeRecommendation(listOfData: List<ProductRecommendationDataModel>?) {
        listOfData?.run {
            forEach {
                clearElement(it)
            }
        }
    }

    fun removeDiscussionSection(data: ProductDiscussionDataModel?) {
        clearElement(data)
    }

    fun removeGeneralInfo(data: ProductGeneralInfoDataModel?) {
        clearElement(data)
    }

    fun removeMostHelpfulReviewSection(data: ProductMostHelpfulReviewDataModel?) {
        clearElement(data)
    }

    fun getTradeinPosition(data: ProductGeneralInfoDataModel?): Int {
        return if(data != null) {
            list.indexOf(data)
        } else {
            0
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ProductRecommendationViewHolder) {
            listener.loadTopads()
        }
    }
}