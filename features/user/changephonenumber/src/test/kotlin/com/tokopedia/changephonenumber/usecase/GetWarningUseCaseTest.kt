package com.tokopedia.changephonenumber.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository
import com.tokopedia.changephonenumber.domain.interactor.GetWarningUseCase
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class GetWarningUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var changePhoneNumberRepository: ChangePhoneNumberRepository

    private lateinit var warningUsecase: GetWarningUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        warningUsecase = GetWarningUseCase(changePhoneNumberRepository)
    }

    @Test
    fun `Successfully Get Warning`() {
        val expectedResult = mockk<WarningUIModel>(relaxed = true)
        val testSubscriber: TestSubscriber<WarningUIModel> = TestSubscriber()

        every {
            changePhoneNumberRepository.getWarning(GetWarningUseCase.getGetWarningParam().parameters)
        } returns Observable.just(expectedResult)

        val observable: Observable<WarningUIModel> = warningUsecase.getExecuteObservable(GetWarningUseCase.getGetWarningParam())

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed Get Warning`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<WarningUIModel> = TestSubscriber()

        every {
            changePhoneNumberRepository.getWarning(GetWarningUseCase.getGetWarningParam().parameters)
        } returns Observable.error(expectedResult)

        val observable: Observable<WarningUIModel> = warningUsecase.getExecuteObservable(GetWarningUseCase.getGetWarningParam())

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}