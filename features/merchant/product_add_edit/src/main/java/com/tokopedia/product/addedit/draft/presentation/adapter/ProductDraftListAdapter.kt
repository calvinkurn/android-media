package com.tokopedia.product.addedit.draft.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.draft.presentation.fragment.AddEditProductDraftFragment.Companion.FIRST_INDEX
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel
import com.tokopedia.product.addedit.draft.presentation.viewholder.ProductDraftListViewHolder
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft

class ProductDraftListAdapter(
        private val listener: ProductDraftListListener
) : RecyclerView.Adapter<ProductDraftListViewHolder>() {

    private val drafts =  mutableListOf<ProductDraftUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDraftListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_product_draft_list, parent, false)
        return ProductDraftListViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: ProductDraftListViewHolder, position: Int) {
        holder.bindData(drafts[position])
    }

    override fun getItemCount(): Int = drafts.size

    fun setDrafts(newDrafts: List<ProductDraft>?) {
        drafts.clear()
        newDrafts?.forEach { draft ->
            drafts.add(AddEditProductMapper.mapProductDraftToProductDraftUiModel(draft))
        }
        notifyDataSetChanged()
    }

    fun deleteDraft(position: Int) {
        if(position >= FIRST_INDEX && position < drafts.size) {
            drafts.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun deleteAllDrafts() {
        drafts.clear()
        notifyDataSetChanged()
    }

    fun isDraftEmpty(): Boolean {
        return drafts.isEmpty()
    }
}