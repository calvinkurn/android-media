package com.tokopedia.homenav.category.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.category.domain.model.DynamicHomeIconEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Lukas on 21/10/20.
 */
class GetCategoryListUseCase (
        private val graphqlUseCase: GraphqlUseCase<DynamicHomeIconEntity>
): UseCase<Result<List<DynamicHomeIconEntity.CategoryRow>>>(){

    private var params : Map<String, Any> = mapOf()

    init {
        val query = """
            query dynamicHomeIcon(${'$'}page:String){
              dynamicHomeIcon{
                categoryGroup(page:${'$'}page){
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

    override suspend fun executeOnBackground(): Result<List<DynamicHomeIconEntity.CategoryRow>> {
        return try {
            Success(graphqlUseCase.executeOnBackground().dynamicHomeIcon.categoryGroup.firstOrNull()?.categoryRows ?: throw Throwable())
        } catch (e: Throwable){
            Fail(e)
        }
    }

    companion object{
        private const val PAGE = "page"
    }

}