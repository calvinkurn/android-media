package com.tokopedia.product.addedit.variant.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductVariantViewModelTest: AddEditProductVariantViewModelTestFixture() {

    @Test
    fun `When get variant combination is success Expect Success result`() {
        runBlocking {
            val successResult = GetCategoryVariantCombinationResponse()
            coEvery {
                getCategoryVariantCombinationUseCase.executeOnBackground()
            } returns successResult

            viewModel.getCategoryVariantCombination("2821")
            coVerify { getCategoryVariantCombinationUseCase.executeOnBackground() }

            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            val result = viewModel.getCategoryVariantCombinationResult.getOrAwaitValue()
            assert(result == Success(successResult))
        }
    }

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

        viewModel.updateSelectedVariantUnitValuesMap(0, selectedUnitValuesLevel1)
        viewModel.updateSelectedVariantUnitValuesMap(1, selectedUnitValuesLevel2)
        viewModel.updateVariantInputModel(variantDetailsTest, listOf())

        val selection = viewModel.productInputModel.value?.variantInputModel?.selections
        assert(selection != null && selection.isNotEmpty())
    }

    @Test
    fun `When updateSizechart with url is success Expect update variantSizechart`() {
        viewModel.updateSizechart("/url/to/file.jpg")

        val variantSizechart = viewModel.variantSizechart.value
        assert(variantSizechart != null && variantSizechart.filePath.isNotEmpty())
    }

    @Test
    fun `When updateSizechart with object is success Expect update variantSizechart`() {
        val obj = PictureVariantInputModel()
        obj.filePath = "/url/to/file.jpg"
        viewModel.updateSizechart(obj)

        val variantSizechart = viewModel.variantSizechart.value
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
    fun `When updateSizechartVisibility Expect change visibility`() {
        viewModel.updateSizechartFieldVisibility(variantDetailTest2, true)

        val isVariantSizechartVisible = viewModel.isVariantSizechartVisible.value
        assert(isVariantSizechartVisible == true)
    }
}