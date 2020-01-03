package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.fragment

import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.tkpd.tkpdreputation.createreputation.usecase.GetProductReputationForm
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import org.mockito.ArgumentMatchers.anyInt
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

/**
 * Created By @ilhamsuaib on 2020-01-03
 */


object CreateReviewViewModelTest : Spek({

    Feature("CreateReviewViewModel") {

        val productReputationFormUseCase = mockk<GetProductReputationForm>(relaxed = true)
        val sendReviewWithoutImageUseCase = mockk<SendReviewValidateUseCase>(relaxed = true)
        val sendReviewWithImageUseCase = mockk<SendReviewUseCase>()

        val mViewModel by lazy {
            CreateReviewViewModel(Dispatchers.Unconfined, productReputationFormUseCase, sendReviewWithoutImageUseCase, sendReviewWithImageUseCase)
        }

        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
        MockKAnnotations.init(this)

        Scenario("should success when get product reputation") {
            mockkObject(GetProductReputationForm)
            Given("mock product reputation form use case return to success") {
                coEvery {
                    productReputationFormUseCase.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
                } returns ProductRevGetForm()
            }

            When("run getProductReputation at view model") {
                mViewModel.getProductReputation(anyInt(), anyInt())
            }

            Then("it run get reputation at product reputation form use case") {
                coVerify {
                    productReputationFormUseCase.getReputationForm(GetProductReputationForm.createRequestParam(anyInt(), anyInt()))
                }
            }

            Then("it should update live data value state to success") {
                assertTrue(mViewModel.getReputationDataForm.value is Success)
            }
        }
    }
})