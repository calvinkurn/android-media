package com.tokopedia.power_merchant.subscribe.view.presenter

import com.tokopedia.gm.common.domain.interactor.ActivatePowerMerchantUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.power_merchant.subscribe.view.contract.PmTermsContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

class PmTermsPresenterTest : Spek({

    Feature("PmTermsPresenter") {
        val graphqlUseCase: GraphqlUseCase = mockk(relaxed = true)
        val pmTermsView: PmTermsContract.View = mockk(relaxed = true)
        val activatePowerMerchantUseCase: ActivatePowerMerchantUseCase = spyk(
                ActivatePowerMerchantUseCase(graphqlUseCase, anyString())
        )

        val pmTermsPresenter = PmTermsPresenter(activatePowerMerchantUseCase)
        pmTermsPresenter.attachView(pmTermsView)

        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })

        Scenario("Activate power merchant use case is successful") {
            Given("Mock use case return to success") {
                every {
                    activatePowerMerchantUseCase.createObservable(any())
                } returns Observable.just(true)
            }
            When("Run activatePowerMerchant method in presenter") {
                pmTermsPresenter.activatePowerMerchant()
            }
            Then("ActivatePowerMerchantUseCase should be executed") {
                verify {
                    activatePowerMerchantUseCase.execute(any())
                }
            }
            Then("View should show loading") {
                verify {
                    pmTermsView.showLoading()
                }
            }
            Then("View should hide loading") {
                verify {
                    pmTermsView.hideLoading()
                }
            }
            Then("View should run onSuccessActivate") {
                verify {
                    pmTermsView.onSuccessActivate()
                }
            }
        }

        Scenario("Activate power merchant use case is failed") {
            Given("Mock use case return to failure") {
                every {
                    activatePowerMerchantUseCase.createObservable(any())
                } returns Observable.error(Throwable())
            }
            When("Run activatePowerMerchant method in presenter") {
                pmTermsPresenter.activatePowerMerchant()
            }
            Then("ActivatePowerMerchantUseCase should be executed") {
                verify {
                    activatePowerMerchantUseCase.execute(any())
                }
            }
            Then("View should show loading") {
                verify {
                    pmTermsView.showLoading()
                }
            }
            Then("View should hide loading") {
                verify {
                    pmTermsView.hideLoading()
                }
            }
            Then("View should run onError") {
                verify {
                    pmTermsView.onError(any())
                }
            }
        }
    }

})