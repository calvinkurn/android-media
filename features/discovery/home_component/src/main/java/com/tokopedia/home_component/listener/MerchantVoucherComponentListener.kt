package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked

/**
 * Created by dhaba
 */
interface MerchantVoucherComponentListener  {
    fun onViewAllClicked(headerName: String, seeMoreAppLink: String)
    fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked)
    fun onMerchantImpressed(channel: ChannelModel, parentPos: Int)
    fun onProductClicked(merchantVoucherProductClicked: MerchantVoucherProductClicked)
    fun onVoucherDetailClicked(merchantVoucherDetailClicked: MerchantVoucherDetailClicked)
    fun getUserId() : String
}