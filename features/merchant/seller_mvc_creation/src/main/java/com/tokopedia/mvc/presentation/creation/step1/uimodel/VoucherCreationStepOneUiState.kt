package com.tokopedia.mvc.presentation.creation.step1.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType

data class VoucherCreationStepOneUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val pageMode: PageMode = PageMode.CREATE,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(
        benefitIdr = 0,
        benefitMax = 0,
        benefitPercent = 0,
        benefitType = BenefitType.NOMINAL,
        promoType = PromoType.CASHBACK,
        minPurchase = 0,
        productIds = emptyList(),
        isVoucherProduct = false
    ),
    val error: Throwable? = null
)
