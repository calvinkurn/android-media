package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import android.os.Bundle
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.util.getIdLocale
import com.tokopedia.product.detail.view.util.getRelativeDateByHours
import com.tokopedia.product.detail.view.util.getRelativeDateByMinute
import java.text.SimpleDateFormat

/**
 * Created by Yehezkiel on 15/06/20
 */
data class ProductShopCredibilityDataModel(
        val name: String = "",
        val type: String = "",

        var shopLastActive: String = "",
        var shopName: String = "",
        var shopAva: String = "",
        var shopLocation: String = "",

        var shopTierBadgeUrl: String = "",
        var isGoApotik: Boolean = false,

        var infoShopData: List<ShopCredibilityUiData> = listOf(),

        //Favorite
        var enableButtonFavorite: Boolean = false,
        var isFavorite: Boolean = false

) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun getTwoShopInfoHieararchy(context: Context, shopSpeed: Long, shopChatSpeed: Long, shopActiveProduct: Long, shopCreated: String, shopRating: Float): List<ShopCredibilityUiData> {
        val createdDated = try {
            SimpleDateFormat("yyyy-MM-dd", getIdLocale()).parse(shopCreated).toFormattedString("MMM yyyy", getIdLocale())
        } catch (e: Throwable) {
            ""
        }

        val listOfData = mutableListOf(
                ShopCredibilityUiData(if (shopRating == 0F) "" else shopRating.toString(), context.getString(R.string.product_shop_rating), com.tokopedia.iconunify.IconUnify.STAR),
                ShopCredibilityUiData(shopSpeed.getRelativeDateByHours(context), context.getString(R.string.product_shop_process_product), com.tokopedia.iconunify.IconUnify.CLOCK),
                ShopCredibilityUiData(shopChatSpeed.getRelativeDateByMinute(context), context.getString(R.string.product_shop_chat_reply), com.tokopedia.iconunify.IconUnify.CHAT),
                ShopCredibilityUiData(shopActiveProduct.productThousandFormatted(0), context.getString(R.string.product_shop_total_product), com.tokopedia.iconunify.IconUnify.PRODUCT),
                ShopCredibilityUiData(createdDated, context.getString(R.string.product_shop_start_sell), com.tokopedia.iconunify.IconUnify.SHOP)
        )

        return listOfData
                .filter { it.value.isNotEmpty() && it.value != "0" }
                .take(2)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShopCredibilityDataModel) {
            shopLocation == newData.shopLocation &&
                    shopAva == newData.shopAva
                    && shopLastActive == newData.shopLastActive
                    && isGoApotik == newData.isGoApotik
                    && isFavorite == newData.isFavorite
                    && enableButtonFavorite == newData.enableButtonFavorite
                    && infoShopData.size == newData.infoShopData.size
                    && shopTierBadgeUrl == newData.shopTierBadgeUrl
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
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
            } else if (enableButtonFavorite != enableButtonFavorite) {
                bundle.putInt(ProductDetailConstant.DIFFUTIL_PAYLOAD, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
            }
            bundle
        } else {
            null
        }
    }
}

data class ShopCredibilityUiData(
        val value: String = "",
        val desc: String = "",
        val icon: Int = 0
) {
    fun iconIsNotEmpty(): Boolean = icon != 0
}