package com.tokopedia.product.manage.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.option.SortProductOption
import com.tokopedia.product.manage.list.data.model.ProductManageSortModel

class ProductManageSortAdapter(val listener: ProductManageSortViewHolder.ProductManageSortViewHolderListener) : RecyclerView.Adapter<ProductManageSortViewHolder>(), ProductManageSortViewHolder.ProductManageSortViewHolderListener {

    private var productSortModel = listOf<ProductManageSortModel>()

    @SortProductOption
    private var sortProductOption = SortProductOption.POSITION

    fun setAdapterData(productSortModel: List<ProductManageSortModel>) {
        this.productSortModel = productSortModel
        notifyDataSetChanged()
    }

    fun setSortProductOption(sortProductOption: String) {
        this.sortProductOption = sortProductOption
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductManageSortViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(com.tokopedia.product.manage.list.R.layout.item_manage_sort, parent, false)
        val viewHolder = ProductManageSortViewHolder(itemView, listener)
        viewHolder.setAdapterListener(this)
        return viewHolder
    }

    override fun getItemCount(): Int = productSortModel.size

    override fun onBindViewHolder(holder: ProductManageSortViewHolder, position: Int) {
        holder.bindObject(productSortModel[position])
    }

    override fun onClickItem(data: ProductManageSortModel) {
        //NO OP SHARING INTERFACE
    }

    override fun isItemChecked(id: String): Boolean? {
        return id == sortProductOption
    }

}