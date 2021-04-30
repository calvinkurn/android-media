package com.tokopedia.cart.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import rx.subscriptions.CompositeSubscription

abstract class CartListPresenterBaseTest {

    @MockK
    protected lateinit var getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase

    @MockK
    protected lateinit var deleteCartUseCase: DeleteCartUseCase

    @MockK
    protected lateinit var undoDeleteCartUseCase: UndoDeleteCartUseCase

    @MockK
    protected lateinit var addCartToWishlistUseCase: AddCartToWishlistUseCase

    @MockK
    protected lateinit var updateCartUseCase: UpdateCartUseCase

    @MockK
    protected lateinit var updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase

    @MockK
    protected lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK
    protected lateinit var compositeSubscription: CompositeSubscription

    @MockK
    protected lateinit var addWishListUseCase: AddWishListUseCase

    @MockK
    protected lateinit var removeWishListUseCase: RemoveWishListUseCase

    @MockK
    protected lateinit var updateAndReloadCartUseCase: UpdateAndReloadCartUseCase

    @MockK
    protected lateinit var userSessionInterface: UserSessionInterface

    @MockK
    protected lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    protected lateinit var getRecentViewUseCase: GetRecommendationUseCase

    @MockK
    protected lateinit var getWishlistUseCase: GetWishlistUseCase

    @MockK
    protected lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @MockK
    protected lateinit var addToCartUseCase: AddToCartUseCase

    @MockK
    protected lateinit var addToCartExternalUseCase: AddToCartExternalUseCase

    @MockK
    protected lateinit var seamlessLoginUsecase: SeamlessLoginUsecase

    @MockK
    protected lateinit var updateCartCounterUseCase: UpdateCartCounterUseCase

    @MockK
    protected lateinit var setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase

    @MockK
    protected lateinit var followShopUseCase: FollowShopUseCase

    @MockK(relaxed = true)
    protected lateinit var view: ICartListView

    protected lateinit var presenter: CartListPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = CartListPresenter(getCartListSimplifiedUseCase, deleteCartUseCase,
                undoDeleteCartUseCase, updateCartUseCase, compositeSubscription, addWishListUseCase,
                addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
                seamlessLoginUsecase, updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase, followShopUseCase, TestSchedulers
        )

        presenter.attachView(view)
    }

}