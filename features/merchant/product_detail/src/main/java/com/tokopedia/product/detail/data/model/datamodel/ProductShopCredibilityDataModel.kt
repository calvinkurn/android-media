package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo.TickerDataResponse

/**
 * Created by Yehezkiel on 15/06/20
 */
data class ProductShopCredibilityDataModel(
    val name: String = String.EMPTY,
    val type: String = String.EMPTY,

    var shopLastActive: String = String.EMPTY,
    var shopId: String = String.EMPTY,
    var shopName: String = String.EMPTY,
    var shopAva: String = String.EMPTY,
    var shopLocation: String = String.EMPTY,
    var shopTierBadgeUrl: String = String.EMPTY,
    var shopWarehouseCount: String = String.EMPTY,
    var shopWarehouseApplink: String = String.EMPTY,

    var isOs: Boolean = false,
    var isPm: Boolean = false,
    var partnerLabel: String = String.EMPTY,

    var infoShopData: List<ShopCredibilityUiData> = listOf(),

    var tickerDataResponse: List<TickerDataResponse> = listOf(),

    //Favorite
    var enableButtonFavorite: Boolean = false,
    var isFavorite: Boolean = false

) : DynamicPdpDataModel {

    companion object {
        const val TIPS_TYPE = "tips"
        const val TICKER_TYPE = "ticker"
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun getTickerType(): String {
        return if (tickerDataResponse.firstOrNull()?.color == TIPS_TYPE) {
            TIPS_TYPE
        } else {
            TICKER_TYPE
        }
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShopCredibilityDataModel) {
            shopLocation == newData.shopLocation
                && shopAva == newData.shopAva
                && shopLastActive == newData.shopLastActive
                && partnerLabel == newData.partnerLabel
                && isFavorite == newData.isFavorite
                && enableButtonFavorite == newData.enableButtonFavorite
                && infoShopData.hashCode() == newData.infoShopData.hashCode()
                && shopTierBadgeUrl == newData.shopTierBadgeUrl
                && shopWarehouseCount == newData.shopWarehouseCount
                && shopWarehouseApplink == newData.shopWarehouseApplink
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductShopCredibilityDataModel) {
            if (shopAva != newData.shopAva) {
                //Means this is update from backend, not click the follow button
                //We dont want to only update with payload, but update entire component
                return null
            }

            if (isFavorite != newData.isFavorite) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP
                )
            } else if (enableButtonFavorite != enableButtonFavorite) {
                bundle.putInt(
                    ProductDetailConstant.DIFFUTIL_PAYLOAD,
                    ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE
                )
            }
            bundle
        } else {
            null
        }
    }
}

data class ShopCredibilityUiData(
    val value: String = "", val icon: String = ""
) {

    companion object {
        fun List<ShopInfo.ShopCredibility.Status>.asUiData() =
            map {
                ShopCredibilityUiData(value = it.value, icon = it.icon)
            }
    }
}
