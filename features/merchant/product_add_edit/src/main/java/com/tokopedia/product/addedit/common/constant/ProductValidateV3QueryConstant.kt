package com.tokopedia.product.addedit.common.constant

object ProductValidateV3QueryConstant {
    private val BASE_QUERY = """
            mutation ProductValidateV3(${'$'}input: ProductInputV3!) {
                  ProductValidateV3(input: ${'$'}input) {
                    header {
                      messages
                      reason
                      errorCode
                    }
                    isSuccess
                    data {
                      %1s
                    }
                  }
             }
        """.trimIndent()
    private val VALIDATE_PRODUCT_DESCRIPTION_REQUEST = """
               description
        """.trimIndent()

    private val VALIDATE_PRODUCT_REQUEST = """
               productName
               sku
        """.trimIndent()

    private val VALIDATE_PRODUCT_NAME_REQUEST = """
                productName
        """.trimIndent()

    fun getValidateProductDescriptionQuery() = String.format(BASE_QUERY, VALIDATE_PRODUCT_DESCRIPTION_REQUEST)
    fun getValidateProductQuery() = String.format(BASE_QUERY, VALIDATE_PRODUCT_REQUEST)
    fun getValidateProductNameQuery() = String.format(BASE_QUERY, VALIDATE_PRODUCT_NAME_REQUEST)

}