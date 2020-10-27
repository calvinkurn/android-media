package com.tokopedia.product.addedit.variant.presentation.viewmodel

import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductVariantViewModelTest: AddEditProductVariantViewModelTestFixture() {

    @Test
    fun `When updateVariantInputModel is success Expect update variantInputModel`() {
        viewModel.productInputModel.value = ProductInputModel()

        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest2.units[0].unitValues[2],
                variantDetailTest2.units[0].unitValues[3]
        )

        val variantPhotos = listOf(
                VariantPhoto(variantDetailTest2.units[0].unitValues[2].value, "/url/to/file.jpg"),
                VariantPhoto(variantDetailTest2.units[0].unitValues[3].value, "/url/to/file2.jpg")
        )

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        viewModel.setSelectedVariantDetails(variantDetailsTest)
        viewModel.updateVariantInputModel(variantPhotos)

        val selection = viewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection != null && selection.isNotEmpty())

        // validate removing variant
        viewModel.removeVariant()
        assert(viewModel.getSelectedVariantUnitValues(0).size == 0)
        assert(viewModel.getSelectedVariantUnitValues(1).size == 0)

    }

    @Test
    fun `When updateSizechart with url is success Expect update variantSizechart`() {
        viewModel.updateSizechart("/url/to/file.jpg")

        val variantSizechart = viewModel.variantSizechart.value
        assert(variantSizechart != null && variantSizechart.urlOriginal.isNotEmpty())
    }

    @Test
    fun `When updateSizechart with object is success Expect update variantSizechart`() {
        val obj = PictureVariantInputModel()
        obj.urlOriginal = "/url/to/file.jpg"
        viewModel.updateSizechart(obj)

        val variantSizechart = viewModel.variantSizechart.value
        assert(variantSizechart != null && variantSizechart.urlOriginal.isNotEmpty())
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

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        viewModel.updateSizechartFieldVisibility(variantDetailsTest)

        val isVariantSizechartVisible = viewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)
    }

    @Test
    fun `When updateSizechartVisibility size unit is not found Expect disable visibility`() {
        val selectedUnitValues = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValues)
        viewModel.updateSizechartFieldVisibility(listOf(variantDetailTest1))

        val isVariantSizechartVisible = viewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == false)
    }

    @Test
    fun `When updateSizechartVisibility Expect change visibility enable visibility`() {
        val selectedUnitValues = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        viewModel.setSelectedVariantDetails(variantDetailsTest)
        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValues)
        viewModel.updateSizechartFieldVisibility()

        val isVariantSizechartVisible = viewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)
    }

    @Test
    fun `When updateSizechartVisibility Expect change visibility disable visibility`() {
        val selectedUnitValues = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        viewModel.setSelectedVariantDetails(mutableListOf(variantDetailTest1))
        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValues)
        viewModel.updateSizechartFieldVisibility()

        val isVariantSizechartVisible = viewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == false)
    }

    @Test
    fun `When clickedVariantPhotoItemPosition Expect change clickedVariantPhotoItemPosition changes`() {
        // check clickedVariantPhotoItemPosition is null at first
        assert(viewModel.clickedVariantPhotoItemPosition == null)

        // check clickedVariantPhotoItemPosition is changed
        viewModel.clickedVariantPhotoItemPosition = 999
        assert(viewModel.clickedVariantPhotoItemPosition == 999)
    }

    @Test
    fun `When selectedUnitValuesLevel Expect change correct isInputValid validity`() {
        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )
        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest1.units[1].unitValues[2],
                variantDetailTest1.units[1].unitValues[3]
        )

        viewModel.isSingleVariantTypeIsSelected = false
        viewModel.updateSelectedVariantUnitValuesLevel1(selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesLevel2(selectedUnitValuesLevel2)

        assert(viewModel.isInputValid.getOrAwaitValue())
    }

    @Test
    fun `When updateIsOldVariantData Expect correct detection`() {
        val latestVariantDetails = variantDetailsTest
        val oldVariantDetails = variantDetailsTest.reversed()

        viewModel.updateIsOldVariantData(variantDetailsTest, latestVariantDetails)
        assert(!viewModel.isOldVariantData)
        viewModel.updateIsOldVariantData(variantDetailsTest, oldVariantDetails)
        assert(viewModel.isOldVariantData)
        viewModel.updateIsOldVariantData(variantDetailsTest, emptyList())
        assert(!viewModel.isOldVariantData)
    }

    @Test
    fun `When disableRemovingVariant Expect correct isRemovingVariant`() {
        viewModel.disableRemovingVariant()
        assert(!viewModel.isRemovingVariant.getOrAwaitValue())
    }

    @Test
    fun `When removeSelectedVariantDetails Expect viewModel's variantDetails is removed`() {
        val tobeRemovedVariantDetail = variantDetailsTest[0]
        viewModel.setSelectedVariantDetails(variantDetailsTest)
        viewModel.removeSelectedVariantDetails(tobeRemovedVariantDetail)

        val isRemoved = !viewModel.getSelectedVariantDetails().any {
            it.variantID == tobeRemovedVariantDetail.variantID
        }

        assert(isRemoved)
    }

    @Test
    fun `When removeSelectedVariantUnitValue Expect update unitValue`() {
        viewModel.productInputModel.value = ProductInputModel()

        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest2.units[0].unitValues[2],
                variantDetailTest2.units[0].unitValues[3]
        )

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        viewModel.setSelectedVariantDetails(variantDetailsTest)

        // validate removing, but variantDetailTest1 is not empty
        viewModel.removeSelectedVariantUnitValue(0, variantDetailTest1.units[0].unitValues[2])
        assert(!viewModel.isVariantUnitValuesEmpty(0))

        // validate removing, but variantDetailTest1 is empty
        viewModel.removeSelectedVariantUnitValue(0, variantDetailTest1.units[0].unitValues[3])
        assert(viewModel.isVariantUnitValuesEmpty(0))

        // validate removing all variant
        viewModel.clearProductVariant()
        val selection = viewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection?.isEmpty() == true)
    }

    @Test
    fun `When hide or show ProductVariantPhotos Expect correct visibility`() {
        viewModel.showProductVariantPhotos(variantDetailTest1)
        assert(viewModel.isVariantPhotosVisible.getOrAwaitValue())
        viewModel.hideProductVariantPhotos(variantDetailTest1)
        assert(!viewModel.isVariantPhotosVisible.getOrAwaitValue())
    }

    @Test
    fun `When updating variantDataMap Expect correct changes`() {
        // test non-custom unit value
        viewModel.updateVariantDataMap(0, variantDetailTest1)
        val isUpdateVariantDataMapSuccess =
                viewModel.getVariantData(0).variantID == variantDetailTest1.variantID
        assert(isUpdateVariantDataMapSuccess)

        //
        val unitResult = viewModel.getSelectedVariantUnit(0)
        assert(unitResult.variantUnitID == 62)

        // test custom unit value
        viewModel.addCustomVariantUnitValue(0, Unit(), UnitValue(value = "silver"))
        val isAddCustomVariantUnitValueSuccess =
                viewModel.getVariantData(0).units[0].unitValues.last().value == "silver"
        assert(isAddCustomVariantUnitValueSuccess)
    }

    @Test
    fun `When updateVariantValuesLayoutMap Expect correct changes`() {
        // updating
        viewModel.updateVariantValuesLayoutMap(0, 0)
        assert(viewModel.getVariantValuesLayoutPosition(0) == 0)
        assert(viewModel.getRenderedLayoutAdapterPosition() == 0)

        // removing
        viewModel.removeVariantValueLayoutMapEntry(0)
        assert(viewModel.isVariantUnitValuesLayoutEmpty())
    }

    @Test
    fun `When updateSelectedVariantUnitMap Expect correct changes`() {
        viewModel.updateSelectedVariantUnitMap(0, Unit(variantUnitID=62, unitName="Volume"))
        assert(viewModel.getSelectedVariantUnit(0).variantUnitID == 62)
    }

    @Test
    fun `When extract variantInputModel is success Expect correct extracted values`() {
        viewModel.productInputModel.value = ProductInputModel()

        val selectedUnitValuesLevel1 = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        val selectedUnitValuesLevel2 = mutableListOf(
                variantDetailTest2.units[0].unitValues[2],
                variantDetailTest2.units[0].unitValues[3]
        )

        val variantPhotos = listOf(
                VariantPhoto(variantDetailTest2.units[0].unitValues[2].value, "/url/to/file.jpg"),
                VariantPhoto(variantDetailTest2.units[0].unitValues[3].value, "/url/to/file2.jpg")
        )

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        viewModel.setSelectedVariantDetails(variantDetailsTest)
        viewModel.updateVariantInputModel(variantPhotos)

        // validate extracted photos count
        val photosCount = viewModel.getProductVariantPhotos(viewModel.productInputModel.value ?: ProductInputModel()).size
        val variantDetailsCount = viewModel.extractSelectedVariantDetails(viewModel.productInputModel.value ?: ProductInputModel()).size

        assert(photosCount == variantPhotos.size)
        assert(variantDetailsCount == variantDetailsTest.size)
    }
}