package com.tokopedia.changephonenumber.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository
import com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class ValidateNumberUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var changePhoneNumberRepository: ChangePhoneNumberRepository

    private lateinit var validateNumberUseCase: ValidateNumberUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        validateNumberUseCase = ValidateNumberUseCase(changePhoneNumberRepository)
    }

    @Test
    fun `Successfully Validate Number`() {
        val expectedResult = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testNumber = "123"

        every {
            changePhoneNumberRepository.validateNumber(ValidateNumberUseCase.getSubmitNumberParam(testNumber).parameters)
        } returns Observable.just(expectedResult)

        val observable: Observable<Boolean> = validateNumberUseCase.getExecuteObservable(ValidateNumberUseCase.getSubmitNumberParam(testNumber))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed Validate Otp Status`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testNumber = "123"

        every {
            changePhoneNumberRepository.validateNumber(ValidateNumberUseCase.getSubmitNumberParam(testNumber).parameters)
        } returns Observable.error(expectedResult)

        val observable: Observable<Boolean> = validateNumberUseCase.getExecuteObservable(ValidateNumberUseCase.getSubmitNumberParam(testNumber))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}