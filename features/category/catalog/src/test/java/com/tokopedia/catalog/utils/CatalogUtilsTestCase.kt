package com.tokopedia.catalog.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.model.util.CatalogUtil
import io.mockk.MockKAnnotations
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CatalogUtilsTestCase {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic(CatalogUtil::class)
    }

    @Test
    fun `Analytics Sort Filter`() {
        val hashmapSearchQuery = HashMap<String,String>()
        hashmapSearchQuery["a"] = "1"
        hashmapSearchQuery["b"] = "2"
        hashmapSearchQuery["c"] = "3"
        val result = CatalogUtil.getSortFilterAnalytics(hashmapSearchQuery)
        Assert.assertEquals(result,"a=1&b=2&c=3")
    }

}