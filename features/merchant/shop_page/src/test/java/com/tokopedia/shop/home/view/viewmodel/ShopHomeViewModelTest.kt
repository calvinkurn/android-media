package com.tokopedia.shop.home.view.viewmodel

import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.InstantTaskExecutorRuleSpek
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.junit.Assert.*

class ShopHomeViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("ShopHomeViewModel") {
        val mockShopId = "1234"
        val testCoroutineDispatcherProvider by lazy {
            TestCoroutineDispatcherProviderImpl
        }
        val getShopProductUseCase: GqlGetShopProductUseCase = mockk(relaxed = true)
        val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase = mockk(relaxed = true)
        val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
        val viewModel: ShopHomeViewModel by lazy {
            ShopHomeViewModel(
                    userSessionInterface,
                    getShopPageHomeLayoutUseCase,
                    getShopProductUseCase,
                    testCoroutineDispatcherProvider
            )
        }
        Scenario("Success get home layout") {
            Given("Mock use case response") {
                coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } returns ShopLayoutWidget()
                coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            }

            When("Get data") {
                viewModel.getShopPageHomeData(mockShopId)
            }

            Then("This function must  be called") {
                coVerify {
                    getShopPageHomeLayoutUseCase.executeOnBackground()
                    getShopProductUseCase.executeOnBackground()
                }
            }

            Then("Success") {
                assertTrue(viewModel.shopHomeLayoutData.value is Success)
                assertTrue(viewModel.productListData.value is Success)
            }
        }

        Scenario("Failed get home layout") {
            Given("Mock use case response") {
                coEvery { getShopPageHomeLayoutUseCase.executeOnBackground() } throws Exception()
                coEvery { getShopProductUseCase.executeOnBackground() } throws Exception()
            }

            When("Get data") {
                viewModel.getShopPageHomeData(mockShopId)
            }

            Then("This function must  be called") {
                coVerify {
                    getShopPageHomeLayoutUseCase.executeOnBackground()
                    getShopProductUseCase.executeOnBackground()
                }
            }
        }
    }
})