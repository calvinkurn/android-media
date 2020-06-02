package com.tokopedia.notifications.data

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.PushController
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase
import com.tokopedia.abstraction.common.utils.GraphqlHelper.loadRawString as loadRaw

object AmplificationDataSource {

    private val useCase by lazy(LazyThreadSafetyMode.NONE) {
        GraphqlUseCase<AmplificationNotifier>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @JvmStatic fun invoke(context: Context) {
        val query = loadRaw(context.resources, R.raw.query_notification_amplification)
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        amplificationUseCase.execute {
            it.webhookAttributionNotifier.pushData.forEach { data ->
                PushController(context).handleNotificationAmplification(data)
            }
        }
    }

}