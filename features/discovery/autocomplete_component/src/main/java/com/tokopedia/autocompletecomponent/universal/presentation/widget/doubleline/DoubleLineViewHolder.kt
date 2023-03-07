package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchDoubleLineItemLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.itemdecoration.DoubleLineItemDecoration
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedAdapter
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemListener
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class DoubleLineViewHolder(
    itemView: View,
    private val doubleLineListener: DoubleLineListener,
    private val relatedItemListener: RelatedItemListener,
    ): AbstractViewHolder<DoubleLineDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_double_line_item_layout
    }

    private var binding: UniversalSearchDoubleLineItemLayoutBinding? by viewBinding()

    override fun bind(data: DoubleLineDataView) {
        bindTitle(data.data)
        bindSubtitle(data.data)
        bindSeeAll(data)
        bindRecyclerView(data)
    }

    private fun bindTitle(data: BaseUniversalDataView) {
        binding?.universalSearchDoubleLineTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchDoubleLineTitle?.text = data.title
        }
    }

    private fun bindSubtitle(data: BaseUniversalDataView) {
        binding?.universalSearchDoubleLineSubtitle?.shouldShowWithAction(data.subtitle.isNotEmpty()) {
            binding?.universalSearchDoubleLineSubtitle?.text = data.subtitle
        }
    }

    private fun bindSeeAll(data: DoubleLineDataView) {
        binding?.universalSearchDoubleLineSeeAll?.shouldShowWithAction(data.data.applink.isNotEmpty()) {
            binding?.universalSearchDoubleLineSeeAll?.setOnClickListener {
                doubleLineListener.onDoubleLineSeeAllClick(data)
            }
        }
    }

    private fun bindRecyclerView(data: DoubleLineDataView) {
        val adapter = RelatedAdapter(
            relatedItemListener,
            RelatedItemViewHolder.TYPE_DOUBLE_LINE,
        ).apply {
            updateList(data.related)
        }

        binding?.universalSearchDoubleLineRecyclerView?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(itemView.context)
            it.addItemDecoration(DoubleLineItemDecoration())
        }
    }
}