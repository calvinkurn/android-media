package com.tokopedia.deals.location_picker.di.module

import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.di.DealsLocationScope
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DealsLocationModule {
    @DealsLocationScope
    @Provides
    @Named(DealsLocationConstants.GQL_SEARCH_LOCATION)
    fun provideGraphqlUseCaseSearchLocationData(graphqlRepository: GraphqlRepository): GraphqlUseCase<LocationData> = GraphqlUseCase(graphqlRepository)

    @DealsLocationScope
    @Provides
    @Named(DealsLocationConstants.GQL_POPULAR_LOCATION)
    fun provideGraphqlUseCasePopularLocationData(graphqlRepository: GraphqlRepository): GraphqlUseCase<LocationData> = GraphqlUseCase(graphqlRepository)

    @DealsLocationScope
    @Provides
    @Named(DealsLocationConstants.GQL_POPULAR_CITIES)
    fun provideGraphqlUseCasePopularCitiesData(graphqlRepository: GraphqlRepository): GraphqlUseCase<LocationData> = GraphqlUseCase(graphqlRepository)

    @DealsLocationScope
    @Provides
    @Named(DealsLocationConstants.GQL_LANDMARK)
    fun provideGraphqlUseCaseLandmarkData(graphqlRepository: GraphqlRepository): GraphqlUseCase<LocationData> = GraphqlUseCase(graphqlRepository)
}