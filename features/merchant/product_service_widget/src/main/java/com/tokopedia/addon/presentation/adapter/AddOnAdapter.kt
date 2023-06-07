package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.ZERO

class AddOnAdapter(
    private val listener: (index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>) -> Unit,
    private val onHelpClickListener: (index: Int, AddOnUIModel) -> Unit
): RecyclerView.Adapter<AddOnViewHolder>() {

    private var items: MutableList<AddOnGroupUIModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        val rootView = AddOnViewHolder.createRootView(parent)
        return AddOnViewHolder(rootView, ::onClickListener, onHelpClickListener)
    }

    private fun onClickListener(index: Int, indexChild: Int, addOnUIModels: List<AddOnUIModel>) {
        items[index].addon = addOnUIModels
        listener(index, indexChild, items)
    }

    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<AddOnGroupUIModel>) {
        this.items = items.toMutableList()
        notifyItemRangeInserted(Int.ZERO, items.size)
    }

}
