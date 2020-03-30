package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.usecase.GetCategoryRecommendationUseCase
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class GetCategoryRecommendationUseCaseTest : Spek({

    this.beforeGroup {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }
    this.afterGroup {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

    Feature("AddEditProductDetailViewModel") {
        val provider: ResourceProvider = mockk(relaxed = true)
        val getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase = mockk(relaxed = true)

        val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined

        val viewmodel by lazy {
            AddEditProductDetailViewModel(provider, getCategoryRecommendationUseCase, dispatcher)
        }

        Scenario("success get category recommendation") {
            val successResult = listOf(ListItemUnify("", ""), ListItemUnify("", ""), ListItemUnify("", ""))

            Given("getCategoryRecommendation return list of category recommendation") {
                coEvery {
                    getCategoryRecommendationUseCase.executeOnBackground()
                } returns successResult
            }

            When("get category recommendation") {
                viewmodel.getCategoryRecommendation("baju")
            }

            Then("run use case") {
                coVerify {
                    getCategoryRecommendationUseCase.executeOnBackground()
                }
            }

            Then("get category recommendation result is success") {
                val result = viewmodel.productCategoryRecommendationLiveData.value
                Assert.assertTrue(result != null && result == Success(successResult))
            }
        }

        Scenario("failed get category recommendation") {

            Given("getCategoryRecommendation throw a throwable") {
                coEvery {
                    getCategoryRecommendationUseCase.executeOnBackground()
                } throws MessageErrorException("")
            }

            When("get category recommendation") {
                viewmodel.getCategoryRecommendation("baju")
            }

            Then("run use case") {
                coVerify {
                    getCategoryRecommendationUseCase.executeOnBackground()
                }
            }

            Then("get category recommendation is failed") {
                Thread.sleep(2000)
                val result = viewmodel.productCategoryRecommendationLiveData.value
                Assert.assertTrue(result != null && result is Fail)
            }
        }
    }
})