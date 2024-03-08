package com.tokopedia.notifications.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetAnimationPopupDataGQLQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_QUERY_GAMI_ANIMATION_POPUP
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
