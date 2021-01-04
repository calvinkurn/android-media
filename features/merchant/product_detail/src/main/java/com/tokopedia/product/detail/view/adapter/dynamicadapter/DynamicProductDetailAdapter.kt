package com.tokopedia.product.detail.view.adapter.dynamicadapter

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
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

    fun notifyBasicContentWithPayloads(contentData: ProductContentDataModel?, payload: Int? = null) {
        contentData?.let {
            val indexOfContent = list.indexOf(it)
            if (payload != null) {
                notifyItemChanged(indexOfContent, payload)
            } else {
                notifyItemChanged(indexOfContent)
            }
        }
    }

    fun notifyMediaWithPayload(contentData: ProductMediaDataModel?, payload: Int? = null) {
        contentData?.let {
            val indexOfMedia = list.indexOf(it)
            if (payload != null) {
                notifyItemChanged(indexOfMedia, payload)
            } else {
                notifyItemChanged(indexOfMedia)
            }
        }
    }

    fun notifyTopAdsBanner(topAdsImageData: TopAdsImageDataModel?) {
        topAdsImageData?.let {
            val indexOfTopAdsImage = list.indexOf(topAdsImageData)
            notifyItemChanged(indexOfTopAdsImage)
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

    fun notifyRecomAdapter(productRecommendationDataModel: ProductRecommendationDataModel?) {
        productRecommendationDataModel?.let{productRecommendationDataModel->
            val index = list.indexOf(productRecommendationDataModel)
            if(index != -1) notifyItemChanged(index)
        }
    }

    fun notifyFilterRecommendation(productRecommendationDataModel: ProductRecommendationDataModel){
        val index = list.indexOf(productRecommendationDataModel)
        if(index != -1) notifyItemChanged(index, Bundle().apply {
            putBoolean(ProductRecommendationViewHolder.KEY_UPDATE_FILTER_RECOM, true)
        })
    }

    fun removeRecommendation(listOfData: List<ProductRecommendationDataModel>?) {
        listOfData?.run {
            forEach {
                clearElement(it)
            }
        }
    }

    fun <T : DynamicPdpDataModel> removeComponentSection(data: T?) {
        data?.let {
            clearElement(it)
        }
    }

    fun <T : DynamicPdpDataModel> notifyItemComponentSections(vararg data: T?) {
        data.forEach {
            it?.let {
                val indexOfData = list.indexOf(it)
                if (indexOfData != -1) {
                    notifyItemChanged(indexOfData)
                }
            }
        }
    }

    fun <T: DynamicPdpDataModel> getItemComponentIndex(data: T?) : Int{
        return if (data != null) {
            list.indexOf(data)
        } else {
            RecyclerView.NO_POSITION
        }
    }


    fun <T : DynamicPdpDataModel> notifyWithPayload(data: T?, payload: Int) {
        data?.let {
            val indexOfData = list.indexOf(data)
            if (indexOfData != -1) {
                notifyItemChanged(indexOfData, payload)
            }
        }
    }

    fun getTopAdsBannerPosition(data: TopAdsImageDataModel): Int {
        return if (data != null) {
            list.indexOf(data)
        } else {
            -1
        }
    }

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
        notifyMeData?.let {
            val indexOfNotifyMe = list.indexOf(notifyMeData)
            if (payload != null) {
                notifyItemChanged(indexOfNotifyMe, payload)
            } else {
                notifyItemChanged(indexOfNotifyMe)
            }
        }
    }

    fun notifyItemRangeChangesExceptFirstPosition() {
        notifyItemRangeChanged(1, itemCount)
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ProductRecommendationViewHolder &&
                holder.adapterPosition < visitables.size &&
                visitables[holder.adapterPosition] is ProductRecommendationDataModel &&
                (visitables[holder.adapterPosition] as ProductRecommendationDataModel).recomWidgetData == null) {
            listener.loadTopads((visitables[holder.adapterPosition] as ProductRecommendationDataModel).name)
        }
    }
}