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
    fun `test one`() {

    }

}