package com.tokopedia.tokofood.stub.postpurchase.di.module

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.DriverPhoneNumberMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderCompletedMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderLiveTrackingMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderCompletedMapperSection
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderLiveTrackingMapperSection
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.model.DriverPhoneNumberResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderStatusResponse
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetUnreadChatCountUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.TokoChatConfigGroupBookingUseCase
import com.tokopedia.tokofood.stub.common.graphql.interactor.GraphqlUseCaseStub
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import com.tokopedia.tokofood.stub.postpurchase.data.repository.TokoChatRepositoryStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.DriverPhoneNumberMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderDetailMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderStatusMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetDriverPhoneNumberUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderDetailUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderStatusUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetUnreadChatCountUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.TokoChatConfigGroupBookingUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [TokoFoodOrderTrackingViewModelModuleStub::class, TokoFoodCourierConversationModuleStub::class, TokoFoodNetworkModuleStub::class])
class TokoFoodOrderTrackingModuleStub {

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSessionStub(context)

    @Provides
    @TokoFoodOrderTrackingScope
    fun providesRemoteConfig(
        @ApplicationContext context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderDetailUseCase(graphqlRepositoryStub: GraphqlRepositoryStub): GraphqlUseCaseStub<TokoFoodOrderDetailResponse> {
        return GraphqlUseCaseStub(graphqlRepositoryStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderStatusUseCase(graphqlRepositoryStub: GraphqlRepositoryStub): GraphqlUseCaseStub<TokoFoodOrderStatusResponse> {
        return GraphqlUseCaseStub(graphqlRepositoryStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodDriverPhoneNumberUseCase(graphqlRepositoryStub: GraphqlRepositoryStub): GraphqlUseCaseStub<DriverPhoneNumberResponse> {
        return GraphqlUseCaseStub(graphqlRepositoryStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderDetailMapperStub(
        orderLiveTrackingMapper: ITokoFoodOrderLiveTrackingMapper,
        orderCompletedMapper: ITokoFoodOrderCompletedMapper
    ): TokoFoodOrderDetailMapper {
        return TokoFoodOrderDetailMapperStub(orderLiveTrackingMapper, orderCompletedMapper)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderDetailUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<TokoFoodOrderDetailResponse>,
        tokoFoodOrderDetailMapper: TokoFoodOrderDetailMapper
    ): GetTokoFoodOrderDetailUseCase {
        return GetTokoFoodOrderDetailUseCaseStub(useCaseStub, tokoFoodOrderDetailMapper)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderStatusUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<TokoFoodOrderStatusResponse>,
        tokoFoodOrderStatusMapper: TokoFoodOrderStatusMapper
    ): GetTokoFoodOrderStatusUseCase {
        return GetTokoFoodOrderStatusUseCaseStub(useCaseStub, tokoFoodOrderStatusMapper)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideDriverPhoneNumberUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<DriverPhoneNumberResponse>,
        driverPhoneNumberMapper: DriverPhoneNumberMapper
    ): GetDriverPhoneNumberUseCase {
        return GetDriverPhoneNumberUseCaseStub(useCaseStub, driverPhoneNumberMapper)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideGetUnreadChatCountUseCaseStub(
        tokoChatRepositoryStub: TokoChatRepository
    ): GetUnreadChatCountUseCase {
        return GetUnreadChatCountUseCaseStub(tokoChatRepositoryStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoChatConfigGroupBookingUseCaseStub(
        tokoChatRepositoryStub: TokoChatRepository
    ): TokoChatConfigGroupBookingUseCase {
        return TokoChatConfigGroupBookingUseCaseStub(tokoChatRepositoryStub)
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideTokoChatRepositoryStub(
        retrofit: Retrofit,
        @ApplicationContext context: Context,
        babbleCourierClient: BabbleCourierClient
    ): TokoChatRepository {
        return TokoChatRepositoryStub(retrofit, context, babbleCourierClient)
    }

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
    fun provideDriverPhoneNumberMapperStub(): DriverPhoneNumberMapper {
        return DriverPhoneNumberMapperStub()
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderStatusMapperStub(): TokoFoodOrderStatusMapper {
        return TokoFoodOrderStatusMapperStub()
    }
}
