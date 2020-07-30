package com.tokopedia.user_identification_common.domain.usecase

//
// Created by Yoris Prayogo on 2019-10-29.
//

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.R
import com.tokopedia.user_identification_common.domain.pojo.CheckKtpStatusPojo
import rx.Subscriber
import javax.inject.Inject

class GetKtpStatusUseCase @Inject
constructor(private val resources: Resources, private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: RequestParams, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw
                .query_is_ktp)

        val graphqlRequest = GraphqlRequest(query,
                CheckKtpStatusPojo::class.java, requestParams.parameters, false)

        graphqlUseCase.apply {
            clearRequest()
            addRequest(graphqlRequest)
            execute(subscriber)
        }
    }

    internal fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun getRequestParam(img: String): RequestParams {
        return RequestParams.create().apply {
            putString(IMAGE, img)
            putString(IDENTIFIER, "")
            putString(SOURCE, "kyc")
        }
    }

    companion object {
        private val IMAGE = "image"
        private val IDENTIFIER = "id"
        private val SOURCE = "src"

        const val MAX_WIDTH = 500
        const val MAX_HEIGHT = 500
    }

}
