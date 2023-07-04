package com.tokopedia.mvc.presentation.quota

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota.*
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.presentation.quota.viewmodel.QuotaInfoViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class QuotaInfoViewModelTest {

    private lateinit var viewModel: QuotaInfoViewModel

    @RelaxedMockK
    lateinit var getVoucherQuotaUseCase: GetVoucherQuotaUseCase

    @RelaxedMockK
    lateinit var quotaInfoObserver: Observer<in VoucherCreationQuota>

    @RelaxedMockK
    lateinit var sourceListObserver: Observer<in List<Sources>>

    @RelaxedMockK
    lateinit var sourceListExpandedObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = QuotaInfoViewModel(
            CoroutineTestDispatchersProvider,
            getVoucherQuotaUseCase
        )
        with(viewModel) {
            quotaInfo.observeForever(quotaInfoObserver)
            sourceList.observeForever(sourceListObserver)
            sourceListExpanded.observeForever(sourceListExpandedObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            quotaInfo.removeObserver(quotaInfoObserver)
            sourceList.removeObserver(sourceListObserver)
            sourceListExpanded.removeObserver(sourceListExpandedObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `when setQuotaInfo is called, should populate quota info with the correct data`() {
        // Given
        val quota = VoucherCreationQuota(used = 10)
        val expected = VoucherCreationQuota(used = 10)

        // When
        viewModel.setQuotaInfo(quota)

        // Then
        val actual = viewModel.quotaInfo.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `when displayShortQuotaList() is called, should set source list value from the first 3 item in quota info`() {
        // Given
        val quotaInfo = VoucherCreationQuota(
            sources = listOf(
                Sources(name = "first source"),
                Sources(name = "second source"),
                Sources(name = "third source"),
                Sources(name = "forth source"),
                Sources(name = "fifth source")
            )
        )

        viewModel.setQuotaInfo(quotaInfo)

        val expected = listOf(
            Sources(name = "first source"),
            Sources(name = "second source"),
            Sources(name = "third source")
        )

        // When
        viewModel.displayShortQuotaList()

        // Then
        val actual = viewModel.sourceList.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `when displayFullQuotaList() is called, should set source list value from all item in quota info`() {
        // Given
        val quotaInfo = VoucherCreationQuota(
            sources = listOf(
                Sources(name = "first source"),
                Sources(name = "second source"),
                Sources(name = "third source"),
                Sources(name = "forth source"),
                Sources(name = "fifth source")
            )
        )

        viewModel.setQuotaInfo(quotaInfo)

        val expected = listOf(
            Sources(name = "first source"),
            Sources(name = "second source"),
            Sources(name = "third source"),
            Sources(name = "forth source"),
            Sources(name = "fifth source")
        )

        // When
        viewModel.displayFullQuotaList()

        // Then
        val actual = viewModel.sourceList.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    @Test
    fun `when toggleSourceListExpanded() is passed with true, then should set sourceListExpanded accordingly and display short quota list`() {
        // Given
        val isExpanded = true
        val quotaInfo = VoucherCreationQuota(
            sources = listOf(
                Sources(name = "first source"),
                Sources(name = "second source"),
                Sources(name = "third source"),
                Sources(name = "forth source"),
                Sources(name = "fifth source")
            )
        )

        viewModel.setQuotaInfo(quotaInfo)
        viewModel.displayShortQuotaList()

        val expectedSourceList = listOf(
            Sources(name = "first source"),
            Sources(name = "second source"),
            Sources(name = "third source")
        )
        val expectedSourceListExpanded = false

        // When
        viewModel.toggleSourceListExpanded(isExpanded)

        // Then
        val actualSourceListExpanded = viewModel.sourceListExpanded.getOrAwaitValue()
        val actualSourceList = viewModel.sourceList.getOrAwaitValue()
        assertEquals(expectedSourceListExpanded, actualSourceListExpanded)
        assertEquals(expectedSourceList, actualSourceList)
    }

    @Test
    fun `when toggleSourceListExpanded() is passed with false, then should set sourceListExpanded accordingly and display full quota list`() {
        // Given
        val isExpanded = false
        val quotaInfo = VoucherCreationQuota(
            sources = listOf(
                Sources(name = "first source"),
                Sources(name = "second source"),
                Sources(name = "third source"),
                Sources(name = "forth source"),
                Sources(name = "fifth source")
            )
        )

        viewModel.setQuotaInfo(quotaInfo)
        viewModel.displayFullQuotaList()

        val expectedSourceList = listOf(
            Sources(name = "first source"),
            Sources(name = "second source"),
            Sources(name = "third source"),
            Sources(name = "forth source"),
            Sources(name = "fifth source")
        )
        val expectedSourceListExpanded = true

        // When
        viewModel.toggleSourceListExpanded(isExpanded)

        // Then
        val actualSourceListExpanded = viewModel.sourceListExpanded.getOrAwaitValue()
        val actualSourceList = viewModel.sourceList.getOrAwaitValue()
        assertEquals(expectedSourceListExpanded, actualSourceListExpanded)
        assertEquals(expectedSourceList, actualSourceList)
    }

    @Test
    fun `when success get voucher quota, should set quota info with the corresponding data`() {
        runBlockingTest {
            // Given
            val expectedVoucherCreationQuota = VoucherCreationQuota()
            mockGetVoucherQuotaGQLCall()

            // When
            viewModel.getVoucherQuota()

            // Then
            val actual = viewModel.quotaInfo.getOrAwaitValue()
            assertEquals(expectedVoucherCreationQuota, actual)
        }
    }

    @Test
    fun `when failed get voucher quota, should return error`() {
        runBlockingTest {
            // Given
            val error = Throwable("Error")
            coEvery { getVoucherQuotaUseCase.execute() } throws error

            // When
            viewModel.getVoucherQuota()

            // Then
            val actual = viewModel.error.getOrAwaitValue()
            assertEquals(error, actual)
        }
    }

    @Test
    fun `if sources variable size from quotaInfo is more then SHORT_QUOTA_LIST_NUMBER, will set enableExpand variable accordingly`() {
        // Given
        val quotaInfo = VoucherCreationQuota(
            sources = listOf(
                Sources(name = "first source"),
                Sources(name = "second source"),
                Sources(name = "third source"),
                Sources(name = "forth source"),
                Sources(name = "fifth source")
            )
        )
        val expected = true

        // When
        viewModel.setQuotaInfo(quotaInfo)

        // Then
        val actual = viewModel.enableExpand.getOrAwaitValue()
        assertEquals(expected, actual)
    }

    private fun mockGetVoucherQuotaGQLCall() {
        val result = VoucherCreationQuota()
        coEvery { getVoucherQuotaUseCase.execute() } returns result
    }
}
