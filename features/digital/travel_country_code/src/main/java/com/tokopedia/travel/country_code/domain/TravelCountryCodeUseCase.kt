package com.tokopedia.travel.country_code.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel.country_code.data.TravelPhoneCodeEntity
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 23/12/2019
 */
class TravelCountryCodeUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(rawQuery: String): Result<List<TravelCountryPhoneCode>> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).setExpiryTime(TimeUnit.DAYS.toMillis(EXPIRED_CACHE_DAYS)).build())
        useCase.clearRequest()
        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, TravelPhoneCodeEntity.Response::class.java)
            useCase.addRequest(graphqlRequest)
            val countryListData = useCase.executeOnBackground().getSuccessData<TravelPhoneCodeEntity.Response>()
            val countryListModel = countryListData.travelGetAllCountries.countries.map {
                TravelCountryPhoneCode(it.id, it.attributes.name, it.attributes.phoneCode)
            }
            Success(countryListModel.sortedBy { it.countryName })
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    companion object {
        const val EXPIRED_CACHE_DAYS: Long = 30
    }

}