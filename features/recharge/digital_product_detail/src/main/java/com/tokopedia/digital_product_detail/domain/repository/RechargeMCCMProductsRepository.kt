package com.tokopedia.digital_product_detail.domain.repository

import com.tokopedia.recharge_component.model.denom.DenomWidgetModel

interface RechargeMCCMProductsRepository {
    suspend fun getMCCMProducts(clientNumbers: List<String>,
                                dgCategoryIds: List<Int>,
                                dgOperatorIds: List<Int> = emptyList(),
                                channelName: String): DenomWidgetModel

}
