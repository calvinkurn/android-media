package com.tokopedia.attachinvoice.view.viewmodel

import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import io.mockk.*
import org.spekframework.spek2.Spek

object AttachInvoiceViewModelTest : Spek({
    group("Load Invoice") {

        val exMsgId = "6696"

        val useCase by memoized { mockk<GetInvoiceUseCase>() }
        val viewmodel by memoized { AttachInvoiceViewModel(useCase) }

        test("Do nothing when messgeId is empty") {
            viewmodel.messageId = ""
            viewmodel.loadInvoices(1)

            verify(exactly = 0) { useCase.getInvoices(any(), any(), any(), any()) }
        }

        test("Call the exact same page provided in argument") {
            viewmodel.messageId = exMsgId
            every { useCase.getInvoices(any(), any(), any(), any()) } just Runs
            viewmodel.loadInvoices(1)

            verify(exactly = 1) { useCase.getInvoices(any(), any(), any(), eq(1)) }
        }

    }
})