package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class MerchantVoucherListBottomsheetPresenter @Inject constructor(
        private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
        private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
        private val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper
) : BaseDaggerPresenter<MerchantVoucherListBottomsheetContract.View>(), MerchantVoucherListBottomsheetContract.Presenter {

    override fun getVoucherList(shopId: String, numVoucher: Int) {
        getMerchantVoucherListUseCase.execute(GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher),
                object : Subscriber<ArrayList<MerchantVoucherModel>>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.hideProgressLoading()
                        view?.onErrorGetMerchantVoucherList(e)
                    }

                    override fun onNext(merchantVoucherModelList: ArrayList<MerchantVoucherModel>) {
                        view?.hideProgressLoading()
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

    override fun checkPromoFirstStep(promoMerchantCode: String, currentCartString: String,
                                     checkPromoFirstStepParam: CheckPromoFirstStepParam?) {
        if (checkPromoFirstStepParam != null) {
            val orders = checkPromoFirstStepParam.orders;
            if (orders != null) {
                for (order in orders) {
                    if (order.uniqueId == currentCartString) {
                        val codes = ArrayList<String>()
                        codes.add(promoMerchantCode)
                        order.codes = codes
                        break
                    }
                }
            }

            view.showLoadingDialog()
            checkPromoStackingCodeUseCase.setParams(checkPromoFirstStepParam)
            checkPromoStackingCodeUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    if (isViewAttached) {
                        view.hideLoadingDialog()
                        view.onErrorCheckPromoFirstStep(ErrorHandler.getErrorMessage(view.getActivityContext(), e))
                    }
                }

                override fun onNext(response: GraphqlResponse?) {
                    if (isViewAttached) {
                        view.hideLoadingDialog()
                        val responseGetPromoStack = checkPromoStackingCodeMapper.call(response)
                        if (responseGetPromoStack.status != "OK" || responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                            var message = responseGetPromoStack.data.message.text
                            view.onErrorCheckPromoFirstStep(message)
                        } else {
                            view.onSuccessCheckPromoFirstStep(responseGetPromoStack)
                        }
                    }
                }
            })
        }
    }

    override fun clearCache() {
        getMerchantVoucherListUseCase.clearCache()
    }

    override fun detachView() {
        super.detachView()
        getMerchantVoucherListUseCase.unsubscribe()
    }

}