package com.tokopedia.home.viewModel.homepage

import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.InjectCouponTimeBasedUseCase
import com.tokopedia.home.beranda.domain.model.InjectCouponTimeBased
import com.tokopedia.home.beranda.domain.model.SetInjectCouponTimeBased
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.rules.InstantTaskExecutorRuleSpek
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

/**
 * Created by Ade Fulki on 14/05/20.
 */

@ExperimentalCoroutinesApi
class HomeViewModelInjectCouponUnitTest : Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Inject coupon based time test") {
        lateinit var homeViewModel: HomeViewModel
        createHomeViewModelTestInstance()

        val injectCouponTimeBasedUseCase by memoized<InjectCouponTimeBasedUseCase>()

        Scenario("Inject coupon successful") {
            val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)

            Given("SetInjectCouponTimeBased data") {
                injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(
                        SetInjectCouponTimeBased(
                                data = InjectCouponTimeBased(isSuccess = true)
                        )
                )
            }

            Given("HomeViewModel object") {
                homeViewModel = createHomeViewModel()
                homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)
            }

            When("Hit injectCouponTimeBased on HomeViewModel") {
                homeViewModel.injectCouponTimeBased()
            }

            Then("Expect inject coupon successfully sent and isSuccess data true") {
                verifyOrder {
                    observerInjectCouponTimeBased.onChanged(match {
                        (it.error == null) && (it.data != null && it.data?.isSuccess == true)
                    })
                }
                confirmVerified(observerInjectCouponTimeBased)
            }
        }

        Scenario("Inject coupon failed") {
            val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)

            Given("SetInjectCouponTimeBased data") {
                injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseReturn(
                        SetInjectCouponTimeBased(
                                data = InjectCouponTimeBased(isSuccess = false)
                        )
                )
            }

            Given("HomeViewModel object") {
                homeViewModel = createHomeViewModel()
                homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)
            }

            When("Hit injectCouponTimeBased on HomeViewModel") {
                homeViewModel.injectCouponTimeBased()
            }

            Then("Expect inject coupon successfully sent and isSuccess data false") {
                verifyOrder {
                    observerInjectCouponTimeBased.onChanged(match {
                        (it.error == null) && (it.data != null && it.data?.isSuccess == false)
                    })
                }
                confirmVerified(observerInjectCouponTimeBased)
            }
        }

        Scenario("Inject coupon failed with Exception") {
            val observerInjectCouponTimeBased: Observer<Result<InjectCouponTimeBased>> = mockk(relaxed = true)

            Given("Exception data") {
                injectCouponTimeBasedUseCase.givenInjectCouponTimeBasedUseCaseThrowReturn()
            }

            Given("HomeViewModel object") {
                homeViewModel = createHomeViewModel()
                homeViewModel.injectCouponTimeBasedResult.observeForever(observerInjectCouponTimeBased)
            }

            When("Hit injectCouponTimeBased on HomeViewModel") {
                homeViewModel.injectCouponTimeBased()
            }

            Then("Expect inject coupon failed sent") {
                verifyOrder {
                    observerInjectCouponTimeBased.onChanged(match {
                        (it.data == null) && (it.error != null && it.error == Exception())
                    })
                }
                confirmVerified(observerInjectCouponTimeBased)
            }
        }
    }
})