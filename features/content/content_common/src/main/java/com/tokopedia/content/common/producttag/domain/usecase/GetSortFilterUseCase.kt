package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.FeedQuickFilterResponse
import com.tokopedia.content.common.producttag.model.GetSortFilterResponse
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 17, 2022
 */
@GqlQuery(GetSortFilterUseCase.QUERY_NAME, GetSortFilterUseCase.QUERY)
class GetSortFilterUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetSortFilterResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetSortFilterUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSortFilterResponse::class.java)
    }

    companion object {
        private const val KEY_PARAMS = "params"

        const val QUERY_NAME = "GetSortFilterUseCaseQuery"
        const val QUERY = """
            query GetSortFilterUseCase(${"$$KEY_PARAMS"}: String!) {
              filter_sort_product($KEY_PARAMS: ${"$$KEY_PARAMS"}) {
                data {  
                 filter {  
                   title  
                   subTitle  
                   search {  
                     searchable  
                     placeholder  
                   }  
                   isNew  
                   filter_attribute_detail  
                   template_name  
                   options {  
                     name  
                     key  
                     icon  
                     Description  
                     value  
                     inputType  
                     totalData  
                     valMax  
                     valMin  
                     isPopular  
                     isNew  
                     hexColor  
                     child {  
                       key  
                       value  
                       name  
                       icon  
                       inputType  
                       totalData  
                       isPopular  
                       child {  
                         key  
                         value  
                         name  
                         icon  
                         inputType  
                         totalData  
                         isPopular  
                       }  
                     }  
                   }  
                 }  
                 sort {  
                   name  
                   key  
                   value  
                   inputType  
                   applyFilter  
                 }  
                }
              }
            }
        """

        fun createParams(
            param: SearchParamUiModel,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                KEY_PARAMS to param.joinToString()
            )
        }
    }
}