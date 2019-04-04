package com.tokopedia.affiliate.feature.onboarding.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername.RegisterUsernameData
import com.tokopedia.profile.R
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject

/**
 * @author by milhamj on 10/7/18.
 */
class RegisterUsernameUseCase @Inject constructor(
        @ApplicationContext private val context: Context) : GraphqlUseCase() {

    override fun createObservable(params: RequestParams): Observable<GraphqlResponse> {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.mutation_af_register_username
        )

        this.clearRequest()
        this.addRequest(GraphqlRequest(
                query,
                RegisterUsernameData::class.java,
                params.parameters,
                false)
        )
        return super.createObservable(params)
    }

    companion object {
        private const val AFFILIATE_NAME = "affiliateName"

        fun createRequestParams(username: String): RequestParams {
            val param = RequestParams.create()
            param.putString(AFFILIATE_NAME, username)
            return param
        }
    }
}
