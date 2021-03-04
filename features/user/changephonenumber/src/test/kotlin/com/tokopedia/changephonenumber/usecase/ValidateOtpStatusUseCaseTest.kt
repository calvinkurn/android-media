package com.tokopedia.changephonenumber.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository
import com.tokopedia.changephonenumber.domain.interactor.ValidateOtpStatusUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

class ValidateOtpStatusUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var changePhoneNumberRepository: ChangePhoneNumberRepository

    private lateinit var validateOtpStatusUseCase: ValidateOtpStatusUseCase

    @Before
    fun before() {
        MockKAnnotations.init(this)
        validateOtpStatusUseCase = ValidateOtpStatusUseCase(changePhoneNumberRepository)
    }

    @Test
    fun `Successfully Validate Otp Status`() {
        val expectedResult = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testUserId = 1
        val testOtpType = 1

        every {
            changePhoneNumberRepository.validateOtpStatus(ValidateOtpStatusUseCase.getValidateOtpParam(testUserId, testOtpType).parameters)
        } returns Observable.just(expectedResult)

        val observable: Observable<Boolean> = validateOtpStatusUseCase.getExecuteObservable(ValidateOtpStatusUseCase.getValidateOtpParam(testUserId, testOtpType))

        observable.subscribe(testSubscriber)

        testSubscriber.assertValues(expectedResult)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed Validate Otp Status`() {
        val expectedResult = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testUserId = 1
        val testOtpType = 1

        every {
            changePhoneNumberRepository.validateOtpStatus(ValidateOtpStatusUseCase.getValidateOtpParam(testUserId, testOtpType).parameters)
        } returns Observable.error(expectedResult)

        val observable: Observable<Boolean> = validateOtpStatusUseCase.getExecuteObservable(ValidateOtpStatusUseCase.getValidateOtpParam(testUserId, testOtpType))

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(expectedResult)
        testSubscriber.assertNotCompleted()
    }
}