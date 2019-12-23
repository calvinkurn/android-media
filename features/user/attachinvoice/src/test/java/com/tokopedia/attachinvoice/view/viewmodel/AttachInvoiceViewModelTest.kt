package com.tokopedia.attachinvoice.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.attachinvoice.InstantTaskExecutorRuleSpek
import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import org.spekframework.spek2.Spek

object AttachInvoiceViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    group("Load Invoice") {

        val exMsgId = "6696"
        val exInvoiceResponse = GetInvoiceResponse()
        val useCase by memoized { mockk<GetInvoiceUseCase>() }
        val viewmodel by memoized { AttachInvoiceViewModel(useCase) }

        group("No message id") {
            beforeEachTest {
                viewmodel.messageId = ""
            }
            test("Do nothing when messgeId is empty") {
                viewmodel.loadInvoices(1)
                verify(exactly = 0) { useCase.getInvoices(any(), any(), any(), any()) }
            }
        }

        group("With message id") {
            beforeEachTest {
                viewmodel.messageId = exMsgId
            }

            test("Call the exact same page provided in argument") {
                every { useCase.getInvoices(any(), any(), any(), any()) } just Runs
                viewmodel.loadInvoices(1)

                verify(exactly = 1) { useCase.getInvoices(any(), any(), any(), eq(1)) }
            }

            test("Observer data changed on success") {
                val observer = mockk<Observer<Result<List<Invoice>>>>(relaxed = true)
                every { useCase.getInvoices(captureLambda(), any(), any(), any()) } answers {
                    val onSuccess = lambda<(GetInvoiceResponse) -> Unit>()
                    onSuccess.invoke(exInvoiceResponse)
                }

                viewmodel.invoices.observeForever(observer)

                viewmodel.loadInvoices(1)

                verify { observer.onChanged(Success(exInvoiceResponse.invoices)) }
            }
        }
    }
})