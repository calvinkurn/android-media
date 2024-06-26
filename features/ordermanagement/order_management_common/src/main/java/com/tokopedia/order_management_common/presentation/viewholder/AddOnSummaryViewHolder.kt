package com.tokopedia.order_management_common.presentation.viewholder

import android.view.View
import android.view.ViewStub
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
import com.tokopedia.order_management_common.databinding.PartialAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.factory.AddOnAdapterFactory
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.order_management_common.util.RecyclerViewItemDivider
import com.tokopedia.order_management_common.util.rotateBackIcon
import com.tokopedia.order_management_common.util.rotateIcon
import com.tokopedia.order_management_common.util.runSafely
import com.tokopedia.unifycomponents.toPx

class AddOnSummaryViewHolder(
    private val addOnListener: AddOnViewHolder.Listener,
    private val binding: PartialAddOnSummaryBinding?,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool?
) {

    companion object {
        private const val ITEM_DECORATION_VERTICAL_MARGIN = 12
    }

    private val typeFactory = AddOnAdapterFactory(addOnListener)
    private val adapter = BaseAdapter(typeFactory)

    init {
        setupLayout()
    }

    private fun setupLayout() {
        binding?.setupRecyclerviewRecycledViewPool()
        binding?.setupRecyclerViewAdapter()
        binding?.setupRecyclerViewItemDecoration()
    }

    private fun PartialAddOnSummaryBinding.setupRecyclerviewRecycledViewPool() {
        rvAddOn.setRecycledViewPool(recyclerViewSharedPool)
    }

    private fun PartialAddOnSummaryBinding.setupRecyclerViewAdapter() {
        rvAddOn.isNestedScrollingEnabled = false
        rvAddOn.adapter = adapter
    }

    private fun PartialAddOnSummaryBinding.setupRecyclerViewItemDecoration() {
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
                        addOnListener.onAddOnsExpand(element.isExpand, element.addOnIdentifier)
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

    private fun PartialAddOnSummaryBinding.setupChevronExpandable(
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

    private fun PartialAddOnSummaryBinding.expandView(canExpandCollapse: Boolean) {
        tvBomDetailAddonsTotalPrice.hide()
        rvAddOn.show()
        icBomDetailAddonsIconArrowDown.showIfWithBlock(canExpandCollapse) { rotateBackIcon() }
    }

    private fun PartialAddOnSummaryBinding.collapseView(totalPriceFmt: StringRes) {
        val totalPriceText = totalPriceFmt.getString(root.context)
        tvBomDetailAddonsTotalPrice.showIfWithBlock(totalPriceText.isNotEmpty()) {
            text = totalPriceText
        }
        rvAddOn.hide()
        icBomDetailAddonsIconArrowDown.rotateIcon()
    }

    private fun PartialAddOnSummaryBinding.setupAddOnSummaryIcon(icon: String) {
        ivAddOnSummary.loadImage(icon)
    }

    private fun PartialAddOnSummaryBinding.setupAddOnSummaryLabel(
        label: String,
        isExpand: Boolean,
        totalPriceFmt: StringRes
    ) {
        tvAddOnLabel.text = label

        if (!isExpand) {
            tvBomDetailAddonsTotalPrice.show()
            tvBomDetailAddonsTotalPrice.text = totalPriceFmt.getString(root.context)
        } else {
            tvBomDetailAddonsTotalPrice.hide()
        }
    }

    private fun setupAddOnSummaryAddOns(addons: List<AddOnSummaryUiModel.AddonItemUiModel>) {
        binding?.rvAddOn?.showIfWithBlock(addons.isNotEmpty()) {
            this@AddOnSummaryViewHolder.adapter.setElements(addons)
        }
    }

    fun bind(addOnSummary: AddOnSummaryUiModel?) {
        setupAddOnSummary(addOnSummary)
    }

    interface Delegate {
        fun registerAddOnSummaryDelegate(mediator: Mediator)
        fun bindAddonSummary(addOnSummary: AddOnSummaryUiModel?)
        interface Mediator {
            fun getAddOnSummaryLayout(): View?
            fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool?
            fun getAddOnSummaryListener(): AddOnViewHolder.Listener
        }

        class Impl : Delegate {

            @Suppress("LateinitUsage")
            private lateinit var viewHolder: AddOnSummaryViewHolder
            @Suppress("LateinitUsage")
            private lateinit var _mediator: Mediator

            override fun registerAddOnSummaryDelegate(mediator: Mediator) {
                _mediator = mediator
            }

            override fun bindAddonSummary(addOnSummary: AddOnSummaryUiModel?) {
                var layout = _mediator.getAddOnSummaryLayout() ?: return
                if (addOnSummary?.addonItemList?.isNotEmpty() == true) {
                    if (layout is ViewStub) layout = layout.inflate() else layout.show()
                    if (!::viewHolder.isInitialized) {
                        viewHolder = AddOnSummaryViewHolder(
                            addOnListener = _mediator.getAddOnSummaryListener(),
                            binding = PartialAddOnSummaryBinding.bind(layout),
                            recyclerViewSharedPool = _mediator.getRecycleViewSharedPool()
                        )
                    }
                    viewHolder.bind(addOnSummary)
                } else {
                    layout.hide()
                }
            }
        }
    }
}
