package com.tokopedia.centralizedpromoold.domain.mapper

import com.tokopedia.centralizedpromoold.domain.model.MerchantPromotionGetPromoListPageOld
import javax.inject.Inject

class MerchantPromotionListEligibleMapperOld @Inject constructor() {

    companion object {
        private const val SLASH_PRICE_PAGE_ID = "16"
        private const val VOUCHER_CASHBACK_PAGE_ID = "23"
    }

    fun isVoucherCashbackEligible(pages: List<MerchantPromotionGetPromoListPageOld>): Boolean {
        val voucherCashbackPage = pages.find { it.pageId == VOUCHER_CASHBACK_PAGE_ID }
        return voucherCashbackPage?.getIsEligible() ?: true
    }

    fun isSlashPriceEligible(pages: List<MerchantPromotionGetPromoListPageOld>): Boolean {
        val slashPricePage = pages.find { it.pageId == SLASH_PRICE_PAGE_ID }
        return slashPricePage?.getIsEligible() ?: true
    }

}