package com.tokopedia.manageaddress.di.manageaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.di.ChooseAddressScope
import com.tokopedia.logisticCommon.domain.mapper.AddressCornerMapper
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ManageAddressModule  {

    @Provides
    @ManageAddressScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @ManageAddressScope
    fun provideGetAddressCornerUseCase(@ApplicationContext context: Context, graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase, mapper: AddressCornerMapper): GetAddressCornerUseCase {
        return GetAddressCornerUseCase(context, graphqlUseCase, mapper)
    }

    @Provides
    @ManageAddressScope
    fun provideDeletePeopleAddressUseCase(repository: GraphqlRepository): DeletePeopleAddressUseCase =
            DeletePeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ManageAddressScope
    fun provideSetDefaultPeopleAddressUseCase(repository: GraphqlRepository): SetDefaultPeopleAddressUseCase =
            SetDefaultPeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ManageAddressScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}