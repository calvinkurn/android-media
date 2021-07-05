package com.tokopedia.explore.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.domain.entity.GetDiscoveryKolData
import com.tokopedia.explore.domain.entity.GetExploreData
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HashTagLandingPageViewModelTest {

    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HashtagLandingPageViewModel
    private val exploreDataUseCase: ExploreDataUseCase = mockk(relaxed = true)
    private val trackClickAffiliateClickUseCase: TrackAffiliateClickUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = spyk(HashtagLandingPageViewModel(testRule.dispatchers, exploreDataUseCase, trackClickAffiliateClickUseCase))
    }

    @Test
    fun `test trackAffiliate method`() {
        every { trackClickAffiliateClickUseCase.execute(any(), any()) } just runs
        viewModel.trackAffiliate("test_url")
        verify { trackClickAffiliateClickUseCase.execute(any(), any()) }
    }

    @Test
    fun `test getContentByHashtag method for exception`() {
        val dummyException = Exception("dummy message")
        coEvery { exploreDataUseCase.executeOnBackground() } throws dummyException
        viewModel.getContentByHashtag()
        coVerify { exploreDataUseCase.executeOnBackground() }
        assertEquals(viewModel.getPostResponse().value, Fail(dummyException))
    }

    @Test
    fun `test getContentByHashtag method for success`() {
        val dummyResponse = GetExploreData().apply {
            getDiscoveryKolData = GetDiscoveryKolData().apply {
                lastCursor = "dummy"
            }
        }
        coEvery { exploreDataUseCase.executeOnBackground() } returns dummyResponse
        viewModel.getContentByHashtag()
        coVerify { exploreDataUseCase.executeOnBackground() }
        assertEquals(viewModel.canLoadMore, true)
    }
}