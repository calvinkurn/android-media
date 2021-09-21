package com.tokopedia.logintest.common.view.banner.domain.query

object QueryDynamicBanner {

    private const val page = "\$page"

    fun getQuery(): String {
        return """
            query getDynamicBanner($page: String!) {
                GetBanner(page: $page) {
                    URL
                    enable
                    message
                    error_message
                }
            }
        """.trimIndent()
    }
}