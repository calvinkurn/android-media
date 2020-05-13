package com.tokopedia.vouchercreation.common.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchercreation.common.di.scope.VoucherCreationScope
import com.tokopedia.vouchercreation.create.domain.usecase.PromoCodeValidationUseCase
import dagger.Module
import dagger.Provides

@VoucherCreationScope
@Module
class VoucherCreationUseCaseModule {

    @VoucherCreationScope
    @Provides
    fun providePromoCodeValidationUseCase(gqlRepository: GraphqlRepository): PromoCodeValidationUseCase =
            PromoCodeValidationUseCase(gqlRepository)

}