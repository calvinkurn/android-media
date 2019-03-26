package com.tokopedia.onboarding.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData
import com.tokopedia.onboarding.view.listener.UsernameInputContract
import rx.Subscriber
import java.util.*

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
                    convertToLowerCase(data.suggestion.suggestions)
            )
        }
    }

    private fun convertToLowerCase(sourceStrings: List<String>): List<String> {
        val lowerCaseStrings = ArrayList<String>()
        for (i in sourceStrings.indices) {
            lowerCaseStrings.add(sourceStrings[i].toLowerCase())
        }
        return lowerCaseStrings
    }
}
