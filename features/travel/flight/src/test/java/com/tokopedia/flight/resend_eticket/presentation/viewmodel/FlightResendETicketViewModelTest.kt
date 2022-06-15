package com.tokopedia.flight.resend_eticket.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.util.PatternsCompat
import com.tokopedia.flight.resend_eticket.domain.FlightOrderResendEmailUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 13/05/2022
 */
class FlightResendETicketViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val resendEmailUseCase: FlightOrderResendEmailUseCase = mockk()

    private lateinit var viewModel : FlightResendETicketViewModel

    @Before
    fun setUp() {
        viewModel = FlightResendETicketViewModel(
            resendEmailUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun isEmailValid_whenEmailNull_ShouldNotValid() {
        assert(viewModel.userEmail == "")
        assert(viewModel.invoiceId == "")

        // given
        val email : String?= null

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "1")
    }

    @Test
    fun isEmailValid_whenEmailEmpty_ShouldNotValid() {
        // given
        val email : String = ""

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "1")
    }

    @Test
    fun isEmailValid_whenEmailWrongFormat_ShouldNotValid() {
        // given
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns false
        val email : String = "asdfasf@asdfasdf"

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "2")
    }

    @Test
    fun isEmailValid_whenEmailWrongHaveDotAt_ShouldNotValid() {
        // given
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns true
        val email : String = "asdfasf.@asdfasdf.com"

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "2")
    }

    @Test
    fun isEmailValid_whenEmailWrongHaveAtDot_ShouldNotValid() {
        // given
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns true
        val email = "asdfasf@.asdf@asdf.com"

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "2")
    }

    @Test
    fun isEmailValid_whenEmailContainsPlus_ShouldNotValid() {
        // given
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns true
        val email : String = "furqan+1@gmail.com"

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(!result)
        assert(viewModel.emailValidation.value is Fail)
        assert((viewModel.emailValidation.value as Fail).throwable.message == "3")
    }

    @Test
    fun isEmailValid_whenEmailCorrect_ShouldBeValid() {
        // given
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(any()).matches() } returns true
        val email : String = "furqan@gmail.com"

        // when
        val result = viewModel.isValidEmailInput(email)

        // then
        assert(result)
        assert(viewModel.emailValidation.value is Success)
        assert((viewModel.emailValidation.value as Success).data)
    }


}