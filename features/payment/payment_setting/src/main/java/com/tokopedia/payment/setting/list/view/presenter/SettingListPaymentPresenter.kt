package com.tokopedia.payment.setting.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.payment.setting.list.domain.GetCreditCardListUseCase
import com.tokopedia.payment.setting.list.model.GQLPaymentQueryResponse
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SettingListPaymentPresenter @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getCreditCardListUseCase: GetCreditCardListUseCase)
    : BaseDaggerPresenter<SettingListPaymentContract.View>(), SettingListPaymentContract.Presenter {

    override fun getCreditCardList(resources: Resources) {
        getCreditCardListUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse?) {
                if (isViewAttached) {
                    val response = objects?.getData<GQLPaymentQueryResponse>(GQLPaymentQueryResponse::class.java)
                    response?.let {
                        view.onPaymentSignature(response.paymentQueryResponse.paymentSignature)
                        view.renderList(response.paymentQueryResponse.creditCard.cards
                                ?: ArrayList())
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (isViewAttached) {
                    e?.let {
                        view.showGetListError(e)
                    }
                }
            }

        })
    }

    override fun checkVerificationPhone() {
        view.showLoadingDialog()
        if (userSession.isMsisdnVerified) {
            view.hideLoadingDialog()
            view.onSuccessVerifPhone()
        } else {
            view.hideLoadingDialog()
            view.onNeedVerifPhone()
        }
    }

    override fun detachView() {
        getCreditCardListUseCase.unSubscribe()
    }
}
