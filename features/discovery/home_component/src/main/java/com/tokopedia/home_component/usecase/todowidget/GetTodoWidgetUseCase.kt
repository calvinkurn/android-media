package com.tokopedia.home_component.usecase.todowidget

import android.os.Bundle
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home_component.query.TodoWidgetQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by frenzel
 */
class GetTodoWidgetUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<HomeTodoWidgetData.HomeTodoWidget>
) : UseCase<HomeTodoWidgetData.HomeTodoWidget>() {

    init {
        graphqlUseCase.setTypeClass(HomeTodoWidgetData.HomeTodoWidget::class.java)
        graphqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
    }

    private val params = RequestParams.create()

    companion object {
        const val PARAM = "param"
        const val LOCATION_PARAM = "location"
    }

    override suspend fun executeOnBackground(): HomeTodoWidgetData.HomeTodoWidget {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(TodoWidgetQuery())
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground()
    }

    fun generateParam(bundle: Bundle) {
        bundle.getString(LOCATION_PARAM, "")?.let {
            params.putString(LOCATION_PARAM, it)
        }
        bundle.getString(PARAM, "")?.let {
            params.putString(PARAM, it)
        }
    }
}
