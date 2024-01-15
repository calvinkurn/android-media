package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountManageProductSubsidyUiModel(
    val listProductDetailData: List<ShopDiscountProductDetailUiModel.ProductDetailData> = listOf(),
    val mode: String = ShopDiscountManageDiscountMode.UPDATE,
    val selectedProductToOptOut: MutableList<ShopDiscountProductDetailUiModel.ProductDetailData> = mutableListOf(),
) : Parcelable {

    fun getListProductParentId(): List<String> {
        return listProductDetailData.map {
            if (it.parentId == Int.ZERO.toString()) {
                it.productId
            } else {
                it.parentId
            }
        }.distinct()
    }

    fun getCtaLink(): String {
        return listProductDetailData.firstOrNull()?.subsidyInfo?.ctaProgramLink.orEmpty()
    }

    fun getTotalProductWithSubsidy(): Int {
        return listProductDetailData.count { productDetail ->
            productDetail.isSubsidy
        }
    }

    fun hasNonSubsidyProduct(): Boolean {
        //TODO need to remove this filter once BE has fixed the response
//        return true
        return listProductDetailData.any { productDetail ->
            !productDetail.isSubsidy
        }
    }

    fun addSelectedProductToOptOut(productDetailData: ShopDiscountProductDetailUiModel.ProductDetailData) {
        selectedProductToOptOut.add(productDetailData)
    }

    fun getSelectedProductToOptOutEventId(): List<String> {
        return selectedProductToOptOut.map {
            it.eventId
        }
    }

    fun getListProductDetailSubsidyData(): List<ShopDiscountProductDetailUiModel.ProductDetailData> {
        return listProductDetailData.filter {
            it.isSubsidy
        }
    }

    fun isAllSelectedProductFullSubsidy(): Boolean {
        return selectedProductToOptOut.all {
            ShopDiscountSubsidyInfoUiModel.getSubsidyType(it.subsidyInfo.subsidyType.value) == ShopDiscountSubsidyInfoUiModel.SubsidyType.FULL
        }
    }
}
