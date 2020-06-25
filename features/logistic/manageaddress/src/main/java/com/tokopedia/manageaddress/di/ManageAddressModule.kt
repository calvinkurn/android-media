package com.tokopedia.manageaddress.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import dagger.Module
import dagger.Provides

@Module
@ManageAddressScope
class ManageAddressModule  {

    @Provides
    @ManageAddressScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @ManageAddressScope
    fun provideGetPeopleAddressUseCase(repository: GraphqlRepository): GetPeopleAddressUseCase =
            GetPeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ManageAddressScope
    fun provideDeletePeopleAddressUseCase(repository: GraphqlRepository): DeletePeopleAddressUseCase =
            DeletePeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ManageAddressScope
    fun provideSetDefaultPeopleAddressUseCase(repository: GraphqlRepository): SetDefaultPeopleAddressUseCase =
            SetDefaultPeopleAddressUseCase(GraphqlUseCase(repository))
}