package com.tokopedia.tokofood.stub.purchase.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailByIdUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointWithAddressIdUseCase
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseScope
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.TokoFoodChosenAddressRequestHelperStub
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutGeneralTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.GetAddressDetailByIdUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.GetAddressDetailUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.UpdatePinpointUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.UpdatePinpointWithAddressIdUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TokoFoodPurchaseModuleStub {

    @Provides
    fun provideUserSessionStub(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSessionStub(context)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideTokoFoodChosenAddressRequestHelperStub(
        @ApplicationContext context: Context
    ): TokoFoodChosenAddressRequestHelper {
        return TokoFoodChosenAddressRequestHelperStub(context)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideKeroEditAddressUseCaseStub(
        coroutineDispatchers: CoroutineDispatchers,
        getAddressDetailById: GetAddressDetailByIdUseCase,
        updatePinpointUseCase: UpdatePinpointUseCase
    ): UpdatePinpointWithAddressIdUseCase {
        return UpdatePinpointWithAddressIdUseCaseStub(getAddressDetailById, updatePinpointUseCase, coroutineDispatchers)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideUpdatePinpointUseCaseStub(
        graphQlRepository: GraphqlRepositoryStub,
        coroutineDispatchers: CoroutineDispatchers
    ): UpdatePinpointUseCase {
        return UpdatePinpointUseCaseStub(graphQlRepository, coroutineDispatchers)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideAddressDetailUseCaseStub(
        graphQlRepository: GraphqlRepositoryStub,
        coroutineDispatchers: CoroutineDispatchers
    ): GetAddressDetailUseCase {
        return GetAddressDetailUseCaseStub(graphQlRepository, coroutineDispatchers)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideKeroGetAddressUseCaseStub(
        getAddressDetailUseCase: GetAddressDetailUseCase,
        coroutineDispatchers: CoroutineDispatchers
    ): GetAddressDetailByIdUseCase {
        return GetAddressDetailByIdUseCaseStub(getAddressDetailUseCase, coroutineDispatchers)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideCheckoutTokoFoodUseCaseStub(
        graphQlRepository: GraphqlRepositoryStub,
        helper: TokoFoodChosenAddressRequestHelper
    ): CheckoutTokoFoodUseCase {
        return CheckoutTokoFoodUseCaseStub(graphQlRepository, helper)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideCheckoutGeneralTokoFoodUseCaseStub(
        graphQlRepository: GraphqlRepositoryStub
    ): CheckoutGeneralTokoFoodUseCase {
        return CheckoutGeneralTokoFoodUseCaseStub(graphQlRepository)
    }
}
