package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.data.GetTickerRepository
import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

@RunWith(JUnitPlatform::class)
class SellerHomeViewModelTestSpek : Spek({

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

    Feature("SellerHomeViewModel") {

        val userSession: UserSessionInterface = mockk(relaxed = true)
        val getShopStatusUseCase: GetStatusShopUseCase = mockk(relaxed = true)
        val getTickerUseCase: GetTickerUseCase = mockk(relaxed = true)
        val getLayoutUseCase: GetLayoutUseCase = mockk(relaxed = true)
        val getShopLocationUseCase: GetShopLocationUseCase = mockk(relaxed = true)
        val getCardDataUseCase: GetCardDataUseCase = mockk(relaxed = true)
        val getLineGraphDataUseCase: GetLineGraphDataUseCase = mockk(relaxed = true)
        val getProgressDataUseCase: GetProgressDataUseCase = mockk(relaxed = true)
        val getPostDataUseCase: GetPostDataUseCase = mockk(relaxed = true)
        val getCarouselDataUseCase: GetCarouselDataUseCase = mockk(relaxed = true)

        val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined

        val sellerHomeViewModel: SellerHomeViewModel by lazy {
            SellerHomeViewModel(getShopStatusUseCase, userSession, getTickerUseCase, getLayoutUseCase, getShopLocationUseCase, getCardDataUseCase,
                    getLineGraphDataUseCase, getProgressDataUseCase, getPostDataUseCase, getCarouselDataUseCase, dispatcher)
        }

        Scenario("success get ticker") {

            val tickerUiModel = TickerUiModel(
                    "","","","","","","","","","","","",""
            )
            val tickerList: List<TickerUiModel> = listOf(tickerUiModel)

            Given("getTickerUseCase return empty list") {
                coEvery {
                    getTickerUseCase.executeOnBackground()
                } returns tickerList
            }

            When("get ticker") {
                sellerHomeViewModel.getTicker()
            }

            Then("run usecase") {
                coVerify {
                    getTickerUseCase.executeOnBackground()
                }
            }

            Then("view model value is success") {
                val result = sellerHomeViewModel.homeTicker.value
                assertTrue(result == Success(tickerList))
            }

        }

        Scenario("error get ticker") {

            Given("getTickerUseCase throw error") {
                coEvery {
                    getTickerUseCase.executeOnBackground()
                } throws Throwable()
            }

            When("get ticker") {
                sellerHomeViewModel.getTicker()
            }

            Then("run usecase") {
                coVerify {
                    getTickerUseCase.executeOnBackground()
                }
            }

            Then("view model value is Fail") {
                assertTrue(sellerHomeViewModel.homeTicker.value is Fail)
            }
        }

        Scenario("success get widget layout") {

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams("12345678")

            val dummyTooltipUiModel = TooltipUiModel("", "", listOf(TooltipListItemUiModel("", "")))
            val dummyCardDataUiModel = CardDataUiModel("","","","","")
            val dummyImpressHolder = ImpressHolder()
            val dummyBaseWidgetUiModel = CardWidgetUiModel(
                    "", "","",dummyTooltipUiModel,"","","","", dummyCardDataUiModel,dummyImpressHolder
            )

            val dummySuccessWidgetLayout : List<BaseWidgetUiModel<*>> = listOf(dummyBaseWidgetUiModel)

            Given("getLayoutUseCase return success") {
                coEvery {
                    getLayoutUseCase.executeOnBackground()
                } returns dummySuccessWidgetLayout
                coEvery {
                    userSession.shopId
                } returns "12345678"
            }

            When("get layout") {
                sellerHomeViewModel.getWidgetLayout()
            }

            Then("run usecase") {
                coVerify { getLayoutUseCase.executeOnBackground() }
            }

            Then("view model value is Success") {
                assertTrue(sellerHomeViewModel.widgetLayout.value == Success(dummySuccessWidgetLayout))
            }
        }

        Scenario("error get widget layout") {

            getLayoutUseCase.params = GetLayoutUseCase.getRequestParams("12345678")

            Given("getLayoutUseCase return error") {
                coEvery {
                    getLayoutUseCase.executeOnBackground()
                } throws Throwable()
            }

            When("get layout") {
                sellerHomeViewModel.getWidgetLayout()
            }

            Then("run usecase") {
                coVerify { getLayoutUseCase.executeOnBackground() }
            }

            Then("view model value is Fail") {
                assertTrue(sellerHomeViewModel.widgetLayout.value is Fail)
            }
        }

        Scenario("success get card widget") {

            val dummyCardDataUiModel = CardDataUiModel("","","","","")

            val dummyCardDataResult : List<CardDataUiModel> = listOf(dummyCardDataUiModel)
            val stringList: List<String> = listOf("")
            val shopId = "12345678"

            Given("getCardWidget return success") {
                coEvery {
                    getCardDataUseCase.executeOnBackground()
                } returns dummyCardDataResult
                coEvery {
                    userSession.shopId
                } returns shopId
            }

            When("get card data") {
                sellerHomeViewModel.getCardWidgetData(stringList)
            }

            Then("view model value is Success") {
                assertTrue(sellerHomeViewModel.cardWidgetData.value == Success(dummyCardDataResult))
            }
        }

        Scenario("error get card widget") {
            val stringList: List<String> = listOf("")
            val shopId = "12345678"

            Given("getCardWidget return success") {
                coEvery {
                    getCardDataUseCase.executeOnBackground()
                } throws Throwable()
                coEvery {
                    userSession.shopId
                } returns shopId
            }

            When("Get Card Data") {
                sellerHomeViewModel.getCardWidgetData(stringList)
            }

            Then("view model value is Fail") {
                assertTrue(sellerHomeViewModel.cardWidgetData.value is Fail)
            }
        }

        Scenario("Success get line graph data") {
            val dummyGraphList = listOf(XYAxisUiModel("", "", 0))
            val dummyXYAxisUiModel = listOf(XYAxisUiModel("", "", 0))
            val dummyLineGraphDataUiModel = LineGraphDataUiModel("","","","", dummyGraphList, dummyXYAxisUiModel)
            val dummyList = listOf(dummyLineGraphDataUiModel)
            val dummyDataKeys = listOf("")

            Given("getLineGraphWidgetData returns success") {
                coEvery {
                    getLineGraphDataUseCase.executeOnBackground()
                } returns dummyList
            }

            When("get line graph widget data") {
                sellerHomeViewModel.getLineGraphWidgetData(dummyDataKeys)
            }

            Then("view model value is success") {
                val result = sellerHomeViewModel.lineGraphWidgetData.value
                assertTrue(result == Success(dummyList))
            }
        }

    }
})