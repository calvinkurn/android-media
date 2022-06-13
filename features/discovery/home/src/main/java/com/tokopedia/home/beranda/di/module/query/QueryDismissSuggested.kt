package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryDismissSuggested.DISMISS_SUGGESTED_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryDismissSuggested.DISMISS_SUGGESTED_QUERY_NAME

@GqlQuery(DISMISS_SUGGESTED_QUERY_NAME, DISMISS_SUGGESTED_QUERY)
internal object QueryDismissSuggested {
    const val DISMISS_SUGGESTED_QUERY_NAME = "DismissSuggestedQuery"
    const val DISMISS_SUGGESTED_QUERY : String = "query productrevDismissSuggestion(){\n" +
            "  productrevDismissSuggestion\n" +
            "}"

}