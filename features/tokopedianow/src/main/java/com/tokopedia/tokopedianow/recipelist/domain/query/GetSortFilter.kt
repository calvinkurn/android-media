package com.tokopedia.tokopedianow.recipelist.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetSortFilter: GqlQueryInterface {

    const val QUERY_PARAMS = "params"

    private const val OPERATION_NAME = "TokonowRecipesFilterSort"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(
            ${'$'}${QUERY_PARAMS}: String!
        ) {
            $OPERATION_NAME(
                ${QUERY_PARAMS}:${'$'}${QUERY_PARAMS}
            ) {
                header{
                  success
                  message
                  statusCode
                  processTime
                }
                data{
                  filter{
                    title
                    options{
                      name
                      key
                      icon
                      value
                      inputType
                      isPopular
                      isNew
                    }
                  }
                  sort{
                    name
                    key
                    value
                  }
                }   
            }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}