package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.usecase.ClaimCouponUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ClaimCouponViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: ClaimCouponViewModel by lazy {
        spyk(ClaimCouponViewModel(application, componentsItem, 99))
    }
    private val claimCouponUseCase: ClaimCouponUseCase = mockk()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.components == componentsItem)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun onAttachToViewHolder() {
        runBlocking {
            val list = mockk<ArrayList<ComponentsItem>>(relaxed = true)
            every { componentsItem.getComponentsItem() } returns list
            every { componentsItem.id } returns "s"
            val properties = Properties(categorySlug = "abc,jasd,kahfsd")
            every { componentsItem.properties } returns properties
            every { componentsItem.pageEndPoint } returns "s"
            viewModel.claimCouponUseCase = claimCouponUseCase
            coEvery { claimCouponUseCase.getClickCouponData(any(),any(),any()) } returns mockk()

            viewModel.onAttachToViewHolder()

            assertEquals(viewModel.getComponentList().value, list)
        }
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
