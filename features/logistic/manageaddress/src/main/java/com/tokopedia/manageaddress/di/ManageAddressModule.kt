package com.tokopedia.manageaddress.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import dagger.Module
import dagger.Provides

@Module
@ManageAddressScope
class ManageAddressModule  {

    @Provides
    @ManageAddressScope
    fun provideGraphQlRepository(): com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    /*withouw gql*/
    //ToDo:
    @Provides
    @ManageAddressScope
    fun provideGetPeopleAddressUseCase(repository: GraphqlRepository): GetPeopleAddressUseCase =
            GetPeopleAddressUseCase(GraphqlUseCase(repository))
}