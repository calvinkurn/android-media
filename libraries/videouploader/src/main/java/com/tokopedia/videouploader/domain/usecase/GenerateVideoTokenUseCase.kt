package com.tokopedia.videouploader.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject
import com.tokopedia.videouploader.R
import com.tokopedia.videouploader.domain.pojo.GenerateTokenPojo
import rx.Observable

/**
 * @author by nisie on 15/03/19.
 */
class GenerateVideoTokenUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_get_upload_video_token)
        val graphqlRequest = GraphqlRequest(query,
                GenerateTokenPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun getExecuteObservable(): Observable<GraphqlResponse> {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_get_upload_video_token)
        val graphqlRequest = GraphqlRequest(query,
                GenerateTokenPojo::class.java, HashMap<String,Any>())

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.create())
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}