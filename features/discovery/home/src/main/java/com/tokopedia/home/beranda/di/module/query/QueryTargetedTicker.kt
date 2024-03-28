package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(QueryTargetedTicker.NAME, QueryTargetedTicker.QUERY)
object QueryTargetedTicker {
    const val NAME = "TargetedTickerQuery"
    const val QUERY = """
        query GetTargetedTicker(${"$"}input: GetTargetedTickerRequest!) {
          GetTargetedTicker(input: ${"$"}input){
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
