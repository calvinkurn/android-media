package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import com.tokopedia.buyerorderdetail.databinding.PartialItemOwocAddonsBinding

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.AddonsItemAdapter
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel

class OwocPartialProductAddonViewHolder(
    private val partialItemBuyerOrderDetailAddonsBinding: PartialItemOwocAddonsBinding
) {

    fun bindViews(element: OwocAddonsListUiModel) {
        setupViews(element)
        setupAddonList(element)
    }

    private fun setupViews(element: OwocAddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            tvOwocAddonsTitle.text = element.addonsTitle
            tvOwocAddonsTotalPriceValue.text = element.totalPriceText
            tvOwocAddonsTotalPriceLabel.text =
                root.context.getString(R.string.order_addons_total_price_label, element.addonsTitle)
        }
    }

    private fun setupAddonList(element: OwocAddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            rvOwocAddonsList.layoutManager = LinearLayoutManager(root.context)
            rvOwocAddonsList.adapter = AddonsItemAdapter(element.addonsItemList)
        }
    }
}
