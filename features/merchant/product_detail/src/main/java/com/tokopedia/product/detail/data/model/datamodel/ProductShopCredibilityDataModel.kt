package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.util.getRelativeDateByHours
import com.tokopedia.product.detail.view.util.getRelativeDateByMinute

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityDataModel(
        val name: String = "",
        val type: String = "",

        var shopLastActive: String = "",
        var shopName: String = "",
        var shopAva: String = "",
        var shopLocation: String = "",
        var shopActiveProduct: Int = 0,

        var isOs: Boolean = false,
        var isPm: Boolean = false,
        var shopSpeed: Int = 0,
        var shopChatSpeed: Int = 0,
        var shopRating: Float = 0F,
        var isGoApotik: Boolean = false,

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

    fun getLastThreeHierarchyData(context: Context): List<ShopCredibilityUiData> {
        val listOfData = mutableListOf(
                ShopCredibilityUiData(if (shopRating == 0F) "" else shopRating.toString(), context.getString(R.string.product_shop_rating), R.drawable.ic_review_gray),
                ShopCredibilityUiData(shopSpeed.getRelativeDateByHours(context), context.getString(R.string.product_shop_process_product), R.drawable.ic_time_grey),
                ShopCredibilityUiData(shopChatSpeed.getRelativeDateByMinute(context), context.getString(R.string.product_shop_chat_reply), R.drawable.ic_chat_grey),
                ShopCredibilityUiData(shopActiveProduct.productThousandFormatted(0), context.getString(R.string.product_shop_total_product), R.drawable.ic_product_grey)
        )

        return listOfData
                .filter { it.value.isNotEmpty() && it.value != "0" }
                .take(2)
    }
}

data class ShopCredibilityUiData(
        val value: String = "",
        val desc: String = "",
        @DrawableRes
        val icon: Int = 0
)