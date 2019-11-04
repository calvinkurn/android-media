package com.tokopedia.logisticaddaddress.di.dropoff_picker

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticaddaddress.domain.model.dropoff.GetStoreResponse
import dagger.Module
import dagger.Provides

@Module
@DropoffPickerScope
class DropoffPickerGraphqlModule {

    @Provides
    @DropoffPickerScope
    fun provideGetStoreUsecase(repository: GraphqlRepository): GraphqlUseCase<GetStoreResponse> =
            GraphqlUseCase(repository)

}