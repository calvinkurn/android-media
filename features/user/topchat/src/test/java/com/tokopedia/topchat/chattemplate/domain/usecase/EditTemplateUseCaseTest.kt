package com.tokopedia.topchat.chattemplate.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class EditTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var templateRepository: EditTemplateRepository

    private lateinit var templateUseCase: EditTemplateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = EditTemplateUseCase(templateRepository)
    }

    @Test
    fun `edit chat template buyer`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0
        val testStringTemplate = "Tokopedia saja!"

        every {
            templateRepository.editTemplate(
                    testIndex,
                    EditTemplateUseCase.generateParam(testIndex, testStringTemplate, false).parameters,
                    false)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(
                EditTemplateUseCase.generateParam(testIndex, testStringTemplate, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `edit chat template seller`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0
        val testStringTemplate = "Tokopedia saja!"

        every {
            templateRepository.editTemplate(
                    testIndex,
                    EditTemplateUseCase.generateParam(testIndex, testStringTemplate, true).parameters,
                    true)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(
                EditTemplateUseCase.generateParam(testIndex, testStringTemplate, true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `error when edit chat template buyer`() {
        val expectedError = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0
        val testStringTemplate = "Tokopedia saja!"

        every {
            templateRepository.editTemplate(
                    testIndex,
                    EditTemplateUseCase.generateParam(testIndex, testStringTemplate, false).parameters,
                    false)
        } returns Observable.error(expectedError)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(
                EditTemplateUseCase.generateParam(testIndex, testStringTemplate, false)).concatWith(Observable.error(expectedError))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedError)
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun `error when edit chat template seller`() {
        val expectedError = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0
        val testStringTemplate = "Tokopedia saja!"

        every {
            templateRepository.editTemplate(
                    testIndex,
                    EditTemplateUseCase.generateParam(testIndex, testStringTemplate, true).parameters,
                    true)
        } returns Observable.error(expectedError)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(
                EditTemplateUseCase.generateParam(testIndex, testStringTemplate, true)).concatWith(Observable.error(expectedError))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedError)
        testSubscriber.assertNotCompleted()
    }
}