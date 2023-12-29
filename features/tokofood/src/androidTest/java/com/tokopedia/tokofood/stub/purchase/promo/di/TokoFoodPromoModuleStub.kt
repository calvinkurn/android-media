package com.tokopedia.tokofood.stub.purchase.promo.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.common.address.TokoFoodChosenAddressRequestHelper
import com.tokopedia.tokofood.feature.purchase.promopage.domain.usecase.PromoListTokoFoodUseCase
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.TokoFoodChosenAddressRequestHelperStub
import com.tokopedia.tokofood.stub.common.util.UserSessionStub
import com.tokopedia.tokofood.stub.purchase.promo.domain.usecase.PromoListTokoFoodUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TokoFoodPromoModuleStub {

    @ActivityScope
    @Provides
    fun provideUserSessionStub(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSessionStub(context)
    }

    @ActivityScope
    @Provides
    fun provideTokoFoodChosenAddressRequestHelperStub(
        @ApplicationContext context: Context
    ): TokoFoodChosenAddressRequestHelper {
        return TokoFoodChosenAddressRequestHelperStub(context)
    }

    @ActivityScope
    @Provides
    fun providePromoListTokoFoodUseCase(
        graphQlRepository: GraphqlRepositoryStub,
        helper: TokoFoodChosenAddressRequestHelper
    ): PromoListTokoFoodUseCase {
        return PromoListTokoFoodUseCaseStub(graphQlRepository, helper)
    }

}
