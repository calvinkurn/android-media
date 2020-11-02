package com.tokopedia.changephonenumber.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.changephonenumber.domain.interactor.ValidateNumberUseCase
import com.tokopedia.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener
import com.tokopedia.changephonenumber.view.presenter.ChangePhoneNumberInputPresenter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class ChangePhoneNumberInputPresenter {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var validateNumberUseCase: ValidateNumberUseCase

    @RelaxedMockK
    private lateinit var view: ChangePhoneNumberInputFragmentListener.View

    private lateinit var presenter: ChangePhoneNumberInputPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ChangePhoneNumberInputPresenter(validateNumberUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `on New Number Text Changed Valid` () {
        val testSelection = 0
        val testNewNumber = "08123456789"

        presenter.onNewNumberTextChanged(testNewNumber, testSelection)

        verify {
            view.enableNextButton()
        }
    }

    @Test
    fun `on New Number Text Changed Valid with Correction less than zero` () {
        val testSelection = 0
        val testUnCorrectNewNumber = "08123-456789"
        val testNewNumber = "08123456789"

        presenter.onNewNumberTextChanged(testUnCorrectNewNumber, testSelection)

        verify {
            view.enableNextButton()
            view.correctPhoneNumber(testNewNumber, testSelection)
        }
    }

    @Test
    fun `on New Number Text Changed Valid with Correction Higher than phone number length` () {
        val testSelection = 12
        val testUnCorrectNewNumber = "08123-456789"
        val testNewNumber = "08123456789"

        presenter.onNewNumberTextChanged(testUnCorrectNewNumber, testSelection)

        verify {
            view.enableNextButton()
            view.correctPhoneNumber(testNewNumber, testNewNumber.length)
        }
    }

    @Test
    fun `on New Number Text Changed Valid with Correction other` () {
        val testSelection = 2
        val testUnCorrectNewNumber = "08123-456789"
        val testNewNumber = "08123456789"
        val lengthDifferentTest = testUnCorrectNewNumber.length - testNewNumber.length

        presenter.onNewNumberTextChanged(testUnCorrectNewNumber, testSelection)

        verify {
            view.enableNextButton()
            view.correctPhoneNumber(testNewNumber, lengthDifferentTest)
        }
    }

    @Test
    fun `on New Number Text Changed Invalid too short` () {
        val testSelection = 0
        val testNewNumber = "123"

        presenter.onNewNumberTextChanged(testNewNumber, testSelection)

        verify {
            view.disableNextButton()
        }
    }

    @Test
    fun `on New Number Text Changed Invalid too long` () {
        val testSelection = 0
        val testNewNumber = "123123123123123123123123123123"

        presenter.onNewNumberTextChanged(testNewNumber, testSelection)

        verify {
            view.disableNextButton()
        }
    }

    @Test
    fun `successfully Validate Number` () {
        val expectedReturn = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testPhoneNumber = "08123456789"

        every {
            validateNumberUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.validateNumber(testPhoneNumber)

        verify {
            validateNumberUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `failed Validate Number` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testPhoneNumber = "08123456789"

        every {
            validateNumberUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.validateNumber(testPhoneNumber)

        verify {
            validateNumberUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `successfully Submit Number` () {
        val expectedReturn = mockk<Boolean>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testPhoneNumber = "08123456789"

        every {
            validateNumberUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.submitNumber(testPhoneNumber)

        verify {
            validateNumberUseCase.execute(any(), any())
        }

        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `failed Submit Number` () {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val testSubscriber: TestSubscriber<Boolean> = TestSubscriber()
        val testPhoneNumber = "08123456789"

        every {
            validateNumberUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.submitNumber(testPhoneNumber)

        verify {
            validateNumberUseCase.execute(any(), any())
        }

        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `on detach` () {
        presenter.detachView()
        verify {
            validateNumberUseCase.unsubscribe()
        }
    }
}