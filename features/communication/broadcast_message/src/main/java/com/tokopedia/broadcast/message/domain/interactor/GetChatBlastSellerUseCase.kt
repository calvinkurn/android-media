package com.tokopedia.broadcast.message.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetChatBlastSellerUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                    @ApplicationContext val context: Context): UseCase<TopChatBlastSeller.BlastSellerList>() {

    override fun createObservable(requestParams: RequestParams?): Observable<TopChatBlastSeller.BlastSellerList> {
        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_bm_list)
        val graphqlRequest = GraphqlRequest(query, TopChatBlastSeller.Response::class.java, requestParams?.parameters, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(null).flatMap { graphqlResponse ->
            val errors: List<GraphqlError> = graphqlResponse.getError(TopChatBlastSeller.Response::class.java) ?: listOf()
            val response = graphqlResponse.getData<TopChatBlastSeller.Response>(TopChatBlastSeller.Response::class.java)

            if (errors.isEmpty()){
                Observable.just(response.result)
            } else {
                val error = errors[0].message
                if (TextUtils.isEmpty(error)){
                    Observable.just(response.result)
                } else {
                    Observable.error(MessageErrorException(error))
                }
            }
        }
    }

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_PER_PAGE = "per_page"

        fun createRequestParams(page: Int, perPage: Int) = RequestParams.create().apply {
            putInt(PARAM_PAGE, page)
            putInt(PARAM_PER_PAGE, perPage)
        }
    }
}