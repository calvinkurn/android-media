package com.tokopedia.saldodetails.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.contract.MerchantSaldoPriorityContract
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus

import javax.inject.Inject

import rx.Subscriber

class MerchantSaldoPriorityPresenter @Inject
constructor() : BaseDaggerPresenter<MerchantSaldoPriorityContract.View>(), MerchantSaldoPriorityContract.Presenter {

    @Inject
    lateinit var setMerchantSaldoStatusUseCase: SetMerchantSaldoStatus


    override fun onDestroyView() {
            setMerchantSaldoStatusUseCase.unsubscribe()
    }

    override fun updateSellerSaldoStatus(value: Boolean) {
        if (!isViewAttached) {
            return
        }
        view.showProgressLoading()
        setMerchantSaldoStatusUseCase.execute(value, object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onSaldoStatusUpdateError(ErrorHandler.getErrorMessage(view.context, e))
                }
            }

            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (!isViewAttached) {
                    return
                }
                if (graphqlResponse?.getData<Any>(GqlSetMerchantSaldoStatus::class.java) != null) {

                    val gqlSetMerchantSaldoStatus = graphqlResponse.getData<GqlSetMerchantSaldoStatus>(GqlSetMerchantSaldoStatus::class.java)

                    if (gqlSetMerchantSaldoStatus.merchantSaldoStatus?.isSuccess!!) {
                        view.onSaldoStatusUpdateSuccess(value)
                    } else {
                        view.onSaldoStatusUpdateError("")
                    }

                } else {
                    view.onSaldoStatusUpdateError("")
                }
                view.hideProgressLoading()
            }
        })
    }

}
