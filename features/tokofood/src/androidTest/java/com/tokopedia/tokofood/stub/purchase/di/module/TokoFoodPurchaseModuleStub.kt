package com.tokopedia.tokofood.stub.purchase.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.di.TokoFoodPurchaseScope
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutGeneralTokoFoodUseCase
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.usecase.CheckoutTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.TokoFoodChosenAddressRequestHelperStub
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutGeneralTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.KeroEditAddressUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.KeroGetAddressUseCaseStub
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
        graphQlRepository: GraphqlRepositoryStub,
        keroGetAddressUseCase: KeroGetAddressUseCase
    ): KeroEditAddressUseCase {
        return KeroEditAddressUseCaseStub(graphQlRepository, keroGetAddressUseCase)
    }

    @Provides
    @TokoFoodPurchaseScope
    fun provideKeroGetAddressUseCaseStub(
        graphQlRepository: GraphqlRepositoryStub
    ): KeroGetAddressUseCase {
        return KeroGetAddressUseCaseStub(graphQlRepository)
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
