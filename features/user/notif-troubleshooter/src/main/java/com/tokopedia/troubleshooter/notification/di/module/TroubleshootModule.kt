package com.tokopedia.troubleshooter.notification.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.service.channel.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.channel.NotificationChannelManagerImpl
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManagerImpl
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManagerImpl
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.troubleshooter.notification.util.dispatchers.AppDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.dispatchers.DispatcherProvider
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
    fun provideNotificationChannelManager(): NotificationChannelManager {
        return NotificationChannelManagerImpl(context)
    }

    @Provides
    @TroubleshootScope
    fun provideNotificationCompatManager(): NotificationCompatManager {
        return NotificationCompatManagerImpl(context)
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