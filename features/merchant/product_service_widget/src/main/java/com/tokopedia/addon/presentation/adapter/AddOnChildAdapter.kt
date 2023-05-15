package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel

class AddOnChildAdapter(
    private val onClickListener: (index: Int, List<AddOnUIModel>) -> Unit
): RecyclerView.Adapter<AddOnChildViewHolder>() {

    private var items: MutableList<AddOnUIModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnChildViewHolder {
        val rootView = AddOnChildViewHolder.createRootView(parent)
        return AddOnChildViewHolder(rootView, onClickListener = ::onItemClick)
    }

    private fun onItemClick(position: Int, isChecked: Boolean) {
        for (index in 0 .. items.size) {
            if (index != position) {
                items.getOrNull(index)?.isSelected = false
                notifyItemChanged(index)
            } else {
                items.getOrNull(index)?.copy(isSelected = isChecked)?.apply {
                    items[index] = this
                }
            }
        }
        onClickListener(position, items)
    }

    override fun onBindViewHolder(holder: AddOnChildViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<AddOnUIModel>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

}
