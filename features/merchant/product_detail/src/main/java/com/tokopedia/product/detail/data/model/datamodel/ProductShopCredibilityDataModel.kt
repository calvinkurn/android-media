package com.tokopedia.product.detail.data.model.datamodel

import android.content.Context
import androidx.annotation.DrawableRes
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.detail.view.util.getIdLocale
import com.tokopedia.product.detail.view.util.getRelativeDateByHours
import com.tokopedia.product.detail.view.util.getRelativeDateByMinute
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import java.text.SimpleDateFormat

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityDataModel(
        val name: String = "",
        val type: String = "",

        var shopInfo: ShopInfo? = null,
        var shopSpeed: Int = 0,
        var shopChatSpeed: Int = 0,
        var shopRating: Float = 0F
) : DynamicPdpDataModel {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    fun getLastThreeHierarchyData(context: Context): List<ShopCredibilityUiData> {
        /**
         * The first position have their own hierarchy
         */
        val firstPositionData = when {
            shopRating > 0 -> {
                ShopCredibilityUiData(shopRating.toString(), "rating toko", R.drawable.ic_review_gray)
            }
            shopInfo?.shopStats?.productSold.toIntOrZero() > 0 -> {
                ShopCredibilityUiData(shopInfo?.shopStats?.productSold.toString(), "penjualan toko", R.drawable.ic_product_sold_grey)
            }
            shopInfo?.activeProduct.orZero() > 0 -> {
                ShopCredibilityUiData(shopInfo?.activeProduct.toString(), "total produk", R.drawable.ic_product_grey)
            }
            else -> {
                null
            }
        }

        val createdDated = SimpleDateFormat("yyyy-MM-dd", getIdLocale()).parse(shopInfo?.createdInfo?.shopCreated
                ?: "").toFormattedString("MMM yyyy", getIdLocale())

        val listOfData = mutableListOf(
                ShopCredibilityUiData(shopSpeed.getRelativeDateByHours(context), "pesanan diproses", R.drawable.ic_time_grey),
                ShopCredibilityUiData(shopChatSpeed.getRelativeDateByMinute(context), "chat dibalas", R.drawable.ic_chat_grey),
                ShopCredibilityUiData(createdDated, "mulai jualan", R.drawable.ic_merchant_grey))

        // This logic is for fail over purpose
        if (firstPositionData != null) {
            listOfData.add(0, firstPositionData)
        }

        return listOfData
                .filter { it.value.isNotEmpty() }
                .take(3)
    }
}

data class ShopCredibilityUiData(
        val value: String = "",
        val desc: String = "",
        @DrawableRes
        val icon: Int = 0
)