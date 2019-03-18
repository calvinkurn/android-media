package com.tokopedia.travelcalendar.domain

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.data.TravelCalendarRepository
import com.tokopedia.travelcalendar.data.entity.HolidayEntity
import com.tokopedia.travelcalendar.view.model.HolidayResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 12/12/18.
 */

class GetHolidayUseCase @Inject constructor(@param:ApplicationContext private val context: Context,
                                            private val graphqlUseCase: GraphqlUseCase,
                                            private val repository: TravelCalendarRepository)
    : UseCase<List<@JvmSuppressWildcards HolidayResult>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<HolidayResult>> {
        return repository.getHolidayResultsLocal()
                .onErrorResumeNext(Func1<Throwable, Observable<List<HolidayResult>>> {
                    return@Func1 Observable.just(it)
                            .flatMap(Func1<Throwable, Observable<GraphqlResponse>> {
                                val query = GraphqlHelper.loadRawString(context.resources, R.raw.holiday_calendar_query)
                                if (!TextUtils.isEmpty(query)) {
                                    graphqlUseCase.clearRequest()
                                    graphqlUseCase.addRequest(GraphqlRequest(query, HolidayEntity::class.java, false))
                                    return@Func1 graphqlUseCase.createObservable(null)
                                }
                                Observable.error(Exception("Query variable are empty"))
                            })
                            .map(Func1<GraphqlResponse, HolidayEntity> { graphqlResponse ->
                                graphqlResponse.getData(HolidayEntity::class.java)
                            })
                            .flatMap(Func1<HolidayEntity, Observable<List<HolidayResult>>> {
                                return@Func1 repository.getHolidayResults(Observable.just(it))
                            })
                })

    }
}
