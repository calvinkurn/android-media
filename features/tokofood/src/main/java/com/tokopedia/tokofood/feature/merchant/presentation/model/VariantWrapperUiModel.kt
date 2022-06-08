package com.tokopedia.tokofood.feature.merchant.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile

data class VariantWrapperUiModel(
    val productListItem: ProductListItem? = null,
    val merchantId: String = "",
    val tokoFoodMerchantProfile: TokoFoodMerchantProfile? = null,
    val position: Int = Int.ZERO
)