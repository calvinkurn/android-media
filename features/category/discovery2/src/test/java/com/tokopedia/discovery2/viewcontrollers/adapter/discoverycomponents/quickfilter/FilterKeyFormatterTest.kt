package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FilterKeyFormatterTest(
    private val selectedParameters: Map<String, String>,
    private val filterKey: Set<String>,
    private val expected: Map<String, String>
) {

    @Test
    fun `given selected parameters, then should only append RPC prefix to key which is contained on filter keys`() {
        assertEquals(expected, FilterKeyFormatter.format(selectedParameters, filterKey))
    }

    //region parameters
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<Any>> {
            val filterKeys = setOf(
                "sortfilter_shipping_child",
                "sortfilter_fcity",
                "sortfilter_is_fulfillment",
                "sortfilter_shop_tier",
                "pmin",
                "rt",
                "sortfilter_cashbackm",
                "sortfilter_condition",
                "sortfilter_shipping",
                "sortfilter_latest_product",
                "sortfilter_preorder"
            )

            return listOf(
                arrayOf(
                    mapOf(
                        "source" to "search-autocomplete.04.01.01",
                        "q" to "kipas",
                        "origin_filter" to "filter",
                        "sortfilter_shipping_child" to "nearby,20,24,49"
                    ),
                    filterKeys,
                    mapOf(
                        "source" to "search-autocomplete.04.01.01",
                        "q" to "kipas",
                        "rpc_origin_filter" to "filter",
                        "rpc_sortfilter_shipping_child" to "nearby,20,24,49"
                    )
                ),
                arrayOf(
                    mapOf(
                        "q" to "kipas",
                        "origin_filter" to "filter",
                        "sortfilter_fcity" to "174,175,176,177,178,179 ",
                        "sortfilter_ob" to "5",
                        "source" to "search-autocomplete.04.01.01"
                    ),
                    filterKeys,
                    mapOf(
                        "q" to "kipas",
                        "rpc_origin_filter" to "filter",
                        "rpc_sortfilter_fcity" to "174,175,176,177,178,179 ",
                        "rpc_sortfilter_ob" to "5",
                        "source" to "search-autocomplete.04.01.01"
                    )
                )
            )
        }
    }
    //endregion
}
