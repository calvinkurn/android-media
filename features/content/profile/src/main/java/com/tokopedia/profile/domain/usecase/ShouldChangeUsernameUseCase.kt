package com.tokopedia.profile.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.R
import com.tokopedia.profile.data.pojo.shouldchangeusername.ShouldChangeUsernameResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 26/03/19.
 */
class ShouldChangeUsernameUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {
    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_is_username_changed
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(GraphqlRequest(
                query,
                ShouldChangeUsernameResponse::class.java,
                requestParams.parameters,
                false
        ))
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val response = it.getData<ShouldChangeUsernameResponse>(ShouldChangeUsernameResponse::class.java)
                response.bymeIsUsernameChanged.state.not()
        }
    }

    companion object {
        private const val USER_ID = "userID"
        fun createRequestParams(userId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(USER_ID, userId)
            return requestParams
        }
    }
}