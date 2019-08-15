package com.tokopedia.profilecompletion.addname.presenter

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 23/04/19.
 * Don't forget to remove basic token after success / fail
 */
class AddNamePresenter @Inject constructor(val registerUseCase: RegisterUseCase)
    : BaseDaggerPresenter<AddNameListener.View>(), AddNameListener.Presenter {

    override fun registerPhoneNumberAndName(name: String, phoneNumber: String) {
        view.showLoading()
        registerUseCase.execute(
                RegisterUseCase.generateParamRegisterPhone(name, phoneNumber), object :
                Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse?) {
                graphqlResponse?.run {
                    val registerPojo = graphqlResponse
                            .getData<RegisterPojo>(RegisterPojo::class.java)
                    val registerInfo = registerPojo.register

                    if (registerInfo.errors.isEmpty()) {
                        view.onSuccessRegister(registerInfo)
                    } else {
                        view.onErrorRegister(MessageErrorException(registerInfo.errors[0].message))
                    }
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (e != null) {
                    view.onErrorRegister(e)
                }
            }
        }

        )
    }

}