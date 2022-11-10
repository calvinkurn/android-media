package com.tokopedia.travel.country_code.domain

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 23/12/2019
 */
class TravelCountryCodeByIdUseCase @Inject constructor(private val useCase: TravelCountryCodeUseCase) {

    suspend fun execute(rawQuery: GqlQueryInterface, countryId: String): Result<TravelCountryPhoneCode> =
            when (val result = useCase.execute(rawQuery)) {
                is Success -> {
                    Success(result.data.firstOrNull { it.countryId == countryId } ?: TravelCountryPhoneCode())
                }
                is Fail -> {
                    result
                }
            }

}