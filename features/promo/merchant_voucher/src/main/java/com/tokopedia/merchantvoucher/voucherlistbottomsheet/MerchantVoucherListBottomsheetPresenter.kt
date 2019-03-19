package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoFirstStepParam
import com.tokopedia.promocheckout.common.data.entity.request.PromoStackingRequestData
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class MerchantVoucherListBottomsheetPresenter @Inject constructor(
        private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
        private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase
) : BaseDaggerPresenter<MerchantVoucherListBottomsheetContract.View>(), MerchantVoucherListBottomsheetContract.Presenter {

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

            view.showProgressLoading()
            checkPromoStackingCodeUseCase.setParams(checkPromoFirstStepParam)
            checkPromoStackingCodeUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    if (isViewAttached) {
                        view.hideProgressLoading()
                        // Todo : Show error
                    }
                }

                override fun onNext(t: GraphqlResponse?) {
                    if (isViewAttached) {
                        view.hideProgressLoading()
                        // Todo : Update view
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