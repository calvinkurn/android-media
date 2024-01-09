package com.tokopedia.tokopedianow.annotation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetAllAnnotationPageQuery : GqlQueryInterface {

    const val PARAM_CATEGORY_ID = "categoryID"
    const val PARAM_WAREHOUSES = "warehouses"
    const val PARAM_ANNOTATION_TYPE = "annotationType"
    const val PARAM_PAGE_SOURCE = "pageSource"
    const val PARAM_PAGE_LAST_ID = "pageLastID"

    private const val OPERATION_NAME = "TokonowGetAnnotationList"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowGetAnnotationList(
                ${'$'}$PARAM_CATEGORY_ID: String!, 
                ${'$'}$PARAM_WAREHOUSES: String!, 
                ${'$'}$PARAM_ANNOTATION_TYPE: String!, 
                ${'$'}$PARAM_PAGE_LAST_ID: String, 
                ${'$'}$PARAM_PAGE_SOURCE: String!
              ){
              TokonowGetAnnotationList(req: {
                $PARAM_CATEGORY_ID: ${'$'}$PARAM_CATEGORY_ID,
                $PARAM_WAREHOUSES: ${'$'}$PARAM_WAREHOUSES,
                $PARAM_ANNOTATION_TYPE: ${'$'}$PARAM_ANNOTATION_TYPE,
                $PARAM_PAGE_LAST_ID: ${'$'}$PARAM_PAGE_LAST_ID
              }, $PARAM_PAGE_SOURCE: ${'$'}$PARAM_PAGE_SOURCE){
                header{
                  messages
                  error_code
                }
                annotationHeader{
                  title
                }
                annotationList{
                  annotationID
                  name
                  imageURL
                  appLink
                }
                pagination{
                  hasNext
                  pageLastID
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}

