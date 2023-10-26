package com.tokopedia.creation.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

/**
 * Created By : Muhammad Furqan on 15/09/23
 */
@Module(includes = [ContentCreationViewModelModule::class])
class ContentCreationModule(private val activityContext: Context) {
    @Provides
    fun provideGraphqlRepository(@ApplicationContext graphqlRepository: GraphqlRepository): GraphqlRepository =
        graphqlRepository

    @Provides
    fun provideContentCreationConfigUseCase(
        graphqlRepository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): ContentCreationConfigUseCase = ContentCreationConfigUseCase(
        graphqlRepository = graphqlRepository,
        dispatcher = dispatchers
    )

    @Provides
    fun provideFirebaseRemoteConfig(): RemoteConfig = FirebaseRemoteConfigImpl(activityContext)
}
