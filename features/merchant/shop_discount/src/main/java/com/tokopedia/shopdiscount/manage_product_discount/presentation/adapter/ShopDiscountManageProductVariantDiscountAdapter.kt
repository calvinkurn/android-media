package com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.shopdiscount.common.adapter.ShopDiscountDiffUtilCallback
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.manage_product_discount.util.ShopDiscountManageProductVariantMapper
import java.util.*

class ShopDiscountManageProductVariantDiscountAdapter(
    typeFactory: ShopDiscountManageProductVariantDiscountTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, ShopDiscountManageProductVariantDiscountTypeFactoryImpl>(
    typeFactory
) {

    fun clearData() {
        val newList = getNewVisitableItems()
        newList.clear()
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = ShopDiscountDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun addVariantSectionData(productData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val newList = getNewVisitableItems()
        newList.addAll(
            ShopDiscountManageProductVariantMapper.mapToListShopDiscountManageProductVariantItemUiModel(
                productData
            )
        )
        submitList(newList)
    }

    fun updateToggleVariantData(matchedProductData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopDiscountManageProductVariantItemUiModel>().firstOrNull {
            it.variantId == matchedProductData.productId
        }?.let {
            val index = newList.indexOf(it)
            newList.setElement(
                index, it.copy(
                    isEnabled = matchedProductData.variantStatus.isVariantEnabled.orFalse()
                )
            )
        }
        submitList(newList)
    }

    private fun <E> MutableList<E>.setElement(index: Int, element: E) {
        if (index in 0 until size) {
            set(index, element)
        }
    }

    fun updateDiscountPeriodData(variantProductData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopDiscountManageProductVariantItemUiModel>().firstOrNull {
            it.variantId == variantProductData.productId
        }?.let {
            val index = newList.indexOf(it)
            newList.setElement(
                index, it.copy(
                    startDate = variantProductData.slashPriceInfo.startDate,
                    endDate = variantProductData.slashPriceInfo.endDate
                )
            )
        }
        submitList(newList)
    }

    fun updateVariantDiscountPercentData(variantProductData: ShopDiscountSetupProductUiModel.SetupProductData) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopDiscountManageProductVariantItemUiModel>().firstOrNull {
            it.variantId == variantProductData.productId
        }?.let {
            val index = newList.indexOf(it)
            newList.setElement(
                index, it.copy(
                    discountedPercentage = variantProductData.slashPriceInfo.discountPercentage
                )
            )
        }
        submitList(newList)
    }

    fun getListVariantItemUiModel(): List<ShopDiscountManageProductVariantItemUiModel> {
        return visitables.filterIsInstance<ShopDiscountManageProductVariantItemUiModel>()
    }

    fun updateListVariantProductData(listVariantProduct: List<ShopDiscountManageProductVariantItemUiModel>) {
        val newList = getNewVisitableItems()
        newList.addAll(listVariantProduct)
        submitList(newList)
    }

    fun getMinStartDateOfProductList(): Date? {
        return getListVariantItemUiModel().map {
            it.startDate.time
        }.minOfOrNull {
            it
        }?.let {
            Date(it)
        }
    }

    fun getMaxStartDateOfProductList(): Date? {
        return getListVariantItemUiModel().map {
            it.endDate.time
        }.minOfOrNull {
            it
        }?.let {
            Date(it)
        }
    }

}