package com.tokopedia.shop_widget.buy_more_save_more.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetItemEventListener
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder.BmsmProductListViewHolder
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder.BmsmSeeAllProductViewHolder
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder.BmsmSingleProductListViewHolder
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder.BmsmTwoProductListViewHolder

class BmsmWidgetProductListAdapter(private val listener: BmsmWidgetItemEventListener) :
    ListAdapter<Product, RecyclerView.ViewHolder>(ProductListAdapterDiffCallback.ProductListDiffCallback) {

    private var productListUiModel: MutableList<Product> = mutableListOf()

    companion object {
        private const val DEFAULT_ITEM_VIEW = 0
        private const val SINGLE_ITEM_VIEW = 1
        private const val TWO_ITEM_VIEW = 2
        private const val SEE_ALL_ITEM_VIEW = 3
        private const val SEE_ALL_PRODUCT_CARD_POSITION = 10
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            currentList.size == SINGLE_ITEM_VIEW -> SINGLE_ITEM_VIEW
            position == SEE_ALL_PRODUCT_CARD_POSITION -> SEE_ALL_ITEM_VIEW
            currentList.size == TWO_ITEM_VIEW -> TWO_ITEM_VIEW
            else -> DEFAULT_ITEM_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SINGLE_ITEM_VIEW -> {
                BmsmSingleProductListViewHolder(
                    parent.inflateLayout(R.layout.item_bmsm_widget_single_product_list),
                    listener
                )
            }

            SEE_ALL_ITEM_VIEW -> {
                BmsmSeeAllProductViewHolder(
                    parent.inflateLayout(R.layout.item_bmsm_widget_see_all),
                    listener
                )
            }

            TWO_ITEM_VIEW -> {
                BmsmTwoProductListViewHolder(
                    parent.inflateLayout(R.layout.item_bmsm_widget_two_product_list_item),
                    listener
                )
            }

            else -> {
                val itemView = parent.inflateLayout(R.layout.item_bmsm_widget_product_list)
                BmsmProductListViewHolder(
                    itemView,
                    listener
                )
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            SINGLE_ITEM_VIEW -> (holder as BmsmSingleProductListViewHolder).bind(getItem(position))
            SEE_ALL_ITEM_VIEW -> (holder as BmsmSeeAllProductViewHolder).bind(getItem(position))
            TWO_ITEM_VIEW -> (holder as BmsmTwoProductListViewHolder).bind(getItem(position))
            else -> (holder as BmsmProductListViewHolder).bind(getItem(position))
        }
    }

    fun addProductList(productList: List<Product>) {
        val newList = currentList.toMutableList()
        productListUiModel.addAll(productList)
        newList.addAll(productList)
        submitList(newList)
    }

    fun removeProductList() {
        val newList = currentList.toMutableList()
        newList.removeAll(newList.filterIsInstance<Product>())
        productListUiModel.clear()
        submitList(newList)
    }
}
