package com.tokopedia.logisticaddaddress.domain.usecase

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticaddaddress.di.RawQueryConstant
import com.tokopedia.logisticaddaddress.domain.executor.SchedulerProvider
import com.tokopedia.logisticaddaddress.domain.model.district_recommendation.DistrictRecommendationResponse
import com.tokopedia.logisticaddaddress.helper.DiscomDummyProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers

class GetDistrictRecommendationTest {

    lateinit var usecaseUt: GetDistrictRecommendation
    val gqlUsecaseMock: GraphqlUseCase = mockk(relaxed = true)
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
        every { queryMapMock.get(RawQueryConstant.GET_DISTRICT_RECOMMENDATION) } answers { KeroLogisticQuery.district_recommendation }
        usecaseUt = GetDistrictRecommendation(queryMapMock, gqlUsecaseMock, testSchedProvider)
    }

    @Test
    fun `execute district response success return object response`() {
        val subscriber = TestSubscriber<DistrictRecommendationResponse>()

        every { gqlUsecaseMock.getExecuteObservable(null)
        } answers {
            Observable.just(DiscomDummyProvider.getSuccessGqlResponse())
        }

        usecaseUt.execute("jak", 1).subscribe(subscriber)

        subscriber.assertCompleted()
        subscriber.assertNoErrors()
        Assert.assertTrue(subscriber.onNextEvents[0].keroDistrictRecommendation.district.isNotEmpty())
    }
}

