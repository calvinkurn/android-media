package com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchCarouselItemLayoutBinding
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchDoubleLineItemLayoutBinding
import com.tokopedia.autocompletecomponent.universal.presentation.model.RelatedItemDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class DoubleLineViewHolder(
    private val itemView: View
    ): AbstractViewHolder<DoubleLineDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_double_line_item_layout

        private const val SPAN_COUNT = 2
    }

    private var binding: UniversalSearchDoubleLineItemLayoutBinding? by viewBinding()

    override fun bind(data: DoubleLineDataView) {
        bindTitle(data)
        bindRecyclerView(data)
    }

    private fun bindTitle(data: DoubleLineDataView) {
        binding?.universalSearchDoubleLineTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchDoubleLineTitle?.text = data.title
        }
    }

    private fun bindRecyclerView(data: DoubleLineDataView) {
        val adapter = DoubleLineRelatedAdapter().apply {
            val test = mutableListOf<RelatedItemDataView>()
            test.addAll(data.related)
            test.addAll(data.related)
            test.addAll(data.related)
            test.addAll(data.related)

            updateList(test)
        }

        binding?.universalSearchDoubleLineRecyclerView?.let {
            it.adapter = adapter
            it.layoutManager = GridLayoutManager(
                itemView.context,
                SPAN_COUNT,
                GridLayoutManager.HORIZONTAL,
                false
            )
        }
    }
}