package com.tokopedia.cart.view.di

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.domain.usecase.AddToCartExternalUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.cart.R
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.usecase.*
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.decorator.CartItemDecoration
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@Module(includes = [
    RecommendationModule::class,
    PromoCheckoutModule::class,
    PurchasePlatformBaseModule::class
])
class CartModule {

    @Provides
    @CartScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @Provides
    @CartScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CartScope
    fun provideGetWishlistUseCase(@ApplicationContext context: Context): GetWishlistUseCase {
        return GetWishlistUseCase(context)
    }

    @Provides
    @CartScope
    fun providesAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @Provides
    @CartScope
    fun providesRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    fun providesGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @CartScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }

    @Provides
    @CartScope
    fun provideCartItemDecoration(): RecyclerView.ItemDecoration {
        return CartItemDecoration()
    }

    @Provides
    @CartScope
    fun provideCheckoutAnalyticsCart(@ApplicationContext context: Context): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart(context)
    }

    @Provides
    @CartScope
    fun provideTrackingPromoCheckoutUtil(): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }

    @Provides
    @CartScope
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @CartScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @Provides
    @CartScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @CartScope
    fun provideGetCartListSimplifiedUseCase(cartSimplifiedMapper: CartSimplifiedMapper, chosenAddressRequestHelper: ChosenAddressRequestHelper): GetCartListSimplifiedUseCase =
            GetCartListSimplifiedUseCase(GraphqlUseCase(), cartSimplifiedMapper, DefaultSchedulers, chosenAddressRequestHelper)

    @Provides
    @CartScope
    fun provideSetCartlistCheckboxStateUseCase(): SetCartlistCheckboxStateUseCase =
            SetCartlistCheckboxStateUseCase(GraphqlUseCase(), DefaultSchedulers)

    @Provides
    @CartScope
    fun provideICartListPresenter(getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                  deleteCartUseCase: DeleteCartUseCase,
                                  undoDeleteCartUseCase: UndoDeleteCartUseCase,
                                  updateCartUseCase: UpdateCartUseCase,
                                  compositeSubscription: CompositeSubscription,
                                  addWishListUseCase: AddWishListUseCase,
                                  addCartToWishlistUseCase: AddCartToWishlistUseCase,
                                  removeWishListUseCase: RemoveWishListUseCase,
                                  updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
                                  userSessionInterface: UserSessionInterface,
                                  clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                  getRecentViewUseCase: GetRecommendationUseCase,
                                  getWishlistUseCase: GetWishlistUseCase,
                                  getRecommendationUseCase: GetRecommendationUseCase,
                                  addToCartUseCase: AddToCartUseCase,
                                  addToCartExternalUseCase: AddToCartExternalUseCase,
                                  seamlessLoginUsecase: SeamlessLoginUsecase,
                                  updateCartCounterUseCase: UpdateCartCounterUseCase,
                                  updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase,
                                  validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                  setCartlistCheckboxStateUseCase: SetCartlistCheckboxStateUseCase,
                                  followShopUseCase: FollowShopUseCase,
                                  schedulers: ExecutorSchedulers): ICartListPresenter {
        return CartListPresenter(getCartListSimplifiedUseCase, deleteCartUseCase,
                undoDeleteCartUseCase, updateCartUseCase, compositeSubscription, addWishListUseCase,
                addCartToWishlistUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
                seamlessLoginUsecase, updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                validateUsePromoRevampUseCase, setCartlistCheckboxStateUseCase, followShopUseCase, schedulers
        )
    }

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.gql_update_cart_counter)
    }

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_ATC_EXTERNAL)
    fun provideAddToCartExternalMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_external)
    }

    @Provides
    @CartScope
    @Named(FollowShopUseCase.MUTATION_NAME)
    fun provideFollowShopMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.shop.common.R.raw.gql_mutation_favorite_shop)
    }

}