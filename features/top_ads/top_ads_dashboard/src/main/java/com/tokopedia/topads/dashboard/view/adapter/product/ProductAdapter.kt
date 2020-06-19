package com.tokopedia.topads.dashboard.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactory
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductViewModel


class ProductAdapter(private val typeFactory: ProductAdapterTypeFactory): RecyclerView.Adapter<ProductViewHolder<ProductViewModel>>() {


    private var isSelectMode = false
    var items: MutableList<ProductViewModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder<ProductViewModel> {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return typeFactory.holder(viewType, view) as ProductViewHolder<ProductViewModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ProductViewHolder<ProductViewModel>, position: Int) {
        holder.bind(items[position],isSelectMode)
    }

    fun setSelectMode(isSelectMode:Boolean){
        this.isSelectMode = isSelectMode
        notifyDataSetChanged()
    }

    fun getSelectedItems(): MutableList<ProductItemViewModel> {
        var list: MutableList<ProductItemViewModel> = mutableListOf()
        items.forEach {
            if (it is ProductItemViewModel) {
                if (it.isChecked) {
                    list.add(it)

                }
            }
        }
        return list
    }
}
