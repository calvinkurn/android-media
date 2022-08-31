package com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchListGridItemLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.itemdecoration.ListGridItemDecoration
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedAdapter
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class ListGridViewHolder(
    itemView: View,
    private val listGridListener: ListGridListener,
    private val relatedItemListener: RelatedItemListener,
): AbstractViewHolder<ListGridDataView>(itemView) {
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_list_grid_item_layout
    }

    private var binding: UniversalSearchListGridItemLayoutBinding? by viewBinding()

    override fun bind(data: ListGridDataView) {
        bindTitle(data.data)
        bindSubtitle(data.data)
        bindSeeAll(data)
        bindRecyclerView(data)
    }

    private fun bindTitle(data: BaseUniversalDataView) {
        binding?.universalSearchListGridTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchListGridTitle?.text = data.title
        }
    }

    private fun bindSubtitle(data: BaseUniversalDataView) {
        binding?.universalSearchListGridSubtitle?.shouldShowWithAction(data.subtitle.isNotEmpty()) {
            binding?.universalSearchListGridSubtitle?.text = data.subtitle
        }
    }

    private fun bindSeeAll(data: ListGridDataView) {
        binding?.universalSearchListGridSubtitle?.shouldShowWithAction(data.data.applink.isNotEmpty()) {
            binding?.universalSearchListGridSubtitle?.setOnClickListener {
                listGridListener.onListGridSeeAllClick(data)
            }
        }
    }

    private fun bindRecyclerView(data: ListGridDataView) {
        val adapter = RelatedAdapter(relatedItemListener).apply {
            val relatedItemDataList = mutableListOf<RelatedItemDataView>()
            relatedItemDataList.addAll(data.related)

            updateList(relatedItemDataList)
        }

        binding?.universalSearchListGridRecyclerView?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(itemView.context)
            it.addItemDecoration(ListGridItemDecoration())
        }
    }
}