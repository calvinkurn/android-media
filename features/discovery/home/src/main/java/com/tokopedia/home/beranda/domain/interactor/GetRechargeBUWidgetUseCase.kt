package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.di.module.query.RechargeBuWidgetQuery
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by resakemal on 2020-10-30
 */

class GetRechargeBUWidgetUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RechargePerso.Response>)
    : UseCase<RechargePerso>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(RechargePerso.Response::class.java)
    }

    companion object {
        const val PARAM_WIDGET_SOURCE = "widgetSource"

        const val NULL_RESPONSE = "null response"
    }

    private var params: RequestParams = RequestParams.create()

    override suspend fun executeOnBackground(): RechargePerso {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(RechargeBuWidgetQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        val response = graphqlUseCase.executeOnBackground().response

        if (response != null) return response
        else throw (MessageErrorException(NULL_RESPONSE))
    }

    fun setParams(widgetSource: WidgetSource) {
        params.parameters.clear()
        params.putString(PARAM_WIDGET_SOURCE, widgetSource.source)
    }
}