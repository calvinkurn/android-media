package com.tokopedia.product.manage.common.feature.variant.data.query

internal object ProductUpdateV3 {

    val QUERY = """
        mutation ProductUpdateV3(${'$'}input:ProductInputV3!){
            ProductUpdateV3(input:${'$'}input){
                header {
                    messages
                    reason
                    errorCode
                }
                isSuccess
            }
        }
    """.trimIndent()
}