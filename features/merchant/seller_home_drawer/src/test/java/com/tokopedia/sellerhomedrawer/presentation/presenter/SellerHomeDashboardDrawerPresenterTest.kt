package com.tokopedia.sellerhomedrawer.presentation.presenter

import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

class SellerHomeDashboardDrawerPresenterTest : Spek({

    Feature("SellerHomeDashboardDrawerPresenter") {

        val getShopStatusUseCase: GetShopStatusUseCase = mockk(relaxed = true)
        val flashSaleGetSellerStatusUseCase: FlashSaleGetSellerStatusUseCase = mockk(relaxed = true)
        val userSession: UserSessionInterface = mockk(relaxed = true)
        val errorThrowable = mockk<Throwable>()

        val sellerHomeDashboardDrawerPresenter: SellerHomeDashboardDrawerPresenter = spyk(
            SellerHomeDashboardDrawerPresenter(
                    getShopStatusUseCase, flashSaleGetSellerStatusUseCase, userSession
            )
        )
        val sellerHomeDashboardView: SellerHomeDashboardContract.View = mockk(relaxed = true)
        sellerHomeDashboardDrawerPresenter.attachView(sellerHomeDashboardView)

        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })

        Scenario("Success check isGoldMerchant") {

            val getShopStatusUseCaseSuccess: GoldGetPmOsStatus = mockk()

            Given("GetShopStatusUseCase returns success") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.just(getShopStatusUseCaseSuccess)
            }

            When("Run isGoldMerchantAsync in presenter") {
                sellerHomeDashboardDrawerPresenter.isGoldMerchantAsync()
            }

            Then("Run onSuccessGetFlashSaleSellerStatus") {
                verify {
                    sellerHomeDashboardView.onSuccessGetShopInfo(any())
                }
            }
        }

        Scenario("Failed check isGoldMerchant") {

            Given("GetShopStatusUseCase returns error") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.error(errorThrowable)
            }

            When("Run isGoldMerchantAsync in presenter") {
                sellerHomeDashboardDrawerPresenter.isGoldMerchantAsync()
            }

            Then("Not throwing onSuccess") {
                verify(exactly = 0) {
                    sellerHomeDashboardView.onSuccessGetShopInfo(any())
                }
            }
        }

        Scenario("Success get FlashSaleSellerStatus") {

            Given("FlashSaleSellerStatusUseCase returns success") {
                every {
                    userSession.shopId
                } returns "12345678"
                every {
                    flashSaleGetSellerStatusUseCase.createObservable(any())
                } returns Observable.just(true)
            }

            When("Run getFlashSaleSellerStatus method in presenter") {
                sellerHomeDashboardDrawerPresenter.getFlashSaleSellerStatus()
            }

            Then("Running onSuccessGetFlashSaleSellerStatus") {
                verify {
                    sellerHomeDashboardView.onSuccessGetFlashSaleSellerStatus(any())
                }
            }
        }

        Scenario("Failed get FlashSaleSellerStatus") {

            Given("FlashSaleGetSellerStatusUseCase returns error") {
                every {
                    flashSaleGetSellerStatusUseCase.createObservable(any())
                } returns Observable.error(errorThrowable)

            }

            When("Run getFlashSaleSellerStatus method in presenter") {
                sellerHomeDashboardDrawerPresenter.getFlashSaleSellerStatus()
            }

            Then("Not running onSuccessGetFlashSaleSellerStatus") {
                verify(exactly = 0) {
                    sellerHomeDashboardView.onSuccessGetFlashSaleSellerStatus(any())
                }
            }

            Then("Print stack trace") {
                verify { errorThrowable.printStackTrace() }
            }
        }
    }
})