package com.tokopedia.shopdiscount.product_detail.data.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactoryImpl
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountSubsidyInfoUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountProductDetailUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listProductDetailData: List<ProductDetailData> = listOf()
) : Parcelable {
    @Parcelize
    data class ProductDetailData(
        val productId: String = "",
        val productImageUrl: String = "",
        val productName: String = "",
        val minOriginalPrice: Int = 0,
        val maxOriginalPrice: Int = 0,
        val minPriceDiscounted: Int = 0,
        val maxPriceDiscounted: Int = 0,
        val minDiscount: Int = 0,
        val maxDiscount: Int = 0,
        val stock: String = "",
        val totalLocation: Int = 0,
        val startDate: String = "",
        val endDate: String = "",
        val isVariant: Boolean = false,
        val maxOrder: Int = 0,
        val parentId: String = "",
        val isSubsidy: Boolean = false,
        val subsidyStatusText: String = "",
        val subsidyInfo: ShopDiscountSubsidyInfoUiModel = ShopDiscountSubsidyInfoUiModel(),
        val eventId: String = ""
    ) : Visitable<ShopDiscountProductDetailTypeFactoryImpl>, Parcelable {

        override fun type(typeFactory: ShopDiscountProductDetailTypeFactoryImpl): Int {
            return typeFactory.type(this)
        }
    }
}
