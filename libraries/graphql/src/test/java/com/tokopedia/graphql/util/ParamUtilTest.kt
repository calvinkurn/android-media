package com.tokopedia.graphql.util

import com.google.gson.JsonSyntaxException
import com.tokopedia.graphql.domain.example.FooModel
import com.tokopedia.graphql.domain.example.NestedFooModel
import com.tokopedia.graphql.domain.example.NumberFooModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.reflect.typeOf

@ExperimentalCoroutinesApi
class ParamUtilTest {

    private val dummyModel= FooModel(1, "")
    private val nestedDummyModel = NestedFooModel(1, "", dummyModel)

    @Test
    fun `property int type number should be converted as int` () {
        val intModel = NumberFooModel(1)
        val output = intModel.toMapParam()
        Assert.assertTrue(output["id"] is Long)
    }

    @Test
    fun `property double type number should be converted as double` () {
        val doubleModel = NumberFooModel(1.2)
        val output = doubleModel.toMapParam()
        Assert.assertTrue(output["id"] is Double)
    }

    @Test
    fun `property float type number should be converted as double` () {
        val floatModel = NumberFooModel(1.2f)
        val output = floatModel.toMapParam()
        Assert.assertTrue(output["id"] is Double)
    }

    @Test
    fun `property Long type number should be converted as Long` () {
        val floatModel = NumberFooModel(1L)
        val output = floatModel.toMapParam()
        Assert.assertTrue(output["id"] is Long)
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
        val expectedOutput = mapOf("id" to 1L, "msg" to "")
        val output = dummyModel.toMapParam().toMap()
        Assert.assertEquals(expectedOutput, output)
    }

    @Test
    fun `convert nested object to map should return as expected`() {
        val expectedOutput = mapOf("id" to 1L, "msg" to "",
            "foo" to mapOf<String, Any>("id" to 1L, "msg" to ""))
        val output = nestedDummyModel.toMapParam().toMap()
        Assert.assertEquals(expectedOutput, output)
    }
}