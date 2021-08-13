package com.tokopedia.topads.edit.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.edit.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductShimmerViewModel
import com.tokopedia.topads.edit.view.adapter.product.viewmodel.ProductViewModel


class ProductListAdapter(private val typeFactory: ProductListAdapterTypeFactory) : RecyclerView.Adapter<ProductViewHolder<ProductViewModel>>() {

    var items: MutableList<ProductViewModel> = mutableListOf()
    private var shimers: MutableList<ProductViewModel> = mutableListOf(
            ProductShimmerViewModel(),
            ProductShimmerViewModel(), ProductShimmerViewModel()
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
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as ProductViewHolder<ProductViewModel>

    }

    fun initLoading() {
        items = shimers
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<TopAdsProductModel> {

        val selected = mutableListOf<TopAdsProductModel>()
        items.forEach { item ->
            if (item is ProductItemViewModel)
                if (item.isChecked)
                    selected.add(item.data)
        }
        return selected
    }


    fun setSelectedList(selectedProductIds: List<TopAdsProductModel>) {
        items.forEachIndexed { _, productViewModel ->
            selectedProductIds.forEach {
                if ((productViewModel is ProductItemViewModel) && productViewModel.data.productID == it.productID) {
                    productViewModel.isChecked = true
                }
            }
        }
    }
}
