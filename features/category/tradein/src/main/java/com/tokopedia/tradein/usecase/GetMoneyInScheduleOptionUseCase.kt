package com.tokopedia.tradein.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tradein.R
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetMoneyInScheduleOptionUseCase @Inject constructor(private val context: Context,
                                                          private val graphqlUseCase: GraphqlUseCase) : UseCase<MoneyInScheduleOptionResponse.Data.GetPickupScheduleOption>(){

    override fun createObservable(requestParams: RequestParams): Observable<MoneyInScheduleOptionResponse.Data.GetPickupScheduleOption> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_get_pickup_schedule_option), MoneyInScheduleOptionResponse::class.java, requestParams.parameters, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(MoneyInScheduleOptionResponse.Data::class.java) as MoneyInScheduleOptionResponse.Data)
                    .getPickupScheduleOption
        }

    }
}