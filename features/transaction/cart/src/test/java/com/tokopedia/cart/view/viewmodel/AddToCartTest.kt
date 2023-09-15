package com.tokopedia.cart.view.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.AddToCartEvent
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class AddToCartTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN add to cart wishlist item success THEN should render success`() {
        // GIVEN
        val productModel = CartWishlistItemHolderData(
            id = "0",
            shopId = "0",
            name = "a",
            category = "s",
            price = "1",
            minOrder = 1
        )
        val successMessage = "Success message add to cart"
        val addToCartDataModel = AddToCartDataModel().apply {
            status = AddToCartDataModel.STATUS_OK
            data = DataModel().apply {
                message = arrayListOf<String>().apply {
                    add(successMessage)
                }
                success = 1
            }
        }
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        assertEquals(
            AddToCartEvent.Success(addToCartDataModel, productModel),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart wishlist item failed THEN should render error`() {
        // GIVEN
        val errorMessage = "Add to cart error"
        val addToCartDataModel = AddToCartDataModel().apply {
            this.status = AddToCartDataModel.STATUS_ERROR
            this.data = DataModel()
            this.errorMessage = arrayListOf<String>().apply {
                add(errorMessage)
            }
        }
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))

        // THEN
        MatcherAssert.assertThat(
            cartViewModel.addToCartEvent.value,
            CoreMatchers.instanceOf<Any>(AddToCartEvent.Failed::class.java)
        )
    }

    @Test
    fun `WHEN add to cart wishlist item failed with exception THEN should render error`() {
        // GIVEN
        val exception = IllegalStateException("Add to cart error with exception")
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))

        // THEN
        assertEquals(
            AddToCartEvent.Failed(exception),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart recent view item success THEN should render success`() {
        // GIVEN
        val productModel = CartRecentViewItemHolderData(
            id = "0",
            shopId = "0",
            name = "a",
            price = "1",
            minOrder = 1,
            clickUrl = "https://",
            isTopAds = true
        )
        val addToCartDataModel = AddToCartDataModel().apply {
            status = AddToCartDataModel.STATUS_OK
            data = DataModel().apply {
                success = 1
                message = arrayListOf<String>().apply {
                    add("Success message")
                }
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        assertEquals(
            AddToCartEvent.Success(addToCartDataModel, productModel),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart recent view item failed THEN should render error`() {
        // GIVEN
        val errorMessage = "Add to cart error"
        val addToCartDataModel = AddToCartDataModel().apply {
            this.status = AddToCartDataModel.STATUS_ERROR
            this.data = DataModel()
            this.errorMessage = arrayListOf<String>().apply {
                add(errorMessage)
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))

        // THEN
        MatcherAssert.assertThat(
            cartViewModel.addToCartEvent.value,
            CoreMatchers.instanceOf<Any>(AddToCartEvent.Failed::class.java)
        )
    }

    @Test
    fun `WHEN add to cart recent view item failed with exception THEN should render error`() {
        // GIVEN
        val exception = IllegalStateException("Add to cart error with exception")
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))

        // THEN
        assertEquals(
            AddToCartEvent.Failed(exception),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart recommendation item success THEN should render success`() {
        // GIVEN
        val productModel =
            CartRecommendationItemHolderData(
                false,
                RecommendationItem(
                    productId = 0,
                    shopId = 0,
                    name = "a",
                    categoryBreadcrumbs = "s",
                    price = "1",
                    minOrder = 1,
                    clickUrl = "https://"
                )
            )
        val addToCartDataModel = AddToCartDataModel().apply {
            status = AddToCartDataModel.STATUS_OK
            data = DataModel().apply {
                success = 1
                message = arrayListOf<String>().apply {
                    add("ATC Success message")
                }
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()

        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        assertEquals(
            AddToCartEvent.Success(addToCartDataModel, productModel),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart recommendation item failed THEN should render error`() {
        // GIVEN
        val errorMessage = "Add to cart error"
        val productModel =
            CartRecommendationItemHolderData(false, RecommendationItem(productId = 0, shopId = 0))
        val addToCartDataModel = AddToCartDataModel().apply {
            this.status = AddToCartDataModel.STATUS_ERROR
            this.data = DataModel().apply {
                success = 1
            }
            this.errorMessage = arrayListOf<String>().apply {
                add(errorMessage)
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        MatcherAssert.assertThat(
            cartViewModel.addToCartEvent.value,
            CoreMatchers.instanceOf<Any>(AddToCartEvent.Failed::class.java)
        )
    }

    @Test
    fun `WHEN add to cart recommendation item failed with exception THEN should render error`() {
        // GIVEN
        val exception = IllegalStateException("Add to cart error with exception")
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(
            CartRecommendationItemHolderData(
                false,
                RecommendationItem(productId = 0, shopId = 0)
            )
        )

        // THEN
        assertEquals(
            AddToCartEvent.Failed(exception),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart shop ads item success THEN should render success`() {
        // GIVEN
        val productModel =
            BannerShopProductUiModel(
                CpmData(),
                ProductCardModel(),
                "",
                "",
                "https://"
            )
        productModel.apply {
            productId = "1"
            shopId = "1"
            productName = "a"
            productCategory = "s"
            productPrice = "1"
            productMinOrder = 1
        }
        val successMessage = "Success message add to cart"
        val addToCartDataModel = AddToCartDataModel().apply {
            status = AddToCartDataModel.STATUS_OK
            data = DataModel().apply {
                message = arrayListOf<String>().apply {
                    add(successMessage)
                }
                success = 1
            }
        }
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { updateCartCounterUseCase.createObservable(any()) } returns Observable.just(0)
        coEvery { getCartRevampV4UseCase(any()) } returns CartData()
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        assertEquals(
            AddToCartEvent.Success(addToCartDataModel, productModel),
            cartViewModel.addToCartEvent.value
        )
    }

    @Test
    fun `WHEN add to cart shop ads item failed THEN should render error`() {
        // GIVEN
        val errorMessage = "Add to cart error"
        val productModel =
            BannerShopProductUiModel(
                CpmData(),
                ProductCardModel(),
                "",
                "",
                ""
            )
        productModel.apply {
            productId = "1"
            shopId = "1"
            productName = "a"
            productCategory = "s"
            productPrice = "1"
            productMinOrder = 1
        }
        val addToCartDataModel = AddToCartDataModel().apply {
            this.status = AddToCartDataModel.STATUS_ERROR
            this.data = DataModel()
            this.errorMessage = arrayListOf<String>().apply {
                add(errorMessage)
            }
        }
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(productModel)

        // THEN
        MatcherAssert.assertThat(
            cartViewModel.addToCartEvent.value,
            CoreMatchers.instanceOf<Any>(AddToCartEvent.Failed::class.java)
        )
    }

    @Test
    fun `WHEN add to cart shop ads item failed with exception THEN should render error`() {
        // GIVEN
        val exception = IllegalStateException("Add to cart error with exception")
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartViewModel.processAddToCart(
            BannerShopProductUiModel(
                CpmData(),
                ProductCardModel(),
                "",
                "",
                ""
            ).apply {
                productId = "1"
                shopId = "1"
                productName = "a"
                productCategory = "s"
                productPrice = "1"
                productMinOrder = 1
            }
        )

        // THEN
        assertEquals(
            AddToCartEvent.Failed(exception),
            cartViewModel.addToCartEvent.value
        )
    }
}
