package com.tokopedia.profilecompletion.addname.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 23/04/19.
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
                    val registerInfo = graphqlResponse
                            .getData<RegisterInfo>(RegisterInfo::class.java)

                    if (registerInfo.errors.isEmpty()) {
                        view.onSuccessRegister(registerInfo)
                    } else {
                        view.onErrorRegister(Throwable(registerInfo.errors[0].message))
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