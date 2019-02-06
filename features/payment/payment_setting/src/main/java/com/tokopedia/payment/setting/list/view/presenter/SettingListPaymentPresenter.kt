package com.tokopedia.payment.setting.list.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.DataCreditCardList
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import rx.Subscriber

class SettingListPaymentPresenter(val userSession: UserSession) : BaseDaggerPresenter<SettingListPaymentContract.View>(), SettingListPaymentContract.Presenter {

    private val getCCListUseCase = GraphqlUseCase()

    override fun getCreditCardList(resources : Resources) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.credit_card_list_query), DataCreditCardList::class.java, null)
        getCCListUseCase.clearRequest()
        getCCListUseCase.addRequest(graphqlRequest)
        getCCListUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListError(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val paymentList = objects.getData<DataCreditCardList>(DataCreditCardList::class.java)
                view.renderList(paymentList?.creditCard?.cards?:ArrayList())
            }
        })
    }

    override fun checkVerificationPhone() {
        view.showLoadingDialog()
        if(userSession.isMsisdnVerified){
            view.hideLoadingDialog()
            view.onSuccessVerifPhone()
        }else{
            view.hideLoadingDialog()
            view.onNeedVerifPhone()
        }
    }

    override fun detachView() {
        getCCListUseCase.unsubscribe()
        super.detachView()
    }

}
