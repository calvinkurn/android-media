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

    fun notifySnapshot(snapshotData: ProductSnapshotDataModel?) {
        snapshotData?.let {
            val indexOfSnapshot = list.indexOf(it)
            notifyItemChanged(indexOfSnapshot)
        }
    }

    fun notifySnapshotWithPayloads(snapshotData: ProductSnapshotDataModel?, payload: Int) {
        snapshotData?.let{
            val indexOfSnapshot = list.indexOf(it)
            notifyItemChanged(indexOfSnapshot, payload)
        }
    }

    fun notifyShopInfo(shopInfoData: ProductShopInfoDataModel?, payload: Int) {
        shopInfoData?.let{
            val indexOfShopInfo = list.indexOf(shopInfoData)
            notifyItemChanged(indexOfShopInfo, payload)
        }
    }

    fun notifyShipingInfo(shipingInfo: ProductGeneralInfoDataModel?) {
        shipingInfo?.let{
            val indexOfShipingInfo = list.indexOf(it)
            notifyItemChanged(indexOfShipingInfo)
        }
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
        data?.let {
            clearElement(it)
        }
    }

    fun removeGeneralInfo(data: ProductGeneralInfoDataModel?) {
        data?.let {
            clearElement(it)
        }
    }

    fun removeMostHelpfulReviewSection(data: ProductMostHelpfulReviewDataModel?) {
        data?.let {
            clearElement(it)
        }
    }

    fun removeMerchantVoucherSection(data : ProductMerchantVoucherDataModel?) {
        data?.let {
            clearElement(it)
        }
    }

    fun getTradeinPosition(data: ProductGeneralInfoDataModel?): Int {
        return if (data != null) {
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