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

class DeleteTemplateUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var templateRepository: EditTemplateRepository

    private lateinit var templateUseCase: DeleteTemplateUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        templateUseCase = DeleteTemplateUseCase(templateRepository)
    }


    @Test
    fun `delete chat template buyer`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        every {
            templateRepository.deleteTemplate(testIndex, false)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(DeleteTemplateUseCase.generateParam(testIndex, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `delete chat template seller`() {
        val expectedResult = mockk<EditTemplateUiModel>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        every {
            templateRepository.deleteTemplate(testIndex, true)
        } returns Observable.just(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(DeleteTemplateUseCase.generateParam(testIndex, true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `error when delete chat template buyer`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        every {
            templateRepository.deleteTemplate(testIndex, false)
        } returns Observable.error(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(DeleteTemplateUseCase.generateParam(testIndex, false))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }

    @Test
    fun `error when delete chat template seller`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<EditTemplateUiModel> = TestSubscriber()
        val testIndex = 0

        every {
            templateRepository.deleteTemplate(testIndex, true)
        } returns Observable.error(expectedResult)

        val observable: Observable<EditTemplateUiModel> = templateUseCase.getExecuteObservable(DeleteTemplateUseCase.generateParam(testIndex, true))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}