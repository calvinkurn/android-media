package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Lukas on 21/10/20.
 */
class GetCategoryGroupUseCase (
        private val graphqlUseCase: GraphqlUseCase<DynamicHomeIconEntity>
): UseCase<Result<List<DynamicHomeIconEntity.Category>>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        val query = """
            query dynamicHomeIcon(${'$'}page:String){
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
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    fun createParams(page: String){
        params = mutableMapOf<String, Any>().apply {
            put(PAGE, page)
        }
    }

    override suspend fun executeOnBackground(): Result<List<DynamicHomeIconEntity.Category>> {
        return try {
            Success(graphqlUseCase.executeOnBackground().dynamicHomeIcon.categoryGroup)
        } catch (e: Throwable){
            Fail(e)
        }
    }

    companion object{
        private const val PAGE = "page"
        const val GLOBAL_MENU = "global-menu"
    }

}