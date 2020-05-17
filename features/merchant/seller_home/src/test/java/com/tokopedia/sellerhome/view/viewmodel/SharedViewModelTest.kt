package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.sellerhome.common.PageFragment
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SharedViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val sharedViewModel by lazy {
        SharedViewModel()
    }

    @Test
    fun `setCurrentSelectedPage will change live data value`() {
        val pageFragment = PageFragment(0, "")

        sharedViewModel.setCurrentSelectedPage(pageFragment)

        assert(sharedViewModel.currentSelectedPage.value == pageFragment)
    }
}