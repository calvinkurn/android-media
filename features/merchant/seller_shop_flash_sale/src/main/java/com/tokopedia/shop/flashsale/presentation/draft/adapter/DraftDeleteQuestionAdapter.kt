package com.tokopedia.shop.flashsale.presentation.draft.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO

class DraftDeleteQuestionAdapter: RecyclerView.Adapter<DraftDeleteQuestionViewHolder>() {

    private var items: MutableList<String> = mutableListOf()
    private var itemsSelection: List<Boolean> = emptyList()
    private var selectionChangedListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftDeleteQuestionViewHolder {
        val rootView = DraftDeleteQuestionViewHolder.createRootView(parent)
        return DraftDeleteQuestionViewHolder(rootView, ::onSelectionChanged)
    }

    override fun onBindViewHolder(holder: DraftDeleteQuestionViewHolder, position: Int) {
        holder.bind(items[position], itemsSelection[position])
    }

    override fun getItemCount() = items.size

    private fun onSelectionChanged(selectionItem: String, selectionIndex: Int) {
        itemsSelection = itemsSelection.mapIndexed { index, _ -> index == selectionIndex }
        notifyItemRangeChanged(Int.ZERO, itemCount)
        selectionChangedListener(selectionItem)
    }

    fun setItems(items: List<String>) {
        this.items = items.toMutableList()
        itemsSelection = items.map { false }
        notifyItemRangeChanged(Int.ZERO, itemCount)
    }

    fun setSelectionChangedListener(listener: (String) -> Unit){
        selectionChangedListener = listener
    }

    fun addItem(item: String) {
        items.add(item)
        itemsSelection = items.map { false }
        notifyItemChanged(itemCount)
    }
}