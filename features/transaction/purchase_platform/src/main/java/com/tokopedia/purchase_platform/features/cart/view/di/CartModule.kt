package com.tokopedia.purchase_platform.features.cart.view.di

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.base.IMapperUtil
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformQualifier
import com.tokopedia.purchase_platform.common.domain.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.domain.usecase.RemoveInsuranceProductUsecase
import com.tokopedia.purchase_platform.common.domain.usecase.UpdateInsuranceProductDataUsecase
import com.tokopedia.purchase_platform.common.utils.CartApiRequestParamGenerator
import com.tokopedia.purchase_platform.features.cart.data.api.CartApi
import com.tokopedia.purchase_platform.features.cart.data.repository.CartRepository
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapper
import com.tokopedia.purchase_platform.features.cart.domain.mapper.ICartMapper
import com.tokopedia.purchase_platform.features.cart.domain.mapper.IVoucherCouponMapper
import com.tokopedia.purchase_platform.features.cart.domain.mapper.VoucherCouponMapper
import com.tokopedia.purchase_platform.features.cart.domain.usecase.*
import com.tokopedia.purchase_platform.features.cart.view.CartItemDecoration
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import rx.subscriptions.CompositeSubscription
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@Module(includes = [
    PromoCheckoutModule::class,
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class
])
class CartModule {

    @Provides
    @CartScope
    fun provideCartApi(@PurchasePlatformQualifier retrofit: Retrofit): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    @CartScope
    fun provideICartRepository(cartApi: CartApi): ICartRepository {
        return CartRepository(cartApi)
    }

    @Provides
    @CartScope
    fun provideICartMapper(mapperUtil: IMapperUtil): ICartMapper {
        return CartMapper(mapperUtil)
    }

    @Provides
    @CartScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
    }

    @Provides
    @CartScope
    fun provideIVoucherCouponMapper(mapperUtil: IMapperUtil): IVoucherCouponMapper {
        return VoucherCouponMapper(mapperUtil)
    }

    @Provides
    @CartScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CartScope
    fun cartApiRequestParamGenerator(): CartApiRequestParamGenerator {
        return CartApiRequestParamGenerator()
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
    fun provideGetRecentViewUseCase(@ApplicationContext context: Context): GetRecentViewUseCase {
        return GetRecentViewUseCase(context)
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
    fun provideCheckoutAnalyticsCart(): CheckoutAnalyticsCart {
        return CheckoutAnalyticsCart()
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
    fun provideICartListPresenter(getCartListSimplifiedUseCase: GetCartListSimplifiedUseCase,
                                  deleteCartListUseCase: DeleteCartListUseCase,
                                  updateCartUseCase: UpdateCartUseCase,
                                  checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                                  checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                  checkPromoCodeCartListUseCase: CheckPromoCodeCartListUseCase,
                                  compositeSubscription: CompositeSubscription,
                                  cartApiRequestParamGenerator: CartApiRequestParamGenerator,
                                  addWishListUseCase: AddWishListUseCase,
                                  removeWishListUseCase: RemoveWishListUseCase,
                                  updateAndReloadCartUseCase: UpdateAndReloadCartUseCase,
                                  userSessionInterface: UserSessionInterface,
                                  clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                  getRecentViewUseCase: GetRecentViewUseCase,
                                  getWishlistUseCase: GetWishlistUseCase,
                                  getRecommendationUseCase: GetRecommendationUseCase,
                                  addToCartUseCase: AddToCartUseCase,
                                  getInsuranceCartUseCase: GetInsuranceCartUseCase,
                                  removeInsuranceProductUsecase: RemoveInsuranceProductUsecase,
                                  updateInsuranceProductDataUsecase: UpdateInsuranceProductDataUsecase): ICartListPresenter {
        return CartListPresenter(getCartListSimplifiedUseCase, deleteCartListUseCase,
                updateCartUseCase, checkPromoStackingCodeUseCase,
                checkPromoStackingCodeMapper, checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, addWishListUseCase, removeWishListUseCase,
                updateAndReloadCartUseCase, userSessionInterface,
                clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                getRecommendationUseCase, addToCartUseCase, getInsuranceCartUseCase,
                removeInsuranceProductUsecase, updateInsuranceProductDataUsecase)
    }

}