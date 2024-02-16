package com.tokopedia.loginregister.registerinitial.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 10/25/18.
 */
@Module
class RegisterInitialModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    //need to be removed after all usecases move to coroutine usecase
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @ActivityScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase {
        return getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @Provides
    fun provideIrisSession(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }
}
