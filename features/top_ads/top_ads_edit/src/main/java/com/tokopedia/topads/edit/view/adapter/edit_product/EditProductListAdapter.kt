package com.tokopedia.topads.edit.view.adapter.edit_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.view.adapter.edit_product.viewholder.EditProductViewHolder
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductItemViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductShimmerViewModel
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductViewModel

/**
 * Created by Pika on 8/4/20.
 */

class EditProductListAdapter(val typeFactory: EditProductListAdapterTypeFactory) : RecyclerView.Adapter<EditProductViewHolder<EditProductViewModel>>() {

    var items: MutableList<EditProductViewModel> = mutableListOf()
    var shimers: MutableList<EditProductViewModel> = mutableListOf(
            EditProductShimmerViewModel(),
            EditProductShimmerViewModel(),
            EditProductShimmerViewModel()
    )

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: EditProductViewHolder<EditProductViewModel>, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditProductViewHolder<EditProductViewModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(viewType, view) as EditProductViewHolder<EditProductViewModel>
    }

    fun getCurrentIds(): ArrayList<String> {
        val itemsIds: MutableList<String> = mutableListOf()
        val finalList: ArrayList<String> = arrayListOf()
        items.forEach {
            if (it is EditProductItemViewModel) {
                itemsIds.add(it.data.itemID)
            }
        }
        finalList.addAll(itemsIds)
        return finalList
    }
}
