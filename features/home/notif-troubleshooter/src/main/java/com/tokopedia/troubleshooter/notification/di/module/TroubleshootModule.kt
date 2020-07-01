package com.tokopedia.troubleshooter.notification.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
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