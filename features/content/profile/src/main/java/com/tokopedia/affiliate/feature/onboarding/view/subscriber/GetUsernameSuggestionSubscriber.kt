package com.tokopedia.affiliate.feature.onboarding.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * @author by milhamj on 10/4/18.
 */
class GetUsernameSuggestionSubscriber(
        private val view: UsernameInputContract.View?)
    : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
        view?.hideLoading()
    }

    override fun onNext(graphqlResponse: GraphqlResponse) {
        view?.hideLoading()
        val data = graphqlResponse.getData<GetUsernameSuggestionData>(GetUsernameSuggestionData::class.java)
        if (data?.suggestion != null) {
            view?.onSuccessGetUsernameSuggestion(
                    data.suggestion.suggestions.map { it.toLowerCase() }
            )
        }
    }
}
