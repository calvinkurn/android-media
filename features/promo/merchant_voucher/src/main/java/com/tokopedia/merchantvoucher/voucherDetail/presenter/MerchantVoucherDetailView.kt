package com.tokopedia.merchantvoucher.voucherDetail.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

/**
 * Created by normansyahputa on 2/13/18.
 */

interface MerchantVoucherDetailView : CustomerView {

    fun onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel: MerchantVoucherViewModel)

    fun onErrorGetMerchantVoucherDetail(e: Throwable)

    fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult)

    fun onErrorUseVoucher(e: Throwable)

}
