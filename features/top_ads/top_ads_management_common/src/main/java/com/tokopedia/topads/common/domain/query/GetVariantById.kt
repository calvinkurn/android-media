package com.tokopedia.topads.common.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.topads.common.data.internal.ParamObject

internal object GetVariantById : GqlQueryInterface {

    private const val OPERATION_NAME = "GetVariantById"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(${'$'}${ParamObject.INPUT}: GetVariantById!){
            $OPERATION_NAME(${ParamObject.INPUT}: ${'$'}${ParamObject.INPUT}){
                userIdVariants {
                    variant
                    experiment
                }
                shopIdVariants {
                    experiment
                    variant
                }
                sessionIdVariants{
                  experiment
                  variant
                }
            }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
