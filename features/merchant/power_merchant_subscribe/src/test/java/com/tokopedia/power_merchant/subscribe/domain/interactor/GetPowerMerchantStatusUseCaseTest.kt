package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreResult
import com.tokopedia.gm.common.domain.interactor.GetPowerMerchantStatusUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopScoreUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import com.tokopedia.user_identification_common.domain.usecase.GetUserProjectInfoUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.observers.AssertableSubscriber

class GetPowerMerchantStatusUseCaseTest : Spek({

    Feature("GetPowerMerchantStatusUseCase") {

        val getShopStatusUseCase: GetShopStatusUseCase = mockk(relaxed = true)
        val getUserProjectInfoUseCase: GetUserProjectInfoUseCase = mockk(relaxed = true)
        val getShopScoreUseCase: GetShopScoreUseCase = mockk(relaxed = true)
        val requestParams: RequestParams = mockk(relaxed = true)

        val getPowerMerchantStatusUseCase by lazy {
            GetPowerMerchantStatusUseCase(
                    getShopStatusUseCase,
                    getUserProjectInfoUseCase,
                    getShopScoreUseCase
            )
        }

        Scenario("GetPowerMerchantStatus is success") {
            val goldGetPmOsStatusSuccess = mockk<GoldGetPmOsStatus>()
            val shopScoreResultSuccess = mockk<ShopScoreResult>()
            val getApprovalStatusPojo = mockk<KycUserProjectInfoPojo>()
            lateinit var testSubscriber : AssertableSubscriber<PowerMerchantStatus>

            Given("Mock getShopStatusUseCase to return empty success POJO") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.just(goldGetPmOsStatusSuccess)
            }

            Given("Mock getShopScoreUseCase to return empty success POJO") {
                every {
                    getShopScoreUseCase.createObservable(any())
                } returns Observable.just(shopScoreResultSuccess)
            }

            Given("Mock getApprovalStatusUseCase to return empty success POJO") {
                every {
                    getUserProjectInfoUseCase.execute(any())
                } returns Observable.just(getApprovalStatusPojo)
            }

            When("Run getPowerMerchantStatusUseCase") {
                testSubscriber = getPowerMerchantStatusUseCase.createObservable(requestParams).test()
            }

            Then("All the use case attributes created and executed") {
                verify {
                    getShopScoreUseCase.createObservable(any())
                    getUserProjectInfoUseCase.execute(any())
                    getShopScoreUseCase.createObservable(any())
                }
            }

            Then("The Subscriber is completed and not throwing error") {
                testSubscriber.assertCompleted()
                testSubscriber.assertNoErrors()
            }
        }

        Scenario("GetPowerMerchantStatus is failed") {
            val errorThrowable = mockk<Throwable>()
            lateinit var testSubscriber : AssertableSubscriber<PowerMerchantStatus>

            Given("Mock getShopStatusUseCase to return error throwable") {
                every {
                    getShopStatusUseCase.createObservable(any())
                } returns Observable.error(errorThrowable)
            }

            When("Run getPowerMerchantStatusUseCase") {
                testSubscriber = getPowerMerchantStatusUseCase.createObservable(requestParams).test()
            }

//            Then("The subscriber will return error throwable") {
//                testSubscriber.assertError(errorThrowable)
//            }
        }
    }
})