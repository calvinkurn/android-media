package com.tokopedia.test.application.graphql

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GqlQueryParserTest {

    @Test
    fun `parse multiple query test`() {
        val cases = listOf(
            """
            query fetchAnnouncementWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchAnnouncementWidgetData(dataKeys:${'$'}dataKeys) {
                data {
                  dataKey
                }
              }
            }
            """.trimIndent() to listOf("fetchAnnouncementWidgetData"),
            """
                query getPMInterruptData(${'$'}shopId: Int!, ${'$'}source: String!) {
              goldGetPMShopInfo(shop_id: ${'$'}shopId, source: ${'$'}source) {
                is_new_seller
              }
              goldGetPMOSStatus(shopID: ${'$'}shopId, includeOS: true) {
                data {
                  power_merchant {
                    status
                  }
                  official_store {
                    status
                  }
                }
              }
            }
            """.trimIndent() to listOf("goldGetPMShopInfo", "goldGetPMOSStatus"),
            """
                mutation register_check (${'$'}id: String!){
                    registerCheck(id: ${'$'}id) {
                        isExist
                        isPending
                    }
                }
            """.trimIndent() to listOf("registerCheck")
        )

        val actual = cases.map { GqlQueryParser.parse(it.first) }

        assertThat(actual, `is`(cases.map { it.second }))
    }

    @Test
    fun `test regex pattern`() {
        val case = listOf(
            "goldGetPMSettingInfo(shopID: ${'$'}shopId, source:${'$'}source)" to "goldGetPMSettingInfo",
            "fetchAnnouncementWidgetData(dataKeys:${'$'}dataKeys)" to "fetchAnnouncementWidgetData"
        )

        val actual = case.map { GqlQueryParser.QUERY_PATTERN.find(it.first)!!.groupValues[1] }

        assertThat(actual, `is`(case.map { it.second }))
    }
}