package com.tokopedia.salam.umrah.homepage.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageCategoryFeaturedUseCase
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageCategoryUseCase
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageEmptyDataUseCase
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageMyUmrahUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 14/10/2019
 */
@Module
class UmrahHomepageModule {
    @UmrahHomepageScope
    @Provides
    fun provideGetEmptyVMsUseCase(): UmrahHomepageEmptyDataUseCase = UmrahHomepageEmptyDataUseCase()

    @UmrahHomepageScope
    @Provides
    fun provideCategoryFeaturedUseCase(graphqlRepository: GraphqlRepository): UmrahHomepageCategoryFeaturedUseCase =
            UmrahHomepageCategoryFeaturedUseCase(graphqlRepository)

    @UmrahHomepageScope
    @Provides
    fun provideCategoryUseCase(graphqlRepository: GraphqlRepository): UmrahHomepageCategoryUseCase =
            UmrahHomepageCategoryUseCase(graphqlRepository)

    @UmrahHomepageScope
    @Provides
    fun provideMyUmrahUseCase(graphqlRepository: GraphqlRepository): UmrahHomepageMyUmrahUseCase =
            UmrahHomepageMyUmrahUseCase(graphqlRepository)
}