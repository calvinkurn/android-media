package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(QueryTargetedTicker.NAME, QueryTargetedTicker.QUERY)
object QueryTargetedTicker {
    const val NAME = "TargetedTickerQuery"
    const val QUERY = """
        query GetTargetedTicker(${"$"}page: String!) {
          GetTargetedTicker(input:{
            Page: ${'$'}page,
            Target: []
          }){
            List{
              ID
              Title
              Content
              Action{
                Label
                Type
                AppURL
                WebURL
              }
              Type
              Priority
              Metadata{
                Type
                Values
              }
            }
          }
        }
    """
}
