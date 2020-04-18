package com.tokopedia.analyticsdebugger.validator.core

import com.tokopedia.analyticsdebugger.FileUtils
import org.junit.Assert.assertTrue
import org.junit.Test

class ComparatorKtTest {

    private val fu: FileUtils by lazy { FileUtils() }

    @Test
    fun `regular subset should return true`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        assertTrue(tes.first().compare(db.first()))
    }

    @Test
    fun `nested object subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.compare(db))
    }

    @Test
    fun `nested array subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_arr.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.compare(db))
    }

    @Test
    fun `exact object should return true`() {
        val tesCase = fu.getJsonArrayResources("exact_nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.compare(db, strict = true))
    }

    @Test
    fun `regex match given * when compared to any string should return true`() {
        val tesVal = "{{.*}}"
        val objVal = "any string would match"
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given d* when compared to any string should return true`() {
        val tesVal = "{{\\d*}}"
        val objVal = "55643"
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given idd* when compared to any string should return true`() {
        val tesVal = "{{id/\\d*}}"
        val objVal = "id/55643"
        assertTrue(regexEquals(tesVal, objVal))
    }
}