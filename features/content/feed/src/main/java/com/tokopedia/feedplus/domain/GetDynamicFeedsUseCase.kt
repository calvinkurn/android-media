package com.tokopedia.feedplus.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.R
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 18/12/18.
 */
class GetDynamicFeedsUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase)
    : UseCase<Any>() {
    override fun createObservable(requestParams: RequestParams?): Observable<Any> {
        val query: String = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.mutation_af_submit_post
        )

//        val variables = HashMap<String, Any>()
//        variables[PARAM_INPUT] = getContentSubmitInput(requestParams)
//
//        val graphqlRequest = GraphqlRequest(query, SubmitPostData::class.java, variables)
//
//        graphqlUseCase.clearRequest()
//        graphqlUseCase.addRequest(graphqlRequest)
//        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
//            val data: SubmitPostData? = it.getData(SubmitPostData::class.java)
//            if (data == null) {
//                throw RuntimeException()
//            } else if (TextUtils.isEmpty(data.feedContentSubmit.error).not()) {
//                throw MessageErrorException(data.feedContentSubmit.error)
//            }
//            data.feedContentSubmit.success == SubmitPostData.SUCCESS
//        }
        return Observable.just("1")
    }
}