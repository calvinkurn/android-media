package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemParentProduct
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.customview.OnStickySingleHeaderListener
import com.tokopedia.shop_widget.customview.StickySingleHeaderView
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductGridViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder.MvcLockedToProductSortSectionViewHolder
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGlobalErrorUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGridListPlaceholderUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGridProductUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortSectionUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherSortPlaceholderUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherUiModel

class MvcLockedToProductAdapter(
    private val typeFactory: MvcLockedToProductTypeFactory
) : BaseListAdapter<Visitable<*>, MvcLockedToProductTypeFactory>(typeFactory),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == MvcLockedToProductSortSectionUiModel::class.java
        }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return typeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is MvcLockedToProductSortSectionViewHolder) {
            visitables.filterIsInstance(MvcLockedToProductSortSectionUiModel::class.java)
                .firstOrNull()?.let {
                    viewHolder.bind(it)
                }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {}

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = isItemFullSpan(position)
        }
        super.onBindViewHolder(holder, position)
    }

    private fun isItemFullSpan(position: Int): Boolean {
        return getItemViewType(position) != MvcLockedToProductGridViewHolder.LAYOUT
    }

    fun addProductListData(mvcLockedToListProductGridProductUiModel: List<MvcLockedToProductGridProductUiModel>) {
        val listNewProduct = mvcLockedToListProductGridProductUiModel.toMutableList()
        listNewProduct.removeAll {
            getProductUiModel(it.productID) != null
        }
        val newList = getNewVisitableItems()
        newList.addAll(listNewProduct)
        submitList(newList)
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = MvcLockedToProductDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    fun addVoucherData(mvcLockedToProductVoucherUiModel: MvcLockedToProductVoucherUiModel) {
        val newList = getNewVisitableItems()
        newList.add(mvcLockedToProductVoucherUiModel)
        submitList(newList)
    }

    fun addTotalProductAndSortData(mvcLockedToProductTotalProductAndSortUiModel: MvcLockedToProductSortSectionUiModel) {
        val newList = getNewVisitableItems()
        newList.add(mvcLockedToProductTotalProductAndSortUiModel)
        submitList(newList)
    }

    fun showInitialPagePlaceholderLoading() {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(MvcLockedToProductVoucherSortPlaceholderUiModel())
        newList.add(MvcLockedToProductGridListPlaceholderUiModel())
        submitList(newList)
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        hideLoadingMore(newList)
        hideVoucherSortPlaceholder(newList)
        hideProductGridListPlaceholder(newList)
        submitList(newList)
    }

    private fun hideVoucherSortPlaceholder(newList: MutableList<Visitable<*>>) {
        visitables.filterIsInstance<MvcLockedToProductVoucherSortPlaceholderUiModel>().firstOrNull()
            ?.let {
                if (newList.contains(it)) {
                    newList.remove(it)
                }
            }
    }

    private fun hideProductGridListPlaceholder(newList: MutableList<Visitable<*>>) {
        visitables.filterIsInstance<MvcLockedToProductGridListPlaceholderUiModel>().firstOrNull()
            ?.let {
                if (newList.contains(it)) {
                    newList.remove(it)
                }
            }
    }

    private fun hideLoadingMore(newList: MutableList<Visitable<*>>) {
        if (newList.contains(loadingMoreModel)) {
            newList.remove(loadingMoreModel)
        }
    }

    fun showLoadMoreLoading() {
        val newList = getNewVisitableItems()
        newList.add(loadingMoreModel)
        submitList(newList)
    }

    fun showGlobalErrorView(uiModel: MvcLockedToProductGlobalErrorUiModel) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.add(uiModel)
        submitList(newList)
    }

    fun showNewProductListPlaceholder() {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<MvcLockedToProductGridProductUiModel>().let {
            newList.removeAll(it)
        }
        newList.add(MvcLockedToProductGridListPlaceholderUiModel())
        submitList(newList)
    }

    fun updateTotalProductAndSortData(selectedSortData: MvcLockedToProductSortUiModel) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<MvcLockedToProductSortSectionUiModel>().firstOrNull()?.let {
            val position = newList.indexOf(it)
            newList.setElement(position, it.copy(selectedSortData = selectedSortData))
        }
        submitList(newList)
    }

    fun getVoucherName(): String {
        return visitables.filterIsInstance<MvcLockedToProductVoucherUiModel>()
            .firstOrNull()?.baseCode.orEmpty()
    }

    fun getVoucherUiModel(): MvcLockedToProductVoucherUiModel? {
        return visitables.filterIsInstance<MvcLockedToProductVoucherUiModel>().firstOrNull()
    }

    fun getProductUiModel(productId: String): MvcLockedToProductGridProductUiModel? {
        return visitables.filterIsInstance<MvcLockedToProductGridProductUiModel>().firstOrNull {
            it.productID == productId
        }
    }

    fun getFirstProductCardPosition(): Int {
        return visitables.indexOfFirst {
            it is MvcLockedToProductGridProductUiModel
        }
    }

    fun updateProductListDataWithMiniCartData(data: MiniCartSimplifiedData) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<MvcLockedToProductGridProductUiModel>().forEach { productUiModel ->
            val matchedMiniCartItem = getMatchedMiniCartItem(productUiModel, data)
            val position = visitables.indexOf(productUiModel)
            val (miniCartProductId, quantity) = when {
                matchedMiniCartItem is MiniCartItem.MiniCartItemProduct && !matchedMiniCartItem.isError -> {
                    matchedMiniCartItem.productId to matchedMiniCartItem.quantity
                }
                matchedMiniCartItem is MiniCartItem.MiniCartItemParentProduct -> {
                    matchedMiniCartItem.parentId to matchedMiniCartItem.totalQuantity
                }
                else -> {
                    null to null
                }
            }
            if(miniCartProductId != null && quantity != null){
                newList.setElement(
                    position, productUiModel.copy(
                        productInCart = MvcLockedToProductGridProductUiModel.ProductInCart(
                            miniCartProductId,
                            quantity
                        ),
                        productCardModel = getMvcProductCardModel(
                            productUiModel,
                            quantity
                        )
                    )
                )
            } else {
                if(productUiModel.productInCart.productId.isNotEmpty()){
                    newList.setElement(
                        position, productUiModel.copy(
                            productInCart = MvcLockedToProductGridProductUiModel.ProductInCart(),
                            productCardModel = getMvcProductCardModel(
                                productUiModel,
                                Int.ZERO
                            )
                        )
                    )
                }
            }
        }
        submitList(newList)
    }

    private fun getMatchedMiniCartItem(
        productUiModel: MvcLockedToProductGridProductUiModel,
        miniCartData: MiniCartSimplifiedData
    ): MiniCartItem? {
        val isVariant = productUiModel.isVariant
        if (isVariant) {
            val miniCartItemParentProduct = miniCartData.miniCartItems.getMiniCartItemParentProduct(productUiModel.productID)
            if (miniCartItemParentProduct != null) {
                return miniCartItemParentProduct
            }
        }
        return miniCartData.miniCartItems.getMiniCartItemProduct(productUiModel.productID)
    }

    private fun getMvcProductCardModel(
        productUiModel: MvcLockedToProductGridProductUiModel,
        productQuantityInCart: Int
    ): ProductCardModel {
        return if (productUiModel.isVariant) {
            productUiModel.productCardModel.copy(
                hasAddToCartButton = true,
                nonVariant = null
            )
        } else {
            if(productQuantityInCart.isZero()){
                productUiModel.productCardModel.copy(
                    nonVariant = null,
                    hasAddToCartButton = true
                )
            } else {
                productUiModel.productCardModel.copy(
                    nonVariant = ProductCardModel.NonVariant(
                        quantity = productQuantityInCart,
                        minQuantity = Int.ONE,
                        maxQuantity = productUiModel.stock
                    ),
                    hasAddToCartButton = false
                )
            }
        }
    }

    private fun <E> MutableList<E>.setElement(index: Int, element: E) {
        if (index in 0 until size) {
            set(index, element)
        }
    }

}