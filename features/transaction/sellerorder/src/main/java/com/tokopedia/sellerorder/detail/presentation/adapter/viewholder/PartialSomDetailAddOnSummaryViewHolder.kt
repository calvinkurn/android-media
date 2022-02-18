package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
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
        binding?.rvAddOn?.run {
            setRecycledViewPool(recyclerViewSharedPool)
            (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
            adapter = this@PartialSomDetailAddOnSummaryViewHolder.adapter
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(
                    RecyclerViewItemDivider(
                        MethodChecker.getDrawable(context, R.drawable.som_detail_add_on_divider),
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx()
                    )
                )
            }
        }
    }

    private fun setupAddOnSummary(element: AddOnSummaryUiModel?) {
        binding?.run {
            if (element == null) {
                root.hide()
            } else {
                root.show()
                setupAddOnTips(element.providedByBranchShop)
                setupAddOnSummaryIcon(element.iconUrl)
                setupAddOnSummaryLabel(element.label)
                setupAddOnSummaryAddOns(element.addons)
            }
        }
    }

    private fun PartialAddOnSummaryBinding.setupAddOnTips(providedByBranchShop: Boolean) {
        labelAddOn.showWithCondition(providedByBranchShop)
    }

    private fun PartialAddOnSummaryBinding.setupAddOnSummaryIcon(iconUrl: String) {
        ivAddOnIcon.loadImage(iconUrl)
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