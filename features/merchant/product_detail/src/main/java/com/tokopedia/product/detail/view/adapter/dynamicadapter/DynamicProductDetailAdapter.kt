package com.tokopedia.product.detail.view.adapter.dynamicadapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl

class DynamicProductDetailAdapter(
        private val adapterTypeFactory: DynamicProductDetailAdapterFactoryImpl
) : BaseListAdapter<DynamicPDPDataModel, DynamicProductDetailAdapterFactoryImpl>(adapterTypeFactory) {

    companion object {
        const val PAYLOADS_WISHLIST = 1
    }

    fun notifySnapshot(snapshotData: ProductSnapshotDataModel) {
        val indexOfSnapshot = list.indexOf(snapshotData)
        notifyItemChanged(indexOfSnapshot)
    }

    fun notifyShopInfo(shopInfoData: ProductShopInfoDataModel, payload: Int) {
        val indexOfShopInfo = list.indexOf(shopInfoData)
        notifyItemChanged(indexOfShopInfo, payload)
    }

    fun notifyWishlistSnapshot(data: ProductSnapshotDataModel) {
        val index = list.indexOf(data)
        notifyItemChanged(index, PAYLOADS_WISHLIST)
    }

    fun getRecommendationIndex(): Int{
        var position = -1
        list.forEachIndexed { index, visitable ->
            if(visitable is ProductRecommendationDataModel){

                return@getRecommendationIndex index
            }
        }
        return position
    }
}