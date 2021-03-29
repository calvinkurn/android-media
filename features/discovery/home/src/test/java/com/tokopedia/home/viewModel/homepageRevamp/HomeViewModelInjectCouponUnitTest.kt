package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.viewModel.homepage.givenInjectCouponTimeBasedUseCaseReturn
import com.tokopedia.home.viewModel.homepage.givenInjectCouponTimeBasedUseCaseThrowReturn
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Rule
import org.junit.Test

/**
 * Created by Ade Fulki on 14/05/20.
 */

class HomeViewModelInjectCouponUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val injectCouponTimeBasedUseCase = mockk<InjectCouponTimeBasedUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel


    @Test
    fun `Inject coupon successful`() {
        val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)
        // SetInjectCouponTimeBased data
        injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(
                SetInjectCouponTimeBased(
                        data = InjectCouponTimeBased(isSuccess = true)
                )
        )

        homeViewModel = createHomeViewModel(injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase)
        homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)

        // Hit injectCouponTimeBased on HomeViewModel
        homeViewModel.injectCouponTimeBased()

        // Expect inject coupon successfully sent and isSuccess data true
        verifyOrder {
            observerInjectCouponTimeBased.onChanged(match {
                (it.error == null) && (it.data != null && it.data?.isSuccess == true)
            })
        }
        confirmVerified(observerInjectCouponTimeBased)
    }

    @Test
    fun `Inject coupon failed`() {
        val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)

        // SetInjectCouponTimeBased data
        injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(
                SetInjectCouponTimeBased(
                        data = InjectCouponTimeBased(isSuccess = false)
                )
        )

        homeViewModel = createHomeViewModel(injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase)
        homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)

        // Hit injectCouponTimeBased on HomeViewModel
        homeViewModel.injectCouponTimeBased()

        // Expect inject coupon successfully sent and isSuccess data false
        verifyOrder {
            observerInjectCouponTimeBased.onChanged(match {
                (it.error == null) && (it.data != null && it.data?.isSuccess == false)
            })
        }
        confirmVerified(observerInjectCouponTimeBased)
    }

    @Test
    fun `Inject coupon failed with Exception`(){
        val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)

        // Exception data
        injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseThrowReturn()

        homeViewModel = createHomeViewModel(injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase)
        homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)

        // Hit injectCouponTimeBased on HomeViewModel
        homeViewModel.injectCouponTimeBased()

        // Expect inject coupon failed sent
        verifyOrder {
            observerInjectCouponTimeBased.onChanged(match {
                it.data == null && it.error != null
            })
        }
        confirmVerified(observerInjectCouponTimeBased)
    }
}