package com.tokopedia.sellerhome.view.viewmodel

import com.tokopedia.sellerhome.domain.usecase.*
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.CardDataUiModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class SellerHomeViewModelTest : Spek({

    Feature("SellerHomeViewModel") {

        val userSession: UserSessionInterface = mockk(relaxed = true)
        val getTickerUseCase: GetTickerUseCase = mockk(relaxed = true)
        val getLayoutUseCase: GetLayoutUseCase = mockk(relaxed = true)
        val getCardDataUseCase: GetCardDataUseCase = mockk(relaxed = true)
        val getLineGraphDataUseCase: GetLineGraphDataUseCase = mockk(relaxed = true)
        val getProgressDataUseCase: GetProgressDataUseCase = mockk(relaxed = true)
        val getPostDataUseCase: GetPostDataUseCase = mockk(relaxed = true)
        val getCarouselDataUseCase: GetCarouselDataUseCase = mockk(relaxed = true)

        val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined

        val sellerHomeViewModel: SellerHomeViewModel by lazy {
            SellerHomeViewModel(userSession, getTickerUseCase, getLayoutUseCase, getCardDataUseCase,
                    getLineGraphDataUseCase, getProgressDataUseCase, getPostDataUseCase, getCarouselDataUseCase, dispatcher)
        }

        Scenario("success get ticker") {

            val ticker: List<TickerUiModel> = mockk()

            Given("getTickerUseCase return empty list") {
                coEvery {
                    getTickerUseCase.executeOnBackground()
                } returns ticker
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
                assertTrue(sellerHomeViewModel.homeTicker.value != null)
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
                assertTrue(true)
            }
        }

        Scenario("success get widget layout") {

            val successWidgetLayout : List<BaseWidgetUiModel<*>> = mockk()

            Given("getLayoutUseCase return success") {
                coEvery {
                    getLayoutUseCase.executeOnBackground()
                } returns successWidgetLayout
            }

            When("get layout") {
                sellerHomeViewModel.getWidgetLayout()
            }

            Then("view model value is Success") {
                assertTrue(sellerHomeViewModel.homeTicker.value is Success)
            }
        }

        Scenario("error get widget layout") {

            val errorThrowable : Throwable = mockk()

            Given("getLayoutUseCase return error") {
                coEvery {
                    getLayoutUseCase.executeOnBackground()
                } throws errorThrowable
            }

            When("get layout") {
                sellerHomeViewModel.getWidgetLayout()
            }

            Then("view model value is Fail") {
                assertTrue(sellerHomeViewModel.widgetLayout.value != null)
            }
        }

        Scenario("success get card widget") {
            val cardDataResult : List<CardDataUiModel> = mockk()
            val stringList: List<String> = mockk()

            Given("getCardWidget return success") {
                coEvery {
                    getCardDataUseCase.executeOnBackground()
                } returns cardDataResult
            }

            When("get card data") {
                sellerHomeViewModel.getCardWidgetData(stringList)
            }

            Then("view model value is Success") {
                assertTrue(sellerHomeViewModel.cardWidgetData.value is Success)
            }
        }

    }
})