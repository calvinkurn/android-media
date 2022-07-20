package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditKeywordUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsInsightUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopAdsInsightPresenterTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private val topAdsInsightUseCase: TopAdsInsightUseCase = mockk(relaxed = true)
    private val topAdsEditKeywordUseCase: TopAdsEditKeywordUseCase = mockk(relaxed = true)
    var view: TopAdsInsightView = mockk(relaxed = true)
    private val res: Resources = mockk(relaxed = true)

    private val presenter: TopAdsInsightPresenter by lazy {
        TopAdsInsightPresenter(topAdsInsightUseCase, topAdsEditKeywordUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `get insight`() {
        presenter.getInsight(res)
        coVerify {
            topAdsInsightUseCase.execute(any(), any())
        }
    }

    @Test
    fun `getInsight success`() {
        val expectedGetInsight = spyk(InsightKeyData())

        coEvery { topAdsInsightUseCase.execute(any(), any()) } returns expectedGetInsight

        presenter.getInsight(res)

        verify {
            view.onSuccessKeywordInsight(expectedGetInsight)
        }
    }

    @Test
    fun `getInsight failure`() {
        val throwable = spyk(Throwable())

        coEvery { topAdsInsightUseCase.setParams() } throws throwable

        presenter.getInsight(res)

        verify {
            throwable.printStackTrace()
        }
    }

    @Test
    fun `create group`() {
        presenter.topAdsCreated("", "", listOf())
        coVerify {
            topAdsEditKeywordUseCase.execute(any(), any())
        }
    }

    @Test
    fun `topAdsCreated success check`() {
        val expected = spyk(FinalAdResponse())

        coEvery { topAdsEditKeywordUseCase.execute(any(),any()) } returns expected

        presenter.topAdsCreated("", "", listOf())

        verify { view.onSuccessEditKeywords(expected) }
    }

    @Test
    fun `topAdsCreated on empty invokes error`() {
        val expected = FinalAdResponse(FinalAdResponse.TopadsManageGroupAds(keywordResponse = FinalAdResponse.TopadsManageGroupAds.KeywordResponse(errors = listOf(
            FinalAdResponse.TopadsManageGroupAds.ErrorsItem()))))

        coEvery { topAdsEditKeywordUseCase.execute(any(),any()) } returns expected

        presenter.topAdsCreated("", "", listOf())

        verify { view.onErrorEditKeyword(expected.topadsManageGroupAds.keywordResponse.errors) }
    }

    @Test
    fun `topAdsCreated error check`() {
        val throwable = spyk(Throwable())

        coEvery { topAdsEditKeywordUseCase.setParam(any(),any()) } throws throwable

        presenter.topAdsCreated("", "", listOf())

        verify {
            throwable.printStackTrace()
        }
    }

}