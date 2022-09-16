package com.tokopedia.topads.sdk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
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
    fun `test get_query_params`() {
        val actual = "query"
        every {
            topAdsImageViewUseCase.getQueryMap(any(), any(), any(), any(), any(), any())
        } returns mutableMapOf("q" to actual)

        val result = viewModel.getQueryParams("query", "", "", 4, 5, "", "", "")
        Assert.assertEquals(result["q"], actual)

    }

    @Test
    fun `test get_query_params verify`() {
        every {
            topAdsImageViewUseCase.getQueryMap(any(), any(), any(), any(), any(), any())
        } returns mutableMapOf()

        val result = viewModel.getQueryParams("query", "", "", 4, 5, "", "", "1")

        verify {
            topAdsImageViewUseCase.getQueryMap(
                "query",
                "",
                "",
                4,
                5,
                "",
                "",
                "1"
            )
        }

    }

    @Test
    fun `test get_query_params default`() {
        val actual = "query"
        every {
            topAdsImageViewUseCase.getQueryMap(any(), any(), any(), any(), any(), any())
        } returns mutableMapOf("q" to actual)

        val result = viewModel.getQueryParams("query", "", "", 4, 5, "", "")
        Assert.assertEquals(result["q"], actual)
    }

    @Test
    fun `test getImageData with success`() {
        val actual = "my_banner"
        val data = arrayListOf(TopAdsImageViewModel(bannerName = actual))
        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns data
        viewModel.getImageData(mutableMapOf())
        Assert.assertEquals((viewModel.getResponse().value as Success).data.first().bannerName, actual)
    }

    @Test
    fun `test getImageData with exception`() {
        val exception = Exception("my exception")
        coEvery { topAdsImageViewUseCase.getImageData(any()) } throws exception
        viewModel.getImageData(mutableMapOf())
        Assert.assertEquals((viewModel.getResponse().value as Fail).throwable.message, exception.message)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
