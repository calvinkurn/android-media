package com.tokopedia.order_management_common.presentation.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.factory.AddOnAdapterFactory
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.order_management_common.util.RecyclerViewItemDivider
import com.tokopedia.order_management_common.util.rotateBackIcon
import com.tokopedia.order_management_common.util.rotateIcon
import com.tokopedia.order_management_common.util.runSafely
import com.tokopedia.unifycomponents.toPx

class BmgmAddOnSummaryViewHolder(
    private val bmgmAddOnListener: BmgmAddOnViewHolder.Listener,
    private val binding: PartialBmgmAddOnSummaryBinding?,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) {

    companion object {
        private const val ITEM_DECORATION_VERTICAL_MARGIN = 12
    }

    private val typeFactory = AddOnAdapterFactory(bmgmAddOnListener)
    private val adapter = BaseAdapter(typeFactory)

    init {
        setupLayout()
    }

    private fun setupLayout() {
        binding?.setupRecyclerviewRecycledViewPool()
        binding?.setupRecyclerViewLayoutManager()
        binding?.setupRecyclerViewAdapter()
        binding?.setupRecyclerViewItemDecoration()
    }

    private fun PartialBmgmAddOnSummaryBinding.setupRecyclerviewRecycledViewPool() {
        rvAddOn.setRecycledViewPool(recyclerViewSharedPool)
    }

    private fun PartialBmgmAddOnSummaryBinding.setupRecyclerViewLayoutManager() {
        (rvAddOn.layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
    }

    private fun PartialBmgmAddOnSummaryBinding.setupRecyclerViewAdapter() {
        rvAddOn.isNestedScrollingEnabled = false
        rvAddOn.adapter = adapter
    }

    private fun PartialBmgmAddOnSummaryBinding.setupRecyclerViewItemDecoration() {
        if (rvAddOn.itemDecorationCount.isZero()) {
            val dividerDrawable = runSafely {
                MethodChecker.getDrawable(root.context, R.drawable.ic_dash_divider)
            }
            rvAddOn.addItemDecoration(
                RecyclerViewItemDivider(
                    dividerDrawable,
                    ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                    Int.ZERO
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
                setupAddOnSummaryIcon(element.addonsLogoUrl)
                setupAddOnSummaryLabel(
                    element.addonsTitle,
                    element.isExpand,
                    element.totalPriceText
                )
                setupAddOnSummaryAddOns(element.addonItemList)

                if (element.canExpandCollapse) {
                    root.setOnClickListener {
                        element.isExpand = !element.isExpand
                        bmgmAddOnListener.onAddOnsBmgmExpand(element.isExpand, element.addOnIdentifier)
                        setupChevronExpandable(
                            isExpand = element.isExpand,
                            totalPriceFmt = element.totalPriceText,
                            canExpandCollapse = true
                        )
                    }
                } else {
                    root.setOnClickListener(null)
                }

                setupChevronExpandable(
                    isExpand = element.isExpand,
                    totalPriceFmt = element.totalPriceText,
                    canExpandCollapse = element.canExpandCollapse
                )
            }
        }
    }

    private fun PartialBmgmAddOnSummaryBinding.setupChevronExpandable(
        isExpand: Boolean,
        totalPriceFmt: StringRes,
        canExpandCollapse: Boolean
    ) {
        if (isExpand) {
            expandView(canExpandCollapse)
        } else {
            collapseView(totalPriceFmt)
        }
    }

    private fun PartialBmgmAddOnSummaryBinding.expandView(canExpandCollapse: Boolean) {
        tvBomDetailBmgmAddonsTotalPrice.hide()
        rvAddOn.show()
        icBomDetailBmgmAddonsIconArrowDown.showIfWithBlock(canExpandCollapse) { rotateBackIcon() }
    }

    private fun PartialBmgmAddOnSummaryBinding.collapseView(totalPriceFmt: StringRes) {
        val totalPriceText = totalPriceFmt.getString(root.context)
        tvBomDetailBmgmAddonsTotalPrice.showIfWithBlock(totalPriceText.isNotEmpty()) {
            text = totalPriceText
        }
        rvAddOn.hide()
        icBomDetailBmgmAddonsIconArrowDown.rotateIcon()
    }

    private fun PartialBmgmAddOnSummaryBinding.setupAddOnSummaryIcon(icon: String) {
        ivAddOnSummary.loadImage(icon)
    }

    private fun PartialBmgmAddOnSummaryBinding.setupAddOnSummaryLabel(
        label: String,
        isExpand: Boolean,
        totalPriceFmt: StringRes
    ) {
        tvAddOnLabel.text = label

        if (!isExpand) {
            tvBomDetailBmgmAddonsTotalPrice.show()
            tvBomDetailBmgmAddonsTotalPrice.text = totalPriceFmt.getString(root.context)
        } else {
            tvBomDetailBmgmAddonsTotalPrice.hide()
        }
    }

    private fun setupAddOnSummaryAddOns(addons: List<AddOnSummaryUiModel.AddonItemUiModel>) {
        binding?.rvAddOn?.showIfWithBlock(addons.isNotEmpty()) {
            this@BmgmAddOnSummaryViewHolder.adapter.setElements(addons)
        }
    }

    fun bind(addOnSummary: AddOnSummaryUiModel?) {
        setupAddOnSummary(addOnSummary)
    }
}
