package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditKeywordUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsInsightUseCase
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TopAdsInsightPresenterTest {
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
        verify {
            topAdsInsightUseCase.execute(any(), any())
        }
    }

    @Test
    fun `create group`() {
        presenter.topAdsCreated("", "", listOf())
        verify {
            topAdsEditKeywordUseCase.execute(any(), any())
        }

    }
}