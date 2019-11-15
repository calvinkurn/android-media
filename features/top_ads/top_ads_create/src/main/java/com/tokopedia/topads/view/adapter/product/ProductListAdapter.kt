package com.tokopedia.topads.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductViewModel

/**
 * Author errysuprayogi on 12,November,2019
 */
class ProductListAdapter(val typeFactory: ProductListAdapterTypeFactory) : RecyclerView.Adapter<ProductViewHolder<ProductViewModel>>() {

    var items: MutableList<ProductViewModel> = mutableListOf()
    var shimers: MutableList<ProductViewModel> = mutableListOf(
            ProductShimmerViewModel(),
            ProductShimmerViewModel(),
            ProductShimmerViewModel()
    )

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ProductViewHolder<ProductViewModel>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder<ProductViewModel> {
        if (parent != null) {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return typeFactory.holder(viewType, view) as ProductViewHolder<ProductViewModel>
        }
        throw RuntimeException("Parent is null")
    }

    fun initLoading() {
        items = shimers
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<ResponseProductList.Data> {
        var selected = mutableListOf<ResponseProductList.Data>()
        items.forEachIndexed { index, productViewModel -> if((productViewModel as ProductItemViewModel).isChecked) selected.add(productViewModel.data) }
        return selected
    }
}
