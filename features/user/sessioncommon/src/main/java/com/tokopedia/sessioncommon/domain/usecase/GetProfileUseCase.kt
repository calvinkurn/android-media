package com.tokopedia.sessioncommon.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.R
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 12/06/19.
 */

open class GetProfileUseCase @Inject constructor(val resources: Resources, val graphqlUseCase: GraphqlUseCase
) {

    open fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_profile)
        val graphqlRequest = GraphqlRequest(query,
                ProfilePojo::class.java, RequestParams.create().parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}