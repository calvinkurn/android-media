package com.tokopedia.loginphone.chooseaccount.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 16/04/19.
 */
class GetAccountsListUseCase @Inject
constructor(private val resources: Resources,
            private val graphqlUseCase: GraphqlUseCase) : UseCase<AccountListPojo>() {

    override fun createObservable(requestParams: RequestParams): Observable<AccountListPojo> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(resources, R.raw.query_get_accounts_list),
                AccountListPojo::class.java,
                requestParams.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: AccountListPojo? = it.getData(AccountListPojo::class.java)
            data
        }
    }

    companion object {
        const val PARAM_VALIDATE_TOKEN = "validate_token" //UUID
        const val PARAM_PHONE = "phone"
        const val PARAM_LOGIN_TYPE = "login_type"

        fun getParam(validateToken : String, phone: String)
                : RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_VALIDATE_TOKEN, validateToken)
            requestParams.putString(PARAM_PHONE, phone)
            return requestParams
        }
    }
}