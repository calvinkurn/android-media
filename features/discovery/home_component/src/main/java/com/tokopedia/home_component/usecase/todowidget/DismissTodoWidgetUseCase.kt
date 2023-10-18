package com.tokopedia.home_component.usecase.todowidget

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home_component.query.TodoWidgetDismissMutation
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by frenzel
 */
class DismissTodoWidgetUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DismissTodoWidgetResponse>(graphqlRepository) {

    init {
        this.setTypeClass(DismissTodoWidgetResponse::class.java)
        this.setGraphqlQuery(TodoWidgetDismissMutation())
        this.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    companion object {
        const val PARAM = "param"
        const val DATASOURCE_PARAM = "dataSource"
    }

    fun getTodoWidgetDismissData(
        dataSource: String,
        param: String
    ) {
        this.setRequestParams(
            RequestParams.create().apply {
                putString(PARAM, param)
                putString(DATASOURCE_PARAM, dataSource)
            }.parameters
        )
        this.execute(
            onSuccess = { },
            onError = { }
        )
    }
}
