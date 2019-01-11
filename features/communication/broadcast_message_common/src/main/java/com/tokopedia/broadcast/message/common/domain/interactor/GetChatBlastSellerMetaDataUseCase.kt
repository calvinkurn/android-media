package com.tokopedia.broadcast.message.common.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData
import com.tokopedia.broadcast.message.common.R
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetChatBlastSellerMetaDataUseCase (private val graphqlUseCase: GraphqlUseCase,
                                                            val context: Context):
        UseCase<TopChatBlastSellerMetaData>() {

    override fun createObservable(requestParams: RequestParams): Observable<TopChatBlastSellerMetaData> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_metadata)
        val graphqlRequest = GraphqlRequest(query, TopChatBlastSellerMetaData.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { graphqlResponse ->
            val errors: List<GraphqlError> = graphqlResponse.getError(TopChatBlastSellerMetaData.Response::class.java) ?: listOf()
            val response = graphqlResponse.getData<TopChatBlastSellerMetaData.Response>(TopChatBlastSellerMetaData.Response::class.java).result

            if (errors.isEmpty()){
                Observable.just(response)
            } else {
                val error = errors[0].message
                if (TextUtils.isEmpty(error)){
                    Observable.just(response)
                } else {
                    Observable.error(MessageErrorException(error))
                }
            }
        }
    }
}