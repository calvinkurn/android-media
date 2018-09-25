package com.tokopedia.flashsale.management.domain

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.Campaign
import com.tokopedia.flashsale.management.data.Data
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetCampaignListUsecase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<Data>() {

    override fun createObservable(requestParams: RequestParams): Observable<Data> {
//        val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_campaign_list)
//        val graphqlRequest = GraphqlRequest(query, Data::class.java, requestParams.parameters)
//        graphqlUseCase.clearRequest()
//        graphqlUseCase.addRequest(graphqlRequest)
//        return graphqlUseCase.createObservable(RequestParams.EMPTY).flatMap { graphqlResponse ->
//            val data = graphqlResponse.getData<Data>(Data::class.java)
//            val graphqlErrorList = graphqlResponse.getError(Data::class.java)
//            if (graphqlErrorList != null && graphqlErrorList.size > 0) {
//                val graphqlError = graphqlErrorList[0]
//                val errorMessage = graphqlError.message
//                if (TextUtils.isEmpty(errorMessage)) {
//                    Observable.just(data)
//                } else {
//                    Observable.error(MessageErrorException(errorMessage))
//                }
//            } else {
//                Observable.just(data)
//            }
//        }

        val query = GraphqlHelper.loadRawString(context.resources, R.raw.dummy_data_campaign)
        return Observable.just(Gson().fromJson(query, Data::class.java))
    }

    override fun unsubscribe() {
        graphqlUseCase.unsubscribe()
        super.unsubscribe()
    }

    companion object {
        private const val PARAM_ALL = "all"
        private const val PARAM_OFFSET = "offset"
        private const val PARAM_ROWS = "rows"
        private const val PARAM_CAMPAIGN_TYPE = "campaign_type"
        private const val PARAM_QUERY = "q"
        private const val PARAM_STATUS = "status"

        fun createRequestParams(all: String, offset: Int, rows: Int, campaign_type: Int, q: String, status: String): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_ALL, all)
                putInt(PARAM_OFFSET, offset)
                putInt(PARAM_ROWS, rows)
                putInt(PARAM_CAMPAIGN_TYPE, campaign_type)
                putString(PARAM_QUERY, q)
                putString(PARAM_STATUS, status)
            }
        }
    }

}