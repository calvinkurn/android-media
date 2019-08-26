package com.tokopedia.purchase_platform.features.cart.view.di

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.example.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.base.IMapperUtil
import com.tokopedia.purchase_platform.common.base.MapperUtil
import com.tokopedia.purchase_platform.common.data.common.api.CartApiInterceptor
import com.tokopedia.purchase_platform.common.data.common.api.CartResponseConverter
import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApiUrl
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter
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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@Module(includes = [PromoCheckoutModule::class])
class CartModule {

    @Provides
    @CartScope
    fun provideRouter(@ApplicationContext context: Context): ICheckoutModuleRouter {
        return context as ICheckoutModuleRouter
    }

    @Provides
    @CartScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @CartScope
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSessionInterface)
    }

    @Provides
    @CartScope
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    @CartScope
    fun getCartApiInterceptor(@ApplicationContext context: Context): CartApiInterceptor {
        return CartApiInterceptor(context, context as AbstractionRouter, CommonPurchaseApiUrl.HMAC_KEY)
    }

    @Provides
    @CartScope
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
                CommonPurchaseApiUrl.NET_READ_TIMEOUT,
                CommonPurchaseApiUrl.NET_WRITE_TIMEOUT,
                CommonPurchaseApiUrl.NET_CONNECT_TIMEOUT,
                CommonPurchaseApiUrl.NET_RETRY
        )
    }

    @Provides
    @CartScope
    fun provideCartApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                   cartApiInterceptor: CartApiInterceptor,
                                   okHttpRetryPolicy: OkHttpRetryPolicy,
                                   fingerprintInterceptor: FingerprintInterceptor,
                                   chuckInterceptor: ChuckInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(AkamaiBotInterceptor())
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @CartScope
    fun provideCartApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(CommonPurchaseApiUrl.BASE_URL)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @CartScope
    fun provideCartApi(retrofit: Retrofit): CartApi {
        return retrofit.create(CartApi::class.java)
    }

    @Provides
    @CartScope
    fun provideICartRepository(cartApi: CartApi): ICartRepository {
        return CartRepository(cartApi)
    }

    @Provides
    @CartScope
    fun provideIMapperUtil(): IMapperUtil {
        return MapperUtil()
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
    fun provideICartListPresenter(getCartListUseCase: GetCartListUseCase,
                                  deleteCartListUseCase: DeleteCartListUseCase,
                                  updateCartUseCase: UpdateCartUseCase,
                                  resetCartGetCartListUseCase: ResetCartGetCartListUseCase,
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
                                  addToCartUseCase: AddToCartUseCase): ICartListPresenter {
        return CartListPresenter(getCartListUseCase, deleteCartListUseCase,
                updateCartUseCase, resetCartGetCartListUseCase, checkPromoStackingCodeUseCase,
                checkPromoStackingCodeMapper, checkPromoCodeCartListUseCase, compositeSubscription,
                cartApiRequestParamGenerator, addWishListUseCase, removeWishListUseCase,
                updateAndReloadCartUseCase, userSessionInterface,
                clearCacheAutoApplyStackUseCase, getRecentViewUseCase, getWishlistUseCase,
                getRecommendationUseCase, addToCartUseCase)
    }

}