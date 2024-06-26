package com.tokopedia.shopdiscount.product_detail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shopdiscount.common.adapter.ShopDiscountDiffUtilCallback
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailShimmeringUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel

class ShopDiscountProductDetailAdapter(
    typeFactory: ShopDiscountProductDetailTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, ShopDiscountProductDetailTypeFactoryImpl>(typeFactory) {

    private val productListData: MutableList<ShopDiscountProductDetailUiModel.ProductDetailData> =
        mutableListOf()

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

    fun addListProductDetailData(data: List<ShopDiscountProductDetailUiModel.ProductDetailData>) {
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
        newList.add(ShopDiscountProductDetailListGlobalErrorUiModel(throwable))
        submitList(newList)
    }

    fun deleteProductFromList(productId: String) {
        productListData.firstOrNull {
            it.productId == productId
        }?.let {
            productListData.remove(it)
            updateProductList()
        }
    }

    fun getTotalProduct(): Int {
        return productListData.size
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = ShopDiscountDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()
    fun anySubsidyProduct(): Boolean {
        return productListData.any {
            it.isSubsidy
        }
    }

    fun getProductListData(): List<ShopDiscountProductDetailUiModel.ProductDetailData> {
        return productListData
    }

}
