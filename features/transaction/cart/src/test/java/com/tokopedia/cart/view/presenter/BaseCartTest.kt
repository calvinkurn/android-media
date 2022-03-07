package com.tokopedia.cart.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
    var addWishListUseCase: AddWishListUseCase = mockk()
    var removeWishListUseCase: RemoveWishListUseCase = mockk()
    var updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    var userSessionInterface: UserSessionInterface = mockk()
    var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase = mockk()
    var getRecentViewUseCase: GetRecommendationUseCase = mockk()
    var getWishlistUseCase: GetWishlistUseCase = mockk()
    var getRecommendationUseCase: GetRecommendationUseCase = mockk()
    var addToCartUseCase: AddToCartUseCase = mockk()
    var addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    var seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    var updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    var setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    var followShopUseCase: FollowShopUseCase = mockk()
    var view: ICartListView = mockk(relaxed = true)
    lateinit var cartListPresenter: ICartListPresenter

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartListPresenter = CartListPresenter(getCartRevampV3UseCase, deleteCartUseCase,
                undoDeleteCartUseCase, updateCartUseCase, compositeSubscription, addWishListUseCase,
                addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
                seamlessLoginUsecase, updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase, followShopUseCase,
                TestSchedulers
        )
        every { addWishListUseCase.unsubscribe() } just Runs
        every { removeWishListUseCase.unsubscribe() } just Runs
        cartListPresenter.attachView(view)
    }

    @After
    fun tearDown() {
        cartListPresenter.detachView()
    }

}