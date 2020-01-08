package com.tokopedia.purchase_platform.features.cart.view.presenter

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.common.domain.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2020-01-07.
 */

class CartListPresenterAddToCartTest : Spek({

    val getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase = mockk()
    val deleteCartListUseCase: DeleteCartUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase = mockk()
    val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecentViewUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val getInsuranceCartUseCase: GetInsuranceCartUseCase = mockk()
    val removeInsuranceProductUsecase: RemoveInsuranceProductUsecase = mockk()
    val updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()

    Feature("add to cart") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartListSimplifiedUseCase, deleteCartListUseCase, updateCartUseCase,
                    checkPromoStackingCodeUseCase, checkPromoStackingCodeMapper, compositeSubscription,
                    addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                    removeInsuranceProductUsecase, updateInsuranceProductDataUsecase, seamlessLoginUsecase, TestSchedulers
            )
        }

        Scenario("success add to cart wishlist item") {

            val view: ICartListView = mockk(relaxed = true)
            val addToCartDataModel = AddToCartDataModel()
            val productModel = CartWishlistItemHolderData(id = "0", shopId = "0")
            val successMessage = "Success message add to cart"

            Given("add to cart data") {
                val dataModel = DataModel()
                val messages = arrayListOf<String>()
                messages.add(successMessage)
                dataModel.message = messages
                dataModel.success = 1
                addToCartDataModel.status = AddToCartDataModel.STATUS_OK
                addToCartDataModel.data = dataModel
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
                every { getCartListSimplifiedUseCase.createObservable(any()) } returns Observable.just(CartListData())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(productModel)
            }

            Then("should render success add to cart wishlist item") {
                verify {
                    view.hideProgressLoading()
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(any(), productModel)
                    view.showToastMessageGreen(successMessage)
                }
            }
        }

        Scenario("failed add to cart wishlist item") {

            val view: ICartListView = mockk(relaxed = true)
            lateinit var addToCartDataModel: AddToCartDataModel
            val errorMessage = "Add to cart error"

            Given("add to cart data") {
                addToCartDataModel = AddToCartDataModel().apply {
                    this.status = AddToCartDataModel.STATUS_ERROR
                    this.data = DataModel()
                    this.errorMessage = arrayListOf<String>().let { errorMessages ->
                        errorMessages.add(errorMessage)
                        errorMessages
                    }
                }
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("failed add to cart wishlist item with exception") {

            val view: ICartListView = mockk(relaxed = true)
            val errorMessage = "Add to cart error with exception"

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartWishlistItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("success add to cart recent view item") {

            val view: ICartListView = mockk(relaxed = true)
            val addToCartDataModel = AddToCartDataModel()

            Given("add to cart data") {
                val dataModel = DataModel()
                val messages = arrayListOf<String>()
                messages.add("Success message")
                dataModel.message = messages
                dataModel.success = 1
                addToCartDataModel.status = AddToCartDataModel.STATUS_OK
                addToCartDataModel.data = dataModel
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(any(), any())
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart recent view item") {

            val view: ICartListView = mockk(relaxed = true)
            lateinit var addToCartDataModel: AddToCartDataModel
            val errorMessage = "Add to cart error"

            Given("add to cart data") {
                addToCartDataModel = AddToCartDataModel().apply {
                    this.status = AddToCartDataModel.STATUS_ERROR
                    this.data = DataModel()
                    this.errorMessage = arrayListOf<String>().let { errorMessages ->
                        errorMessages.add(errorMessage)
                        errorMessages
                    }
                }
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("failed add to cart recent view item with exception") {

            val view: ICartListView = mockk(relaxed = true)

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.error(IllegalStateException())
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecentViewItemHolderData(id = "0", shopId = "0"))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(any())
                }
            }
        }

        Scenario("success add to cart recommendation item") {

            val view: ICartListView = mockk(relaxed = true)
            val addToCartDataModel = AddToCartDataModel()

            Given("add to cart data") {
                val dataModel = DataModel()
                val messages = arrayListOf<String>()
                messages.add("Success message")
                dataModel.message = messages
                dataModel.success = 1
                addToCartDataModel.status = AddToCartDataModel.STATUS_OK
                addToCartDataModel.data = dataModel
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecommendationItemHolderData(RecommendationItem(productId = 0, shopId = 0)))
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.triggerSendEnhancedEcommerceAddToCartSuccess(any(), any())
                    view.showToastMessageGreen(addToCartDataModel.data.message[0])
                }
            }
        }

        Scenario("failed add to cart recommendation item") {

            val view: ICartListView = mockk(relaxed = true)
            lateinit var addToCartDataModel: AddToCartDataModel
            val errorMessage = "Add to cart error"

            Given("add to cart data") {
                addToCartDataModel = AddToCartDataModel().apply {
                    this.status = AddToCartDataModel.STATUS_ERROR
                    this.data = DataModel().let { dataModel ->
                        dataModel.success = 1
                        dataModel
                    }
                    this.errorMessage = arrayListOf<String>().let { errorMessages ->
                        errorMessages.add(errorMessage)
                        errorMessages
                    }
                }
                every { addToCartUseCase.createObservable(any()) } returns Observable.just(addToCartDataModel)
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecommendationItemHolderData(RecommendationItem(productId = 0, shopId = 0)))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

        Scenario("failed add to cart wishlist item with exception") {

            val view: ICartListView = mockk(relaxed = true)
            val errorMessage = "Add to cart error with exception"

            Given("add to cart data") {
                every { addToCartUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processAddToCart(CartRecommendationItemHolderData(RecommendationItem(productId = 0, shopId = 0)))
            }

            Then("should show error") {
                verify {
                    view.hideProgressLoading()
                    view.showToastMessageRed(errorMessage)
                }
            }
        }

    }

})