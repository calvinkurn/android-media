package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.constant.NumberConstant

data class ProductListUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val currentPageMode: PageMode = PageMode.CREATE,
    val showCtaChangeProductOnToolbar: Boolean = false,
    val isEntryPointFromVoucherSummaryPage: Boolean = false,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(
        voucherId = NumberConstant.VOUCHER_ID_NOT_CREATED,
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
    val selectedProductsIdsToBeRemoved: Set<Long> = emptySet(),
    val isSelectAllActive: Boolean = false,
    val selectedWarehouseId: Long = 0,
    val error: Throwable? = null,
)
