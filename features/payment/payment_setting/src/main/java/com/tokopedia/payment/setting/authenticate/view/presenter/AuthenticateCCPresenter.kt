package com.tokopedia.payment.setting.authenticate.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.domain.CheckUpdateWhiteListCreditCartUseCase
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListResponse
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.authenticate.model.WhiteListData
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class AuthenticateCCPresenter @Inject constructor(
        private val checkUpdateWhiteListCreditCartUseCase: CheckUpdateWhiteListCreditCartUseCase,
        private val userSession: UserSessionInterface) : BaseDaggerPresenter<AuthenticateCCContract.View>(),
        AuthenticateCCContract.Presenter {

    override fun updateWhiteList(authValue: Int, isNeedCheckOtp: Boolean,
                                 token: String?) {
        if (authValue == SINGLE_AUTH_VALUE && isNeedCheckOtp) {
            view?.goToOtpPage(userSession.phoneNumber)
            return
        }
        view.showProgressLoading()
        checkUpdateWhiteListCreditCartUseCase.execute(authValue, true, token,
                object : Subscriber<GraphqlResponse>() {
                    override fun onNext(response: GraphqlResponse?) {
                        if (isViewAttached) {
                            view.hideProgressLoading()
                            response?.let {
                                val checkWhiteListResponse = response
                                        .getData<CheckWhiteListResponse>(CheckWhiteListResponse::class.java)
                                view.onResultUpdateWhiteList(checkWhiteListResponse.checkWhiteListStatus)
                            }
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            view.hideProgressLoading()
                            e?.let {
                                view.onErrorUpdateWhiteList(it)
                            }
                        }
                    }
                })
    }

    override fun checkWhiteList() {
        checkUpdateWhiteListCreditCartUseCase.execute(0, false, null,
                object : Subscriber<GraphqlResponse>() {
                    override fun onNext(response: GraphqlResponse?) {
                        if (isViewAttached) {
                            view.hideProgressLoading()
                            response?.let {
                                val checkWhiteListResponse = response
                                        .getData<CheckWhiteListResponse>(CheckWhiteListResponse::class.java)
                                view.renderList(generateDataListAuth(checkWhiteListResponse
                                        .checkWhiteListStatus?.data))
                            }
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            e?.let {
                                view.onErrorUpdateWhiteList(it)
                            }
                        }
                    }
                })
    }

    private fun generateDataListAuth(data: List<WhiteListData>?): List<TypeAuthenticateCreditCard> {
        val listAuth = ArrayList<TypeAuthenticateCreditCard>()
        data?.let {
            if (data.isNotEmpty()) {
                listAuth.add(initiateSingleAuthentication(data.first()))
                listAuth.add(initiateDoubleAuthentication(data.first()))
            }
        }
        return listAuth
    }

    private fun initiateSingleAuthentication(model: WhiteListData): TypeAuthenticateCreditCard {
        val singleAuthentication = TypeAuthenticateCreditCard()
        singleAuthentication.title = view.getString(R.string.payment_authentication_title_1)
        singleAuthentication.description = view.getString(R.string.payment_authentication_description_1)
        singleAuthentication.stateWhenSelected = SINGLE_AUTH_VALUE
        singleAuthentication.isSelected = model.state == SINGLE_AUTH_VALUE
        return singleAuthentication
    }

    private fun initiateDoubleAuthentication(model: WhiteListData): TypeAuthenticateCreditCard {
        val doubleAuthentication = TypeAuthenticateCreditCard()
        doubleAuthentication.title = view.getString(R.string.payment_authentication_title_2)
        doubleAuthentication.description = view.getString(R.string.payment_authentication_description_2)
        doubleAuthentication.stateWhenSelected = DOUBLE_AUTH_VALUE
        doubleAuthentication.isSelected = model?.state == DOUBLE_AUTH_VALUE
        return doubleAuthentication
    }

    override fun detachView() {
        checkUpdateWhiteListCreditCartUseCase.unSubscribe()
        super.detachView()
    }

    companion object {
        const val SINGLE_AUTH_VALUE = 1
        const val DOUBLE_AUTH_VALUE = 0
    }

}
