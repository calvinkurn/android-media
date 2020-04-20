package com.tokopedia.power_merchant.subscribe.view.presenter

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopScoreUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import com.tokopedia.user_identification_common.domain.usecase.GetUserProjectInfoUseCase
import io.mockk.*
import org.mockito.ArgumentMatchers.anyString
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

class PmSubscribePresenterTest : Spek( {

    RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    })

    Feature("PmSubscribePresenter") {

        val getShopStatusUseCase: GetShopStatusUseCase = mockk()

        val getUserProjectInfoUseCase: GetUserProjectInfoUseCase = mockk()

        val getShopScoreUseCase: GetShopScoreUseCase = mockk()

        val pmSubscribeView: PmSubscribeContract.View = mockk()

        val getPowerMerchantStatusUseCase: GetPowerMerchantStatusUseCase = spyk(
                GetPowerMerchantStatusUseCase(getShopStatusUseCase,
                        getUserProjectInfoUseCase,
                        getShopScoreUseCase))

        val pmSubscribePresenter = PmSubscribePresenter(getPowerMerchantStatusUseCase)
        pmSubscribePresenter.attachView(pmSubscribeView)

        Scenario("GetPowerMerchantStatusUseCase is successful") {
            Given("Mock GetPowerMerchantStatusUseCase return as success") {
                val powerMerchantStatus = mockk<PowerMerchantStatus>()
                every {
                    getPowerMerchantStatusUseCase.createObservable(any())
                } returns Observable.just(powerMerchantStatus)
            }
            When("Run getPmStatusInfo method in presenter") {
                pmSubscribePresenter.getPmStatusInfo(anyString())
            }
            Then("GetPowerMerchantStatusUseCase is executed") {
                verify {
                    getPowerMerchantStatusUseCase.execute(any(), any())
                }
            }
            Then("Run onSuccessGetPmInfo on view") {
                verify {
                    pmSubscribeView.onSuccessGetPmInfo(any())
                }
            }
        }

        Scenario("GetPowerMerchantStatusUseCase is failed") {
            val errorThrowable = Throwable()
            Given("Mock GetPowerMerchantStatusUseCase return as failure") {
                every {
                    getPowerMerchantStatusUseCase.createObservable(any())
                } returns Observable.error(errorThrowable)
            }
            When("Run getPmStatusInfo method in presenter") {
                pmSubscribePresenter.getPmStatusInfo(anyString())
            }
            Then("GetPowerMerchantStatusUseCase is executed") {
                verify {
                    getPowerMerchantStatusUseCase.execute(any(), any())
                }
            }
//            Then("Run shopEmptyState on view") {
//                verify {
//                    pmSubscribeView.showEmptyState(errorThrowable)
//                }
//            }
        }
    }

})