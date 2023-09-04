package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.*
import com.tokopedia.affiliate.usecase.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateEducationSearchViewModelTest {
    private var affiliateEducationSearchViewModel = spyk(AffiliateEducationSearchViewModel())

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search keyword null success`() {
        assertEquals(null, affiliateEducationSearchViewModel.getSearchKeyword().value)
    }

    @Test
    fun `search keyword empty success`() {
        affiliateEducationSearchViewModel.searchKeyword.value = ""
        assertEquals("", affiliateEducationSearchViewModel.getSearchKeyword().value)
    }

    @Test
    fun `search keyword non-empty success`() {
        affiliateEducationSearchViewModel.searchKeyword.value = "1"
        assertEquals("1", affiliateEducationSearchViewModel.getSearchKeyword().value)
    }
}
