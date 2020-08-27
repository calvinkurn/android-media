package com.tokopedia.product.addedit.variant.presentation.viewmodel

import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductVariantViewModelTest : AddEditProductVariantViewModelTestFixture() {

    @Test
    fun `When updateVariantInputModel is success Expect update variantInputModel`() {
        spiedViewModel.productInputModel.value = ProductInputModel()

        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest2.units[0].unitValues[2],
                variantDetailTest2.units[0].unitValues[3]
        )

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        spiedViewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        spiedViewModel.setSelectedVariantDetails(variantDetailsTest)
        spiedViewModel.updateVariantInputModel(listOf())

        val selection = spiedViewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection != null && selection.isNotEmpty())
    }

    @Test
    fun `When updateSizechart with url is success Expect update variantSizechart`() {
        spiedViewModel.updateSizechart("/url/to/file.jpg")

        val variantSizechart = spiedViewModel.variantSizechart.value
        assert(variantSizechart != null && variantSizechart.filePath.isNotEmpty())
    }

    @Test
    fun `When updateSizechart with object is success Expect update variantSizechart`() {
        val obj = PictureVariantInputModel()
        obj.filePath = "/url/to/file.jpg"
        spiedViewModel.updateSizechart(obj)

        val variantSizechart = spiedViewModel.variantSizechart.value
        assert(variantSizechart != null && variantSizechart.filePath.isNotEmpty())
    }

    @Test
    fun `When updateSizechartVisibility size unit is found Expect enable visibility`() {
        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest2.units[0].unitValues[2],
                variantDetailTest2.units[0].unitValues[3]
        )

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        spiedViewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        spiedViewModel.updateSizechartFieldVisibility(variantDetailsTest)

        val isVariantSizechartVisible = spiedViewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)
    }

    @Test
    fun `When updateSizechartVisibility size unit is not found Expect disable visibility`() {
        val selectedUnitValues = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValues)
        spiedViewModel.updateSizechartFieldVisibility(listOf(variantDetailTest1))

        val isVariantSizechartVisible = spiedViewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == false)
    }

    @Test
    fun `When updateSizechartVisibility Expect change visibility`() {
        spiedViewModel.updateSizechartFieldVisibility(variantDetailTest2, true)

        val isVariantSizechartVisible = spiedViewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)
    }

    @Test
    fun `product variant input should be valid when selected variant type is single and selected variant unit values are not empty`() {
        // variant type selection is marked as single
        viewModel.isSingleVariantTypeIsSelected = true
        // prepare dummy variant unit values
        val dummyUnitValue = UnitValue()
        val dummyUnitValues = mutableListOf(dummyUnitValue)
        // fill selected vuv level 1 with dummy data
        viewModel.updateSelectedVariantUnitValuesLevel1(dummyUnitValues)
        // the product variant input should be valid
        assert(viewModel.isInputValid.value == true)
        // empty the level 1 selected variant unit values to prepare another test
        viewModel.updateSelectedVariantUnitValuesLevel1(mutableListOf())
        // fill selected vuv level 2 with dummy data
        viewModel.updateSelectedVariantUnitValuesLevel2(dummyUnitValues)
        // the product variant input should be valid
        assert(viewModel.isInputValid.value == true)
    }

    @Test
    fun `product variant input should be valid when selected variant unit values are not empty`() {
        // prepare dummy variant unit values
        val dummyUnitValue = UnitValue()
        val dummyUnitValues = mutableListOf(dummyUnitValue)
        // fill selected vuv level 1 & 2 with dummy data
        viewModel.updateSelectedVariantUnitValuesLevel1(dummyUnitValues)
        viewModel.updateSelectedVariantUnitValuesLevel2(dummyUnitValues)
        // the product variant input should be valid
        assert(viewModel.isInputValid.value == true)
    }

    @Test
    fun `getCategoryVariantCombination function should execute getCategoryVariantCombinationUseCase`() {
        viewModel.getCategoryVariantCombination("0")
        coVerify {
            getCategoryVariantCombinationUseCase.executeOnBackground()
        }
    }

    @Test
    fun `custom should be added to respective variant unit inside selected variant data`() {
        val customVuv = UnitValue()
        val variantUnitId = 1
        val dummyVariantUnit = Unit(variantUnitID = variantUnitId)
        val selectedVariantUnit = Unit(variantUnitID = variantUnitId)
        val variantData = VariantDetail(units = listOf(dummyVariantUnit))
        variantDataMap[VARIANT_VALUE_LEVEL_ONE_POSITION] = variantData
        viewModel.addCustomVariantUnitValue(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnit, customVuv)

        val vu = variantDataMap[VARIANT_VALUE_LEVEL_ONE_POSITION]?.units?.find {
            it.variantUnitID == variantUnitId
        }
        assert(vu?.unitValues?.contains(customVuv) ?: false)
    }

    @Test
    fun `view model should return variant value level one position when the layout map is empty`() {
        val adapterPosition = 0
        val layoutPosition = viewModel.getVariantValuesLayoutPosition(adapterPosition)
        assert(layoutPosition == VARIANT_VALUE_LEVEL_ONE_POSITION)
    }

    @Test
    fun `view model should return expected layout position from the layout map`() {
        val adapterPosition = 0
        val expectedLayoutPosition = VARIANT_VALUE_LEVEL_TWO_POSITION
        viewModel.updateVariantValuesLayoutMap(adapterPosition, expectedLayoutPosition)
        assert(viewModel.getVariantValuesLayoutPosition(adapterPosition) == expectedLayoutPosition)
    }

    @Test
    fun `view model should return expected variant data from the map`() {
        val layoutPosition = VARIANT_VALUE_LEVEL_TWO_POSITION
        val expectedVariantData = VariantDetail()
        viewModel.updateVariantDataMap(layoutPosition, expectedVariantData)
        assert(viewModel.getVariantData(layoutPosition) == expectedVariantData)
    }

    @Test
    fun `product variant photos should be visible when the variant type is color`() {
        val selectedVariantDetail = VariantDetail(variantID = AddEditProductVariantConstants.COLOUR_VARIANT_TYPE_ID)
        viewModel.showProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == true)
    }

    @Test
    fun `product variant photos should be invisible when the variant type is color`() {
        val selectedVariantDetail = VariantDetail(variantID = AddEditProductVariantConstants.COLOUR_VARIANT_TYPE_ID)
        viewModel.hideProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == false)
    }

    @Test
    fun `clear product variant should the product list inside variant input model`() {
        viewModel.productInputModel.value?.variantInputModel?.products = listOf(ProductVariantInputModel())
        viewModel.clearProductVariant()
        viewModel.productInputModel.value?.variantInputModel?.products?.run {
            assert(this.isEmpty())
        }
    }

    @Test
    fun `view model should return expected variant unit from the map`() {
        val expectedVariantUnit = Unit(variantUnitID = 32, unitName = "expectedVariantUnit")
        viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_TWO_POSITION, expectedVariantUnit)
        val actualVariantUnit = viewModel.getSelectedVariantUnit(VARIANT_VALUE_LEVEL_TWO_POSITION)
        assert(actualVariantUnit == expectedVariantUnit)
    }

    @Test
    fun `view model should return expected variant unit values from the map`() {
        val expectedVariantUnitValues = mutableListOf(UnitValue(variantUnitValueID = 10))
        viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, expectedVariantUnitValues)
        val actualVariantUnitValues = viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION)
        assert(actualVariantUnitValues == expectedVariantUnitValues)
    }
}