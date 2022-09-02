package com.tokopedia.topads.sdk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsImageViewViewModelTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val topAdsImageViewUseCase: TopAdsImageViewUseCase =
        mockk(relaxed = true)

    private val viewModel = spyk(TopAdsImageViewViewModel(topAdsImageViewUseCase))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test getQueryParams`() {
        val a = mutableMapOf<String, Any>("q" to "query")
        every {
            topAdsImageViewUseCase.getQueryMap(any(), any(), any(), any(), any(), any())
        } returns a

        val r = viewModel.getQueryParams("query", "2", "3", 4, 5, "", "", "")
        Assert.assertEquals(r["q"], "query")

    }
}