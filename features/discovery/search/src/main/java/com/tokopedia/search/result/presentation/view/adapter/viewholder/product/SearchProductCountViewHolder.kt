package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductCountLayoutBinding
import com.tokopedia.search.result.presentation.model.SearchProductCountDataView
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationClickListener
import com.tokopedia.utils.view.binding.viewBinding

class SearchProductCountViewHolder(itemView: View, private val listener: SearchNavigationClickListener): AbstractViewHolder<SearchProductCountDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_count_layout
    }
    private var binding: SearchResultProductCountLayoutBinding? by viewBinding()

    override fun bind(element: SearchProductCountDataView) {
        bindCountTitle(element)
        bindChangeViewListener(element)
    }

    private fun bindCountTitle(element: SearchProductCountDataView) {
        val binding = binding ?: return
        binding.searchProductCountTitle.shouldShowWithAction(element.productCountString.isNotEmpty()) {
            val countTitle = "<b>" + element.productCountString + "</b>" + " hasil pencarian"
            binding.searchProductCountTitle.text = MethodChecker.fromHtml(countTitle)
        }
    }

    private fun bindChangeViewListener(element: SearchProductCountDataView) {
        binding?.searchProductCountChangeViewButton?.setOnClickListener {
            listener.onChangeViewClicked(element.position)
        }
    }

    override fun bind(element: SearchProductCountDataView?, payloads: MutableList<Any>) {
        val payload = payloads.getOrNull(0) ?: return

        when(payload) {
            SearchConstant.RecyclerView.VIEW_LIST -> changeViewButton(IconUnify.VIEW_LIST)
            SearchConstant.RecyclerView.VIEW_PRODUCT_SMALL_GRID -> changeViewButton(IconUnify.VIEW_GRID)
            SearchConstant.RecyclerView.VIEW_PRODUCT_BIG_GRID -> changeViewButton(IconUnify.VIEW_GRID_BIG)
        }
    }

    private fun changeViewButton(id: Int) {
        binding?.searchProductCountChangeViewButton?.setImage(newIconId = id)
    }
}