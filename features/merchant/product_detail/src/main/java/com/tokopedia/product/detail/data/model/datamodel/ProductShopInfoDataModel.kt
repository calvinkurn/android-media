package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductShopInfoDataModel(
        //From P1
        val type: String = "",
        val name: String = "",
        var isOs: Boolean = false,
        var isPm: Boolean = false,
        var shopName: String = "",

        //From P2 Shop
        var isFavorite: Boolean = false,
        var shopAvatar: String = "",
        var isAllowManage: Int = 0,
        var shopLocation: String = "",
        var shopLastActive: String = "",
        var shouldRenderShopInfo: Boolean = false,
        var shopBadge: String? = "",
        var isGoAPotik: Boolean = false,

        //Static
        var enableButtonFavorite: Boolean = false

) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShopInfoDataModel) {
            shopLocation == newData.shopLocation &&
                    shopAvatar == newData.shopAvatar
                    && shouldRenderShopInfo == newData.shouldRenderShopInfo
                    && shopLastActive == newData.shopLastActive
                    && shopBadge == newData.shopBadge
                    && isGoAPotik == newData.isGoAPotik
                    && isFavorite == newData.isFavorite
                    && enableButtonFavorite == newData.enableButtonFavorite
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductShopInfoDataModel) {
            if (shopAvatar != newData.shopAvatar) {
                //Means this is update from backend, not click the follow button
                //We dont want to only update with payload, but update entire component
                return null
            }

            if (isFavorite != newData.isFavorite && enableButtonFavorite != newData.enableButtonFavorite) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
            } else if (isFavorite == newData.isFavorite && enableButtonFavorite != newData.enableButtonFavorite) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
            }

            bundle
        } else {
            null
        }
    }
}