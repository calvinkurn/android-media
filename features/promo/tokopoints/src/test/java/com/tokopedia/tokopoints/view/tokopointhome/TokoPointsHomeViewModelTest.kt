package com.tokopedia.tokopoints.view.tokopointhome

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager
import com.tokopedia.tokopoints.view.model.*
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
import org.junit.Test

import org.junit.Rule
import kotlin.reflect.KClass

class TokoPointsHomeViewModelTest {


    lateinit var  viewModel: TokoPointsHomeViewModel
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
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>(){
            every { onChanged(any()) } just Runs
        }
        val tokenObserver = mockk<Observer<LuckyEggEntity>>{
            every { onChanged(any()) } just Runs
        }
        val data = mockk<TokoPointEntity>()
        val dataSection = mockk<List<SectionContent>>()
        val tokenData = mockk<LuckyEggEntity>{
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery{repository.getTokoPointDetailData()}returns mockk{
           every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns mockk{
              every { tokoPoints } returns data
           }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk{
                every { sectionContent } returns mockk{
                    every { sectionContent } returns dataSection
                }
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk{
                every { tokenDetail } returns tokenData
            }
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.tokoenDetailLiveData.observeForever(tokenObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokopointObserver.onChanged(ofType(Success::class as KClass<Success<TokopointSuccess>>))
            tokenObserver.onChanged(any())
        }

        val result = viewModel.tokopointDetailLiveData.value as Success
        assert(result.data.sectionList == dataSection)
        assert(result.data.tokoPointEntity == data)
        assert(viewModel.tokoenDetailLiveData.value == tokenData)
    }

    @Test
    fun `getTokoPointDetail for token detail success and error in detail`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>(){
            every { onChanged(any()) } just Runs
        }
        val tokenObserver = mockk<Observer<LuckyEggEntity>>{
            every { onChanged(any()) } just Runs
        }
        val data = mockk<TokoPointEntity>()
        val dataSection = mockk<List<SectionContent>>()
        val tokenData = mockk<LuckyEggEntity>{
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery{repository.getTokoPointDetailData()}returns mockk{
            every { getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java) } returns mockk{
                every { tokoPoints } returns data
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk{
                every { sectionContent } returns null
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk{
                every { tokenDetail } returns tokenData
            }
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.tokoenDetailLiveData.observeForever(tokenObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokenObserver.onChanged(any())
        }
    }

    @Test
    fun `getTokoPointDetail for error`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>(){
            every { onChanged(any()) } just Runs
        }
        val tokenObserver = mockk<Observer<LuckyEggEntity>>{
            every { onChanged(any()) } just Runs
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.tokoenDetailLiveData.observeForever(tokenObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokopointObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokopointSuccess>>))
        }
    }

    @Test
    fun tokopointOnboarding2020() {
        mockkStatic(TokoPointsNotificationManager::class)

        every { TokoPointsNotificationManager.fetchNotification(any<Context>(),any<String>(),any<Tokopoint2020Subscriber>())} just Runs
        val view = mockk<TokoPointsHomeContract.View>()
        every { view.activityContext } returns mockk()
        viewModel.tokopointOnboarding2020(view)
        verify(exactly = 1) { TokoPointsNotificationManager.fetchNotification(any<Context>(),any<String>(),any<Tokopoint2020Subscriber>())}
    }

    @Test
    fun `getCouponCount for success`() {
        val observer = mockk<Observer<TokoPointSumCoupon>>()
        val data = mockk<TokoPointSumCoupon>()
        coEvery {  repository.getCouponCountData() } returns mockk{
            every { tokopointsSumCoupon } returns data
        }
        viewModel.couponCountLiveData.observeForever(observer)
        viewModel.couponCount

        verify(exactly = 1) { observer.onChanged(any()) }

        assert(viewModel.couponCountLiveData.value == data)
    }
    @Test
    fun `getCouponCount for error`() {
        val observer = mockk<Observer<TokoPointSumCoupon>>()
        viewModel.couponCountLiveData.observeForever(observer)
        viewModel.couponCount

        verify(exactly = 0) { observer.onChanged(any()) }
    }
}