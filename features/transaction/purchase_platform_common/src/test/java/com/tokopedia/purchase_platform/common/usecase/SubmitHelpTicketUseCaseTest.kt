package com.tokopedia.purchase_platform.common.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitHelpTicketGqlResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitHelpTicketResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.data.response.SubmitTicketDataResponse
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.lang.reflect.Type

class SubmitHelpTicketUseCaseTest {

    val graphqlUseCase: GraphqlUseCase = mockk(relaxed = true)
    val param = RequestParams.create().apply {
        putObject(SubmitHelpTicketUseCase.PARAM, SubmitHelpTicketRequest())
    }

    @Before
    fun setup() {
        RxAndroidPlugins.getInstance().reset()
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.trampoline()
            }
        })
        RxJavaHooks.setOnIOScheduler { Schedulers.trampoline() }
    }

    @Test
    fun `Success Submit Ticket`() {
        val useCase = SubmitHelpTicketUseCase("", graphqlUseCase)
        val successMessage = "success"

        // Given
        val result = HashMap<Type, Any>()
        val objectType = SubmitHelpTicketGqlResponse::class.java
        result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = SubmitHelpTicketUseCase.STATUS_OK, data = SubmitTicketDataResponse(message = arrayListOf(successMessage))))
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                result, null, false))

        // When
        val subscriber = useCase.createObservable(param).test()

        // Then
        subscriber.assertValue(SubmitTicketResult(status = true, message = successMessage))
    }

    @Test
    fun `Fail Submit Ticket`() {
        val useCase = SubmitHelpTicketUseCase("", graphqlUseCase)
        val errorMessage = "failure"

        // Given
        val result = HashMap<Type, Any>()
        val objectType = SubmitHelpTicketGqlResponse::class.java
        result[objectType] = SubmitHelpTicketGqlResponse(SubmitHelpTicketResponse(status = "ERROR", errorMessages = arrayListOf(errorMessage)))
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(
                result, null, false))

        // When
        val subscriber = useCase.createObservable(param).test()

        // Then
        subscriber.assertValue(SubmitTicketResult(status = false, message = errorMessage))
    }
}