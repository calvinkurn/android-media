package com.tokopedia.graphql.util

import com.google.gson.JsonSyntaxException
import com.tokopedia.graphql.domain.example.FooModel
import com.tokopedia.graphql.domain.example.NestedFooModel
import com.tokopedia.graphql.domain.example.NumberFooModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.typeOf

@ExperimentalCoroutinesApi
class ParamUtilTest {

    private val dummyModel= FooModel(1, "")
    private val nestedDummyModel = NestedFooModel(1, "", dummyModel)

    @Test
    fun `property int type number should be converted as double` () {
        val intModel = NumberFooModel(1)
        val output = intModel.toMapParam()
        Assert.assertTrue(output["id"] is Double)
    }

    @Test
    fun `property double type number should be converted as double` () {
        val doubleModel = NumberFooModel(1.0)
        val output = doubleModel.toMapParam()
        Assert.assertTrue(output["id"] is Double)
    }

    @Test
    fun `property float type number should be converted as double` () {
        val floatModel = NumberFooModel(1.0)
        val output = floatModel.toMapParam()
        Assert.assertTrue(output["id"] is Double)
    }

    @Test
    fun `all properties name correspond to key map param` () {
        val expectedOutput = mapOf("id" to 1.0, "msg" to "")
        val expectedKeys = expectedOutput.keys
        val outputKeys = dummyModel.toMapParam().keys
        Assert.assertEquals(expectedKeys, outputKeys)
    }

    @Test
    fun `convert normal object to map should return as expected`() {
        val expectedOutput = mapOf("id" to 1.0, "msg" to "")
        val output = dummyModel.toMapParam()
        Assert.assertEquals(expectedOutput, output)
    }

    @Test
    fun `convert nested object to map should return as expected`() {
        val expectedOutput = mapOf("id" to 1.0, "msg" to "",
            "foo" to mapOf<String, Any>("id" to 1.0, "msg" to ""))
        val output = nestedDummyModel.toMapParam()
        Assert.assertEquals(expectedOutput, output)
    }

    @Test(expected = JsonSyntaxException::class)
    fun `convert String object to map should throw error`() {
        val dummyString = "foo model"
        dummyString.toMapParam()
    }

    @Test(expected = JsonSyntaxException::class)
    fun `convert Int object to map should throw error`() {
        val dummyInt = 1
        dummyInt.toMapParam()
    }
}