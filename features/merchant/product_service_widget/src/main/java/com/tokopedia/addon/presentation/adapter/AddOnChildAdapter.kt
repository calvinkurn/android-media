package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.ZERO

class AddOnChildAdapter(
    private val onClickListener: (index: Int, List<AddOnUIModel>) -> Unit,
    private val onHelpClickListener: (index: Int, AddOnUIModel) -> Unit,
    private val onItemImpressionListener: (index: Int, AddOnUIModel) -> Unit
): RecyclerView.Adapter<AddOnChildViewHolder>() {

    private var items: MutableList<AddOnUIModel> = mutableListOf()
    private var isShowDescription: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnChildViewHolder {
        val rootView = AddOnChildViewHolder.createRootView(parent)
        return AddOnChildViewHolder(
            rootView,
            onClickListener = ::onItemClick,
            onHelpClickListener = ::onHelpClick,
            onItemImpression = ::onItemImpression
        )
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

    private fun onHelpClick(position: Int) {
        items.getOrNull(position)?.let {
            onHelpClickListener(position, it)
        }
    }

    private fun onItemImpression(index: Int, addOnUIModel: AddOnUIModel) {
        onItemImpressionListener(index, addOnUIModel)
    }

    override fun onBindViewHolder(holder: AddOnChildViewHolder, position: Int) {
        holder.bind(items[position], isShowDescription)
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<AddOnUIModel>) {
        this.items = items.toMutableList()
        notifyItemRangeInserted(Int.ZERO, items.size)
    }

    fun showDescription(showDescription: Boolean) {
        isShowDescription = showDescription
    }

}
