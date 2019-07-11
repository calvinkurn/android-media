package com.tokopedia.profilecompletion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileInfoData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@ProfileCompletionScope
@Module
class ProfileCompletionModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ProfileCompletionScope
    @Provides
    fun provideGraphqlUseCase(graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<UserProfileInfoData> = GraphqlUseCase<UserProfileInfoData>(graphqlRepository).apply {
            setTypeClass(UserProfileInfoData::class.java)
        }

    @ProfileCompletionScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

}