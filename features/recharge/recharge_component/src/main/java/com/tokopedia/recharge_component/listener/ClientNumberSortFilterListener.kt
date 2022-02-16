package com.tokopedia.recharge_component.listener

import com.tokopedia.common.topupbills.data.product.CatalogOperator

interface ClientNumberSortFilterListener {
    fun getSelectedChipOperator(operator: CatalogOperator)
}