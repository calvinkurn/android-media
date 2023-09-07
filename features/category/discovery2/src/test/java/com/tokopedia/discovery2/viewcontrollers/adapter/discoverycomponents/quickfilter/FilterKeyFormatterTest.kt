package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FilterKeyFormatterTest(
    private val filterParameters: Map<String, String>,
    private val originalKeyParameters: Set<String>,
    private val expected: Map<String, String>
) {

    @Test
    fun `given filter parameters, the should only append RPC prefix to non-original key`() {
        assertEquals(expected, FilterKeyFormatter.format(filterParameters, originalKeyParameters))
    }

    //region parameters
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                mapOf(
                    "source" to "search-autocomplete.04.01.01",
                    "q" to "kipas",
                    "origin_filter" to "filter",
                    "sortfilter_shipping_child" to "nearby,20,24,49"
                ),
                setOf(
                    "source",
                    "q"
                ),
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
                    "sortfilter_is_fulfillment" to "true"
                ),
                setOf("q"),
                mapOf(
                    "q" to "kipas",
                    "rpc_origin_filter" to "filter",
                    "rpc_sortfilter_fcity" to "174,175,176,177,178,179 ",
                    "rpc_sortfilter_ob" to "5",
                    "rpc_sortfilter_is_fulfillment" to "true"
                )
            )
        )
    }
    //endregion
}
