package com.tokopedia.home_component.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home_component.query.MutationTodoWidgetDismiss.TODO_WIDGET_DISMISS_MUTATION
import com.tokopedia.home_component.query.MutationTodoWidgetDismiss.TODO_WIDGET_DISMISS_MUTATION_NAME

/**
 * Created by frenzel
 */
@GqlQuery(TODO_WIDGET_DISMISS_MUTATION_NAME, TODO_WIDGET_DISMISS_MUTATION)
internal object MutationTodoWidgetDismiss {
    const val TODO_WIDGET_DISMISS_MUTATION_NAME = "TodoWidgetDismissMutation"
    const val TODO_WIDGET_DISMISS_MUTATION = "mutation dismissTodoWidget(\$dataSource: String!, \$param: String!) { " +
        "closeMultisource(dataSource: \$dataSource, param: \$param) {\n" +
        "   success\n" +
        "   message\n" +
        "}\n" +
        "}"
}
