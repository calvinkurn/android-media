package com.tokopedia.notifications.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.PushController
import com.tokopedia.notifications.R
import com.tokopedia.notifications.data.converters.JsonBundleConverter.jsonToBundle
import com.tokopedia.notifications.data.model.AmplificationNotifier
import com.tokopedia.notifications.domain.AmplificationUseCase

object AmplificationDataManager {

    @JvmStatic fun amplification(context: Context, deviceToken: String) {
        val useCase = GraphqlUseCase<AmplificationNotifier>(
                GraphqlInteractor.getInstance().graphqlRepository
        )
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_notification_amplification
        )
        val amplificationUseCase = AmplificationUseCase(useCase, query)
        val params = amplificationUseCase.params(deviceToken)
        amplificationUseCase.execute(params) {
            it.webhookAttributionNotifier.pushData.forEach { data ->
                val bundle = jsonToBundle(data)
                PushController(context).handleNotificationBundle(bundle)
            }
        }
    }

}