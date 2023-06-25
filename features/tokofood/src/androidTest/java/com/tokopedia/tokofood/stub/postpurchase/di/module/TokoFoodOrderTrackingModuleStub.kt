package com.tokopedia.tokofood.stub.postpurchase.di.module

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
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
import com.tokopedia.tokofood.stub.common.graphql.interactor.GraphqlUseCaseStub
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.postpurchase.data.repository.TokoChatRepositoryStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.DriverPhoneNumberMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderDetailMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.mapper.TokoFoodOrderStatusMapperStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetDriverPhoneNumberUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderDetailUseCaseStub
import com.tokopedia.tokofood.stub.postpurchase.domain.usecase.GetTokoFoodOrderStatusUseCaseStub
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [TokoFoodOrderTrackingViewModelModuleStub::class, TokoFoodCourierConversationModuleStub::class, TokoFoodNetworkModuleStub::class])
class TokoFoodOrderTrackingModuleStub {

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
    fun provideTokoFoodOrderDetailUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<TokoFoodOrderDetailResponse>,
        tokoFoodOrderDetailMapperStub: TokoFoodOrderDetailMapperStub
    ): GetTokoFoodOrderDetailUseCase {
        return GetTokoFoodOrderDetailUseCaseStub(useCaseStub, tokoFoodOrderDetailMapperStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderStatusUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<TokoFoodOrderStatusResponse>,
        tokoFoodOrderStatusMapperStub: TokoFoodOrderStatusMapperStub
    ): GetTokoFoodOrderStatusUseCase {
        return GetTokoFoodOrderStatusUseCaseStub(useCaseStub, tokoFoodOrderStatusMapperStub)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideDriverPhoneNumberUseCaseStub(
        useCaseStub: GraphqlUseCaseStub<DriverPhoneNumberResponse>,
        driverPhoneNumberMapperStub: DriverPhoneNumberMapperStub
    ): GetDriverPhoneNumberUseCase {
        return GetDriverPhoneNumberUseCaseStub(useCaseStub, driverPhoneNumberMapperStub)
    }

    @Provides
    @TokoFoodOrderTrackingScope
    fun provideTokoChatRepositoryStub(
        retrofit: Retrofit,
        context: Context,
        babbleCourierClient: BabbleCourierClient
    ): TokoChatRepositoryStub {
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
    fun provideTokoFoodOrderDetailMapperStub(
        orderLiveTrackingMapper: ITokoFoodOrderLiveTrackingMapper,
        orderCompletedMapper: ITokoFoodOrderCompletedMapper
    ): TokoFoodOrderDetailMapper {
        return TokoFoodOrderDetailMapperStub(orderLiveTrackingMapper, orderCompletedMapper)
    }

    @TokoFoodOrderTrackingScope
    @Provides
    fun provideTokoFoodOrderStatusMapperStub(): TokoFoodOrderStatusMapper {
        return TokoFoodOrderStatusMapperStub()
    }
}
