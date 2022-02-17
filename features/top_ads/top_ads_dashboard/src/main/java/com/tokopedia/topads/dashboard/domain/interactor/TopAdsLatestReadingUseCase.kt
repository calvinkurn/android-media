package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.dashboard.data.model.beranda.TopAdsLatestReading
import com.tokopedia.topads.dashboard.data.utils.Utils
import javax.inject.Inject

class TopAdsLatestReadingUseCase @Inject constructor() {

    suspend fun getLatestReading(): TopAdsLatestReading? {
        return Utils.executeQuery(query,TopAdsLatestReading::class.java,createParams())
    }

    private fun createParams() = mapOf<String,Any>()

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