package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.AddonsItemAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PartialProductItemViewHolder.ProductViewListener
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.order_management_common.util.rotateBackIcon
import com.tokopedia.order_management_common.util.rotateIcon

class PartialProductAddonViewHolder(
    private val listener: ProductViewListener,
    private val partialItemBuyerOrderDetailAddonsBinding: PartialItemBuyerOrderDetailAddonsBinding
) {

    fun bindViews(element: AddonsListUiModel) {
        setupViews(element)
        setupAddonList(element)
    }

    private fun setupViews(element: AddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            tvBomDetailAddonsTitle.text = element.addonsTitle
            tvBomDetailAddonsTotalPriceValue.showIfWithBlock(element.showTotalPrice) {
                text = element.totalPriceText?.getString(root.context).orEmpty()
            }
            tvBomDetailAddonsTotalPriceLabel.showIfWithBlock(element.showTotalPrice) {
                text = root.context.getString(R.string.order_addons_total_price_label, element.addonsTitle)
            }

            if (element.canExpandCollapse) {
                root.setOnClickListener {
                    element.isExpand = !element.isExpand
                    listener.onAddOnsExpand(element.addOnIdentifier, element.isExpand)
                    setupExpandCollapseChevron(
                        isExpand = element.isExpand,
                        totalPriceFmt = element.totalPriceText,
                        canExpandCollapse = element.canExpandCollapse,
                        showTotalPrice = element.showTotalPrice
                    )
                }
            }

            setupExpandCollapseChevron(
                isExpand = element.isExpand,
                totalPriceFmt = element.totalPriceText,
                canExpandCollapse = element.canExpandCollapse,
                showTotalPrice = element.showTotalPrice
            )
            setupAddOnSummaryIcon(element.addonsLogoUrl)
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupAddOnSummaryIcon(icon: String) {
        icBomDetailAddonsIcon.loadImage(icon)
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupExpandCollapseChevron(
        isExpand: Boolean,
        totalPriceFmt: StringRes?,
        canExpandCollapse: Boolean,
        showTotalPrice: Boolean
    ) {
        if (isExpand) {
            expandView(canExpandCollapse, showTotalPrice)
        } else {
            collapseView(totalPriceFmt)
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.expandView(
        canExpandCollapse: Boolean,
        showTotalPrice: Boolean
    ) {
        tvBomDetailAddonsTotalPrice.hide()
        rvAddonsList.show()

        icBomDetailAddonsIconArrowDown.showIfWithBlock(canExpandCollapse) { rotateBackIcon() }
        tvBomDetailAddonsTotalPriceLabel.showWithCondition(showTotalPrice)
        tvBomDetailAddonsTotalPriceValue.showWithCondition(showTotalPrice)
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.collapseView(totalPriceFmt: StringRes?) {
        val totalPriceText = totalPriceFmt?.getString(root.context).orEmpty()
        tvBomDetailAddonsTotalPrice.showIfWithBlock(totalPriceText.isNotEmpty()) {
            text = totalPriceText
        }
        rvAddonsList.hide()
        icBomDetailAddonsIconArrowDown.rotateIcon()
        tvBomDetailAddonsTotalPriceLabel.hide()
        tvBomDetailAddonsTotalPriceValue.hide()
    }

    private fun setupAddonList(element: AddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            rvAddonsList.layoutManager = LinearLayoutManager(root.context)
            rvAddonsList.adapter = AddonsItemAdapter(element.addonsItemList, listener)
        }
    }
}
