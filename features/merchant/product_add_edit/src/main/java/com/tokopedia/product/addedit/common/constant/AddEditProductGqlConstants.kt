package com.tokopedia.product.addedit.common.constant

object AddEditProductGqlConstants {
    private val BASE_QUERY_MUTATION_PRODUCT_VALIDATE_V3 = """
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

    fun getValidateProductDescriptionQuery() = String.format(BASE_QUERY_MUTATION_PRODUCT_VALIDATE_V3, VALIDATE_PRODUCT_DESCRIPTION_REQUEST)
    fun getValidateProductQuery() = String.format(BASE_QUERY_MUTATION_PRODUCT_VALIDATE_V3, VALIDATE_PRODUCT_REQUEST)
    fun getValidateProductNameQuery() = String.format(BASE_QUERY_MUTATION_PRODUCT_VALIDATE_V3, VALIDATE_PRODUCT_NAME_REQUEST)

}