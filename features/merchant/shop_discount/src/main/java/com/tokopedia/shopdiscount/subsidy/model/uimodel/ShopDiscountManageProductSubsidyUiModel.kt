package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageEntrySource
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.utils.extension.allCheckEmptyList
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountManageProductSubsidyUiModel(
    val listProductDetailData: List<ShopDiscountProductDetailUiModel.ProductDetailData> = listOf(),
    val mode: String = ShopDiscountManageDiscountMode.UPDATE,
    val entrySource: ShopDiscountManageEntrySource = ShopDiscountManageEntrySource.EDIT,
    val selectedProductToOptOut: MutableList<ShopDiscountProductDetailUiModel.ProductDetailData> = mutableListOf(),
) : Parcelable {

    fun getListProductParentIdWithNonSubsidyVariant(): List<String> {
        return listProductDetailData.filter { !it.isSubsidy }.map {
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

    fun getListProductAbleToOptOutData(): List<ShopDiscountProductDetailUiModel.ProductDetailData> {
        return listProductDetailData.filter {
            it.productRule.isAbleToOptOut
        }
    }

    fun isAllSelectedProductFullSubsidy(): Boolean {
        return selectedProductToOptOut.allCheckEmptyList {
            ShopDiscountSubsidyInfoUiModel.getSubsidyType(it.subsidyInfo.subsidyType.value) == ShopDiscountSubsidyInfoUiModel.SubsidyType.FULL
        }
    }

    fun getListProductIdVariantNonSubsidy(): List<String> {
        return listProductDetailData.filter {
            !it.isSubsidy
        }.map {
            it.productId
        }
    }

    fun getListProductIdVariant(): List<String> {
        return listProductDetailData.map {
            it.productId
        }
    }
}
