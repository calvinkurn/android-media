package com.tokopedia.deals.pdp

import com.tokopedia.unit.test.rule.CoroutineTestRule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class DealsPDPAllLocationViewModelTest : DealsPDPAllLocationViewModelTestFixture() {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun `when getting search result by name give expected search result`() {
        val rawOutlets = createPDPData().eventProductDetail.productDetailData.outlets
        val expectedOutlets = listOf(
            com.tokopedia.deals.ui.pdp.data.Outlet(
                id = "20196",
                productId = "0",
                locationId = "318",
                name = "Aeon Mall",
                searchName = "",
                metaTitle = "",
                metaDescription = "",
                district = "Jl. Bsd Raya Utama, Pagedangan, Tangerang, Banten",
                gmapAddress = "",
                neighbourhood = "",
                coordinates = "-6.3046014,106.6434081",
                state = "",
                country = "Indonesia",
                isSearchable = 0,
                locationStatus = 1,
                priority = 0,
                createdAt = "",
                updatedAt = ""
            )
        )

        var outlet: List<com.tokopedia.deals.ui.pdp.data.Outlet>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowSearchResult.collectLatest {
                    outlet = it
                }
            }
            viewModel.submitSearch(keyWord, rawOutlets)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedOutlets, outlet)
    }

    @Test
    fun `when getting search result by district give expected search result`() {
        val rawOutlets = createPDPData().eventProductDetail.productDetailData.outlets
        val expectedOutlets = listOf(
            com.tokopedia.deals.ui.pdp.data.Outlet(
                id = "20196",
                productId = "0",
                locationId = "318",
                name = "Aeon Mall",
                searchName = "",
                metaTitle = "",
                metaDescription = "",
                district = "Jl. Bsd Raya Utama, Pagedangan, Tangerang, Banten",
                gmapAddress = "",
                neighbourhood = "",
                coordinates = "-6.3046014,106.6434081",
                state = "",
                country = "Indonesia",
                isSearchable = 0,
                locationStatus = 1,
                priority = 0,
                createdAt = "",
                updatedAt = ""
            )
        )

        var outlet: List<com.tokopedia.deals.ui.pdp.data.Outlet>? = null

        runBlockingTest {
            val collectorJob = launch {
                viewModel.flowSearchResult.collectLatest {
                    outlet = it
                }
            }
            viewModel.submitSearch(districtKeyWord, rawOutlets)
            collectorJob.cancel()
        }

        Assert.assertEquals(expectedOutlets, outlet)
    }
}
