package com.tokopedia.manageaddress.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
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
object ManageAddressModule  {

    @Provides
    @ActivityScope
    fun provideDeletePeopleAddressUseCase(@ApplicationContext repository: GraphqlRepository): DeletePeopleAddressUseCase =
            DeletePeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ActivityScope
    fun provideSetDefaultPeopleAddressUseCase(@ApplicationContext repository: GraphqlRepository): SetDefaultPeopleAddressUseCase =
            SetDefaultPeopleAddressUseCase(GraphqlUseCase(repository))

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}