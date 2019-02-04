package com.tokopedia.cod.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cod.R
import com.tokopedia.cod.model.CodResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by fajarnuha on 07/01/19.
 */
class CodConfirmUseCase
@Inject constructor(@ApplicationContext val context: Context): GraphqlUseCase() {

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        clearRequest()
        addRequest(getRequest())
        super.execute(requestParams, subscriber)
    }

    fun getRequest(): GraphqlRequest {
        val query = GraphqlHelper
                .loadRawString(context.resources, R.raw.cod_confirmation_query)
        return GraphqlRequest(query, CodResponse::class.java)
    }

}