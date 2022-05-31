package com.tokopedia.shop.flash_sale.presentation.draft.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flash_sale.presentation.draft.uimodel.DraftItemModel

class DraftDeleteQuestionAdapter: RecyclerView.Adapter<DraftDeleteQuestionViewHolder>() {

    private var items: List<String> = emptyList()
    private var itemsSelection: List<Boolean> = emptyList()
    private var selectionChangedListener: (String, Int) -> Unit = { _: String, i: Int ->
        itemsSelection = itemsSelection.map { false }
        itemsSelection[i]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftDeleteQuestionViewHolder {
        val rootView = DraftDeleteQuestionViewHolder.createRootView(parent)
        return DraftDeleteQuestionViewHolder(rootView, selectionChangedListener)
    }

    override fun onBindViewHolder(holder: DraftDeleteQuestionViewHolder, position: Int) {
        holder.bind(items[position], itemsSelection[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<String>) {
        this.items = items
        itemsSelection = items.map { false }
    }

    fun setSelectionChangedListener(listener: (String, Int) -> Unit){
        selectionChangedListener = listener
    }
}