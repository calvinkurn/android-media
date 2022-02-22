package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherImpressed
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked

/**
 * Created by dhaba
 */
interface MerchantVoucherComponentListener  {
    fun onViewAllClicked(headerName: String, seeMoreAppLink: String, userId: String)
    fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked)
    fun onMerchantImpressed(merchantVoucherImpressed: MerchantVoucherImpressed)
    fun onProductClicked(merchantVoucherProductClicked: MerchantVoucherProductClicked)
    fun getUserId() : String
}