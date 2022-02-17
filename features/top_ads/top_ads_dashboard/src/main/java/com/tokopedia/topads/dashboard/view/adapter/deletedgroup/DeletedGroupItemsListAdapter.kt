package com.tokopedia.topads.dashboard.view.adapter.deletedgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder.DeletedGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsModel

class DeletedGroupItemsListAdapter(private val typeFactory: DeletedGroupItemsAdapterTypeFactory) :
    RecyclerView.Adapter<DeletedGroupItemsViewHolder<DeletedGroupItemsModel>>() {

    private val items: MutableList<DeletedGroupItemsModel> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeletedGroupItemsViewHolder<DeletedGroupItemsModel> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.holder(
            viewType,
            view
        ) as DeletedGroupItemsViewHolder<DeletedGroupItemsModel>
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun submitList(deletedGroupItemsModelList: List<DeletedGroupItemsModel>) {
        items.addAll(deletedGroupItemsModelList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: DeletedGroupItemsViewHolder<DeletedGroupItemsModel>,
        position: Int
    ) {
        holder.bind(items[position])
    }
}
