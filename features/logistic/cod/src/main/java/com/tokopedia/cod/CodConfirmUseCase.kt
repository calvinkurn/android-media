package com.tokopedia.cod

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cod.model.CodResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

/**
 * Created by fajarnuha on 07/01/19.
 */
class CodConfirmUseCase @Inject constructor(@ApplicationContext val context: Context): GraphqlUseCase() {

    fun getRequest(): GraphqlRequest {
        val query = GraphqlHelper
                .loadRawString(context.resources, R.raw.cod_confirmation_query)
        return GraphqlRequest(query, CodResponse::class.java)
    }

}