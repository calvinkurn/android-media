package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.usecase.RequestParams
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
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
        `Given choose address data`(ChooseAddressConstant.emptyAddress)
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
        categoryViewModel.onLocalizingAddressSelected()
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
        `Then assert mini cart API is updated`()
    }

    private fun `Given choose address is updated`() {
        every {
            chooseAddressWrapper.isChooseAddressUpdated(ChooseAddressConstant.emptyAddress)
        } returns true
    }

    private fun `When view resumed`() {
        categoryViewModel.onViewResumed()
    }

    private fun `Then assert mini cart API is updated`() {
        verify {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `onResume should not reload page when choose address is not updated`() {
        `Given choose address data`()
        `Given choose address is not updated`()
        `Given view model setup until view created`()

        `When view resumed`()

        `Then verify get first page API is only called once from view created`()
        `Then assert mini cart API is updated`()
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
}