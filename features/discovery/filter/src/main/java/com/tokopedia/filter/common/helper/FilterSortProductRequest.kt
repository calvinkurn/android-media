package com.tokopedia.filter.common.helper

import com.tokopedia.filter.common.helper.FilterSortProductRequest.FILTER_SORT_PRODUCT_GQL
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery("FilterSortProduct", FILTER_SORT_PRODUCT_GQL)
internal object FilterSortProductRequest {

    const val FILTER_SORT_PRODUCT_GQL = """
        query FilterSortProductQuery(${'$'}params: String!) {
            filter_sort_product(params: ${'$'}params) {
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
}