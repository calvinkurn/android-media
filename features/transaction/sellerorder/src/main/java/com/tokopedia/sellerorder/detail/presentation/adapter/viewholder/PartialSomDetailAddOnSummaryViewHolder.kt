package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.RecyclerViewItemDivider
import com.tokopedia.sellerorder.databinding.PartialAddOnSummaryBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.AddOnAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.model.AddOnSummaryUiModel
import com.tokopedia.sellerorder.detail.presentation.model.AddOnUiModel
import com.tokopedia.unifycomponents.toPx

class PartialSomDetailAddOnSummaryViewHolder(
    somDetailAddOnListener: SomDetailAddOnViewHolder.Listener,
    private val binding: PartialAddOnSummaryBinding?,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) {

    companion object {
        private const val ITEM_DECORATION_VERTICAL_MARGIN = 12
    }

    private val typeFactory = AddOnAdapterFactory(somDetailAddOnListener)
    private val adapter = BaseAdapter(typeFactory)

    init {
        setupLayout()
    }

    private fun setupLayout() {
        binding?.rvAddOn?.setRecycledViewPool(recyclerViewSharedPool)
        (binding?.rvAddOn?.layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
        binding?.rvAddOn?.adapter = this@PartialSomDetailAddOnSummaryViewHolder.adapter
        if (binding?.rvAddOn?.itemDecorationCount == Int.ZERO) {
            val dividerDrawable = try {
                MethodChecker.getDrawable(
                    binding.root.context,
                    R.drawable.som_detail_add_on_divider
                )
            } catch (t: Throwable) {
                null
            }
            binding.rvAddOn.addItemDecoration(
                RecyclerViewItemDivider(
                    dividerDrawable,
                    ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                    ITEM_DECORATION_VERTICAL_MARGIN.toPx()
                )
            )
        }
    }

    private fun setupAddOnSummary(element: AddOnSummaryUiModel?) {
        binding?.run {
            if (element == null) {
                root.hide()
            } else {
                root.show()
                setupAddOnSummaryLabel(element.label)
                setupAddOnSummaryAddOns(element.addons)
            }
        }
    }

    private fun PartialAddOnSummaryBinding.setupAddOnSummaryLabel(label: String) {
        tvAddOnLabel.text = label
    }

    private fun setupAddOnSummaryAddOns(addons: List<AddOnUiModel>) {
        adapter.setElements(addons)
    }

    fun bind(addOnSummary: AddOnSummaryUiModel?) {
        setupAddOnSummary(addOnSummary)
    }

    fun isShowing() = binding?.root?.isVisible == true
}