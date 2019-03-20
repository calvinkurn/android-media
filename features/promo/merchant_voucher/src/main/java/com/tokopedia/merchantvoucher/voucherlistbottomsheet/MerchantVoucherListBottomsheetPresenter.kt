package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class MerchantVoucherListBottomsheetPresenter
@Inject constructor(private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase)
    : BaseDaggerPresenter<MerchantVoucherListBottomsheetContract.View>(), MerchantVoucherListBottomsheetContract.Presenter {

    override fun getVoucherList(shopId: String, numVoucher: Int) {
        getMerchantVoucherListUseCase.execute(GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher),
                object : Subscriber<ArrayList<MerchantVoucherModel>>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onErrorGetMerchantVoucherList(e)
                    }

                    override fun onNext(merchantVoucherModelList: ArrayList<MerchantVoucherModel>) {
                        val merchantViewModelList: ArrayList<MerchantVoucherViewModel> = ArrayList()
                        for (merchantVoucherModel in merchantVoucherModelList) {
                            val viewModel = MerchantVoucherViewModel(merchantVoucherModel)
                            viewModel.enableButtonUse = true
                            merchantViewModelList.add(viewModel)
                        }
                        view?.onSuccessGetMerchantVoucherList(merchantViewModelList)
                    }
                })
    }

    override fun clearCache() {
        getMerchantVoucherListUseCase.clearCache()
    }

    override fun detachView() {
        super.detachView()
        getMerchantVoucherListUseCase.unsubscribe()
    }

}