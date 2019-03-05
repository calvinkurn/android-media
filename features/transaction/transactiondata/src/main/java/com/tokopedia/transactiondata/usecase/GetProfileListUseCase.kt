package com.tokopedia.transactiondata.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.transactiondata.R
import com.tokopedia.transactiondata.entity.response.expresscheckout.profile.ProfileListGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

class GetProfileListUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.query_profile_list), ProfileListGqlResponse::class.java, null, false)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

}