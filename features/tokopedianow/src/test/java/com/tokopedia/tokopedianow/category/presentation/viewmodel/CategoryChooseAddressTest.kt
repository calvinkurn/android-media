package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.oldcategory.presentation.view.TokoNowCategoryFragment.Companion.DEFAULT_CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryChooseAddressTest: CategoryTestFixtures() {

    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
    private val requestParamsSlot = slot<RequestParams>()
    private val requestParams by lazy { requestParamsSlot.captured }

    override fun setUp() { }

    @Test
    fun `choose address params should not be sent if empty`() {
        val chooseAddressEmptyWithShopId = LocalCacheModel(shop_id = "12345", warehouse_id = "12345")
        `Given choose address data`(chooseAddressEmptyWithShopId)
        `Given category view model`()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)

        `When view created`()

        `Then assert request params does not contain choose address params`()
    }

    private fun `Then assert request params does not contain choose address params`() {
        val tokonowQueryParams = requestParams.parameters[TOKONOW_QUERY_PARAMS] as Map<String, Any>
        val shouldNotContainKeys = listOf(
                SearchApiConst.USER_CITY_ID,
                SearchApiConst.USER_ADDRESS_ID,
                SearchApiConst.USER_DISTRICT_ID,
                SearchApiConst.USER_LAT,
                SearchApiConst.USER_LONG,
                SearchApiConst.USER_POST_CODE,
        )

        shouldNotContainKeys.forEach {
            val reason = "Tokonow query params should not contain key $it."
            assertThat(reason, tokonowQueryParams.containsKey(it), shouldBe(false))
        }
    }

    @Test
    fun `onLocalizingAddressSelected should reload page with new address`() {
        `Given choose address data changes`()
        `Given view model setup until view created`()

        `When localizing address selected`()

        `Then assert request params contains new address`(dummyChooseAddressData)
    }

    @Test
    fun `Get current category id if default value of category level 2 and level 3 are empty`(){
        `Given category view model`()

        val categoryLvl1 = "1000"
        var categoryLvl2 = ""
        var categoryLvl3 = ""

        var currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl1, currentCategoryId)

        categoryLvl2 = "2000"
        currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl2, currentCategoryId)

        categoryLvl3 = "3000"
        currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl3, currentCategoryId)
    }

    @Test
    fun `Get current category id if default value of category level 2 and level 3 are zero string`(){
        `Given category view model`()

        val categoryLvl1 = "1000"
        var categoryLvl2 = DEFAULT_CATEGORY_ID
        var categoryLvl3 = DEFAULT_CATEGORY_ID

        var currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl1, currentCategoryId)

        categoryLvl2 = "2000"
        currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl2, currentCategoryId)

        categoryLvl3 = "3000"
        currentCategoryId = tokoNowCategoryViewModel.getCurrentCategoryId(categoryLvl1, categoryLvl2, categoryLvl3)

        assertEquals(categoryLvl3, currentCategoryId)
    }

    private fun `Given choose address data changes`() {
        every {
            chooseAddressWrapper.getChooseAddressData()
        } answers {
            ChooseAddressConstant.emptyAddress
        } andThen {
            dummyChooseAddressData
        }
    }

    private fun `Given view model setup until view created`() {
        `Given category view model`()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)
        `Given view already created`()
    }

    private fun `When localizing address selected`() {
        tokoNowCategoryViewModel.onLocalizingAddressSelected()
    }

    private fun `Then assert request params contains new address`(
            chooseAddressData: LocalCacheModel,
    ) {
        val tokonowQueryParams = requestParams.parameters[TOKONOW_QUERY_PARAMS] as Map<String, Any>

        val mandatoryParams = createMandatoryTokonowQueryParams(chooseAddressData)
        mandatoryParams.forEach { (key, value) ->
            assertThat(tokonowQueryParams[key].toString(), shouldBe(value))
        }
    }

    @Test
    fun `onResume should reload page when choose address updated`() {
        `Given choose address data changes`()
        `Given choose address is updated`()
        `Given view model setup until view created`()

        `When view resumed`()

        `Then assert request params contains new address`(dummyChooseAddressData)
    }

    private fun `Given choose address is updated`() {
        every {
            chooseAddressWrapper.isChooseAddressUpdated(ChooseAddressConstant.emptyAddress)
        } returns true
    }

    private fun `When view resumed`() {
        tokoNowCategoryViewModel.onViewResumed()
    }

    @Test
    fun `onResume should not reload page when choose address is not updated`() {
        `Given choose address data`()
        `Given choose address is not updated`()
        `Given view model setup until view created`()

        `When view resumed`()

        `Then verify get first page API is only called once from view created`()
    }

    private fun `Given choose address is not updated`() {
        every {
            chooseAddressWrapper.isChooseAddressUpdated(dummyChooseAddressData)
        } returns false
    }

    private fun `Then verify get first page API is only called once from view created`() {
        verify(exactly = 1) {
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        }
    }

    @Test
    fun `onResume should not reload page when choose address is null`() {
        `Given choose address data`()
        `Given choose address is not updated`()
        `Given view model setup until view created`()
        `Given choose address data is null`()

        `When view resumed`()

        `Then verify get first page API is only called once from view created`()
    }

    private fun `Given choose address data is null`() {
        tokoNowCategoryViewModel.mockSuperClassField("chooseAddressData", null)
    }
}
