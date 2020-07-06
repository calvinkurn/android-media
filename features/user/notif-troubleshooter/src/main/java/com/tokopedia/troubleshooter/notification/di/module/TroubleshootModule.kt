package com.tokopedia.troubleshooter.notification.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.data.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.FirebaseInstanceManagerImpl
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.util.AppDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module class TroubleshootModule(private val context: Context) {

    @Provides
    @TroubleshootContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @TroubleshootScope
    fun provideMainDispatcher(): DispatcherProvider {
        return AppDispatcherProvider()
    }

    @Provides
    @TroubleshootScope
    fun provideFirebaseInstanceManager(): FirebaseInstanceManager {
        return FirebaseInstanceManagerImpl()
    }

    @Provides
    @TroubleshootScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @TroubleshootScope
    fun provideTroubleshootUseCase(
            repository: GraphqlRepository,
            @TroubleshootContext context: Context
    ): TroubleshootStatusUseCase {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_send_notif_troubleshooter
        )
        return TroubleshootStatusUseCase(repository, query)
    }

}