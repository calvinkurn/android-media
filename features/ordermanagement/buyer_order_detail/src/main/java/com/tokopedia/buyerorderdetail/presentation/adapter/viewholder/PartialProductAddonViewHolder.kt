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
            tvBomDetailAddonsTotalPriceValue.text = element.totalPriceText?.getString(root.context).orEmpty()
            tvBomDetailAddonsTotalPriceLabel.text =
                root.context.getString(R.string.order_addons_total_price_label, element.addonsTitle)

            root.setOnClickListener {
                element.isExpand = !element.isExpand
                listener.onAddOnsExpand(element.addOnIdentifier, element.isExpand)
                setupChevronExpandable(
                    element.isExpand,
                    element.totalPriceText
                )
            }

            setupChevronExpandable(
                isExpand = element.isExpand,
                totalPriceFmt = element.totalPriceText
            )
            setupAddOnSummaryIcon(element.addonsLogoUrl)
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupAddOnSummaryIcon(icon: String) {
        icBomDetailAddonsIcon.loadImage(icon)
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.setupChevronExpandable(
        isExpand: Boolean,
        totalPriceFmt: StringRes?
    ) {
        if (isExpand) {
            expandView()
        } else {
            collapseView(totalPriceFmt)
        }
    }

    private fun PartialItemBuyerOrderDetailAddonsBinding.expandView() {
        tvBomDetailAddonsTotalPrice.hide()
        rvAddonsList.show()

        icBomDetailAddonsIconArrowDown.rotateBackIcon()
        tvBomDetailAddonsTotalPriceLabel.show()
        tvBomDetailAddonsTotalPriceValue.show()
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
