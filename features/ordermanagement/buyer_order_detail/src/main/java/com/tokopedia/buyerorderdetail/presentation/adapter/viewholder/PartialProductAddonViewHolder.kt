package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.AddonsItemAdapter
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class PartialProductAddonViewHolder(
    private val listener: PartialProductItemViewHolder.ProductViewListener,
    private val partialItemBuyerOrderDetailAddonsBinding: PartialItemBuyerOrderDetailAddonsBinding
) {

    fun bindViews(element: AddonsListUiModel) {
        setupViews(element)
        setupAddonList(element)
    }

    private fun setupViews(element: AddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            tvBomDetailAddonsTitle.text = element.addonsTitle
            tvBomDetailAddonsTotalPriceValue.text = element.totalPriceText
            tvBomDetailAddonsTotalPriceLabel.text =
                root.context.getString(R.string.order_addons_total_price_label, element.addonsTitle)
            tvBomDetailAddonsTotalPrice.text = "(${element.totalPriceText})"

            root.setOnClickListener {
                if (element.isExpand) {
                    collapseView()
                } else {
                    expandView()
                }
                element.isExpand = !element.isExpand
                listener.onAddOnsExpand(element.addOnIdentifier, element.isExpand)
            }

            if (element.isExpand) {
                expandView()
            } else {
                collapseView()
            }
        }
    }

    private fun expandView() = with(partialItemBuyerOrderDetailAddonsBinding) {
        tvBomDetailAddonsTotalPrice.hide()
        rvAddonsList.show()

        icBomDetailAddonsIconArrowDown.run {
            animate().rotation(0F).duration = 250
        }

        tvBomDetailAddonsTotalPriceLabel.show()
        tvBomDetailAddonsTotalPriceValue.show()
    }

    private fun collapseView() = with(partialItemBuyerOrderDetailAddonsBinding) {
        tvBomDetailAddonsTotalPrice.show()
        rvAddonsList.hide()

        icBomDetailAddonsIconArrowDown.run {
            animate().rotation(180F).duration = 250
        }

        tvBomDetailAddonsTotalPriceLabel.hide()
        tvBomDetailAddonsTotalPriceValue.hide()
    }

    private fun setupAddonList(element: AddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            rvAddonsList.layoutManager = LinearLayoutManager(root.context)
            rvAddonsList.adapter = AddonsItemAdapter(element.addonsItemList)
        }
    }
}