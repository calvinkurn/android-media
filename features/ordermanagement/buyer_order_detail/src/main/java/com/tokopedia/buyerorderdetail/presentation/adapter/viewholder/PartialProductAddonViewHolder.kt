package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.AddonsItemAdapter
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel

class PartialProductAddonViewHolder(
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
        }
    }

    private fun setupAddonList(element: AddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            rvAddonsList.layoutManager = LinearLayoutManager(root.context)
            rvAddonsList.adapter = AddonsItemAdapter(element.addonsItemList)
        }
    }
}