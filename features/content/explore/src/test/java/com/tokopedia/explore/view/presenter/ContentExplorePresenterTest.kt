package com.tokopedia.explore.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.domain.entity.GetDiscoveryKolData
import com.tokopedia.explore.domain.entity.GetExploreData
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.explore.view.listener.ContentExploreContract
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ContentExplorePresenterTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getExploreDataUseCase: ExploreDataUseCase

    @RelaxedMockK
    lateinit var trackAffiliateClickUseCase: TrackAffiliateClickUseCase

    @RelaxedMockK
    lateinit var view: ContentExploreContract.View

    private val presenter: ContentExplorePresenter by lazy {
        ContentExplorePresenter(getExploreDataUseCase, trackAffiliateClickUseCase)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `test detachView method`() {
        presenter.detachView()
        verify { getExploreDataUseCase.cancelJobs() }
    }

    @Test
    fun `test getExploreData method for exception`() {
        val dummyException = Exception("dummy exception")
        coEvery { getExploreDataUseCase.executeOnBackground() } throws dummyException
        presenter.getExploreData(true)
        coVerify { view.showRefreshing() }
        coVerify { view.dismissLoading() }
        coVerify { view.stopTrace() }
        coVerify { view.onErrorGetExploreDataFirstPage(ErrorHandler.getErrorMessage(view.context, dummyException)) }

        presenter.getExploreData(false)
        coVerify { view.showLoading() }
        coVerify { view.dismissLoading() }
        coVerify { view.stopTrace() }
        coVerify { view.onErrorGetExploreDataMore() }
    }

    @Test
    fun `test getExploreData method for error in response`() {
        val dummyResponse = GetExploreData().apply {
            getDiscoveryKolData = GetDiscoveryKolData().apply {
                error = "dummy error"
            }
        }
        coEvery { getExploreDataUseCase.executeOnBackground() } returns dummyResponse
        presenter.getExploreData(true)
        coVerify { view.showRefreshing() }
        coVerify { view.clearData() }
        coVerify { view.dismissLoading() }
        coVerify { view.onErrorGetExploreDataFirstPage(dummyResponse.getDiscoveryKolData?.error) }
        coVerify(exactly = 0) { view.updateCursor(dummyResponse.getDiscoveryKolData?.lastCursor) }

        presenter.getExploreData(false)
        coVerify { view.updateCursor(dummyResponse.getDiscoveryKolData?.lastCursor) }
        coVerify { view.dismissLoading() }
        coVerify { view.stopTrace() }
        coVerify { view.onSuccessGetExploreData(any(), false) }
    }

    @Test
    fun `test getExploreData method for success in response`() {
        val dummyResponse = GetExploreData().apply {
            getDiscoveryKolData = GetDiscoveryKolData().apply {
                lastCursor = "dummy cursor"
            }
        }
        coEvery { getExploreDataUseCase.executeOnBackground() } returns dummyResponse

        presenter.getExploreData(false)
        coVerify { getExploreDataUseCase.setParams(any(), any(), any()) }
        coVerify { getExploreDataUseCase.executeOnBackground() }
        coVerify { view.updateCursor(dummyResponse.getDiscoveryKolData?.lastCursor) }
        coVerify { view.dismissLoading() }
        coVerify { view.stopTrace() }
        coVerify { view.onSuccessGetExploreData(any(), false) }
    }

    @Test
    fun `test trackAffiliate method`() {
        every { trackAffiliateClickUseCase.execute(any(), any()) } just runs
        presenter.trackAffiliate("test_url")
        verify { trackAffiliateClickUseCase.execute(any(), any()) }
    }

    @Test
    fun `test trackBulkAffiliate method`() {
        //when urlList is empty
        presenter.trackBulkAffiliate(mutableListOf())
        verify(exactly = 0) { presenter.trackAffiliate("") }
        //when urlList is not empty
        presenter.trackBulkAffiliate(mutableListOf("test1", "test2"))
        verify { trackAffiliateClickUseCase.execute(any(), any()) }
    }

}