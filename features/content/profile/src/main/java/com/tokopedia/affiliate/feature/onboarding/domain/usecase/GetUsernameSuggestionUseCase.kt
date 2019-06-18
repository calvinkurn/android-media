package com.tokopedia.affiliate.feature.onboarding.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion.GetUsernameSuggestionData
import com.tokopedia.profile.R
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/4/18.
 */
class GetUsernameSuggestionUseCase @Inject constructor(
        @ApplicationContext private val context: Context) : GraphqlUseCase() {

    override fun createObservable(params: RequestParams): Observable<GraphqlResponse> {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_get_username_suggestion
        )

        this.clearRequest()
        this.addRequest(GraphqlRequest(query, GetUsernameSuggestionData::class.java, false))
        return super.createObservable(params)
    }
}