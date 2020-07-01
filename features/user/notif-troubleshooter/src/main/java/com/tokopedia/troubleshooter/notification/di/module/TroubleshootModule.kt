package com.tokopedia.troubleshooter.notification.di.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.domain.UpdateTokenUseCase
import com.tokopedia.troubleshooter.notification.util.AppDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module class TroubleshootModule(val context: Context) {

    @Provides
    @TroubleshootContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @TroubleshootScope
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @TroubleshootScope
    fun provideMainDispatcher(): DispatcherProvider {
        return AppDispatcherProvider()
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

    @Provides
    @TroubleshootScope
    fun provideUpdateTokenUseCase(
            repository: GraphqlRepository,
            @TroubleshootContext context: Context
    ): UpdateTokenUseCase {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_update_gcm_token
        )
        return UpdateTokenUseCase(repository, query)
    }

}