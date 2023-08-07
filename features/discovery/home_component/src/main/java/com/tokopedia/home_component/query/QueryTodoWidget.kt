package com.tokopedia.home_component.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home_component.query.QueryTodoWidget.TODO_WIDGET_QUERY
import com.tokopedia.home_component.query.QueryTodoWidget.TODO_WIDGET_QUERY_NAME

/**
 * Created by frenzel
 */
@GqlQuery(TODO_WIDGET_QUERY_NAME, TODO_WIDGET_QUERY)
internal object QueryTodoWidget {
    const val TODO_WIDGET_QUERY_NAME = "TodoWidgetQuery"
    const val TODO_WIDGET_QUERY = "query getHomeToDoWidget(\$param: String!, \$location: String!) {\n" +
        "   getHomeToDoWidget(param: \$param, location: \$location) {\n" +
        "       header {\n" +
        "           title\n" +
        "       }\n" +
        "       toDos {\n" +
        "           id\n" +
        "           dataSource\n" +
        "           title\n" +
        "           dueDate\n" +
        "           imageUrl\n" +
        "           contextInfo\n" +
        "           price\n" +
        "           discountPercentage\n" +
        "           slashedPrice\n" +
        "           cta {\n" +
        "               type\n" +
        "               mode\n" +
        "               text\n" +
        "               applink\n" +
        "               url\n" +
        "           }\n" +
        "           applink\n" +
        "           url\n" +
        "           feParam" +
        "       }\n" +
        "   }\n" +
        "}\n"
}
