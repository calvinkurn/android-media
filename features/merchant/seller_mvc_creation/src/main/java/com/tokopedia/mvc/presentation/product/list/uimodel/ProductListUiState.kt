package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer

data class ProductListUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val pageMode: PageMode = PageMode.CREATE,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(
        benefitIdr = 0,
        benefitMax = 0,
        benefitPercent = 0,
        benefitType = BenefitType.NOMINAL,
        promoType = PromoType.CASHBACK,
        isVoucherProduct = true,
        minPurchase = 0,
        productIds = emptyList(),
        targetBuyer = VoucherTargetBuyer.ALL_BUYER
    ),
    val products: List<Product> = emptyList(),
    val maxProductSelection : Int = 0,
    val selectedProductsIds: Set<Long> = emptySet(),
    val isSelectAllActive: Boolean = false,
    val error: Throwable? = null
)
