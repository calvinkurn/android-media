package com.tokopedia.changephonenumber.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.changephonenumber.domain.interactor.GetWarningUseCase
import com.tokopedia.changephonenumber.domain.interactor.ValidateOtpStatusUseCase
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberWarningFragmentListener
import com.tokopedia.changephonenumber.view.presenter.ChangePhoneNumberWarningPresenter
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class ChangePhoneNumberWarningPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getWarningUseCase: GetWarningUseCase

    @RelaxedMockK
    private lateinit var getValidateOtpStatusUseCase: ValidateOtpStatusUseCase

    @RelaxedMockK
    private lateinit var view: ChangePhoneNumberWarningFragmentListener.View

    private lateinit var presenter: ChangePhoneNumberWarningPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ChangePhoneNumberWarningPresenter(getWarningUseCase, getValidateOtpStatusUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `on InitView` () {
        presenter.initView()
        verify {
            view.showLoading()
        }
    }

    @Test
    fun `Successfully get warning` () {
        val expectedReturn = mockk<WarningUIModel>(relaxed = true)
        val testSubscriber: TestSubscriber<WarningUIModel> = TestSubscriber()

        every {
            getWarningUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.getWarning()

        verify {
            getWarningUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed get warning` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<WarningUIModel> = TestSubscriber()

        every {
            getWarningUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.getWarning()

        verify {
            getWarningUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Successfully validate otp status` () {
        val expectedReturn = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testUserId = 1

        every {
            getValidateOtpStatusUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.validateOtpStatus(testUserId)

        verify {
            getValidateOtpStatusUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `Failed validate otp status` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testUserId = 1

        every {
            getValidateOtpStatusUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.validateOtpStatus(testUserId)

        verify {
            getValidateOtpStatusUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `on detach` () {
        presenter.detachView()
        verify {
            getWarningUseCase.unsubscribe()
            getValidateOtpStatusUseCase.unsubscribe()
        }
    }
}