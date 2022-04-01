package com.tokopedia.shopdiscount.product_detail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailShimmeringUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel

class ShopDiscountProductDetailAdapter(
    private val typeFactory: ShopDiscountProductDetailTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, ShopDiscountProductDetailTypeFactoryImpl>(typeFactory) {

    override fun showLoading() {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(ShopDiscountProductDetailShimmeringUiModel())
        submitList(newList)
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        visitables.filterIsInstance<ShopDiscountProductDetailShimmeringUiModel>().firstOrNull()
            ?.let {
                if (newList.contains(it)) {
                    newList.remove(it)
                }
            }
        submitList(newList)
    }

    fun addListProductDetailData(data: List<ShopDiscountProductDetailUiModel>) {
        val newList = getNewVisitableItems()
        newList.addAll(data)
        submitList(newList)
    }

    fun showGlobalErrorView(throwable: Throwable) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(ShopDiscountProductDetailListGlobalErrorUiModel(throwable))
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = ShopDiscountProductDetailDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

}