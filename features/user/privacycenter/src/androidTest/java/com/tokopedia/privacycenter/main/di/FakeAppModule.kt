package com.tokopedia.privacycenter.main.di

import android.content.Context
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

class FakeAppModule(context: Context?) : AppModule(context) {
    override fun provideGraphqlRepository(): GraphqlRepository {
        return FakeGraphqlRepository()
    }
}
