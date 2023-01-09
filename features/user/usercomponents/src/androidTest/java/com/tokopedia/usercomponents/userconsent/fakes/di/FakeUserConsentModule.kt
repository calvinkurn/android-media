package com.tokopedia.usercomponents.userconsent.fakes.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.userconsent.domain.collection.GetConsentCollectionUseCase
import com.tokopedia.usercomponents.userconsent.fakes.UserConsentRepositoryStub
import dagger.Module
import dagger.Provides

@Module
class FakeUserConsentModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideUserConsentRepositoryStub(): GraphqlRepository {
        return UserConsentRepositoryStub()
    }

    @Provides
    @ApplicationScope
    fun provideUserConsentUserCase(
        @ApplicationContext repositoryStub: UserConsentRepositoryStub,
        dispatcher: CoroutineDispatchers
    ): GetConsentCollectionUseCase {
        return GetConsentCollectionUseCase(repositoryStub, dispatcher)
    }
}
