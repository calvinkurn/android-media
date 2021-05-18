package com.tokopedia.shop.home.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.ShopPlayWidgetAnalyticListener
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.home.GqlQueryConstant.GQL_ATC_MUTATION
import com.tokopedia.shop.home.GqlQueryConstant.GQL_ATC_OCC_MUTATION
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

@Module(includes = [ShopPageHomeViewModelModule::class, PlayWidgetModule::class])
class ShopPageHomeModule {

    @ShopPageHomeScope
    @Provides
    @Named(GQL_ATC_MUTATION)
    fun provideAddToCartMutation(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart);
    }

    @ShopPageHomeScope
    @Provides
    @Named(GQL_ATC_OCC_MUTATION)
    fun provideAddToCartOCCMutation(@ShopPageContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart_one_click_checkout);
    }

    @ShopPageHomeScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @ShopPageHomeScope
    @Provides
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor, commonErrorResponseInterceptor)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ShopPageContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @ShopPageHomeScope
    @Provides
    fun provideAddToWishListUseCase(@ShopPageContext context: Context?): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRemoveFromWishListUseCase(@ShopPageContext context: Context?): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideGetYoutubeVideoUseCase(restRepository: RestRepository): GetYoutubeVideoDetailUseCase {
        return GetYoutubeVideoDetailUseCase(restRepository)
    }

    @ShopPageHomeScope
    @Provides
    fun provideUserSessionInterface(@ShopPageContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageHomeScope
    @Provides
    fun provideTrackingQueue(@ShopPageContext context: Context) = TrackingQueue(context)


    @ShopPageHomeScope
    @Provides
    fun provideShopPageHomeTracking(trackingQueue: TrackingQueue): ShopPageHomeTracking {
        return ShopPageHomeTracking(trackingQueue)
    }

    @ShopPageHomeScope
    @Provides
    fun provideShopProductSortMapper(): ShopProductSortMapper {
        return ShopProductSortMapper()
    }

    /**
     * Play Widget
     */
    @ShopPageHomeScope
    @Provides
    fun providePlayWidget(playWidgetUseCase: PlayWidgetUseCase,
                          playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
                          playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
                          mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>): PlayWidgetTools {
        return PlayWidgetTools(playWidgetUseCase, playWidgetReminderUseCase, playWidgetUpdateChannelUseCase, mapperProviders)
    }


    @ShopPageHomeScope
    @Provides
    fun providePlayWidgetTracking(trackingQueue: TrackingQueue,
                                  userSession: UserSessionInterface): ShopPlayWidgetAnalyticListener {
        return ShopPlayWidgetAnalyticListener(trackingQueue, userSession)
    }
}