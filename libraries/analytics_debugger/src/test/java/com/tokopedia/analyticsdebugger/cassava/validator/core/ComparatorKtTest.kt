package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.analyticsdebugger.FileUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ComparatorKtTest {

    private val fu: FileUtils by lazy { FileUtils() }

    @Test
    fun `regular subset should return true`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        assertTrue(tes.first().canValidate(db.first()))
    }

    @Test
    fun `regular subset with strictly compare should return false`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        assertFalse(tes.first().canValidate(db.first(), strict = true))
    }

    @Test
    fun `nested object subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidate(db))
    }

    @Test
    fun `nested array subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_arr.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidate(db))
    }

    @Test
    fun `exact object should return true`() {
        val tesCase = fu.getJsonArrayResources("exact_nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidate(db, strict = true))
    }

    @Test
    fun `regex match given * when compared to any string should return true`() {
        val tesVal = ".*"
        val objVal = "any string would match"
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given d* when compared to any string should return true`() {
        val tesVal = "\\d*"
        val objVal = "55643"
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given idd* when compared to any string should return true`() {
        val tesVal = "id/\\d*"
        val objVal = "id/55643"
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given d regex when compared to int json val should return true`() {
        // this test should rarely happen as gson parses int to double
        val tesVal = "\\d*"
        val objVal = 55643
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given d regex when compared to double json val should return true`() {
        val tesVal = "\\d*\\.\\d*"
        val objVal = 749000.5
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `regex match given bool regex when compared to bool json val should return true`() {
        val tesVal = "true|false"
        val objVal = true
        assertTrue(regexEquals(tesVal, objVal))
    }

    @Test
    fun `contains pair can validate root case`() {
        val test = """
            { "foo": "bar" }
        """.trimIndent()
        val result = test.toJsonMap().containsPairOf("foo" to "bar")
        assertTrue(result)
    }

    @Test
    fun `contains pair can validate nested case with regex`() {
        val test = """
            {
                "nested": {
                    "foo": "bar"
                    }
            }
        """.trimIndent()
        val result = test.toJsonMap().containsPairOf("foo" to "\\w+")
        assertTrue(result)
    }
}