package com.tokopedia.explore.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.explore.domain.interactor.ExploreDataUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
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
    fun `test getContentByHashTag for exception`(){
        val dummyException = Exception("my excep")

        coEvery { exploreDataUseCase.executeOnBackground() } throws dummyException
        viewModel.getContentByHashtag()
        coVerify { exploreDataUseCase.executeOnBackground() }
        assertEquals(viewModel.getPostResponse().value, dummyException)
    }

}