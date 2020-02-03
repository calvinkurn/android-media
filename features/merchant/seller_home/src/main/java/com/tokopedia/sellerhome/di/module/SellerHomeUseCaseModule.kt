package com.tokopedia.sellerhome.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.mapper.CardMapper
import com.tokopedia.sellerhome.domain.mapper.LineGraphMapper
import com.tokopedia.sellerhome.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhome.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhome.domain.usecase.GetLineGraphDataUseCase
import dagger.Module
import dagger.Provides

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

@SellerHomeScope
@Module
class SellerHomeUseCaseModule {

    @SellerHomeScope
    @Provides
    fun provideGetSellerHomeLayoutUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase): GetLayoutUseCase {
        return GetLayoutUseCase(multiRequestGraphqlUseCase)
    }

    @SellerHomeScope
    @Provides
    fun provideGetCardDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: CardMapper
    ): GetCardDataUseCase {
        return GetCardDataUseCase(gqlRepository, mapper)
    }

    @SellerHomeScope
    @Provides
    fun provideGetLineGraphDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LineGraphMapper
    ): GetLineGraphDataUseCase {
        return GetLineGraphDataUseCase(gqlRepository, mapper)
    }
}