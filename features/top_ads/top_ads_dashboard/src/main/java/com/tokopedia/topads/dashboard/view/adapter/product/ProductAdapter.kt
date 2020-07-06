package com.tokopedia.topads.dashboard.view.adapter.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductViewModel


class ProductAdapter(private val typeFactory: ProductAdapterTypeFactory): RecyclerView.Adapter<ProductViewHolder<ProductViewModel>>() {


    private var isSelectMode = false
    var statsData: MutableList<WithoutGroupDataItem> = mutableListOf()
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
        holder.bind(items[position],isSelectMode,statsData)
    }

    fun setSelectMode(isSelectMode:Boolean){
        this.isSelectMode = isSelectMode
        clearData(isSelectMode)
        notifyDataSetChanged()
    }

    fun getSelectedItems(): MutableList<ProductItemViewModel> {
        val list: MutableList<ProductItemViewModel> = mutableListOf()
        items.forEach {
            if (it is ProductItemViewModel) {
                if (it.isChecked) {
                    list.add(it)

                }
            }
        }
        return list
    }
    private fun clearData(selectedMode: Boolean) {
        if (!selectedMode){
            items.forEach {
                if (it is ProductItemViewModel) {
                    it.isChecked = false
                }
            }
        }
    }

    fun setstatistics(data: List<WithoutGroupDataItem>) {
        statsData = data.toMutableList()
        notifyDataSetChanged()
    }

}
