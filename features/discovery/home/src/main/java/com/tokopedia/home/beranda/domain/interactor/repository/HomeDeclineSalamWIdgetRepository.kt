package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by firmandanugroho on 15-06-2020
 */

class HomeDeclineSalamWIdgetRepository @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<SalamWidget>)
    : UseCase<SalamWidget>(), HomeRepository<SalamWidget> {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(SalamWidget::class.java)
    }

    companion object {
        const val PARAM_WIDGET_ID = "widgetID"
        const val PARAM_REQUEST = "params"
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val params = "\$params"

        """mutation CloseWidget($params: SalamCloseWidgetInput!){
            salamCloseWidget(params: $params){
                ID
             }
        } 
        """.trimIndent()
    }

    override suspend fun executeOnBackground(): SalamWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    override suspend fun getRemoteData(bundle: Bundle): SalamWidget {
        return executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): SalamWidget {
        return SalamWidget()
    }

    fun setParams(requestParams: Map<String, Int>) {
        params.parameters.clear()
        //validate params
        if (requestParams.containsKey(PARAM_WIDGET_ID)) {
            params.putObject(PARAM_REQUEST, requestParams)
        }
    }

}