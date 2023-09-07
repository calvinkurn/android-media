package com.tokopedia.discovery2.datamapper

import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SearchParameterFactoryTest {

    private val path = arrayOf(
        "discovery/deals-c-search",
        "discovery/deals",
        "discovery/waktu-indonesia-belanja"
    ).random()

    @Test
    fun `given query and path, when construct SearchParameter, should return SearchParameter instance`() {
        val keyword = arrayOf("kipas", "TV", "Smartphone").random()
        val query = "q=$keyword"

        val param = SearchParameterFactory.construct(query, path)
        assertEquals(keyword, param?.getSearchQuery())
    }

    @Test
    fun `given query is unavailable, when construct SearchParameter, should return null`() {
        val param = SearchParameterFactory.construct(null, path)
        assertNull(param)
    }
}
