package com.tokopedia.tokopedianow.recipebookmark.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokopedianow.recipebookmark.di.scope.RecipeBookmarkScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RecipeBookmarkModule {

    @RecipeBookmarkScope
    @Provides
    fun provideLocalizingAddressData(@ApplicationContext context: Context): LocalCacheModel {
        return ChooseAddressUtils.getLocalizingAddressData(context)
    }

    @RecipeBookmarkScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @RecipeBookmarkScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}