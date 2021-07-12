package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.tokopedia.analyticsdebugger.FileUtils
import com.tokopedia.analyticsdebugger.database.GtmLogDB
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
    fun `regular subset should return false with message when regex is not matched`() {
        val tes = mapOf<String, Any>("field" to "\\d")
        val db = mapOf<String, Any>("field" to "some value")

        val result = tes.canValidateWithErrorMessage(db)

        assertFalse(result.first)
        assert(result.second == "Regex \"\\d\" didn't match with \"some value\" in field field")
    }

    @Test
    fun `regular subset should return true with no message`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        val result = tes.first().canValidateWithErrorMessage(db.first())

        assertTrue(result.first)
        assert(result.second == "")
    }

    @Test
    fun `regular subset with strictly compare should return false with error message`() {
        val tes = fu.getJsonArrayResources("ana.json")
        val db = fu.getJsonArrayResources("ana_actual.json")

        val result = tes.first().canValidateWithErrorMessage(db.first(), strict = true)
        assertFalse(result.first)
        assert(result.second == "Map size is different")
    }

    @Test
    fun `validate with message nested object subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidateWithErrorMessage(db).first)
    }

    @Test
    fun `validate with message nested array subset should return true`() {
        val tesCase = fu.getJsonArrayResources("nested_arr.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidateWithErrorMessage(db).first)
    }

    @Test
    fun `validate with message exact object should return true`() {
        val tesCase = fu.getJsonArrayResources("exact_nested_obj.json")
        val tes = tesCase[0]
        val db = tesCase[1]

        assertTrue(tes.canValidateWithErrorMessage(db, strict = true).first)
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

    @Test
    fun `contains pair when no map`() {
        val result = mapOf<String, Any>().containsPairOf("foo" to "\\w+")
        assertFalse(result)
    }

    @Test
    fun `has same event and label with test without event key should return false`() {
        val test = mapOf<String, Any>("eventLabel" to "")
        val db = mapOf<String, Any>("event" to "", "eventLabel" to "")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label with db without event key should return false`() {
        val test = mapOf<String, Any>("event" to "", "eventLabel" to "")
        val db = mapOf<String, Any>("eventLabel" to "")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label with different event key but test without event label should return false`() {
        val test = mapOf<String, Any>("event" to "other event")
        val db = mapOf<String, Any>("event" to "some event", "eventLabel" to "")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label with same event key but test without event label should return false`() {
        val test = mapOf<String, Any>("event" to "some event")
        val db = mapOf<String, Any>("event" to "some event", "eventLabel" to "")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label with same event key but db without event label should return false`() {
        val test = mapOf<String, Any>("event" to "some event", "eventLabel" to "")
        val db = mapOf<String, Any>("event" to "some event")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label with same event key but different event label should return false`() {
        val test = mapOf<String, Any>("event" to "some event", "eventLabel" to "some label")
        val db = mapOf<String, Any>("event" to "some event", "eventLabel" to "other label")

        assertFalse(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `has same event and label should return true`() {
        val test = mapOf<String, Any>("event" to "some event", "eventLabel" to "some label")
        val db = mapOf<String, Any>("event" to "some event", "eventLabel" to "some label")

        assertTrue(test.haveSameEventAndLabel(db))
    }

    @Test
    fun `contains map of with empty gtm log should return false`() {
        val gtmLogs = arrayListOf<GtmLogDB>()
        assertFalse(gtmLogs.containsMapOf(mapOf(), true))
    }

    @Test
    fun `contains map of but not matched gtm log should return false`() {
        val gtmLogs = arrayListOf<GtmLogDB>(
                GtmLogDB(
                        id = 0,
                        data = """
                            {
                                "field": "some value"
                            }
                        """.trimIndent(),
                        name = "",
                        timestamp = 0,
                        source = null
                )
        )
        val test = mapOf<String, Any>(
                "field" to "\\d"
        )
        assertFalse(gtmLogs.containsMapOf(test, true))
    }

    @Test
    fun `contains map of with different map size should return false`() {
        val gtmLogs = arrayListOf<GtmLogDB>(
                GtmLogDB(
                        id = 0,
                        data = """
                            {
                                "field": "some value",
                                "other_field": "other value"
                            }
                        """.trimIndent(),
                        name = "",
                        timestamp = 0,
                        source = null
                )
        )
        val test = mapOf<String, Any>(
                "field" to "some value"
        )
        assertFalse(gtmLogs.containsMapOf(test, true))
    }

    @Test
    fun `contains map of with matched field in strict mode should return true`() {
        val gtmLogs = arrayListOf<GtmLogDB>(
                GtmLogDB(
                        id = 0,
                        data = """
                            {
                                "field": "some value"
                            }
                        """.trimIndent(),
                        name = "",
                        timestamp = 0,
                        source = null
                )
        )
        val test = mapOf<String, Any>(
                "field" to "some value"
        )
        assertTrue(gtmLogs.containsMapOf(test, true))
    }

    @Test
    fun `contains map of with matched field in not strict mode should return true`() {
        val gtmLogs = arrayListOf<GtmLogDB>(
                GtmLogDB(
                        id = 0,
                        data = """
                            {
                                "field": "some value",
                                "other_field": "other value"
                            }
                        """.trimIndent(),
                        name = "",
                        timestamp = 0,
                        source = null
                )
        )
        val test = mapOf<String, Any>(
                "field" to "some value"
        )
        assertTrue(gtmLogs.containsMapOf(test, false))
    }
}