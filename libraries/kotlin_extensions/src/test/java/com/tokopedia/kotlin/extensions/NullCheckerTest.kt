package com.tokopedia.kotlin.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.util.ContainNullException
import com.tokopedia.kotlin.util.isContainNull
import com.tokopedia.kotlin.util.throwExceptionWhenNull
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by fajarnuha on 01/02/19.
 */

data class Shop(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("isGold")
        @Expose
        val isGold: Boolean = false,
        @SerializedName("address")
        @Expose
        val address: Address = Address())

data class Address(
        @SerializedName("alias")
        @Expose
        val alias: List<String> = ArrayList())

val objectNull = """
    |{
    |   "name": "Tokopedia",
    |   "isGold": true,
    |   "address": null
    |}
""".trimMargin()

val stringNull = """
    |{
    |   "name": null,
    |   "isGold": true,
    |   "address": {
    |       "alias": ["hi"]
    |   }
    |}
""".trimMargin()

val arrayNull = """
    |{
    |   "name": "Tokopedia",
    |   "isGold": true,
    |   "address": {
    |       "alias": null
    |   }
    |}
""".trimMargin()

val primitiveNull = """
    |{
    |   "name": "Tokopedia",
    |   "isGold": null,
    |   "address": {
    |       "alias": ["hi", "ha"]
    |   }
    |}
""".trimMargin()

val fieldNotProvided = """
    |{
    |   "isGold": true,
    |   "address": {
    |       "alias": ["ha"]
    |   }
    |}
""".trimMargin()


public class NullCheckerTest {

    lateinit var gson: Gson

    @Before
    fun beforeTest() {
        gson = GsonBuilder().create()
    }

    @Test
    fun shouldReturnErrorWhenGivenNullObject() {
        val shop = gson.fromJson(objectNull, Shop::class.java)
        val actual = isContainNull(shop)
        Assert.assertTrue(actual)
    }

    @Test
    fun shouldReturnErrorWhenGivenNullString() {
        val shop = gson.fromJson(stringNull, Shop::class.java)
        val actual = isContainNull(shop)
        Assert.assertTrue(actual)
    }

    @Test
    fun shouldReturnErrorWhenGivenNullArray() {
        val shop = gson.fromJson(arrayNull, Shop::class.java)
        val actual = isContainNull(shop)
        Assert.assertTrue(actual)
    }

    @Test
    fun shouldNotReturnErrorAndHavingDefaultValueWhenGivenNullField() {
        val shop = gson.fromJson(primitiveNull, Shop::class.java)
        val actual = isContainNull(shop)
        Assert.assertFalse(actual)
        Assert.assertFalse(shop.isGold)
    }

    @Test
    fun shouldReturnNotErrorAndHavingDefaultValueWhenFieldIsNotProvidedByJson() {
        val shop = gson.fromJson(fieldNotProvided, Shop::class.java)
        val actual = isContainNull(shop)
        Assert.assertFalse(actual)
        Assert.assertEquals("", shop.name)
    }

    @Test(expected = ContainNullException::class)
    fun shouldThrowExceptionWhenJsonHasNull() {
        val shop = gson.fromJson(objectNull, Shop::class.java)
        throwExceptionWhenNull(shop)
    }
}
