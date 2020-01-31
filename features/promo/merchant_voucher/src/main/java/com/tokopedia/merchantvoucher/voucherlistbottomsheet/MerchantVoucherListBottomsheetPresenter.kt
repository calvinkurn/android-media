package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.promocheckout.common.data.entity.request.CurrentApplyCode
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class MerchantVoucherListBottomsheetPresenter @Inject constructor(
        private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
        private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase
) : BaseDaggerPresenter<MerchantVoucherListBottomsheetContract.View>(), MerchantVoucherListBottomsheetContract.Presenter {
    private val paramMerchant = "merchant"
    private val statusOK = "OK"

    override fun getVoucherList(shopId: String, numVoucher: Int, cartItemDataVoucherList: List<CartItemDataVoucher>) {
        getMerchantVoucherListUseCase.execute(GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher, cartItemDataVoucherList),
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

    override fun checkPromoFirstStep(voucherId: String, promoMerchantCode: String, currentCartString: String,
                                     promo: Promo?, isFromList: Boolean) {
        if (promo != null) {
            promo.codes = ArrayList()
            val orders = promo.orders
            if (orders != null) {
                for (order in orders) {
                    val codes = ArrayList<String>()
                    if (order.uniqueId == currentCartString) {
                        codes.add(promoMerchantCode)

                        var currentApplyCode: CurrentApplyCode? = null
                        if (promoMerchantCode.isNotEmpty()) {
                            currentApplyCode = CurrentApplyCode(
                                    promoMerchantCode,
                                    paramMerchant
                            )
                        }
                        promo.currentApplyCode = currentApplyCode

                    }
                    order.codes = codes
                }
            }

            view.showLoadingDialog()
            checkPromoStackingCodeUseCase.setParams(promo)
            checkPromoStackingCodeUseCase.createObservable(RequestParams.create())
                    .subscribe(object : Subscriber<ResponseGetPromoStackUiModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            if (isViewAttached) {
                                view.hideLoadingDialog()
                                view.onErrorCheckPromoFirstStep(ErrorHandler.getErrorMessage(view.getActivityContext(), e), "", false)
                            }
                        }

                        override fun onNext(responseGetPromoStack: ResponseGetPromoStackUiModel) {
                            if (isViewAttached) {
                                view.hideLoadingDialog()
                                if (responseGetPromoStack.status.equals(statusOK, true)) {
                                    if (responseGetPromoStack.data.clashings.isClashedPromos) {
                                        view.onClashCheckPromoFirstStep(responseGetPromoStack.data.clashings, paramMerchant)
                                    } else {
                                        responseGetPromoStack.data.voucherOrders.forEach {
                                            if (it.code.equals(promoMerchantCode, true)) {
                                                val promoId = if (isFromList) voucherId else responseGetPromoStack.data.promoCodeId.toString()
                                                if (it.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                                                    view?.hideProgressLoading()
                                                    view.onErrorCheckPromoFirstStep(it.message.text, promoId, isFromList)
                                                } else {
                                                    view.onSuccessCheckPromoFirstStep(responseGetPromoStack, promoMerchantCode, isFromList, promoId)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    view?.hideProgressLoading()
                                    view.onErrorCheckPromoFirstStep(responseGetPromoStack.data.message.text, "", isFromList)
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
        checkPromoStackingCodeUseCase.unsubscribe()
    }

}