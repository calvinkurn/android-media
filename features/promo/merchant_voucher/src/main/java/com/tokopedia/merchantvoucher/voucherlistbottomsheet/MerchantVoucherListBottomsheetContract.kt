package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

interface MerchantVoucherListBottomsheetContract {

    interface View : CustomerView {

        fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>)

        fun onErrorGetMerchantVoucherList(e: Throwable)

        fun onErrorCheckPromoFirstStep(message: String)

        fun onSuccessCheckPromoFirstStep(model: ResponseGetPromoStackUiModel, promoCode: String, isFromList: Boolean)

        fun onClashCheckPromoFirstStep(model: ClashingInfoDetailUiModel, type: String)

        fun showLoadingDialog()

        fun hideLoadingDialog()

        fun showProgressLoading()

        fun hideProgressLoading()

        fun getActivityContext(): Context?

    }

    interface Presenter : CustomerPresenter<View> {

        fun getVoucherList(shopId: String, numVoucher: Int)

        fun checkPromoFirstStep(promoMerchantCode: String, currentCartString: String, promo: Promo?, isFromList: Boolean)

        fun clearCache()

    }
}