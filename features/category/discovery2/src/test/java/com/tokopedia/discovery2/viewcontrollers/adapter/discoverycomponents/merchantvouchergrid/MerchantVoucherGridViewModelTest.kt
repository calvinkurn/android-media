package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MerchantVoucherGridViewModelTest {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    private val application = mockk<Application>()
    private val component = mockk<ComponentsItem>(relaxed = true)

    private val useCase: MerchantVoucherUseCase by lazy { mockk(relaxed = true) }

    private lateinit var viewModel: MerchantVoucherGridViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = spyk(
            MerchantVoucherGridViewModel(
                application,
                component,
                99
            )
        )

        viewModel.useCase = useCase
    }

    @Test
    fun `given successfully fetch with empty vouchers, should not post any value for voucher list`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-mvc-grid-infinite-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItem = arrayListOf<ComponentsItem>()

        coEvery { component.getComponentsItem() } returns componentsItem

        coEvery { useCase.loadFirstPageComponents(componentId, pageEndpoint) } returns true

        viewModel.fetchCoupons()

        assertEquals(true, viewModel.loadError.value)
        assertEquals(null, viewModel.couponList.value)
    }

    @Test
    fun `given successfully fetch vouchers, should post the vouchers and should not treat as error`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-mvc-grid-infinite-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItem = arrayListOf<ComponentsItem>(mockk())

        coEvery { component.getComponentsItem() } returns componentsItem

        coEvery { useCase.loadFirstPageComponents(componentId, pageEndpoint) } returns true

        viewModel.fetchCoupons()

        assertEquals(false, viewModel.loadError.value)
        assertEquals(componentsItem, viewModel.couponList.value)
    }

    @Test
    fun `given failed to fetch vouchers, should post load error value`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-mvc-grid-infinite-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        coEvery { component.getComponentsItem() } throws Throwable()

        coEvery { useCase.loadFirstPageComponents(componentId, pageEndpoint) } returns true

        viewModel.fetchCoupons()

        assertEquals(true, viewModel.loadError.value)
        assertEquals(null, viewModel.couponList.value)
    }
}
