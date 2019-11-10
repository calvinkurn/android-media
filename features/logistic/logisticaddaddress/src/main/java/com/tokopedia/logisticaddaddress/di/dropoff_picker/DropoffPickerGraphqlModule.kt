package com.tokopedia.logisticaddaddress.di.dropoff_picker

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.data.entity.response.AddressResponse
import com.tokopedia.logisticaddaddress.data.entity.response.GetStoreResponse
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
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