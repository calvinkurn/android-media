package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import javax.inject.Inject

class TopAdsLatestReadingUseCase @Inject constructor() {

    suspend fun getLatestReading(): TopAdsLatestReading? {
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, TopAdsLatestReading::class.java, mapOf())
        gql.addRequest(request)
        val response = gql.executeOnBackground()
        return response.getData(TopAdsLatestReading::class.java) as? TopAdsLatestReading
    }

    private fun createParams() = mapOf(
        "" to ""
    )

    companion object {
        private const val SOURCE = ""
        private const val query = """
            query categoryTree(${'$'}level: Int!, ${'$'}source: String!){
              categoryTree(level:${'$'}level, source:${'$'}source){
                data{
                  status
                  categories{
                    id
                    title
                    description
                    url
                    children{
                      id
                      title
                      description
                      url
                    }
                    icon{
                      url
                    }
                  }
                }
              }
            }"""
    }
}