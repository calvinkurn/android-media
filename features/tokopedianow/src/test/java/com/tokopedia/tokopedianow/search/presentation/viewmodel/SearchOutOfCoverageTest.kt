package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_SOURCE_SEARCH
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.response.Tokonow
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.emptyAddress
import com.tokopedia.tokopedianow.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.assertOutOfCoverageDataView
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.core.Is.`is` as shouldBe

class SearchOutOfCoverageTest: SearchTestFixtures() {

    override fun setUp() { }

    @Test
    fun `test shop id should come from choose address data`() {
        `Given choose address data`()
        `Given search view model`()

        `When view created`()

        `Then assert shop id`(dummyChooseAddressData.shop_id)
        `Then assert warehouse id`(dummyChooseAddressData.warehouse_id)
        `Then assert first page API is called`()
    }

    private fun `Then assert shop id`(shopId: String) {
        assertThat(tokoNowSearchViewModel.shopIdLiveData.value, shouldBe(shopId))
    }

    private fun `Then assert warehouse id`(warehouseId: String) {
        assertThat(tokoNowSearchViewModel.warehouseId, shouldBe(warehouseId))
    }

    private fun `Then assert first page API is called`() {
        verify {
            getSearchFirstPageUseCase.execute(any(), any(), any())
        }
    }

    @Test
    fun `test get shop and warehouse id from API if choose address shop id empty`() {
        val shopId: Long = 12345
        val warehouseId: Long = 12356
        val warehouseResponse = GetStateChosenAddressResponse(
                tokonow = Tokonow(shopId = shopId, warehouseId = warehouseId)
        )

        `Given choose address data`(emptyAddress)
        `Given search view model`()
        `Given get warehouse API will be successful`(warehouseResponse)

        `When view created`()

        `Then assert shop id`(shopId.toString())
        `Then assert warehouse id`(warehouseId.toString())
        `Then assert first page API is called`()
        `Then assert get mini cart API is called`()
    }

    private fun `Given get warehouse API will be successful`(warehouseResponse: GetStateChosenAddressResponse) {
        every {
            getWarehouseUseCase.getStateChosenAddress(any(), any(), DEFAULT_VALUE_SOURCE_SEARCH)
        } answers {
            firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(warehouseResponse)
        }
    }

    private fun `Then assert get mini cart API is called`() {
        verify {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test get shop and warehouse id from API fail should not call search API`() {
        `Given choose address data`(emptyAddress)
        `Given search view model`()
        `Given get warehouse API will fail`()

        `When view created`()

        `Then verify search API first page is not called`()
    }

    private fun `Given get warehouse API will fail`() {
        every {
            getWarehouseUseCase.getStateChosenAddress(any(), any(), DEFAULT_VALUE_SOURCE_SEARCH)
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }
    }

    private fun `Then verify search API first page is not called`() {
        verify(exactly = 0) {
            getSearchFirstPageUseCase.execute(any(), any(), any())
        }
    }

    @Test
    fun `test warehouse id empty from choose address should show out of coverage and choose address`() {
        `Given choose address data`(LocalCacheModel(shop_id = "12345"))
        `Given search view model`()

        `When view created`()

        `Then verify search API first page is not called`()
        `Then assert scrollable false`()
        `Then assert mini cart is not visible`()
        `Then assert out of coverage visitable list`()
    }

    private fun `Then assert scrollable false`() {
        assertThat(tokoNowSearchViewModel.isRecyclerViewScrollEnabledLiveData.value, shouldBe(false))
    }

    private fun `Then assert mini cart is not visible`() {
        assertThat(tokoNowSearchViewModel.isShowMiniCartLiveData.value, shouldBe(false))
    }

    private fun `Then assert out of coverage visitable list`() {
        val visitableList = tokoNowSearchViewModel.visitableListLiveData.value!!

        visitableList.first().assertChooseAddressDataView()
        visitableList.last().assertOutOfCoverageDataView()
    }

    @Test
    fun `test warehouse id from API empty should show out of coverage`() {
        val shopId: Long = 12345
        val warehouseId: Long = 0
        val warehouseResponse = GetStateChosenAddressResponse(
                tokonow = Tokonow(shopId = shopId, warehouseId = warehouseId)
        )

        `Given choose address data`(emptyAddress)
        `Given search view model`()
        `Given get warehouse API will be successful`(warehouseResponse)

        `When view created`()

        `Then verify search API first page is not called`()
        `Then assert scrollable false`()
        `Then assert mini cart is not visible`()
        `Then assert out of coverage visitable list`()
    }
}