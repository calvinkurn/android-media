package com.tokopedia.cart.bundle.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.bundle.domain.usecase.*
import com.tokopedia.cart.bundle.utils.DataProvider
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
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

object CartListPresenterUpdateCartForPromoTest : Spek({

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

    Feature("update cart list for promo action") {

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

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                val mockResponse = DataProvider.provideUpdateCartSuccess()
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
                }
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render success") {
                verify {
                    view.hideProgressLoading()
                    view.navigateToPromoRecommendation()
                }
            }
        }

        Scenario("failed update cart with exception") {

            val exception = ResponseErrorException("error message")

            val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
                add(CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000
                    quantity = 10
                })
            }

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { cartItemDataList }
            }

            Given("update cart data") {
                coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
                coEvery { updateCartUseCase.execute(any(), any()) } answers {
                    secondArg<(Throwable) -> Unit>().invoke(exception)
                }
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should render error") {
                verify {
                    view.showToastMessageRed(exception)
                }
            }
        }

        Scenario("failed update cart because data is empty") {

            Given("shop data list") {
                every { view.getAllSelectedCartDataList() } answers { emptyList() }
            }

            When("process to update cart data") {
                cartListPresenter.doUpdateCartForPromo()
            }

            Then("should hide progress loading") {
                verify {
                    view.hideProgressLoading()
                }
            }
        }
    }

})