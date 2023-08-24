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
        return HomeTodoWidgetData.HomeTodoWidget(
            HomeTodoWidgetData.GetHomeTodoWidget(
                header = HomeTodoWidgetData.Header(title = "Title"),
                todos = listOf(
                    HomeTodoWidgetData.Todo(id = 123L, title = "title", contextInfo = "test", imageUrl = "https://images.tokopedia.net/img/cache/200-square/phZFtb/2023/8/7/fb191714-163a-4272-a4a5-eca03c42f0e1.jpg", cta = HomeTodoWidgetData.Cta(text = "click")),
                    HomeTodoWidgetData.Todo(id = 1234L, title = "title2", contextInfo = "test2", imageUrl = "https://images.tokopedia.net/img/cache/200-square/phZFtb/2023/8/7/fb191714-163a-4272-a4a5-eca03c42f0e1.jpg", cta = HomeTodoWidgetData.Cta(text = "click")),
                    HomeTodoWidgetData.Todo(id = 3234L, title = "title2", contextInfo = "test2", imageUrl = "https://images.tokopedia.net/img/cache/200-square/phZFtb/2023/8/7/fb191714-163a-4272-a4a5-eca03c42f0e1.jpg", cta = HomeTodoWidgetData.Cta(text = "click"))
                )
            )
        )
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
