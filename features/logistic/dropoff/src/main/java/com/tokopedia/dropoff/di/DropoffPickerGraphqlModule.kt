package com.tokopedia.dropoff.di

import com.tokopedia.dropoff.data.response.getAddress.AddressResponse
import com.tokopedia.dropoff.data.response.getDistrict.GetDistrictResponse
import com.tokopedia.dropoff.data.response.getStore.GetStoreResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.dropoff.data.response.autoComplete.AutocompleteResponse
import dagger.Module
import dagger.Provides

@Module
@DropoffPickerScope
class DropoffPickerGraphqlModule {

    @Provides
    @DropoffPickerScope
    fun provideGetStoreUsecase(repository: GraphqlRepository): GraphqlUseCase<GetStoreResponse> =
            GraphqlUseCase(repository)

    @Provides
    @DropoffPickerScope
    fun provideAutoCompleteUsecase(repository: GraphqlRepository)
            : GraphqlUseCase<AutocompleteResponse> = GraphqlUseCase(repository)

    @Provides
    @DropoffPickerScope
    fun provideGetDistrictUsecase(repository: GraphqlRepository)
            : GraphqlUseCase<GetDistrictResponse> = GraphqlUseCase(repository)

    @Provides
    @DropoffPickerScope
    fun provideGetAddressUsecasee(repository: GraphqlRepository)
            : GraphqlUseCase<AddressResponse> = GraphqlUseCase(repository)

}