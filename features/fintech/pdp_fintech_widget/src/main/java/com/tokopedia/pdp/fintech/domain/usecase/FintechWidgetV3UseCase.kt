package com.tokopedia.pdp.fintech.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.pdp.fintech.constants.GQL_GET_WIDGET_DETAIL_V3
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetailV3
import javax.inject.Inject

@GqlQuery("PayLaterGetPdpWidgetV3", GQL_GET_WIDGET_DETAIL_V3)
class FintechWidgetV3UseCase @Inject constructor(graphqlRepository: GraphqlRepository):
    GraphqlUseCase<WidgetDetailV3>(graphqlRepository) {

        fun getWidgetV3Data(
            onSuccess: (WidgetDetailV3) -> Unit,
            onError: (Throwable) -> Unit,
        ) {
            this.setTypeClass(WidgetDetailV3::class.java)
            this.setGraphqlQuery(PayLaterGetPdpWidgetV3.GQL_QUERY)
            this.execute({
                onSuccess.invoke(it)
            }, {
                onError.invoke(it)
            })
        }
}
