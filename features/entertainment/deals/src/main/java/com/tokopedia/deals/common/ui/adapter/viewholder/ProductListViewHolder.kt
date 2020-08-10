package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.common.listener.ProductListListener
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.adapter.DealsProductCardAdapter
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_product_list.view.*

class ProductListViewHolder(itemView: View, private val productListListener: ProductListListener) :
    BaseViewHolder(itemView), ProductCardListener {

    private val productCardAdapter: DealsProductCardAdapter = DealsProductCardAdapter(this)

    fun bind(productList: ProductListDataView) {
        itemView.run {
            if(!productList.isLoaded){
                shimmering.show()
            } else {
                lst_product_list.show()
                shimmering.hide()
                lst_product_list.apply {
                    layoutManager = GridLayoutManager(context, PRODUCT_SPAN_COUNT)
                    adapter = productCardAdapter
                }

                ViewCompat.setNestedScrollingEnabled(lst_product_list, false)
            }
        }
        productCardAdapter.productCards = productList.productCards
        productCardAdapter.page = productList.page
    }

    override fun onProductClicked(
        itemView: View,
        productCardDataView: ProductCardDataView,
        position: Int
    ) {
        productListListener.onProductClicked(productCardDataView, position)
    }

    override fun onImpressionProduct(productCardDataView: ProductCardDataView, productItemPosition: Int, page:Int) {
        productListListener.onImpressionProduct(productCardDataView, productItemPosition, page)
    }

    companion object {
        private const val PRODUCT_SPAN_COUNT = 2
        val LAYOUT = R.layout.item_deals_product_list
    }
}