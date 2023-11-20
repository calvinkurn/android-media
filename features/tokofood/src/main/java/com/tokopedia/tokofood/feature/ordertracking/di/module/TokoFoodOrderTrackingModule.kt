package com.tokopedia.tokofood.feature.ordertracking.di.module

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderCompletedMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderLiveTrackingMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderCompletedMapperSection
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderLiveTrackingMapperSection
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.TokoChatConfigGroupBookingUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [TokoFoodOrderTrackingViewModelModule::class])
class TokoFoodOrderTrackingModule {

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderLiveTrackingMapper(): ITokoFoodOrderLiveTrackingMapper {
        return TokoFoodOrderLiveTrackingMapperSection()
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderCompletedMapper(): ITokoFoodOrderCompletedMapper {
        return TokoFoodOrderCompletedMapperSection()
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoChatConfigMutationProfileUseCase(
        repository: TokoChatRepository
    ): TokoChatConfigGroupBookingUseCase {
        return TokoChatConfigGroupBookingUseCase(repository)
    }
}
