package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryBestSellerViewModelTest{

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: CategoryBestSellerViewModel by lazy {
        spyk(CategoryBestSellerViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

}