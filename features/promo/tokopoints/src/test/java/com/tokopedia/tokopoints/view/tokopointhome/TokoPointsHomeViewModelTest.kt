package com.tokopedia.tokopoints.view.tokopointhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.notification.PopupNotifUsecase
import com.tokopedia.tokopoints.view.model.LuckyEggEntity
import com.tokopedia.tokopoints.view.model.TokenDetailOuter
import com.tokopedia.tokopoints.view.model.homeresponse.TokopointSuccess
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.TokopointsSection
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommUsecase
import com.tokopedia.tokopoints.view.util.*
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    lateinit var repository: TokopointsHomeUsecase
    lateinit var recomUsecase: RewardsRecommUsecase
    lateinit var popupNotifUsecase: PopupNotifUsecase

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mockk(relaxed = true)
        recomUsecase = mockk(relaxed = true)
        popupNotifUsecase = mockk(relaxed = true)
        viewModel = TokoPointsHomeViewModel(repository, recomUsecase , popupNotifUsecase)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTokoPointDetail for token detail success and error in detail`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }
        val data = mockk<TokopediaRewardTopSection>()
        val tokenData = mockk<LuckyEggEntity> {
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk {
                every { tokopediaRewardTopSection } returns data
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns TokopointsSection()
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
    fun `getTokoPointDetail null pointer error`(){
        val tokenData = mockk<LuckyEggEntity> {
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk {
                every { tokopediaRewardTopSection } returns null
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns TokopointsSection()
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk {
                every { tokenDetail } returns tokenData
            }
        }
        viewModel.getTokoPointDetail()
        Assert.assertEquals((viewModel.tokopointDetailLiveData.value as ErrorMessage).data,"error in data")
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
