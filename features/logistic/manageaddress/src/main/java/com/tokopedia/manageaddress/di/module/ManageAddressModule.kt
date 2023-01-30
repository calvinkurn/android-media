package com.tokopedia.manageaddress.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.manageaddress.domain.usecase.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.SetDefaultPeopleAddressUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object ManageAddressModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ActivityScope
    fun provideDeletePeopleAddressUseCase(@ApplicationContext repository: GraphqlRepository) =
        GraphqlUseCase<DeletePeopleAddressUseCase>(repository)

    @Provides
    @ActivityScope
    fun provideSetDefaultPeopleAddressUseCase(@ApplicationContext repository: GraphqlRepository) =
        GraphqlUseCase<SetDefaultPeopleAddressUseCase>(repository)

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}