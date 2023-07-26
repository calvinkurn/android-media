package com.tokopedia.addon.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.kotlin.extensions.view.ZERO

class AddOnAdapter(
    private val listener: (index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>) -> Unit,
    private val onHelpClickListener: (index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>, addonSelected: AddOnUIModel) -> Unit,
    private val onItemImpressionListener: (index: Int, indexChild: Int, addonGroups: List<AddOnGroupUIModel>) -> Unit
): RecyclerView.Adapter<AddOnViewHolder>() {

    private var items: MutableList<AddOnGroupUIModel> = mutableListOf()
    private var isShowDescription: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        val rootView = AddOnViewHolder.createRootView(parent)
        return AddOnViewHolder(
            rootView, ::onClickListener, ::onHelpClickTriggered, ::onItemImpressionTriggered)
    }

    private fun onHelpClickTriggered(index: Int, indexChild: Int, addOnUIModel: AddOnUIModel) {
        onHelpClickListener(index, indexChild, items, addOnUIModel)
    }

    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it, isShowDescription)
        }
    }

    override fun getItemCount() = items.size

    private fun onClickListener(index: Int, indexChild: Int, addOnUIModels: List<AddOnUIModel>) {
        items.getOrNull(index)?.let {
            items[index].addon = addOnUIModels
            listener(index, indexChild, items)
        }
    }

    private fun onItemImpressionTriggered(index: Int, indexChild: Int, addOnUIModel: AddOnUIModel) {
        onItemImpressionListener(index, indexChild, items)
    }

    fun setItems(items: List<AddOnGroupUIModel>) {
        this.items = items.toMutableList()
        notifyItemRangeChanged(Int.ZERO, items.size)
    }

    fun showDescription(isShow: Boolean) {
        isShowDescription = isShow
    }

}
