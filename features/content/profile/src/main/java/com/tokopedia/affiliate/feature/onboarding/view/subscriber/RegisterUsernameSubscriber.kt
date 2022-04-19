package com.tokopedia.affiliate.feature.onboarding.view.subscriber

import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.utils.ErrorHandler
import rx.Subscriber

/**
 * @author by milhamj on 10/7/18.
 */
class RegisterUsernameSubscriber(private val view: UsernameInputContract.View) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        view.hideLoading()
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        view.onErrorRegisterUsername(ErrorHandler.getErrorMessage(view.getContext(), e))
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        view.hideLoading()
        val data = graphqlResponse.getData<RegisterUsernameData>(RegisterUsernameData::class.java)

        if ((data.bymeRegisterAffiliateName.error.message.isNotEmpty())) {
            if (ERROR_VALIDATION.equals(data.bymeRegisterAffiliateName.error.type, ignoreCase = true)) {
                view.onErrorInputRegisterUsername(
                        data.bymeRegisterAffiliateName.error.message
                )
            } else {
                view.onErrorRegisterUsername(
                        data.bymeRegisterAffiliateName.error.message
                )
            }
            return
        }

        if (!data.bymeRegisterAffiliateName.isSuccess) {
            throw RuntimeException()
        } else {
            view.onSuccessRegisterUsername()
        }
    }

    companion object {
        const val ERROR_VALIDATION = "validation"
    }
}