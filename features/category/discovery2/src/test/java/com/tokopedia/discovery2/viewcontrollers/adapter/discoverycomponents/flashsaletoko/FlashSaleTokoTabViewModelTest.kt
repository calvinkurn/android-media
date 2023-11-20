package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.flashsaletokousecase.FlashSaleTokoUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlashSaleTokoTabViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val application = mockk<Application>()
    private val component = mockk<ComponentsItem>(relaxed = true)

    private val useCase: FlashSaleTokoUseCase by lazy { mockk(relaxed = true) }

    private lateinit var viewModel: FlashSaleTokoTabViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = spyk(FlashSaleTokoTabViewModel(application, component, 99))

        viewModel.useCase = useCase
    }

    @Test
    fun `given view model is attached, should get tab component data`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        viewModel.onAttachToViewHolder()

        coVerify(exactly = 1) { useCase.getData(componentId, pageEndpoint) }
    }

    @Test
    fun `given unselected tab is clicked, should trigger to sync data and update selected tab position`() {
        val firstFilterValue = "filterValue01"
        val secondFilterValue = "filterValue02"

        val data = listOf(
            DataItem(
                filterValue = firstFilterValue,
                isSelected = true
            ),
            DataItem(
                filterValue = secondFilterValue,
                isSelected = false
            )
        )

        coEvery { component.data } returns data

        viewModel.onTabClick(secondFilterValue)

        assertEquals(true, viewModel.getSyncPageLiveData().value)

        assertEquals(false, component.data?.get(0)?.isSelected)
        assertEquals(true, component.data?.get(1)?.isSelected)

        verify(exactly = 1) { component.reInitComponentItems() }
    }

    @Test
    fun `given successfully get tab data, should post the value and trigger sync data`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItems = arrayListOf<ComponentsItem>()
        coEvery { component.getComponentsItem() } returns componentsItems

        coEvery { useCase.getData(componentId, pageEndpoint) } returns true

        viewModel.onAttachToViewHolder()

        assertEquals(componentsItems, viewModel.getTabLiveData().value)

        assert(viewModel.getSyncPageLiveData().value == true)
    }

    @Test
    fun `given failed to get tab data, should post the value and trigger sync data with false value`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItems = arrayListOf<ComponentsItem>()
        coEvery { component.getComponentsItem() } returns componentsItems

        coEvery { useCase.getData(componentId, pageEndpoint) } returns false

        viewModel.onAttachToViewHolder()

        assertEquals(componentsItems, viewModel.getTabLiveData().value)

        assert(viewModel.getSyncPageLiveData().value == false)
    }

    @Test
    fun `given error on getting tab data, should not post any value`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val throwable = mockk<Throwable>()

        coEvery { useCase.getData(componentId, pageEndpoint) } throws throwable

        viewModel.onAttachToViewHolder()

        assertEquals(null, viewModel.getTabLiveData().value)

        assertEquals(null, viewModel.getSyncPageLiveData().value)
    }
}
