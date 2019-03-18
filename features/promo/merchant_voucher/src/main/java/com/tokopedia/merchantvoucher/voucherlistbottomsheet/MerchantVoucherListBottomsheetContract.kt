package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

interface MerchantVoucherListBottomsheetContract {

    interface View : CustomerView {

        fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>)

        fun onErrorGetMerchantVoucherList(e: Throwable)

    }

    interface Presenter : CustomerPresenter<View> {

        fun getVoucherList(shopId: String, numVoucher: Int)

        fun clearCache()

    }
}