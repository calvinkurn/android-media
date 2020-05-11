package com.tokopedia.centralizedpromo.view.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.domain.usecase.GetOnGoingPromotionUseCase
import com.tokopedia.centralizedpromo.domain.usecase.GetPostUseCase
import com.tokopedia.centralizedpromo.view.LayoutType
import com.tokopedia.centralizedpromo.view.PromoCreationStaticData
import com.tokopedia.centralizedpromo.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@RunWith(JUnitPlatform::class)
class CentralizedPromoViewModelTest : Spek({
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

    Feature("CentralizedPromoViewModel") {
        val userSession: UserSessionInterface = mockk(relaxed = true)
        val getOnGoingPromotionUseCase: GetOnGoingPromotionUseCase = mockk(relaxed = true)
        val getPostUseCase: GetPostUseCase = mockk(relaxed = true)

        val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined

        val viewModel = CentralizedPromoViewModel(userSession, getOnGoingPromotionUseCase, getPostUseCase, dispatcher)

        CentralizedPromoViewModel::class.declaredMemberProperties.filter { it.name in arrayOf("startDate", "endDate") }.forEach {
            it.isAccessible = true
            it.get(viewModel)
        }

        CentralizedPromoViewModel::class.declaredMemberProperties.find { it.name == "shopId" }.let {
            it?.isAccessible = true
            it?.get(viewModel)
        }

        Scenario("Success get layout data for on going promotion") {
            val successResult = OnGoingPromoListUiModel(
                    title = "Track your promotion",
                    items = listOf(
                            OnGoingPromoUiModel(
                                    title = "Flash Sale",
                                    status = Status(
                                            text = "Terdaftar",
                                            count = 56,
                                            url = "sellerapp://flashsale/management"
                                    ),
                                    footer = Footer(
                                            text = "Lihat Semua",
                                            url = "sellerapp://flashsale/management"
                                    )
                            )
                    ),
                    errorMessage = ""
            )

            Given("getOnGoingPromoUseCase return 1 on going promo") {
                coEvery {
                    getOnGoingPromotionUseCase.executeOnBackground()
                } returns successResult
            }

            When("get layout data for on going promotion") {
                viewModel.getLayoutData(LayoutType.ON_GOING_PROMO)
            }

            Then("run usecase") {
                coVerify {
                    getOnGoingPromotionUseCase.executeOnBackground()
                }
            }

            Then("view model layout data for on going promotion is success") {
                val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
                assertTrue(result != null && result == Success(successResult))
            }
        }

        Scenario("Failed get layout data for on going promotion") {
            Given("getOnGoingPromoUseCase throw a throwable") {
                coEvery {
                    getOnGoingPromotionUseCase.executeOnBackground()
                } throws MessageErrorException("")
            }

            When("get layout data for on going promotion") {
                viewModel.getLayoutData(LayoutType.ON_GOING_PROMO)
            }

            Then("run usecase") {
                coVerify {
                    getOnGoingPromotionUseCase.executeOnBackground()
                }
            }

            Then("view model layout data for on going promotion is fail") {
                val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.ON_GOING_PROMO)
                assertTrue(result != null && result is Fail)
            }
        }

        Scenario("Success get layout data for post") {
            val successResult = PostListUiModel(
                    items = listOf(
                            PostUiModel(
                                    title = "Test Post",
                                    applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                    url = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                    featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                    subtitle = "<p>Info &#183; 20 SEP 19</p>"
                            ),
                            PostUiModel(
                                    title = "Test ke 2",
                                    applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                    url = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                    featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                    subtitle = "<p>Info &#183; 6 SEP 19</p>"
                            ),
                            PostUiModel(
                                    title = "Kumpul Keluarga Tokopedia Bersama Toko Cabang",
                                    applink = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                    url = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                    featuredMediaUrl = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/tokocabang-event-seller-center_1024x439/",
                                    subtitle = "<p>Seller Event &#183; 5 MAR 20</p>"
                            )
                    ),
                    errorMessage = ""
            )

            Given("getCentralizedPromoPostUseCase return 3 posts") {
                coEvery {
                    getPostUseCase.executeOnBackground()
                } returns successResult
            }

            When("get layout data for post") {
                viewModel.getLayoutData(LayoutType.POST)
            }

            Then("run usecase") {
                coVerify {
                    getPostUseCase.executeOnBackground()
                }
            }

            Then("view model layout data for posts is success") {
                val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.POST)
                assertTrue(result != null && result == Success(successResult))
            }
        }

        Scenario("Failed get layout data for posts") {
            Given("getCentralizedPromoPostUseCase throw a throwable") {
                coEvery {
                    getPostUseCase.executeOnBackground()
                } throws MessageErrorException("")
            }

            When("get layout data for post") {
                viewModel.getLayoutData(LayoutType.POST)
            }

            Then("run usecase") {
                coVerify {
                    getPostUseCase.executeOnBackground()
                }
            }

            Then("view model layout data for posts is fail") {
                val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.POST)
                assertTrue(result != null && result is Fail)
            }
        }

        Scenario("Success get layout data for promo creation") {
            val successResult = PromoCreationStaticData.provideStaticData()

            When("get layout data for post") {
                viewModel.getLayoutData(LayoutType.PROMO_CREATION)
            }

            Then("view model layout data for promo creation is success") {
                val result = viewModel.getLayoutResultLiveData.value?.get(LayoutType.PROMO_CREATION)
                assertTrue(result != null && result is Success)
            }
        }
    }
})