package com.tokopedia.chatbot.attachinvoice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chatbot.attachinvoice.domain.usecase.GetFilteredInvoiceListUseCase
import com.tokopedia.chatbot.attachinvoice.view.viewmodel.TransactionInvoiceListViewModel
import com.tokopedia.chatbot.domain.pojo.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TransactionInvoiceListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var testRule = CoroutineTestRule()

    private lateinit var viewModel: TransactionInvoiceListViewModel
    private lateinit var getFilteredInvoiceListUsecase: GetFilteredInvoiceListUseCase

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    private lateinit var userSession: UserSessionInterface

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        userSession = mockk(relaxed = true)
        getFilteredInvoiceListUsecase = mockk(relaxed = true)
        viewModel =
            TransactionInvoiceListViewModel(
                userSession,
                getFilteredInvoiceListUsecase
            )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getFilteredInvoice Success`() {

        val response = mockk<GetInvoiceListPojo>(relaxed = true )
        every {
            getFilteredInvoiceListUsecase.setParams(any(),any(),any())
        } just runs

        coEvery {
            getFilteredInvoiceListUsecase.executeOnBackground()
        } returns response

        viewModel.getFilteredInvoice("A",1,12)

        Assert.assertTrue(
            viewModel.getFilteredInvoiceLiveData().value is Success
        )

    }
}
