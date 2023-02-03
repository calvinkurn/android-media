package com.tokopedia.privacycenter.dsar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.privacycenter.data.CreateRequestResponse
import com.tokopedia.privacycenter.data.GetRequestDetailResponse
import com.tokopedia.privacycenter.domain.SearchRequestUseCase
import com.tokopedia.privacycenter.domain.SubmitRequestUseCase
import com.tokopedia.privacycenter.ui.dsar.DsarConstants
import com.tokopedia.privacycenter.ui.dsar.DsarUtils
import com.tokopedia.privacycenter.ui.dsar.DsarViewModel
import com.tokopedia.privacycenter.ui.dsar.uimodel.CustomDateModel
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DsarViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    lateinit var viewModel: DsarViewModel

    val submitRequestUseCase = mockk<SubmitRequestUseCase>(relaxed = true)
    val searchRequestUseCase = mockk<SearchRequestUseCase>(relaxed = true)
    val getProfileUseCase = mockk<GetProfileUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)

    private var toasterErrorObserver = mockk<Observer<String>>(relaxed = true)
    private var mainLoaderObserver = mockk<Observer<Boolean>>(relaxed = true)
    private var mainLayoutObserver = mockk<Observer<Boolean>>(relaxed = true)

    val email = "yoris.prayogo@tokopedia.com"
    val exception = Exception("error")

    @Before
    fun setup() {
        mockkObject(DsarUtils)

        viewModel = DsarViewModel(submitRequestUseCase, searchRequestUseCase, getProfileUseCase, userSession, dispatcherProviderTest)
        viewModel.toasterError.observeForever(toasterErrorObserver)
        viewModel.mainLoader.observeForever(mainLoaderObserver)
        viewModel.showMainLayout.observeForever(mainLayoutObserver)
    }

    @Test
    fun `submitRequest success`() {
        val deadline = "2022-12-04T04:59:21.630Z"
        val submitSuccessResponse = CreateRequestResponse(email = email, deadline = deadline)
        coEvery { submitRequestUseCase(any()) } returns submitSuccessResponse

        viewModel.submitRequest()

        assert(viewModel.submitRequestState.value?.email == email)
        assert(viewModel.submitRequestState.value?.deadline == deadline)

        verify {
            submitRequestUseCase.constructParams(any())
        }

        verify(exactly = 1) {
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
            mainLayoutObserver.onChanged(true)
        }
    }

    @Test
    fun `submitRequest exception`() {
        coEvery { submitRequestUseCase(any()) } throws exception

        viewModel.submitRequest()
        val toaster = viewModel.toasterError.getOrAwaitValue()

        assert(toaster == DsarConstants.LABEL_ERROR_REQUEST)
        verify(exactly = 1) {
            toasterErrorObserver.onChanged(DsarConstants.LABEL_ERROR_REQUEST)
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
        }
    }

    @Test
    fun `formatRequest executed`() {
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PAYMENT)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_TRANSACTION)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)

        // full item is 9
        assert(viewModel.formatRequest().count() == 9)
    }

    @Test
    fun `checkRequest success`() {
        val status = "SVP Approval"

        val searchRequestResp = GetRequestDetailResponse(requestQueueRefId = "123", status = status)
        coEvery { searchRequestUseCase(any()) } returns searchRequestResp

        viewModel.checkRequestStatus()

        val result = viewModel.requestDetails.getOrAwaitValue()
        assert(result.status == status)

        verify(exactly = 1) {
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
        }
    }

    @Test
    fun `checkRequest success - empty content`() {
        val searchRequestResp = GetRequestDetailResponse()
        coEvery { searchRequestUseCase(any()) } returns searchRequestResp

        viewModel.checkRequestStatus()

        verify(exactly = 1) {
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
            mainLayoutObserver.onChanged(true)
        }
    }

    @Test
    fun `checkRequest success - Completed`() {
        val status = DsarConstants.STATUS_COMPLETED

        val searchRequestResp = GetRequestDetailResponse(requestQueueRefId = "123", status = status)
        coEvery { searchRequestUseCase(any()) } returns searchRequestResp

        viewModel.checkRequestStatus()

        verify(exactly = 1) {
            mainLayoutObserver.onChanged(true)
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
        }
    }

    @Test
    fun `checkRequest exception`() {
        coEvery { searchRequestUseCase(any()) } throws exception

        viewModel.checkRequestStatus()
        val globalError = viewModel.globalError.getOrAwaitValue()

        assert(globalError)

        verify(exactly = 1) {
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
        }
    }

    @Test
    fun `setSelectedDate executed with custom date`() {
        val startDate = Date()
        val endDate = Date()

        viewModel.setSelectedDate(DsarConstants.LABEL_RANGE_CUSTOM, startDate, endDate)

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(result.selectedDate.startDate == startDate.toString(DateUtil.YYYYMMDD))
        assert(result.selectedDate.endDate == endDate.toString(DateUtil.YYYYMMDD))
    }

    @Test
    fun `setSelectedDate executed with pre-existing date`() {
        val mockDateResponse = CustomDateModel("12Nov2022", "13Nov2022")

        every { DsarUtils.getDateFromSelectedId(any()) } returns mockDateResponse

        viewModel.setSelectedDate(DsarConstants.LABEL_RANGE_30_DAYS, null, null)

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(result.selectedDate == mockDateResponse)
    }

    @Test
    fun `setSelectedDate executed - startDate null`() {
        val endDate = Date()

        val mockDateResponse = CustomDateModel("12Nov2022", "13Nov2022")

        every { DsarUtils.getDateFromSelectedId(any()) } returns mockDateResponse

        viewModel.setSelectedDate(DsarConstants.LABEL_RANGE_30_DAYS, endDate = endDate)

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(result.selectedDate == mockDateResponse)
    }

    @Test
    fun `setSelectedDate executed - endDate null`() {
        val startDate = Date()

        val mockDateResponse = CustomDateModel("12Nov2022", "13Nov2022")

        every { DsarUtils.getDateFromSelectedId(any()) } returns mockDateResponse

        viewModel.setSelectedDate(DsarConstants.LABEL_RANGE_30_DAYS, startDate = startDate)

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(result.selectedDate == mockDateResponse)
    }

    @Test
    fun `onTransactionHistorySelected executed`() {
        viewModel.onTransactionHistorySelected()

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(result.showBottomSheet)
        assert(result.isChecked)
        assert(viewModel._filterItems.contains(DsarConstants.FILTER_TYPE_TRANSACTION))
    }

    @Test
    fun `onTransactionHistoryDeselected executed`() {
        viewModel.onTransactionHistoryDeselected()

        val result = viewModel.transactionHistoryModel.getOrAwaitValue()
        assert(!result.showBottomSheet)
        assert(!result.isChecked)

        assert(!viewModel._filterItems.contains(DsarConstants.FILTER_TYPE_TRANSACTION))
    }

    @Test
    fun `addFilter executed`() {
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)
        assert(viewModel._filterItems.contains(DsarConstants.FILTER_TYPE_PERSONAL))
    }

    @Test
    fun `addFilter executed more than 3 times`() {
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_TRANSACTION)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PAYMENT)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)

        assert(viewModel._filterItems.count() == 3)
    }

    @Test
    fun `removeFilter executed`() {
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)

        // When
        viewModel.removeFilter(DsarConstants.FILTER_TYPE_PERSONAL)

        assert(!viewModel._filterItems.contains(DsarConstants.FILTER_TYPE_PERSONAL))
    }

    @Test
    fun `backToFormPage executed`() {
        viewModel.backToFormPage()
        assert(viewModel.showSummary.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `showSummary executed`() {
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PERSONAL)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_PAYMENT)
        viewModel.addFilter(DsarConstants.FILTER_TYPE_TRANSACTION)

        viewModel.showSummary()
        assert(viewModel.showSummary.getOrAwaitValue().contains(DsarConstants.PERSONAL_LABEL))
        assert(viewModel.showSummary.getOrAwaitValue().contains(DsarConstants.PAYMENT_LABEL))
        assert(viewModel.showSummary.getOrAwaitValue().contains(DsarConstants.TRANSACTION_LABEL))
    }

    @Test
    fun `showSummary executed - empty filter`() {
        viewModel._filterItems.clear()
        viewModel.showSummary()
        assert(viewModel.toasterError.getOrAwaitValue().contains(DsarConstants.SUMMARY_ERROR))
    }

    @Test
    fun `getSelectedRangeItems executed`() {
        val startDate = Date()
        val endDate = Date()

        viewModel.setSelectedDate(DsarConstants.LABEL_RANGE_CUSTOM, startDate, endDate)

        assert(viewModel.getSelectedRangeItems()?.startDate == startDate.toString(DateUtil.YYYYMMDD))
        assert(viewModel.getSelectedRangeItems()?.endDate == endDate.toString(DateUtil.YYYYMMDD))
    }

    @Test
    fun `fetchInitialData success`() {
        /* When */
        val profileInfo = ProfileInfo(firstName = "yoris")
        val response = ProfilePojo(profileInfo = profileInfo)

        coEvery { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onSuccessGetProfile(response)
        }

        val searchRequestResp = GetRequestDetailResponse()
        coEvery { searchRequestUseCase(any()) } returns searchRequestResp

        viewModel.fetchInitialData()

        /* Then */
        verify(exactly = 1) {
            mainLoaderObserver.onChanged(true)
            mainLoaderObserver.onChanged(false)
            mainLayoutObserver.onChanged(true)
        }
    }

    @Test
    fun `fetchInitialData failed`() {
        /* When */
        coEvery { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onErrorGetProfile(Throwable())
        }

        viewModel.fetchInitialData()
        val globalError = viewModel.globalError.getOrAwaitValue()

        assert(globalError)
    }

    @Test
    fun `on Show Location Admin Popup`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showLocationAdminPopUp?.invoke()
        }

        viewModel.fetchInitialData()

        /* Then */
        verify {
            getProfileUseCase.execute(any())
        }
    }

    @Test
    fun `on Admin Redirection`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().onLocationAdminRedirection?.invoke()
        }

        viewModel.fetchInitialData()

        /* Then */
        verify {
            getProfileUseCase.execute(any())
        }
    }

    @Test
    fun `on Show Location Admin Popup Error`() {

        every { getProfileUseCase.execute(any()) } answers {
            firstArg<GetProfileSubscriber>().showErrorGetAdminType?.invoke(Throwable())
        }

        viewModel.fetchInitialData()

        /* Then */
        verify {
            getProfileUseCase.execute(any())
        }
    }

}
