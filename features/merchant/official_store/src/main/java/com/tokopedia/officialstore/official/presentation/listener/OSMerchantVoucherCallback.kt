package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherImpressed
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by dhaba
 */
class OSMerchantVoucherCallback(private val dcEventHandler: DynamicChannelEventHandler): MerchantVoucherComponentListener {
    override fun onViewAllClicked(headerName: String, seeMoreAppLink: String, userId: String) {

    }

    override fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked) {

    }

    override fun onMerchantImpressed(merchantVoucherImpressed: MerchantVoucherImpressed) {

    }

    override fun onProductClicked(merchantVoucherProductClicked: MerchantVoucherProductClicked) {

    }

    override fun getUserId(): String = dcEventHandler.getUserId()
}