package com.tokopedia.merchantvoucher.voucherList.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by normansyahputa on 2/13/18.
 */

interface MerchantVoucherListView : CustomerView {

    fun onSuccessGetShopInfo(shopInfo: ShopInfo)

    fun onErrorGetShopInfo(e: Throwable)

}
