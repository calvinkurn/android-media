package com.tokopedia.shop.home.di.module

import android.content.Context
import com.tokopedia.atc_common.domain.mapper.AddToCartBundleDataMapper
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.play.widget.di.PlayWidgetModule
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.ShopPlayWidgetAnalyticListener
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.shop.home.di.scope.ShopPageHomeScope
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module(includes = [ShopPageHomeViewModelModule::class, PlayWidgetModule::class])
class ShopPageHomeModule {

    @ShopPageHomeScope
    @Provides
    fun provideAddToCartOccMultiUseCase(
        graphqlRepository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartOccMultiUseCase {
        return AddToCartOccMultiUseCase(graphqlRepository, addToCartDataMapper, chosenAddressRequestHelper)
    }

    @ShopPageHomeScope
    @Provides
    fun provideAddToCart(
        graphqlRepository: GraphqlRepository,
        addToCartDataMapper: AddToCartDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartUseCase {
        return AddToCartUseCase(graphqlRepository, addToCartDataMapper, chosenAddressRequestHelper)
    }

    @ShopPageHomeScope
    @Provides
    fun provideUpdateCart(
        graphqlRepository: GraphqlRepository,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): UpdateCartUseCase {
        return UpdateCartUseCase(graphqlRepository, chosenAddressRequestHelper)
    }

    @ShopPageHomeScope
    @Provides
    fun provideDeleteCart(graphqlRepository: GraphqlRepository): DeleteCartUseCase {
        return DeleteCartUseCase(graphqlRepository)
    }

    @ShopPageHomeScope
    @Provides
    fun provideAddToCartBundleUseCase(
        graphqlRepository: GraphqlRepository,
        addToCartBundleDataMapper: AddToCartBundleDataMapper,
        chosenAddressRequestHelper: ChosenAddressRequestHelper
    ): AddToCartBundleUseCase {
        return AddToCartBundleUseCase(
            graphqlRepository,
            addToCartBundleDataMapper,
            chosenAddressRequestHelper
        )
    }

    @ShopPageHomeScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @ShopPageHomeScope
    @Provides
    fun provideInterceptors(
        loggingInterceptor: HttpLoggingInterceptor,
        commonErrorResponseInterceptor: CommonErrorResponseInterceptor
    ): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor, commonErrorResponseInterceptor)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRestRepository(
        interceptors: MutableList<Interceptor>,
        @ShopPageContext context: Context
    ): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @ShopPageHomeScope
    @Provides
    fun provideAddToWishListV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @ShopPageHomeScope
    @Provides
    fun provideRemoveFromWishListV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
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
    fun providePlayWidget(
        playWidgetUseCase: PlayWidgetUseCase,
        playWidgetReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
        playWidgetUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
        mapper: PlayWidgetUiMapper,
        connectionUtil: PlayWidgetConnectionUtil
    ): PlayWidgetTools {
        return PlayWidgetTools(
            playWidgetUseCase,
            playWidgetReminderUseCase,
            playWidgetUpdateChannelUseCase,
            mapper,
            connectionUtil
        )
    }

    @ShopPageHomeScope
    @Provides
    fun providePlayWidgetTracking(userSession: UserSessionInterface): ShopPlayWidgetAnalyticListener {
        return ShopPlayWidgetAnalyticListener(userSession)
    }
}
