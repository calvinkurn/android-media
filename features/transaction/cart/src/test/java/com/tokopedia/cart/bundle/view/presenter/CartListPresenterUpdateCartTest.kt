package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.utils.DataProvider
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.uimodel.CartShopHolderData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription

object CartListPresenterUpdateCartTest : Spek({

    val getCartRevampV3UseCase: GetCartRevampV3UseCase = mockk()
    val deleteCartUseCase: DeleteCartUseCase = mockk()
    val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    val addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    val updateCartUseCase: UpdateCartUseCase = mockk()
    val updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    val validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase = mockk()
    val compositeSubscription = CompositeSubscription()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    val clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase = mockk()
    val getRecentViewUseCase: GetRecommendationUseCase = mockk()
    val getWishlistUseCase: GetWishlistUseCase = mockk()
    val getRecommendationUseCase: GetRecommendationUseCase = mockk()
    val addToCartUseCase: AddToCartUseCase = mockk()
    val addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    val seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    val updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    val setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    val followShopUseCase: FollowShopUseCase = mockk()
    val view: ICartListView = mockk(relaxed = true)

    Feature("update cart list") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, TestSchedulers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("success update cart") {

            val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000
                    quantity = 10
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
            }

            Given("shop data list for COD eligibility checking") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success") {
                verify {
                    view.renderToShipmentFormSuccess(any(), cartItemDataList, any(), any())
                }
            }
        }

        Scenario("success update cart with eligible COD") {

            val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000
                    quantity = 10
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllAvailableCartDataList() } answers { cartItemDataList }
            }

            Given("attach view") {
                cartListPresenter.attachView(view)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with eligible COD") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), true, any())
                }
            }
        }

        Scenario("success update cart with not eligible COD") {

            val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    isCod = false
                    productPrice = 1000000
                    quantity = 10
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with not eligible COD") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), false, any())
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("state user have not uncheck and recheck item") {
                cartListPresenter.setHasPerformChecklistChange(false)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_ALL_WITHOUT_CHANGES") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_ALL_WITH_CHANGES") {

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("state user have uncheck and recheck item") {
                cartListPresenter.setHasPerformChecklistChange(true)
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_ALL_WITH_CHANGES") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_ITEM") {

            val shopDataList = mutableListOf<CartShopHolderData>().apply {
                add(CartShopHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(CartItemHolderData().apply {
                            isSelected = false
                        })
                    }
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_ITEM") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_SHOP") {

            val shopDataList = mutableListOf<CartShopHolderData>().apply {
                add(CartShopHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(CartItemHolderData().apply {
                            isSelected = true
                        })
                    }
                    isAllSelected = true
                })
                add(CartShopHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(CartItemHolderData().apply {
                            isSelected = false
                        })
                    }
                    isAllSelected = false
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_SHOP") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP)
                }
            }
        }

        Scenario("success update cart with item change state ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM") {

            val shopDataList = mutableListOf<CartShopHolderData>().apply {
                add(CartShopHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(CartItemHolderData().apply {
                            isSelected = true
                        })
                        add(CartItemHolderData().apply {
                            isSelected = false
                        })
                    }
                    isAllSelected = false
                    isPartialSelected = true
                })
                add(CartShopHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(CartItemHolderData().apply {
                            isSelected = false
                        })
                    }
                    isAllSelected = false
                })
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            Given("shop data list") {
                every { view.getAllShopDataList() } answers { shopDataList }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render success with item change state ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM") {
                verify {
                    view.renderToShipmentFormSuccess(any(), any(), any(), CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM)
                }
            }
        }

        Scenario("failed update cart") {

            val throwable = ResponseErrorException("error")

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartFailed()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(throwable)
                }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should render error") {
                verify {
                    view.renderErrorToShipmentForm(throwable)
                }
            }
        }

        Scenario("failed update cart because data is empty") {

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { emptyList() }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(false)
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

        Scenario("failed update cart to save state because data is empty") {

            Given("shop data list") {
                every { view.getAllAvailableCartDataList() } answers { emptyList() }
            }

            When("process to update cart data") {
                cartListPresenter.processUpdateCartData(true)
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }

    }

})