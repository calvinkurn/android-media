package com.tokopedia.shop.flashsale.presentation.draft.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.flashsale.presentation.draft.uimodel.DraftItemModel

class DraftListAdapter: RecyclerView.Adapter<DraftListViewHolder>() {

    private var items: List<DraftItemModel> = emptyList()
    private var deleteIconClickListener: (DraftItemModel) -> Unit = {}
    private var onDraftClicked : (DraftItemModel) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftListViewHolder {
        val rootView = DraftListViewHolder.createRootView(parent)
        return DraftListViewHolder(rootView, deleteIconClickListener, onDraftClicked)
    }

    override fun onBindViewHolder(holder: DraftListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<DraftItemModel>) {
        this.items = items
    }

    fun setDeleteIconClickListener(listener: (DraftItemModel) -> Unit){
        deleteIconClickListener = listener
    }

    fun setOnDraftClick(onDraftClicked : (DraftItemModel) -> Unit) {
        this.onDraftClicked = onDraftClicked
    }

}