package com.tokopedia.product.detail.view.adapter.dynamicadapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.variant.VariantDataModel
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

    fun notifySnapshotWithPayloads(snapshotData: ProductSnapshotDataModel?, payload: Int? = null) {
        snapshotData?.let {
            val indexOfSnapshot = list.indexOf(it)
            if (payload != null) {
                notifyItemChanged(indexOfSnapshot, payload)
            } else {
                notifyItemChanged(indexOfSnapshot)
            }
        }
    }

    fun notifyShopInfo(shopInfoData: ProductShopInfoDataModel?, payload: Int) {
        shopInfoData?.let {
            val indexOfShopInfo = list.indexOf(shopInfoData)
            notifyItemChanged(indexOfShopInfo, payload)
        }
    }

    fun notifyGeneralInfo(generalInfo: ProductGeneralInfoDataModel?, payload: Int? = null) {
        generalInfo?.let {
            val indexOfGeneralInfo = list.indexOf(generalInfo)
            if (payload != null) {
                notifyItemChanged(indexOfGeneralInfo, payload)
            } else {
                notifyItemChanged(indexOfGeneralInfo)
            }
        }
    }

    fun notifyShipingInfo(shipingInfo: ProductGeneralInfoDataModel?) {
        shipingInfo?.let {
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

    fun removeMerchantVoucherSection(data: ProductMerchantVoucherDataModel?) {
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

    fun getVariantPosition(data: VariantDataModel?): Int = if (data != null) list.indexOf(data) else 0

    fun notifyVariantSection(data: VariantDataModel?, payload: Int?) {
        data?.let {
            val indexOfVariant = list.indexOf(it)
            if (payload != null) {
                notifyItemChanged(indexOfVariant, payload)
            } else {
                notifyItemChanged(indexOfVariant)
            }
        }
    }

    fun notifyNotifyMe(notifyMeData: ProductNotifyMeDataModel?, payload: Int?) {
        notifyMeData?.let{
            val indexOfNotifyMe = list.indexOf(notifyMeData)
            if (payload != null) {
                notifyItemChanged(indexOfNotifyMe, payload)
            } else {
                notifyItemChanged(indexOfNotifyMe)
            }
        }
    }

    fun notifyDiscussion(productDiscussionMostHelpfulDataModel: ProductDiscussionMostHelpfulDataModel?) {
        productDiscussionMostHelpfulDataModel?.let {
            val indexOfDiscussion = list.indexOf(it)
            if(indexOfDiscussion == -1) {
                return
            }
            notifyItemChanged(indexOfDiscussion)
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ProductRecommendationViewHolder) {
            listener.loadTopads()
        }
    }
}