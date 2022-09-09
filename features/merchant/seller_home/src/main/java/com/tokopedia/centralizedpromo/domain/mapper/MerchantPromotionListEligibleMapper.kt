package com.tokopedia.centralizedpromo.domain.mapper

import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListPage
import javax.inject.Inject

class MerchantPromotionListEligibleMapper @Inject constructor() {

    companion object {
        private const val SLASH_PRICE_PAGE_ID = "16"
        private const val VOUCHER_CASHBACK_PAGE_ID = "23"
    }

    fun isVoucherCashbackEligible(pages: List<MerchantPromotionGetPromoListPage>): Boolean {
        val voucherCashbackPage = pages.find { it.pageId == VOUCHER_CASHBACK_PAGE_ID }
        return voucherCashbackPage?.getIsEligible() ?: true
    }

    fun isSlashPriceEligible(pages: List<MerchantPromotionGetPromoListPage>): Boolean {
        val slashPricePage = pages.find { it.pageId == SLASH_PRICE_PAGE_ID }
        return slashPricePage?.getIsEligible() ?: true
    }

}