package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class SetAvailabilityTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var templateRepository: TemplateRepository

    private lateinit var templateUseCase: SetAvailabilityTemplateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = SetAvailabilityTemplateUseCase(templateRepository)
    }

    @Test
    fun `set availability true template buyer` () {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = true
            isSuccess = true
        }
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, true, false)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.just(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `set availability false template buyer` () {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = false
            isSuccess = true
        }
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, false, false)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.just(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `set availability true template seller` () {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = true
            isSuccess = true
        }
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, true, true)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.just(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `set availability false template seller` () {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = false
            isSuccess = true
        }
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, false, true)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.just(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `error when set availability template buyer` () {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, true, false)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.error(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun `error when set availability template seller` () {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()
        val requestParams = SetAvailabilityTemplateUseCase.generateParam(null, true, true)

        every {
            templateRepository.setAvailabilityTemplate(any(), any())
        } returns Observable.error(expectedResult)

        val observable: Observable<GetTemplateUiModel> = templateUseCase.getExecuteObservable(requestParams)

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}