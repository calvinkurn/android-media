package com.tokopedia.product.manage.common.constant

object ProductUpdateV3QueryConstant {
    val BASE_QUERY = """
            mutation ProductUpdateV3(%1s) {
                ProductUpdateV3(%2s) {
                    %3s
                  }
                }
        """.trimIndent()
}