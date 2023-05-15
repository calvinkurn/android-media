package com.tokopedia.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.datastore.UserSessionDataStore

class AppModuleStub(val appContext: Context) : AppModule(appContext) {


    override fun provideGraphqlRepository(): GraphqlRepository {
        return super.provideGraphqlRepository()
    }

    override fun provideUserSessionDataStore(context: Context?): UserSessionDataStore {
        return super.provideUserSessionDataStore(context)
    }
}
