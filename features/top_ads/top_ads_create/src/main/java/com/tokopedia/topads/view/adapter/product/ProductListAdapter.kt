package com.tokopedia.topads.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductUiModel

/**
 * Author errysuprayogi on 12,November,2019
 */
class ProductListAdapter(val typeFactory: ProductListAdapterTypeFactory) : RecyclerView.Adapter<ProductViewHolder<ProductUiModel>>() {

    var items: MutableList<ProductUiModel> = mutableListOf()
    private var shimers: MutableList<ProductUiModel> = mutableListOf(
            ProductShimmerUiModel(),
            ProductShimmerUiModel(),
            ProductShimmerUiModel()
    )

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ProductViewHolder<ProductUiModel>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder<ProductUiModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as ProductViewHolder<ProductUiModel>

    }

    fun initLoading() {
        items = shimers
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<TopAdsProductModel> {
        val selected = mutableListOf<TopAdsProductModel>()
        selected.clear()
        items.forEach { productViewModel ->
            if ((productViewModel is ProductItemUiModel) && productViewModel.isChecked)
                selected.add(productViewModel.data)
        }
        return selected
    }

    fun setSelectedList(selectedProductIds: MutableList<String>) {
        items.forEach { productViewModel ->
            selectedProductIds.forEach {
                if ((productViewModel is ProductItemUiModel) && productViewModel.data.productID == it) {
                    productViewModel.isChecked = true
                }
            }
        }
    }
}
