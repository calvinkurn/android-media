package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MerchantVoucherGridItemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val application = mockk<Application>()
    private val componentItems = mockk<ComponentsItem>(relaxed = true)

    private lateinit var viewModel: MerchantVoucherGridItemViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = spyk(
            MerchantVoucherGridItemViewModel(
                application,
                componentItems,
                99
            )
        )
    }

    @Test
    fun `test for components`() {
        assert(viewModel.component === componentItems)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for mapping and live data`() {
        every { componentItems.data } returns null

        viewModel.onAttachToViewHolder()

        assert(viewModel.getComponentData().value == null)

        val list: MutableList<DataItem> = ArrayList()
        list.add(DataItem())

        every { componentItems.data } returns list

        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentData().value != null)
    }
}
