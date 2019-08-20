package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticaddaddress.di.RawQueryConstant
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.model.AddressResponse
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.internal.schedulers.TrampolineScheduler
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers

class GetDistrictRecommendationTest {

    lateinit var usecaseUt: GetDistrictRecommendation
    val gqlUsecaseMock: GraphqlUseCase = mockk(relaxed = true)
    val mapper: DistrictRecommendationMapper = DistrictRecommendationMapper()
    val queryMapMock: Map<String, String> = mockk()
    val testSchedProvider = object: SchedulerProvider {
        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun ui(): Scheduler {
            return Schedulers.trampoline()
        }
    }

    @Before
    fun setup() {
        every { queryMapMock.get(RawQueryConstant.GET_DISTRICT_RECOMMENDATION) } answers { queryTest }
        usecaseUt = GetDistrictRecommendation(queryMapMock, gqlUsecaseMock, mapper, testSchedProvider)
    }

    @Test
    fun executeSuccessResponse_returnObjectResponse() {
        val subscriber = TestSubscriber<AddressResponse>()
        every { gqlUsecaseMock.getExecuteObservable(null) } answers {
            Observable.just(DiscomDummyProvider.getSuccessGqlResponse())
        }

        val actual = usecaseUt.execute("jak", 1)
        actual.subscribe(subscriber)

        subscriber.assertCompleted()
        subscriber.assertNoErrors()
        assertTrue(subscriber.onNextEvents[0].addresses.size > 0)
    }

}

val queryTest = """
query GetDistrictRecommendation(${"$"}query: String, ${"$"}page: String){
  kero_district_recommendation(query: ${"$"}query, page: ${"$"}page) {
    district {
      district_id
      district_name
      city_id
      city_name
      province_id
      province_name
      zip_code
    }
    next_available
  }
}
""".trimIndent()