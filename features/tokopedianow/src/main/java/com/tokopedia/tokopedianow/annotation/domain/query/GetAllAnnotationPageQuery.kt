package com.tokopedia.tokopedianow.annotation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetAllAnnotationPageQuery : GqlQueryInterface {

    const val PARAM_CATEGORY_ID = "categoryID"
    const val PARAM_WAREHOUSE_IDS = "warehouseIDs"
    const val PARAM_ANNOTATION_TYPE = "annotationType"
    const val PARAM_PAGE_SOURCE = "pageSource"
    const val VALUE_PAGE_SOURCE = "ALL_ANNOTATION"

    private const val OPERATION_NAME = "TokonowGetAnnotationList"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query TokonowGetAnnotationList(
                ${'$'}$PARAM_CATEGORY_ID: String!, 
                ${'$'}$PARAM_WAREHOUSE_IDS: String!, 
                ${'$'}$PARAM_ANNOTATION_TYPE: String!, 
                ${'$'}$PARAM_PAGE_SOURCE: String!
            ){
              TokonowGetAnnotationList(req: {
                $PARAM_CATEGORY_ID: ${'$'}$PARAM_CATEGORY_ID,
                $PARAM_WAREHOUSE_IDS: ${'$'}$PARAM_WAREHOUSE_IDS,
                $PARAM_ANNOTATION_TYPE: ${'$'}$PARAM_ANNOTATION_TYPE,
              }, $PARAM_PAGE_SOURCE: ${'$'}$PARAM_PAGE_SOURCE){
                header{
                  process_time
                  messages
                  reason
                  error_code
                }
                status
                annotationHeader{
                  title
                  hasMore
                  allPageAppLink
                  allPageWebLink
                }
                annotationList{
                  annotationID
                  name
                  imageURL
                  appLink
                  webLink
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}

