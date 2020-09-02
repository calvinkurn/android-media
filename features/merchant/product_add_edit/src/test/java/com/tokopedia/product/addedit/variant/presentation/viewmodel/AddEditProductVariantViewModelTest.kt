package com.tokopedia.product.addedit.variant.presentation.viewmodel

import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.*
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
        assert(viewModel.isSelectedVariantUnitValuesEmpty.value ?: false)
    }

    @Test
    fun `removing product variant should reset both variant page and input model but keep remote data flag`() {
        val expectedCategoryId = "10"
        spiedViewModel.productInputModel.value = ProductInputModel()
        spiedViewModel.productInputModel.value?.detailInputModel?.categoryId = expectedCategoryId
        val expectedState = true
        spiedViewModel.productInputModel.value?.variantInputModel?.isRemoteDataHasVariant = expectedState
        spiedViewModel.removeVariant()
        val isRemoteDataHasVariant = spiedViewModel.productInputModel.value?.variantInputModel?.isRemoteDataHasVariant
                ?: false
        assert(isRemoteDataHasVariant)
        assert(spiedViewModel.getSelectedVariantDetails().isEmpty())
        assert(spiedViewModel.isSelectedVariantUnitValuesEmpty.value ?: false)
        assert(spiedViewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION).isEmpty())
        assert(spiedViewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION).isEmpty())
        assert(spiedViewModel.getVariantValuesLayoutPosition(0) == VARIANT_VALUE_LEVEL_ONE_POSITION)
        assert(spiedViewModel.isVariantPhotosVisible.value == false)
        coVerify { spiedViewModel.getCategoryVariantCombination(expectedCategoryId) }
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
                "",
                expectedFilePathUrlOriginal,
                expectedPicID,
                expectedDescription,
                expectedFileName,
                expectedWidth,
                expectedHeight,
                expectedIsFromIG,
                expectedUploadId)
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.setSelectedVariantDetails(mutableListOf(VariantDetail(variantID = COLOUR_VARIANT_TYPE_ID)))
        viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, mutableListOf(UnitValue()))
        viewModel.updateVariantInputModel(listOf(variantPhoto))
        val productVariantInputModel = viewModel.productInputModel.value?.variantInputModel?.products?.firstOrNull()
                ?: ProductVariantInputModel()
        val pictureVariantInputModel = productVariantInputModel.pictures.firstOrNull()
                ?: PictureVariantInputModel()
        assert(pictureVariantInputModel.filePath == expectedFilePathUrlOriginal)
        assert(pictureVariantInputModel.picID == expectedPicID)
        assert(pictureVariantInputModel.description == expectedDescription)
        assert(pictureVariantInputModel.fileName == expectedFileName)
        assert(pictureVariantInputModel.width == expectedWidth)
        assert(pictureVariantInputModel.height == expectedHeight)
        assert(pictureVariantInputModel.isFromIG == expectedIsFromIG)
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
}