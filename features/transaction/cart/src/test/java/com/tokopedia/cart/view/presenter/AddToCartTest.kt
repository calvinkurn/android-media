package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import rx.Observable

class AddToCartTest : BaseCartTest() {

    @Test
    fun `WHEN add to cart wishlist item success THEN should render success`() {
        // GIVEN
        val productModel = CartWishlistItemHolderData(id = "0", shopId = "0")
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
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verifyOrder {
            view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
            view.showToastMessageGreen(addToCartDataModel.data.message[0])
        }
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
        cartListPresenter.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))

        // THEN
        verify {
            view.showToastMessageRed(errorMessage)
        }
    }

    @Test
    fun `WHEN add to cart recent view item success THEN should render success`() {
        // GIVEN
        val productModel = CartRecentViewItemHolderData(id = "0", shopId = "0")
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
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verifyOrder {
            view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
            view.showToastMessageGreen(addToCartDataModel.data.message[0])
        }
    }

    @Test
    fun `WHEN add to cart recent view item failed THEN should render error`() {
        // GIVEN
        val addToCartDataModel = AddToCartDataModel().apply {
            this.status = AddToCartDataModel.STATUS_ERROR
            this.data = DataModel()
            this.errorMessage = arrayListOf<String>().apply {
                add("Add to cart error")
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))

        // THEN
        verify {
            view.showToastMessageRed(addToCartDataModel.errorMessage[0])
        }
    }

    @Test
    fun `WHEN add to cart recent view item failed with exception THEN should render error`() {
        // GIVEN
        val exception = IllegalStateException("Add to cart error with exception")
        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))

        // THEN
        verify {
            view.showToastMessageRed(exception)
        }
    }

    @Test
    fun `WHEN add to cart recommendation item success THEN should render success`() {
        // GIVEN
        val productModel = CartRecommendationItemHolderData(false, RecommendationItem(productId = 0, shopId = 0))
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
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verifyOrder {
            view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
            view.showToastMessageGreen(addToCartDataModel.data.message[0])
        }
    }

    @Test
    fun `WHEN add to cart recommendation item failed THEN should render error`() {
        // GIVEN
        val productModel = CartRecommendationItemHolderData(false, RecommendationItem(productId = 0, shopId = 0))
        val addToCartDataModel = AddToCartDataModel().apply {
            status = AddToCartDataModel.STATUS_ERROR
            data = DataModel().apply {
                success = 1
            }
            errorMessage = arrayListOf<String>().apply {
                add("Add to cart error")
            }
        }

        coEvery { addToCartUseCase.setParams(any()) } just Runs
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
        every { userSessionInterface.userId } returns "123"

        // WHEN
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verify {
            view.showToastMessageRed(addToCartDataModel.errorMessage[0])
        }
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
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verifyOrder {
            view.triggerSendEnhancedEcommerceAddToCartSuccess(addToCartDataModel, productModel)
            view.showToastMessageGreen(addToCartDataModel.data.message[0])
        }
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
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verify {
            view.showToastMessageRed(errorMessage)
        }
    }

    @Test
    fun `WHEN add to cart wishlist item with view is detached THEN should not render view`() {
        // GIVEN
        val productModel = CartWishlistItemHolderData(id = "0", shopId = "0")
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

        cartListPresenter.detachView()

        // WHEN
        cartListPresenter.processAddToCart(productModel)

        // THEN
        verify(inverse = true) {
            view.showProgressLoading()
        }
    }
}
