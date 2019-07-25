package com.tokopedia.common.travel.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.data.TravelCalendarHolidayRepository
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jessica on 03/07/19.
 */

class TravelCalendarHolidayUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase,
                                                       @Named("travel_calendar_holiday_query")
                                                       val travelCalendarHolidayQuery: String,
                                                       val repository: TravelCalendarHolidayRepository) {

    suspend fun execute(): Result<TravelCalendarHoliday.HolidayData> {

        useCase.clearRequest()

        val localHoliday = repository.getHolidayResultsLocal()
        return if (localHoliday == null || localHoliday.data.isEmpty()) {
            try {
                val graphqlRequest = GraphqlRequest(travelCalendarHolidayQuery, TravelCalendarHoliday.Response::class.java, false)
                useCase.addRequest(graphqlRequest)
                val hotelCalendarHoliday = useCase.executeOnBackground().getSuccessData<TravelCalendarHoliday.Response>().response
                repository.saveHolidayResults(hotelCalendarHoliday)
                Success(hotelCalendarHoliday)
            } catch (throwable: Throwable) {
                Fail(throwable)
            }
        } else Success(localHoliday)
    }
}