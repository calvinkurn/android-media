package com.tokopedia.shop.home.view.viewmodel

import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.domain.interactor.ShopPageHomeGetlayoutUseCase
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ShopHomeViewModelTest : Spek({

    Feature("ShopHomeViewModel") {
        val testCoroutineDispatcherProvider by lazy {
            TestCoroutineDispatcherProviderImpl
        }
        val getShopProductUseCase: GqlGetShopProductUseCase = mockk(relaxed = true)
        val shopPageHomeGetlayoutUseCase: ShopPageHomeGetlayoutUseCase = mockk(relaxed = true)
        val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
        val viewModel: ShopHomeViewModel by lazy {
            ShopHomeViewModel(
                    userSessionInterface,
                    getShopProductUseCase,
                    shopPageHomeGetlayoutUseCase,
                    testCoroutineDispatcherProvider
            )
        }
        Scenario("Success get home layout") {
            Given("Mock use case response") {
                coEvery { getShopProductUseCase.executeOnBackground() } returns ShopProduct.GetShopProduct()
            }

            When("Get data"){
                viewModel.getShopPageHomeData()
            }

            Then("Success"){
//                assertTrue(viewModel.shopHomeLayoutData.value is Success)
//                assertTrue(viewModel.productListData.value is Success)
            }
        }



    }

})