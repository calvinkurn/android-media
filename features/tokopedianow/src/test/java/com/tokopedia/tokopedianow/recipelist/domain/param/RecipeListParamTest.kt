package com.tokopedia.tokopedianow.recipelist.domain.param

import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecipeListParamTest {

    private lateinit var recipeListParam: RecipeListParam

    private val warehouseList = listOf(
        WarehouseData(
            warehouseId = "152612",
            serviceType = "2h"
        )
    )

    @Before
    fun setUp() {
        recipeListParam = RecipeListParam()
    }

    @Test
    fun `given recipe list params when create request param should return expected request param`() {
        val queryParams =
            "title=hello world&ingredient_ids=4,5,6&sort_by=Newest"

        recipeListParam.apply {
            page = 1
            perPage = 5
            warehouses = warehouseList
            sourcePage = "TokoNow Home"
            mapToQueryParamsMap(queryParams)
        }

        val actualParams = recipeListParam.create()

        val expectedPage = 1
        val expectedPerPage = 5
        val expectedWarehouses = listOf(
            WarehouseData(
                warehouseId = "152612",
                serviceType = "2h"
            )
        )
        val expectedSourcePage = "TokoNow Home"
        val expectedQueryParams = "title=hello+world&ingredient_ids=4,5,6&sort_by=Newest"

        assertEquals(expectedPage, actualParams["page"])
        assertEquals(expectedPerPage, actualParams["perPage"])
        assertEquals(expectedWarehouses, actualParams["warehouses"])
        assertEquals(expectedSourcePage, actualParams["sourcePage"])
        assertEquals(expectedQueryParams, actualParams["queryParam"])
    }

    @Test
    fun `given params are updated when create request param should return expected request param`() {
        val queryParams = "title=hello world&ingredient_ids=4,5,6&sort_by=Newest"

        recipeListParam.apply {
            page = 1
            perPage = 5
            warehouses = warehouseList
            sourcePage = "TokoNow Home"
            mapToQueryParamsMap(queryParams)
        }

        // update query param
        recipeListParam.apply {
            page = 2
            perPage = 7
            warehouses = listOf(
                WarehouseData(
                    warehouseId = "152613",
                    serviceType = "fc"
                )
            )
            sourcePage = "TokoNow Search"
            queryParamsMap["title"] = "Hello now"
            queryParamsMap["ingredient_ids"] = "5,6,7"
            queryParamsMap["sort_by"] = "Oldest"
        }

        val actualParams = recipeListParam.create()

        val expectedPage = 2
        val expectedPerPage = 7
        val expectedWarehouses = listOf(
            WarehouseData(
                warehouseId = "152613",
                serviceType = "fc"
            )
        )
        val expectedSourcePage = "TokoNow Search"
        val expectedQueryParams = "title=Hello+now&ingredient_ids=5,6,7&sort_by=Oldest"

        assertEquals(expectedPage, actualParams["page"])
        assertEquals(expectedPerPage, actualParams["perPage"])
        assertEquals(expectedWarehouses, actualParams["warehouses"])
        assertEquals(expectedSourcePage, actualParams["sourcePage"])
        assertEquals(expectedQueryParams, actualParams["queryParam"])
    }

    @Test
    fun `given params is empty when create request param should NOT include empty param`() {
        recipeListParam.apply {
            page = 2
            perPage = 7
            warehouses = warehouseList
            sourcePage = "TokoNow Search"
            queryParamsMap["title"] = "Hello now"
            queryParamsMap["ingredient_ids"] = ""
            queryParamsMap["sort_by"] = ""
        }

        val actualParams = recipeListParam.create()

        val expectedPage = 2
        val expectedPerPage = 7
        val expectedWarehouses = listOf(
            WarehouseData(
                warehouseId = "152612",
                serviceType = "2h"
            )
        )
        val expectedSourcePage = "TokoNow Search"
        val expectedQueryParams = "title=Hello+now"

        assertEquals(expectedPage, actualParams["page"])
        assertEquals(expectedPerPage, actualParams["perPage"])
        assertEquals(expectedWarehouses, actualParams["warehouses"])
        assertEquals(expectedSourcePage, actualParams["sourcePage"])
        assertEquals(expectedQueryParams, actualParams["queryParam"])
    }

    @Test
    fun `given query params when generateQueryParams should return expected query string`() {
        val queryParams = "title=hello world&tag_ids=1,2,3&ingredient_ids=4,5,6&category_ids=7,8,9&from_duration=5&to_duration=10&from_portion=10&to_portion=20&sort_by=Newest"

        recipeListParam.mapToQueryParamsMap(queryParams)
        val actualQueryParams = recipeListParam.generateQueryParams()

        val expectedQueryParams = "title=hello+world&tag_ids=1,2,3&ingredient_ids=4,5,6&category_ids=7,8,9&from_duration=5&to_duration=10&from_portion=10&to_portion=20&sort_by=Newest"
        assertEquals(expectedQueryParams, actualQueryParams)
    }
}
