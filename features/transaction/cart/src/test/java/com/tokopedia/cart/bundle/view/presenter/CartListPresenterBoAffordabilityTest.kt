package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.uimodel.CartShopBoAffordabilityData
import com.tokopedia.cart.bundle.view.uimodel.CartShopBoAffordabilityState
import com.tokopedia.cart.bundle.view.uimodel.CartShopHolderData
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityResponse
import com.tokopedia.logisticcart.boaffordability.model.BoAffordabilityTexts
import com.tokopedia.logisticcart.boaffordability.usecase.BoAffordabilityUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import rx.subscriptions.CompositeSubscription
import java.io.IOException

@ExperimentalCoroutinesApi
object CartListPresenterBoAffordabilityTest : Spek({

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
    val boAffordabilityUseCase: BoAffordabilityUseCase = mockk()
    val coroutineTestDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers
    val view: ICartListView = mockk(relaxed = true)

    Feature("BO Affordability") {

        val cartListPresenter by memoized {
            CartListPresenter(
                    getCartRevampV3UseCase, deleteCartUseCase, undoDeleteCartUseCase,
                    updateCartUseCase, compositeSubscription, addWishListUseCase,
                    addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                    userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                    getWishlistUseCase, getRecommendationUseCase, addToCartUseCase,
                    addToCartExternalUseCase, seamlessLoginUsecase, updateCartCounterUseCase,
                    updateCartAndValidateUseUseCase, validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase,
                    followShopUseCase, boAffordabilityUseCase, TestSchedulers, coroutineTestDispatchers
            )
        }

        beforeEachTest {
            cartListPresenter.attachView(view)
        }

        Scenario("get bo affordability success not afford") {

            val cartString = "123-123-123"
            val cartShopHolderData = CartShopHolderData(
                    cartString = cartString
            )
            val ticker = "+ Rp10.000 lagi untuk dapat bebas ongkir"

            Given("success response") {
                coEvery {
                    boAffordabilityUseCase.setParam(any()).executeOnBackground()
                } returns BoAffordabilityResponse(1_000, BoAffordabilityTexts(
                        tickerCart = ticker
                ))
            }

            When("process get bo affordability") {
                cartListPresenter.checkBoAffordability(cartShopHolderData)
                coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()
            }

            Then("should show success not afford") {
                verify {
                    cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                            state = CartShopBoAffordabilityState.SUCCESS_NOT_AFFORD,
                            tickerText = ticker
                    )
                    view.updateCartBoAffordability(cartShopHolderData)
                }
            }
        }

        Scenario("get bo affordability success afford") {

            val cartString = "123-123-123"
            val cartShopHolderData = CartShopHolderData(
                    cartString = cartString
            )
            val ticker = "dapat bebas ongkir"

            Given("success response") {
                coEvery {
                    boAffordabilityUseCase.setParam(any()).executeOnBackground()
                } returns BoAffordabilityResponse(0, BoAffordabilityTexts(
                        tickerCart = ticker
                ))
            }

            When("process get bo affordability") {
                cartListPresenter.checkBoAffordability(cartShopHolderData)
                coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()
            }

            Then("should show success afford") {
                verify {
                    cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                            state = CartShopBoAffordabilityState.SUCCESS_AFFORD,
                            tickerText = ticker
                    )
                    view.updateCartBoAffordability(cartShopHolderData)
                }
            }
        }

        Scenario("get bo affordability failed") {

            val cartString = "123-123-123"
            val cartShopHolderData = CartShopHolderData(
                    cartString = cartString
            )

            Given("success response") {
                coEvery {
                    boAffordabilityUseCase.setParam(any()).executeOnBackground()
                } throws IOException()
            }

            When("process get bo affordability") {
                cartListPresenter.checkBoAffordability(cartShopHolderData)
                coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()
            }

            Then("should show failed") {
                verify {
                    cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                            state = CartShopBoAffordabilityState.FAILED
                    )
                    view.updateCartBoAffordability(cartShopHolderData)
                }
            }
        }

        Scenario("get bo affordability failed due to overweight") {

            val cartString = "123-123-123"
            val cartShopHolderData = CartShopHolderData(
                    cartString = cartString,
                    maximumShippingWeight = 1.0,
                    maximumWeightWording = "overweight",
                    productUiModelList = arrayListOf(
                            CartItemHolderData(
                                    quantity = 10,
                                    productWeight = 1000
                            )
                    )
            )

            When("process get bo affordability") {
                cartListPresenter.checkBoAffordability(cartShopHolderData)
                coroutineTestDispatchers.coroutineDispatcher.advanceUntilIdle()
            }

            Then("should show failed") {
                verify {
                    cartShopHolderData.boAffordability = CartShopBoAffordabilityData(
                            state = CartShopBoAffordabilityState.FAILED
                    )
                    view.updateCartBoAffordability(cartShopHolderData)
                }
            }

            Then("should not get bo affordability") {
                coVerify(inverse = true) {
                    boAffordabilityUseCase.setParam(any()).executeOnBackground()
                }
            }
        }
    }
})