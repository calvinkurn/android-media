package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.PartialItemOwocAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.AddonsItemAdapter
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel

class OwocPartialProductAddonViewHolder(
    private val partialItemBuyerOrderDetailAddonsBinding: PartialItemOwocAddonsBinding,
    private val navigator: BuyerOrderDetailNavigator?
) {

    fun bindViews(element: OwocAddonsListUiModel) {
        setupViews(element)
        setupAddonList(element)
    }

    private fun setupViews(element: OwocAddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            tvOwocAddonsTitle.text = element.addonsTitle
        }
    }

    private fun setupAddonList(element: OwocAddonsListUiModel) {
        partialItemBuyerOrderDetailAddonsBinding.run {
            rvOwocAddonsList.layoutManager = LinearLayoutManager(root.context)
            rvOwocAddonsList.adapter = AddonsItemAdapter(element.addonsItemList, navigator)
        }
    }
}
