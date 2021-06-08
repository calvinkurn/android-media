package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.callPrivateFunc
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.setPrivateProperty
import com.tokopedia.product.addedit.variant.data.model.GetVariantCategoryCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

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

        val variantPhotos = listOf(
                VariantPhoto(variantDetailTest2.units[0].unitValues[2].value, "/url/to/file.jpg"),
                VariantPhoto(variantDetailTest2.units[0].unitValues[3].value, "/url/to/file2.jpg")
        )

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        spiedViewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        spiedViewModel.setSelectedVariantDetails(variantDetailsTest)
        spiedViewModel.updateVariantInputModel(variantPhotos)

        val selection = spiedViewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection != null && selection.isNotEmpty())

        // validate removing variant
        viewModel.removeVariant()
        assert(viewModel.getSelectedVariantUnitValues(0).size == 0)
        assert(viewModel.getSelectedVariantUnitValues(1).size == 0)

    }

    @Test
    fun `When updateVariantInputModel with old data is success Expect update variantInputModel`() {
        spiedViewModel.productInputModel.value = ProductInputModel()

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

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        spiedViewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        spiedViewModel.setSelectedVariantDetails(variantDetailsTest)
        spiedViewModel.isOldVariantData = true
        spiedViewModel.updateVariantInputModel(variantPhotos)

        val selection = spiedViewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection != null && selection.isNotEmpty())

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
        spiedViewModel.updateSizechart(obj)

        val variantSizechart = spiedViewModel.variantSizechart.value
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

        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        spiedViewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        spiedViewModel.updateSizechartFieldVisibility(variantDetailsTest)

        val isVariantSizechartVisible = spiedViewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)

        //test if reversed
        spiedViewModel.updateSizechartFieldVisibility(variantDetailsTest.reversed())
        assert(spiedViewModel.isVariantSizechartVisible.value == true)
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
    fun `When updateSizechartVisibility Expect change visibility enable visibility`() {
        val selectedUnitValues = mutableListOf(
                variantDetailTest1.units[0].unitValues[2],
                variantDetailTest1.units[0].unitValues[3]
        )

        spiedViewModel.setSelectedVariantDetails(variantDetailsTest)
        spiedViewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValues)
        spiedViewModel.updateSizechartFieldVisibility()

        val isVariantSizechartVisible = spiedViewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)

        // change sizechart based on selectedVariantDetails
        spiedViewModel.updateSizechartFieldVisibility(mutableListOf(variantDetailTest2))
        assert(spiedViewModel.isVariantSizechartVisible.value == true)
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

        // validate removing unidentified layout pos
        viewModel.removeSelectedVariantUnitValue(1000, variantDetailTest1.units[0].unitValues[2])
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
        assert(viewModel.getRenderedLayoutAdapterPosition() == 0)
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
    fun `getCategoryVariantCombination function should execute getCategoryVariantCombinationUseCase`() = runBlocking {

        coEvery {
            getVariantCategoryCombinationUseCase.executeOnBackground()
        } returns GetVariantCategoryCombinationResponse()

        viewModel.getVariantCategoryCombination(1, listOf())
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getVariantCategoryCombinationUseCase.executeOnBackground()
        }

        assert(viewModel.getVariantCategoryCombinationResult.getOrAwaitValue() is Success)
    }

    @Test
    fun `getCategoryVariantCombination function in edit mode should execute getCategoryVariantCombinationUseCase`() = runBlocking {
        val selections = listOf(SelectionInputModel(
                variantId = "111"
        ))
        viewModel.productInputModel.value = ProductInputModel(productId = 10000L)
        viewModel.productInputModel.getOrAwaitValue()
        viewModel.isEditMode.getOrAwaitValue()

        coEvery {
            getVariantCategoryCombinationUseCase.executeOnBackground()
        } returns GetVariantCategoryCombinationResponse()

        viewModel.getVariantCategoryCombination(1, selections)
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getVariantCategoryCombinationUseCase.executeOnBackground()
        }

        assert(viewModel.getVariantCategoryCombinationResult.getOrAwaitValue() is Success)
    }

    @Test
    fun `getCategoryVariantCombination function should not execute getCategoryVariantCombinationUseCase if categoryId 0`() = runBlocking {
        viewModel.getVariantCategoryCombination(0, listOf())
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify (exactly = 0) {
            getVariantCategoryCombinationUseCase.executeOnBackground()
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
        viewModel.addCustomVariantUnitValue(10, selectedVariantUnit, customVuv)
        viewModel.addCustomVariantUnitValue(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnit, customVuv)

        val vu = variantDataMap[VARIANT_VALUE_LEVEL_ONE_POSITION]?.units?.find {
            it.variantUnitID == variantUnitId
        }
        assert(vu?.unitValues?.contains(customVuv) ?: false)
    }

    @Test
    fun `non-custom should be added to respective variant unit inside selected variant data`() {
        val customVuv = UnitValue()
        val variantUnitId = 1
        val dummyVariantUnit = Unit(variantUnitID = variantUnitId)
        val selectedVariantUnit = Unit(variantUnitID = variantUnitId, unitName = "Hijau")
        val variantData = VariantDetail(units = listOf(dummyVariantUnit))
        variantDataMap[VARIANT_VALUE_LEVEL_ONE_POSITION] = variantData
        viewModel.addCustomVariantUnitValue(10, selectedVariantUnit, customVuv)
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
    fun `view model should return default value when position is not defined`() {
        val adapterPosition = 999
        viewModel.updateVariantValuesLayoutMap(0, 0)
        val layoutPosition = viewModel.getVariantValuesLayoutPosition(adapterPosition)
        assert(layoutPosition == VARIANT_VALUE_LEVEL_ONE_POSITION)
    }

    @Test
    fun `view model should return expected variant data from the map`() {
        val layoutPosition = VARIANT_VALUE_LEVEL_TWO_POSITION
        val expectedVariantData = VariantDetail()
        viewModel.updateVariantDataMap(layoutPosition, expectedVariantData)
        assert(viewModel.getVariantData(layoutPosition) == expectedVariantData)
        assert(viewModel.getVariantData(9999).variantID == 0)
    }

    @Test
    fun `product variant photos should be visible when the variant type is color`() {
        val selectedVariantDetail = VariantDetail(variantID = COLOUR_VARIANT_TYPE_ID)
        viewModel.showProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == true)
    }

    @Test
    fun `product variant photos should be invisible when the variant type is color`() {
        val selectedVariantDetail = VariantDetail(variantID = COLOUR_VARIANT_TYPE_ID)
        viewModel.hideProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == false)
    }

    @Test
    fun `product variant photos should ignore set value when the variant type is not color`() {
        val selectedVariantDetail = VariantDetail(variantID = 9999999)
        viewModel.showProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == null)

        viewModel.hideProductVariantPhotos(selectedVariantDetail)
        assert(viewModel.isVariantPhotosVisible.value == null)
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

    @Test
    fun `view model should be able to remove variant value layout map entry`() {
        val adapterPosition = 2
        val layoutPosition = VARIANT_VALUE_LEVEL_TWO_POSITION
        viewModel.updateVariantValuesLayoutMap(adapterPosition, layoutPosition)
        viewModel.removeVariantValueLayoutMapEntry(adapterPosition)
        assert(viewModel.isVariantUnitValuesLayoutEmpty())
    }

    @Test
    fun `view model remove vuv function should remove selected variant unit value from map and live data`() {
        val variantUnitValue = UnitValue()
        val variantUnitValues = mutableListOf(variantUnitValue)
        viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, variantUnitValues)
        viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, variantUnitValues)
        viewModel.updateSelectedVariantUnitValuesLevel1(variantUnitValues)
        viewModel.updateSelectedVariantUnitValuesLevel2(variantUnitValues)
        viewModel.removeSelectedVariantUnitValue(VARIANT_VALUE_LEVEL_ONE_POSITION, variantUnitValue)
        viewModel.removeSelectedVariantUnitValue(VARIANT_VALUE_LEVEL_TWO_POSITION, variantUnitValue)
        assert(viewModel.isVariantUnitValuesEmpty(VARIANT_VALUE_LEVEL_ONE_POSITION))
        assert(viewModel.isVariantUnitValuesEmpty(VARIANT_VALUE_LEVEL_TWO_POSITION))
    }

    @Test
    fun `removing product variant should reset both variant page and input model but keep remote data flag`() {
        val expectedCategoryId = 10
        spiedViewModel.productInputModel.value = ProductInputModel()
        spiedViewModel.productInputModel.value?.detailInputModel?.categoryId = expectedCategoryId.toString()
        val expectedState = true
        spiedViewModel.productInputModel.value?.variantInputModel?.isRemoteDataHasVariant = expectedState
        spiedViewModel.removeVariant()
        val isRemoteDataHasVariant = spiedViewModel.productInputModel.value?.variantInputModel?.isRemoteDataHasVariant
                ?: false
        assert(isRemoteDataHasVariant)
        assert(spiedViewModel.getSelectedVariantDetails().isEmpty())
        assert(spiedViewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION).isEmpty())
        assert(spiedViewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION).isEmpty())
        assert(spiedViewModel.getVariantValuesLayoutPosition(0) == VARIANT_VALUE_LEVEL_ONE_POSITION)
        assert(spiedViewModel.isVariantPhotosVisible.value == false)
        coVerify { spiedViewModel.getVariantCategoryCombination(expectedCategoryId,listOf()) }
    }

    @Test
    fun `view model should be able to map variant photo into variant picture input model`() {
        val expectedFilePathUrlOriginal = "expected value"
        val expectedPicID = "expected value"
        val expectedDescription = "expected value"
        val expectedFileName = "expected value"
        val expectedWidth = 1L
        val expectedHeight = 1L
        val expectedIsFromIG = "expected value"
        val expectedUploadId = "expected value"
        val variantPhoto = VariantPhoto(
                variantUnitValueName = "",
                imageUrlOrPath = expectedFilePathUrlOriginal,
                picID = expectedPicID,
                description = expectedDescription,
                fileName = expectedFileName,
                filePath = expectedWidth.toString(),
                width = expectedHeight,
                height = expectedHeight,
                isFromIG = expectedIsFromIG,
                uploadId = expectedUploadId)
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.setSelectedVariantDetails(mutableListOf(VariantDetail(variantID = COLOUR_VARIANT_TYPE_ID)))
        viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, mutableListOf(UnitValue()))
        viewModel.updateVariantInputModel(listOf(variantPhoto))
        val productVariantInputModel = viewModel.productInputModel.value?.variantInputModel?.products?.firstOrNull()
                ?: ProductVariantInputModel()
        val pictureVariantInputModel = productVariantInputModel.pictures.firstOrNull()
                ?: PictureVariantInputModel()
        assert(pictureVariantInputModel.urlOriginal == expectedFilePathUrlOriginal)
        assert(pictureVariantInputModel.picID == expectedPicID)
        assert(pictureVariantInputModel.description == expectedDescription)
        assert(pictureVariantInputModel.fileName == expectedFileName)
        assert(pictureVariantInputModel.width == expectedWidth)
        assert(pictureVariantInputModel.height == expectedHeight)
        assert(pictureVariantInputModel.isFromIG == expectedIsFromIG.toString())
        assert(pictureVariantInputModel.uploadId == expectedUploadId)
    }

    @Test
    fun `view model should be able to convert selection input model into variant detail`() {
        val expectedVariantId = 10
        val expectedIdentifier = "expected identifier"
        val expectedName = "expected name"
        val expectedUnit = Unit(variantUnitID = 10, unitName = "expected unit name")
        val expectedUnitValueId = 10
        val expectedUnitValue = "expected unit value 1"
        expectedUnit.unitValues = mutableListOf(UnitValue(variantUnitValueID = expectedUnitValueId, value = expectedUnitValue))
        val expectedUnits = listOf(expectedUnit)
        val productInputModel = ProductInputModel()
        val optionInputModel1 = OptionInputModel(unitValueID = expectedUnitValueId.toString(), value = expectedUnitValue)
        val selectionInputModel1 = SelectionInputModel(
                variantId = expectedVariantId.toString(),
                variantName = expectedName,
                unitID = expectedUnit.variantUnitID.toString(),
                unitName = expectedUnit.unitName,
                identifier = expectedIdentifier,
                options = listOf(optionInputModel1))
        productInputModel.variantInputModel.selections = listOf(selectionInputModel1)
        val selectedVariantDetails = viewModel.extractSelectedVariantDetails(productInputModel)
        val selectedVariantDetail = selectedVariantDetails.first()
        assert(selectedVariantDetail.variantID == expectedVariantId)
        assert(selectedVariantDetail.identifier == expectedIdentifier)
        assert(selectedVariantDetail.name == expectedName)
        assert(selectedVariantDetail.units == expectedUnits)
    }

    @Test
    fun `view model should be able to remove selected variant detail from collection`() {
        val selectedVariantDetail1 = VariantDetail(variantID = 10)
        val selectedVariantDetail2 = VariantDetail(variantID = 20)
        viewModel.setSelectedVariantDetails(mutableListOf(selectedVariantDetail1, selectedVariantDetail2))
        viewModel.removeSelectedVariantDetails(selectedVariantDetail1)
        assert(viewModel.getSelectedVariantDetails().size == 1)
    }

    @Test
    fun `view model should be able to convert picture variant input model into variant photo`() {
        val expectedPhotoUrl = "expectedPhotoUrl"
        val expectedPhotoPicID = "expectedPhotoPicID"
        val expectedPhotoDescription = "expectedPhotoDescription"
        val expectedPhotoFileName = "expectedPhotoFileName"
        val expectedPhotoWidth = 10L
        val expectedPhotoHeight = 10L
        val expectedPhotoIsFromIG = "true"
        val expectedPhotoUploadId = "expectedPhotoUploadId"
        val pictureVariantInputModel = PictureVariantInputModel(
                urlOriginal = expectedPhotoUrl,
                picID = expectedPhotoPicID,
                description = expectedPhotoDescription,
                fileName = expectedPhotoFileName,
                width = expectedPhotoWidth,
                height = expectedPhotoHeight,
                isFromIG = expectedPhotoIsFromIG,
                uploadId = expectedPhotoUploadId)
        val productVariantInputModel = ProductVariantInputModel(combination = listOf(0), pictures = listOf(pictureVariantInputModel))
        val selectionInputModel = SelectionInputModel(variantId = COLOUR_VARIANT_TYPE_ID.toString())
        val optionInputModel = OptionInputModel(value = "Red")
        selectionInputModel.options = listOf(optionInputModel)
        val productInputModel = ProductInputModel(variantInputModel = VariantInputModel(products = listOf(productVariantInputModel), selections = listOf(selectionInputModel)))
        val variantPhotos = viewModel.getProductVariantPhotos(productInputModel)
        val actualVariantPhoto = variantPhotos.first()
        assert(actualVariantPhoto.fileName == expectedPhotoFileName)
    }

    @Test
    fun `view model should be able to convert picture variant input model into variant photo if picture is empty`() {
        val productVariantInputModel = ProductVariantInputModel(combination = listOf(0), pictures = listOf())
        val selectionInputModel = SelectionInputModel(variantId = COLOUR_VARIANT_TYPE_ID.toString())
        val optionInputModel = OptionInputModel(value = "Red")
        selectionInputModel.options = listOf(optionInputModel)
        val productInputModel = ProductInputModel(variantInputModel = VariantInputModel(products = listOf(productVariantInputModel), selections = listOf(selectionInputModel)))
        val variantPhotos = viewModel.getProductVariantPhotos(productInputModel)
        assert(variantPhotos.first().imageUrlOrPath.isEmpty())
    }

    @Test
    fun `view model should unable to convert picture variant input model into variant photo if variant is not color type`() {
        val productVariantInputModel = ProductVariantInputModel(combination = listOf(0), pictures = listOf())
        val selectionInputModel = SelectionInputModel(variantId = "999")
        val productInputModel = ProductInputModel(variantInputModel = VariantInputModel(products = listOf(productVariantInputModel), selections = listOf(selectionInputModel)))
        val variantPhotos = viewModel.getProductVariantPhotos(productInputModel)
        assert(variantPhotos.isEmpty())
    }

    @Test
    fun `mapSizechart should return PictureVariantInputModel`() {
        viewModel.setPrivateProperty("mIsVariantSizechartVisible", MutableLiveData(true))
        val picture = viewModel.callPrivateFunc("mapSizechart", PictureVariantInputModel(picID = "1")) as PictureVariantInputModel
        assertEquals("1", picture.picID)

        val pictureEmpty = viewModel.callPrivateFunc("mapSizechart", null) as PictureVariantInputModel
        assertEquals("", pictureEmpty.picID)
    }

    @Test
    fun `mapProductVariant should return ProductVariantInputModel`() {
        viewModel.productInputModel = MutableLiveData(ProductInputModel(
                variantInputModel = VariantInputModel(
                        products = listOf(
                                ProductVariantInputModel(combination = listOf(1))
                        )
                )))
        viewModel.callPrivateFunc("mapProductVariant",
                emptyList<PictureVariantInputModel>(),
                listOf(1)) as ProductVariantInputModel
        val product = viewModel.callPrivateFunc("mapProductVariant",
                listOf(PictureVariantInputModel(picID = "1")),
                listOf(1)) as ProductVariantInputModel
        assertEquals(listOf(1), product.combination)
        assertEquals(0L, viewModel.productInputModel.value?.productId)
    }

    @Test
    fun `isVariantUnitValuesEmpty should return valid output`() {
        val isEmpty = viewModel.isVariantUnitValuesEmpty(99)
        assertEquals(true, isEmpty)
    }

    @Test
    fun `mapVariantPhoto should return emptylist if input is null or empty`() {
        val result1 = viewModel.callPrivateFunc("mapVariantPhoto", null) as List<*>
        assert(result1.isEmpty())

        val result2 = viewModel.callPrivateFunc("mapVariantPhoto", VariantPhoto("", "")) as List<*>
        assert(result2.isEmpty())
    }
}