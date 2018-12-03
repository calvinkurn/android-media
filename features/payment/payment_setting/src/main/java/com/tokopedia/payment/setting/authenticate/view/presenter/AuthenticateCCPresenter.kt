package com.tokopedia.payment.setting.authenticate.view.presenter

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.model.Data
import com.tokopedia.payment.setting.authenticate.model.Datum
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import rx.Subscriber

class AuthenticateCCPresenter(val whiteListCCUseCase : GraphqlUseCase, val userSession: UserSession) : BaseDaggerPresenter<AuthenticateCCContract.View>(), AuthenticateCCContract.Presenter {

    override fun updateWhiteList(authValue: Int, resources: Resources, isNeedCheckOtp : Boolean) {
        if(authValue == SINGLE_AUTH_VALUE && isNeedCheckOtp){
            view?.goToOtpPage(userSession.phoneNumber)
            return
        }
        view.showProgressLoading()
        val variables = HashMap<String, Any?>()
        variables.put(UPDATE_STATUS, true)
        variables.put(AUTH_VALUE, authValue)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.whitelist_credit_card), Data::class.java, variables)
        whiteListCCUseCase.clearRequest()
        whiteListCCUseCase.addRequest(graphqlRequest)
        whiteListCCUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorUpdateWhiteList(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                view.hideProgressLoading()
                val whiteListCC = objects.getData<Data>(Data::class.java)
                view.onResultUpdateWhiteList(whiteListCC?.checkWhiteListStatus)
            }
        })
    }

    override fun checkWhiteList(resources : Resources) {
        val variables = HashMap<String, Any?>()
        variables.put(UPDATE_STATUS, false)
        variables.put(AUTH_VALUE, 0)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.whitelist_credit_card), Data::class.java, variables)
        whiteListCCUseCase.clearRequest()
        whiteListCCUseCase.addRequest(graphqlRequest)
        whiteListCCUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showGetListError(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val whiteListCC = objects.getData<Data>(Data::class.java)
                view.renderList(generateDataListAuth(whiteListCC?.checkWhiteListStatus?.data))
            }
        })
    }

    private fun generateDataListAuth(data: List<Datum>?): List<TypeAuthenticateCreditCard> {
        val listAuth = ArrayList<TypeAuthenticateCreditCard>()
        listAuth.add(initiateSingleAuthentication(data?.get(0)))
        listAuth.add(initiateDoubleAuthentication(data?.get(0)))
        return listAuth
    }

    private fun initiateSingleAuthentication(model: Datum?): TypeAuthenticateCreditCard {
        val singleAuthentication = TypeAuthenticateCreditCard()
        singleAuthentication.title = view.getString(R.string.payment_authentication_title_1)
        singleAuthentication.description = view.getString(R.string.payment_authentication_description_1)
        singleAuthentication.stateWhenSelected = SINGLE_AUTH_VALUE
        singleAuthentication.isSelected = model?.state == SINGLE_AUTH_VALUE
        return singleAuthentication
    }

    private fun initiateDoubleAuthentication(model: Datum?): TypeAuthenticateCreditCard {
        val doubleAuthentication = TypeAuthenticateCreditCard()
        doubleAuthentication.title = view.getString(R.string.payment_authentication_title_2)
        doubleAuthentication.description = view.getString(R.string.payment_authentication_description_2)
        doubleAuthentication.stateWhenSelected = DOUBLE_AUTH_VALUE
        doubleAuthentication.isSelected = model?.state == DOUBLE_AUTH_VALUE
        return doubleAuthentication
    }

    override fun detachView() {
        whiteListCCUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        val UPDATE_STATUS = "updateStatus"
        val AUTH_VALUE = "authValue"

        val SINGLE_AUTH_VALUE = 1
        val DOUBLE_AUTH_VALUE = 0
    }

}
