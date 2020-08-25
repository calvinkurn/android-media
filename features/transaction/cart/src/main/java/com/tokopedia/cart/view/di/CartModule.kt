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
import com.tokopedia.cart.view.CartItemDecoration
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
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
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.common.schedulers.IOSchedulers
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
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
    @CartScope
    fun providesGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @CartScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @Provides
    @CartScope
    fun provideGetRecommendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSessionInterface)
    }

    @Provides
    @CartScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
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
    @Named("shopGroupSimplifiedQuery")
    fun provideGetCartListSimplifiedQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_shop_group_simplified)
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
    @Named("UpdateReloadUseCase")
    fun provideGetCartListSimplifiedUseCase(@Named("shopGroupSimplifiedQuery") queryString: String,
                                            cartSimplifiedMapper: CartSimplifiedMapper): GetCartListSimplifiedUseCase =
            GetCartListSimplifiedUseCase(queryString, GraphqlUseCase(), cartSimplifiedMapper, IOSchedulers)

    @Provides
    @CartScope
    fun provideICartListPresenter(getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                  deleteCartUseCase: DeleteCartUseCase,
                                  undoDeleteCartUseCase: UndoDeleteCartUseCase,
                                  updateCartUseCase: UpdateCartUseCase,
                                  compositeSubscription: CompositeSubscription,
                                  addWishListUseCase: AddWishListUseCase,
                                  removeWishListUseCase: RemoveWishListUseCase,
                                  updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
                                  userSessionInterface: UserSessionInterface,
                                  clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                  getRecentViewUseCase: GetRecentViewUseCase,
                                  getWishlistUseCase: GetWishlistUseCase,
                                  getRecommendationUseCase: GetRecommendationUseCase,
                                  addToCartUseCase: AddToCartUseCase,
                                  addToCartExternalUseCase: AddToCartExternalUseCase,
                                  getInsuranceCartUseCase: GetInsuranceCartUseCase,
                                  removeInsuranceProductUsecase: RemoveInsuranceProductUsecase,
                                  updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase,
                                  seamlessLoginUsecase: SeamlessLoginUsecase,
                                  updateCartCounterUseCase: UpdateCartCounterUseCase,
                                  updateCartAndValidateUseUseCase: UpdateCartAndValidateUseUseCase,
                                  validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                  schedulers: ExecutorSchedulers): ICartListPresenter {
        return CartListPresenter(getCartListSimplifiedUseCase, deleteCartUseCase,
                undoDeleteCartUseCase, updateCartUseCase, compositeSubscription,
                addWishListUseCase, removeWishListUseCase, updateAndReloadCartUseCase,
                userSessionInterface, clearCacheAutoApplyStackUseCase, getRecentViewUseCase,
                getWishlistUseCase, getRecommendationUseCase, addToCartUseCase, addToCartExternalUseCase,
                getInsuranceCartUseCase, removeInsuranceProductUsecase,
                updateInsuranceProductDataUsecase, seamlessLoginUsecase,
                updateCartCounterUseCase, updateCartAndValidateUseUseCase,
                validateUsePromoRevampUseCase, schedulers
        )
    }

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_UPDATE_CART_COUNTER)
    fun provideUpdateCartCounterMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_update_cart_counter)
    }

    @Provides
    @CartScope
    @Named(AtcConstant.MUTATION_ATC_EXTERNAL)
    fun provideAddToCartExternalMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart_external)
    }

}