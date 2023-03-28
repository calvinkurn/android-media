package com.tokopedia.shopdiscount.manage_discount.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shopdiscount.common.adapter.ShopDiscountDiffUtilCallback
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountManageDiscountGlobalErrorUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductShimmeringUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.ALL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.PARTIAL_ABUSIVE_ERROR
import java.util.Date

class ShopDiscountManageDiscountAdapter(
    typeFactory: ShopDiscountManageDiscountTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, ShopDiscountManageDiscountTypeFactoryImpl>(typeFactory) {

    private val productListData: MutableList<ShopDiscountSetupProductUiModel.SetupProductData> =
        mutableListOf()

    override fun showLoading() {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(ShopDiscountSetupProductShimmeringUiModel())
        submitList(newList)
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        visitables.filterIsInstance<ShopDiscountSetupProductShimmeringUiModel>().firstOrNull()
            ?.let {
                if (newList.contains(it)) {
                    newList.remove(it)
                }
            }
        submitList(newList)
    }

    fun addListSetupProductData(data: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        productListData.clear()
        productListData.addAll(data)
        updateProductList()
    }

    fun updateProductList() {
        val newList = getNewVisitableItems()
        newList.addAll(productListData)
        submitList(newList)
    }

    fun showGlobalErrorView(throwable: Throwable) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(ShopDiscountManageDiscountGlobalErrorUiModel(throwable))
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

    fun getAllProductData(): List<ShopDiscountSetupProductUiModel.SetupProductData> {
        return visitables.filterIsInstance(ShopDiscountSetupProductUiModel.SetupProductData::class.java)
            .map {
                it.copy()
            }
    }

    fun clearData() {
        val newList = getNewVisitableItems()
        newList.clear()
        submitList(newList)
    }

    fun removeProduct(productId: String) {
        productListData.firstOrNull {
            it.productId == productId
        }?.let {
            productListData.remove(it)
            updateProductList()
        }
    }

    fun getTotalAbusiveProduct(): Int {
        return visitables.filterIsInstance(ShopDiscountSetupProductUiModel.SetupProductData::class.java)
            .count {
                it.productStatus.errorType == ALL_ABUSIVE_ERROR ||
                    it.productStatus.errorType == PARTIAL_ABUSIVE_ERROR
            }
    }

    fun getMinStartDateOfProductList(): Date? {
        return getAllProductData().mapNotNull {
            it.mappedResultData.minStartDateUnix
        }.minOfOrNull {
            it
        }?.let {
            Date(it)
        }
    }

    fun getMaxStartDateOfProductList(): Date? {
        return getAllProductData().mapNotNull {
            it.mappedResultData.minEndDateUnix
        }.maxOfOrNull {
            it
        }?.let {
            Date(it)
        }
    }
}
