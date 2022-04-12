package com.tokopedia.shopdiscount.manage_discount.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shopdiscount.common.adapter.ShopDiscountDiffUtilCallback
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountManageDiscountGlobalErrorUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductShimmeringUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel

class ShopDiscountManageDiscountAdapter(
    typeFactory: ShopDiscountManageDiscountTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, ShopDiscountManageDiscountTypeFactoryImpl>(typeFactory) {

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
        val newList = getNewVisitableItems()
        newList.addAll(data)
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
        return visitables.filterIsInstance(ShopDiscountSetupProductUiModel.SetupProductData::class.java).map {
            it.copy()
        }
    }

    fun clearData() {
        val newList = getNewVisitableItems()
        newList.clear()
        submitList(newList)
    }

    fun removeProduct(productId: String) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance(ShopDiscountSetupProductUiModel.SetupProductData::class.java).firstOrNull{
            it.productId == productId
        }?.let {
            newList.remove(it)
            submitList(newList)
        }
    }

}