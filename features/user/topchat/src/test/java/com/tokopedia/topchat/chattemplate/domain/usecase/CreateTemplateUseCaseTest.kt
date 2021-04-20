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

class CreateTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var templateRepository: EditTemplateRepository

    private lateinit var templateUseCase: CreateTemplateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = CreateTemplateUseCase(templateRepository)
    }

    @Test
    fun `create chat template buyer`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString = "Tokopedia saja!"

        every {
            templateRepository.createTemplate(CreateTemplateUseCase.generateParam(testString, false).parameters)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(CreateTemplateUseCase.generateParam(testString, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `create chat template seller`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString = "Tokopedia saja!"

        every {
            templateRepository.createTemplate(CreateTemplateUseCase.generateParam(testString, true).parameters)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(CreateTemplateUseCase.generateParam(testString, true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `error when create chat template buyer`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString = "Tokopedia saja!"

        every {
            templateRepository.createTemplate(CreateTemplateUseCase.generateParam(testString, false).parameters)
        } returns Observable.error(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(CreateTemplateUseCase.generateParam(testString, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun `error when create chat template seller`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testString = "Tokopedia saja!"

        every {
            templateRepository.createTemplate(CreateTemplateUseCase.generateParam(testString, false).parameters)
        } returns Observable.error(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(CreateTemplateUseCase.generateParam(testString, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}