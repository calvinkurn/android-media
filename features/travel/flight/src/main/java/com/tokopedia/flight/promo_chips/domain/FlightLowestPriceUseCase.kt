package com.tokopedia.flight.promo_chips.domain

import com.tokopedia.flight.promo_chips.data.model.FlightLowestPrice
import com.tokopedia.flight.promo_chips.data.model.FlightLowestPriceArgs
import com.tokopedia.flight.search.presentation.viewmodel.FlightSearchViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * author by astidhiyaa on 12/03/2021
 */

class FlightLowestPriceUseCase @Inject constructor(val graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<FlightLowestPrice.Response>(graphqlRepository) {
    suspend fun execute(rawQuery: String, dataParam: FlightLowestPriceArgs): Result<FlightLowestPrice> {
        return try {
            this.setTypeClass(FlightLowestPrice.Response::class.java)
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(mapOf(FlightSearchViewModel.PARAM_PROMO_CHIPS to dataParam))

            val data = this.executeOnBackground()
            Success(data.response)
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}