package com.tokopedia.merchantvoucher.voucherList.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo

/**
 * Created by normansyahputa on 2/13/18.
 */

interface MerchantVoucherListView : CustomerView {

    fun onSuccessGetShopInfo(shopInfo: ShopInfo)

    fun onErrorGetShopInfo(e: Throwable)

    fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult)

    fun onErrorUseVoucher(e: Throwable)

    fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>)

    fun onErrorGetMerchantVoucherList(e: Throwable)

}
