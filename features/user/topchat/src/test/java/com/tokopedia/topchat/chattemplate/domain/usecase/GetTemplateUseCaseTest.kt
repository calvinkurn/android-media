package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

class GetTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var templateRepository: TemplateRepository

    private lateinit var getTemplateUseCase: GetTemplateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        getTemplateUseCase = GetTemplateUseCase(templateRepository)
    }

    @Test
    fun `get template buyer data`() {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = true
            isSuccess = true
        }

        every {
            templateRepository.getTemplate(GetTemplateUseCase.generateParam(false).parameters)
        } returns Observable.just(expectedResult)

        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()

        val observable: Observable<GetTemplateUiModel> = getTemplateUseCase.getExecuteObservable(GetTemplateUseCase.generateParam(false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `get template seller data`() {
        val expectedResult = mockk<GetTemplateUiModel>(relaxed = true).apply {
            isEnabled = true
            isSuccess = true
        }

        every {
            templateRepository.getTemplate(GetTemplateUseCase.generateParam(true).parameters)
        } returns Observable.just(expectedResult)

        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()

        val observable: Observable<GetTemplateUiModel> = getTemplateUseCase.getExecuteObservable(GetTemplateUseCase.generateParam(true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValue(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `get error template buyer data`() {
        val expectedError = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()

        every {
            templateRepository.getTemplate(GetTemplateUseCase.generateParam(false).parameters)
        } returns Observable.error(expectedError)

        val observable: Observable<GetTemplateUiModel> = getTemplateUseCase.getExecuteObservable(GetTemplateUseCase.generateParam(false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedError)
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun `get error template seller data`() {
        val expectedError = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<GetTemplateUiModel> = TestSubscriber()

        every {
            templateRepository.getTemplate(GetTemplateUseCase.generateParam(true).parameters)
        } returns Observable.error(expectedError)

        val observable: Observable<GetTemplateUiModel> = getTemplateUseCase.getExecuteObservable(GetTemplateUseCase.generateParam(true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedError)
        testSubscriber.assertNotCompleted()
    }
}