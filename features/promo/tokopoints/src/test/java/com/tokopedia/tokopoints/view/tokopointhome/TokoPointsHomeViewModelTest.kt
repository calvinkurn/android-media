package com.tokopedia.tokopoints.view.tokopointhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.LuckyEggEntity
import com.tokopedia.tokopoints.view.model.TokenDetailOuter
import com.tokopedia.tokopoints.view.model.TokoPointSumCoupon
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.util.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

class TokoPointsHomeViewModelTest {


    lateinit var viewModel: TokoPointsHomeViewModel
    val repository = mockk<TokopointsHomeRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = TokoPointsHomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTokoPointDetail for success data`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<TokopediaRewardTopSection>{
            every { isShowIntroActivity } returns false
        }
        val dataSection = mockk<List<SectionContent>>()
        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk{
                every { tokopediaRewardTopSection } returns data
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns mockk {
                    every { sectionContent } returns dataSection
                }
            }
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokopointObserver.onChanged(ofType(Success::class as KClass<Success<TokopointSuccess>>))
        }

        val result = viewModel.tokopointDetailLiveData.value as Success
        assert(result.data.sectionList == dataSection)
        assert(result.data.tokoPointEntity == data)
    }

    @Test
    fun `getTokoPointDetail for token detail success and error in detail`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<TokopediaRewardTopSection>()
        val dataSection = mockk<List<SectionContent>>()
        val tokenData = mockk<LuckyEggEntity> {
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk {
                every { tokopediaRewardTopSection } returns data
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns null
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk {
                every { tokenDetail } returns tokenData
            }
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
        }
    }

    @Test
    fun `getTokoPointDetail for error`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokopointObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokopointSuccess>>))
        }
    }

    @Test
    fun tokopointIntro() {
        val tokopointIntroObserver = mockk<Observer<Resources<IntroResponse>>>()
        val data = mockk<TokopediaRewardIntroPage>()
        every { tokopointIntroObserver.onChanged(any()) } just runs
        coEvery { repository.getRewardIntroData() } returns mockk {
            every { getData<IntroResponse>(IntroResponse::class.java) } returns mockk {
                every { tokopediaRewardIntroPage } returns data
            }
        }
        viewModel.rewardIntroData.observeForever(tokopointIntroObserver)
        viewModel.getRewardIntroData()
        verify(exactly = 1) { tokopointIntroObserver.onChanged(any()) }
    }
}