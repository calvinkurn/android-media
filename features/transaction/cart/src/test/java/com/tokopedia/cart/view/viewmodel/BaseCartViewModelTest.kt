package com.tokopedia.cart.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cart.domain.usecase.CartShopGroupTickerAggregatorUseCase
import com.tokopedia.cart.domain.usecase.FollowShopUseCase
import com.tokopedia.cart.domain.usecase.GetCartRevampV4UseCase
import com.tokopedia.cart.domain.usecase.UpdateAndReloadCartUseCase
import com.tokopedia.cart.domain.usecase.UpdateCartAndGetLastApplyUseCase
import com.tokopedia.cart.view.ICartListView
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UndoDeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.cartrevamp.domain.usecase.SetCartlistCheckboxStateUseCase
import com.tokopedia.cartrevamp.view.CartViewModel
import com.tokopedia.cartrevamp.view.processor.CartCalculator
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.GetWishlistV2UseCase
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import rx.subscriptions.CompositeSubscription

abstract class BaseCartViewModelTest {

    var getCartRevampV4UseCase: GetCartRevampV4UseCase = mockk()
    var deleteCartUseCase: DeleteCartUseCase = mockk()
    var undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    var updateCartUseCase: UpdateCartUseCase = mockk()
    var updateCartAndGetLastApplyUseCase: UpdateCartAndGetLastApplyUseCase = mockk()
    var compositeSubscription: CompositeSubscription = mockk(relaxed = true)
    var addToWishListV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true)
    var deleteWishlistV2UseCase: DeleteWishlistV2UseCase = mockk(relaxed = true)
    var updateAndReloadCartUseCase: UpdateAndReloadCartUseCase = mockk()
    var userSessionInterface: UserSessionInterface = mockk()
    var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase = mockk()
    var getRecentViewUseCase: GetRecommendationUseCase = mockk()
    var getWishlistV2UseCase: GetWishlistV2UseCase = mockk()
    var getRecommendationUseCase: GetRecommendationUseCase = mockk()
    var addToCartUseCase: AddToCartUseCase = mockk()
    var addToCartExternalUseCase: AddToCartExternalUseCase = mockk()
    var seamlessLoginUsecase: SeamlessLoginUsecase = mockk()
    var updateCartCounterUseCase: UpdateCartCounterUseCase = mockk()
    var setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase = mockk()
    var followShopUseCase: FollowShopUseCase = mockk()
    val cartShopGroupTickerAggregatorUseCase: CartShopGroupTickerAggregatorUseCase = mockk()
    val coroutineTestDispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers
    var view: ICartListView = mockk(relaxed = true)
    lateinit var cartViewModel: CartViewModel

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartViewModel = CartViewModel(
            getCartRevampV4UseCase, deleteCartUseCase,
            undoDeleteCartUseCase, updateCartUseCase, compositeSubscription,
            addToWishListV2UseCase, deleteWishlistV2UseCase,
            updateAndReloadCartUseCase, userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
            getWishlistV2UseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
            seamlessLoginUsecase, updateCartCounterUseCase, updateCartAndGetLastApplyUseCase,
            setCartlistCheckboxStateUseCase, followShopUseCase,
            cartShopGroupTickerAggregatorUseCase, TestSchedulers, coroutineTestDispatchers,
            CartCalculator()
        )
        every { addToWishListV2UseCase.cancelJobs() } just Runs
        every { deleteWishlistV2UseCase.cancelJobs() } just Runs
    }
}
