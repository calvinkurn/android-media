package com.tokopedia.tokopedianow.annotation.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetAnnotationWidgetQuery : GqlQueryInterface {

    const val PARAM_CATEGORY_ID = "categoryID"
    const val PARAM_WAREHOUSES = "warehouses"
    const val PARAM_ANNOTATION_TYPE = "annotationType"
    const val PARAM_PAGE_SOURCE = "pageSource"

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
                ${'$'}$PARAM_PAGE_SOURCE: String!
            ){
              TokonowGetAnnotationList(req: {
                $PARAM_CATEGORY_ID: ${'$'}$PARAM_CATEGORY_ID,
                $PARAM_WAREHOUSES: ${'$'}$PARAM_WAREHOUSES,
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
