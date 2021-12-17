package com.tokopedia.sellerorder.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSomFilterListBinding
import com.tokopedia.sellerorder.filter.presentation.adapter.FilterItemDecoration
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter.Companion.PAYLOAD_CHIPS_FILTER
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterItemChipsAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding

class SomFilterViewHolder(view: View, private val filterListener: SomFilterListener) : AbstractViewHolder<SomFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_som_filter_list
    }

    private var somFilterItemChipsAdapter: SomFilterItemChipsAdapter? = null
    private val binding by viewBinding<ItemSomFilterListBinding>()

    override fun bind(element: SomFilterUiModel) {
        somFilterItemChipsAdapter = SomFilterItemChipsAdapter(filterListener)
        binding?.run {
            tvTitleHeaderSomFilter.text = element.nameFilter
            tvHeaderSeeAll.apply {
                if (element.canSelectMany) {
                    show()
                    setOnClickListener {
                        filterListener.onSeeAllFilter(element, adapterPosition, element.nameFilter)
                    }
                } else {
                    hide()
                }
            }
            dividerSomFilter.apply {
                if (element.isDividerVisible) {
                    show()
                } else {
                    hide()
                }
            }
        }
        setupChipsAdapter(element)
    }

    override fun bind(element: SomFilterUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isNullOrEmpty()) return

        when(payloads[0] as Int) {
            PAYLOAD_CHIPS_FILTER -> {
                setupChipsAdapter(element)
            }
        }
    }

    private fun setupChipsAdapter(data: SomFilterUiModel) {
        binding?.run {
            val layoutManagerChips = ChipsLayoutManager.newBuilder(root.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            rvSomFilter?.also {
                if (it.itemDecorationCount == 0) {
                    it.addItemDecoration(FilterItemDecoration())
                }
                it.layoutManager = layoutManagerChips
                ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
                it.adapter = somFilterItemChipsAdapter
                somFilterItemChipsAdapter?.setChipsFilter(data.somFilterData, data.nameFilter)
            }
        }
    }
}