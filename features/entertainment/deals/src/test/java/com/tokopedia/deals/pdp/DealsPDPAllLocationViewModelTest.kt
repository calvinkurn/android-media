package com.tokopedia.deals.pdp

import com.tokopedia.deals.pdp.data.Outlet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class DealsPDPAllLocationViewModelTest : DealsPDPAllLocationViewModelTestFixture() {

    @Test
    fun `when getting search result by name give expected search result`() {
        val rawOutlets = createPDPData().eventProductDetail.productDetailData.outlets
        val expectedOutlets = listOf(
            Outlet(
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

        var outlet: List<Outlet>? = null

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
            Outlet(
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

        var outlet: List<Outlet>? = null

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
