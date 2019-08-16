package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetMoneyInAddressUseCase
@Inject constructor(private val context: Context,
                    private val graphqlUseCase: GraphqlUseCase) : UseCase<MoneyInKeroGetAddressResponse.Data.KeroGetAddress>() {

    override fun createObservable(requestParams: RequestParams): Observable<MoneyInKeroGetAddressResponse.Data.KeroGetAddress> {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_kero_get_address), MoneyInKeroGetAddressResponse::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(MoneyInKeroGetAddressResponse.Data::class.java) as MoneyInKeroGetAddressResponse.Data)
                    .keroGetAddress
        }
    }
}