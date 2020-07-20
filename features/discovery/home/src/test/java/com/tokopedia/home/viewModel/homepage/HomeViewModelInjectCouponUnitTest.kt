package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Test

/**
 * Created by Ade Fulki on 14/05/20.
 */

class HomeViewModelInjectCouponUnitTest {

    private val injectCouponTimeBasedUseCase = mockk<InjectCouponTimeBasedUseCase>(relaxed = true)
    private val homeViewModel: HomeViewModel = createHomeViewModel(injectCouponTimeBasedUseCase = injectCouponTimeBasedUseCase)


    @Test
    fun `Inject coupon successful`() {
        val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)
        // SetInjectCouponTimeBased data
        injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(
                SetInjectCouponTimeBased(
                        data = InjectCouponTimeBased(isSuccess = true)
                )
        )


        // HomeViewModel object
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

        // HomeViewModel object
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

        // HomeViewModel object
        homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)

        // Hit injectCouponTimeBased on HomeViewModel
        homeViewModel.injectCouponTimeBased()

        // Expect inject coupon failed sent
        verifyOrder {
            observerInjectCouponTimeBased.onChanged(match {
                (it.data == null) && (it.error != null && it.error == Exception())
            })
        }
        confirmVerified(observerInjectCouponTimeBased)

    }
}