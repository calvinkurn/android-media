package com.tokopedia.cart.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.AddCartToWishlistUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartRevampV3UseCase
import com.tokopedia.cart.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndValidateUseUseCase
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
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
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.GetWishlistV2UseCase
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import rx.subscriptions.CompositeSubscription

abstract class BaseCartTest {

    var getCartRevampV3UseCase: GetCartRevampV3UseCase = mockk()
    var deleteCartUseCase: DeleteCartUseCase = mockk()
    var undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    var addCartToWishlistUseCase: AddCartToWishlistUseCase = mockk()
    var updateCartUseCase: UpdateCartUseCase = mockk()
    var updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase = mockk()
    var validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase = mockk()
    var compositeSubscription: CompositeSubscription = mockk(relaxed = true)
    var addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    var addToWishListV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true)
    var removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    var deleteWishlistV2UseCase: DeleteWishlistV2UseCase = mockk(relaxed = true)
    var updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    var userSessionInterface: UserSessionInterface = mockk()
    var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase = mockk()
    var getRecentViewUseCase: GetRecommendationUseCase = mockk()
    var getWishlistUseCase: GetWishlistUseCase = mockk()
    var getWishlistV2UseCase: GetWishlistV2UseCase = mockk()
    var getRecommendationUseCase: GetRecommendationUseCase = mockk()
    var addToCartUseCase: AddToCartUseCase = mockk()
    var addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    var seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    var updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    var setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    var followShopUseCase: FollowShopUseCase = mockk()
    val boAffordabilityUseCase: BoAffordabilityUseCase = mockk()
    val coroutineTestDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers
    var view: ICartListView = mockk(relaxed = true)
    lateinit var cartListPresenter: ICartListPresenter

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartListPresenter = CartListPresenter(getCartRevampV3UseCase, deleteCartUseCase,
                undoDeleteCartUseCase, updateCartUseCase, compositeSubscription, addWishListUseCase,
                addToWishListV2UseCase, addCartToWishlistUseCase, removeWishListUseCase, deleteWishlistV2UseCase,
                updateAndReloadCartUseCase, userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                getWishlistUseCase, getWishlistV2UseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
                seamlessLoginUsecase, updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase, followShopUseCase,
                boAffordabilityUseCase, TestSchedulers, coroutineTestDispatchers
        )
        every { addWishListUseCase.unsubscribe() } just Runs
        every { removeWishListUseCase.unsubscribe() } just Runs
        every { addToWishListV2UseCase.cancelJobs() } just Runs
        every { deleteWishlistV2UseCase.cancelJobs() } just Runs
        cartListPresenter.attachView(view)
    }

    @After
    fun tearDown() {
        cartListPresenter.detachView()
    }

}