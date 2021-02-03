package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.mapper.BuListMapper
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Lukas on 21/10/20.
 */
class GetCategoryGroupUseCase (
        private val buListMapper: BuListMapper,
        private val graphqlUseCase: GraphqlUseCase<DynamicHomeIconEntity>
): UseCase<List<Visitable<*>>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        val query = """
            query businessUnitList(${'$'}page:String){
              dynamicHomeIcon{
                categoryGroup(page:${'$'}page){
                  id
                  title
                  imageUrl
                  applink
                  url
                  categoryRows{
                    id
                    name
                    imageUrl
                    applinks
                    url
                  }
                }
              }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(DynamicHomeIconEntity::class.java)
    }

    fun createParams(page: String){
        params = mutableMapOf<String, Any>().apply {
            put(PAGE, page)
        }
    }

    override suspend fun executeOnBackground(): List<Visitable<*>> {
        graphqlUseCase.setRequestParams(params)
        val data = graphqlUseCase.executeOnBackground()
        return buListMapper.mapToBuListModel(data.dynamicHomeIcon.categoryGroup)
    }

    companion object{
        private const val PAGE = "page"
        const val GLOBAL_MENU = "global-menu"
    }

    fun setStrategyCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }

    fun setStrategyCloudThenCache() {
        graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }
}