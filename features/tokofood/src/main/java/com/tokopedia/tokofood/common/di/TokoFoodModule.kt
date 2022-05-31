package com.tokopedia.tokofood.common.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TokoFoodModule {

    @Provides
    @TokoFoodScope
    fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

    @Provides
    @TokoFoodScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}