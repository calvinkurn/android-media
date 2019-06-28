package com.tokopedia.loginphone.choosetokocashaccount.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.loginphone.R
import com.tokopedia.loginphone.choosetokocashaccount.data.AccountList
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 16/04/19.
 */
class GetAccountsListUseCase @Inject
constructor(private val resources: Resources,
            private val graphqlUseCase: GraphqlUseCase) : UseCase<AccountList>() {

    override fun createObservable(requestParams: RequestParams): Observable<AccountList> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(resources, R.raw.query_get_accounts_list),
                AccountList::class.java,
                requestParams.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: AccountList? = it.getData(AccountList::class.java)
            data
        }
    }

    companion object {
        const val PARAM_VALIDATE_TOKEN = "validate_token" //UUID
        const val PARAM_PHONE = "phone"

        fun getParam(validateToken : String, phone: String)
                : RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_VALIDATE_TOKEN, validateToken)
            requestParams.putString(PARAM_PHONE, phone)
            return requestParams
        }
    }
}