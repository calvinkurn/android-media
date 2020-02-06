package com.tokopedia.sellerhome.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.domain.mapper.*
import com.tokopedia.sellerhome.domain.usecase.GetCardDataUseCase
import com.tokopedia.sellerhome.domain.usecase.GetLayoutUseCase
import com.tokopedia.sellerhome.domain.usecase.GetLineGraphDataUseCase
import com.tokopedia.sellerhome.domain.usecase.GetProgressDataUseCase
import com.tokopedia.sellerhome.domain.usecase.GetPostDataUseCase
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
    fun provideGetSellerHomeLayoutUseCase(
            gqlRepository: GraphqlRepository,
            mapper: LayoutMapper
    ): GetLayoutUseCase {
        return GetLayoutUseCase(gqlRepository, mapper)
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

    @SellerHomeScope
    @Provides
    fun provideGetProgressDataUseCase(
            gqlRepository: GraphqlRepository,
            progressMapper: ProgressMapper
    ): GetProgressDataUseCase
            = GetProgressDataUseCase(gqlRepository, progressMapper)

    @SellerHomeScope
    @Provides
    fun provideGetPostDataUseCase(
            gqlRepository: GraphqlRepository,
            mapper: PostMapper
    ): GetPostDataUseCase {
        return GetPostDataUseCase(gqlRepository, mapper)
    }
}