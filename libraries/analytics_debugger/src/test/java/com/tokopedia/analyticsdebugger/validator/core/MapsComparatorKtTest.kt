package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.FileUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class MapsComparatorKtTest {

    private val fu: FileUtils by lazy { FileUtils() }

    @Test
    fun `regular subset should return true`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        assertTrue(tes.first().subSetOf(db.first()))
    }

    @Test
    fun `nested object subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.subSetOf(db))
    }

    @Test
    fun `nested array subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_arr.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.subSetOf(db))
    }

    @Test
    fun `exact object should return true`() {
        val tesCase = fu.getJsonArrayResources("exact_nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes == db)
    }
}