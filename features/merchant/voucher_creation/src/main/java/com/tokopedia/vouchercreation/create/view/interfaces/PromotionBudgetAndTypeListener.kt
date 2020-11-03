package com.tokopedia.vouchercreation.create.view.interfaces

import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType

interface PromotionBudgetAndTypeListener: CreateMerchantVoucherListener {

    fun onSetVoucherBenefit(imageType: VoucherImageType, minPurchase: Int, quota: Int)
    fun onSetShopInfo(shopName: String, shopAvatarUrl: String)

}