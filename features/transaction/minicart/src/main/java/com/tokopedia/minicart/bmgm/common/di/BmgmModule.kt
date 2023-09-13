package com.tokopedia.minicart.bmgm.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

@Module
class BmgmModule {

    @BmgmMiniCartScope
    @Provides
    fun provideUseSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @BmgmMiniCartScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @BmgmMiniCartScope
    @Provides
    fun provideIrisSession(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }
}