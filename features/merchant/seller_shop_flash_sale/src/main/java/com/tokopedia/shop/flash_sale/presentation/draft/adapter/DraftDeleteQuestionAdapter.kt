package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel

class DraftDeleteQuestionAdapter: RecyclerView.Adapter<DraftDeleteQuestionViewHolder>() {

    private var items: List<String> = emptyList()
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
        this.items = items
        itemsSelection = items.map { false }
    }

    fun setSelectionChangedListener(listener: (String) -> Unit){
        selectionChangedListener = listener
    }
}