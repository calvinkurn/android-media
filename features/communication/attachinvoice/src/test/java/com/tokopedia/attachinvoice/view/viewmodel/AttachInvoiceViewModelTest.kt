package com.tokopedia.attachinvoice.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.usecase.AttachInvoiceUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class AttachInvoiceViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var useCase: AttachInvoiceUseCase

    @RelaxedMockK
    lateinit var observer: Observer<Result<List<Invoice>>>

    lateinit var vm: AttachInvoiceViewModel

    private val exMsgId = "6696"
    private val exInvoiceResponse = GetInvoiceResponse()
    private val exErrorInvoiceResponse = Throwable("Dummy throwable")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        vm = AttachInvoiceViewModel(useCase, CoroutineTestDispatchersProvider)
        vm.invoices.observeForever(observer)
    }

    @Test
    fun `do nothing when messageId is empty` () {
        //GIVEN
        val emptyMessageId = ""

        //WHEN
        vm.loadInvoices(1, emptyMessageId)

        //THEN
        coVerify(exactly = 0) { useCase(any()) }
    }

    @Test
    fun `success load invoices`() {
        //GIVEN
        val expectedValue = exInvoiceResponse.invoices
        coEvery { useCase(any()) } returns exInvoiceResponse

        //WHEN
        vm.loadInvoices(1, exMsgId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verify {
            observer.onChanged(Success(expectedValue))
        }
    }

    @Test
    fun `fail load invoices` () {
        //GIVEN
        coEvery { useCase(any()) } throws exErrorInvoiceResponse

        //WHEN
        vm.loadInvoices(1, exMsgId)

        //THEN
        coVerify(exactly = 1) { useCase(any()) }
        verify {
            observer.onChanged(Fail(exErrorInvoiceResponse))
        }
    }
}