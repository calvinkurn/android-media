package com.tokopedia.tokofood.stub.postpurchase.di.component

import com.tokopedia.tokochat.config.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokofood.feature.ordertracking.di.component.TokoFoodOrderTrackingComponent
import com.tokopedia.tokofood.feature.ordertracking.di.scope.TokoFoodOrderTrackingScope
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.DriverPhoneNumberMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderStatusMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetUnreadChatCountUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.TokoChatConfigGroupBookingUseCase
import com.tokopedia.tokofood.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.tokofood.stub.postpurchase.di.module.TokoFoodOrderTrackingModuleStub
import com.tokopedia.tokofood.stub.postpurchase.presentation.activity.TokoFoodOrderTrackingActivityStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@TokoFoodOrderTrackingScope
@Component(
    modules = [TokoFoodOrderTrackingModuleStub::class],
    dependencies = [BaseAppComponentStub::class, TokoChatConfigComponent::class]
)
interface TokoFoodOrderTrackingComponentStub : TokoFoodOrderTrackingComponent {

    fun inject(activity: TokoFoodOrderTrackingActivityStub)
    fun coroutineDispatcher(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository

    fun userSessionInterface(): UserSessionInterface

    fun getDriverPhoneNumberUseCaseStub(): GetDriverPhoneNumberUseCase

    fun getTokoFoodOrderDetailUseCaseStub(): GetTokoFoodOrderDetailUseCase

    fun getTokoFoodOrderStatusUseCaseStub(): GetTokoFoodOrderStatusUseCase

    fun getUnreadChatCountUseCaseStub(): GetUnreadChatCountUseCase

    fun getTokoChatConfigGroupBookingUseCaseStub(): TokoChatConfigGroupBookingUseCase

    fun driverPhoneNumberMapperStub(): DriverPhoneNumberMapper

    fun tokoFoodOrderDetailMapperStub(): TokoFoodOrderDetailMapper

    fun tokoFoodOrderStatusMapperStub(): TokoFoodOrderStatusMapper

    fun getRemoteConfig(): RemoteConfig
}
