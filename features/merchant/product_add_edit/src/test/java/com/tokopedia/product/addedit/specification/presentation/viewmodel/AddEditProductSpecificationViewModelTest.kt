package com.tokopedia.product.addedit.specification.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryResponse
import com.tokopedia.product.addedit.specification.domain.model.DrogonAnnotationCategoryV2
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductSpecificationViewModelTest: AddEditProductSpecificationViewModelTestFixture() {

    private val annotationCategoryData = listOf(
            AnnotationCategoryData(
                    variant = "Merek",
                    data = listOf(
                            Values("1", "Indomie", true, ""),
                            Values("2", "Seedap", false, ""))
            ),
            AnnotationCategoryData(
                    variant = "Rasa",
                    data = listOf(
                            Values("3", "Soto", false, ""),
                            Values("4", "Bawang", true, ""))
            )
    )

    @Test
    fun `getAnnotationCategory should return specification data when productId is provided`() = runBlocking {
        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } returns AnnotationCategoryResponse(
                DrogonAnnotationCategoryV2(annotationCategoryData)
        )

        viewModel.getSpecifications("")
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        val result = viewModel.annotationCategoryData.getOrAwaitValue()
        assertEquals(annotationCategoryData.size, result.size)
    }

    @Test
    fun `getAnnotationCategory should return error when connection problem`() = runBlocking {
        val expectedErrorMessage = "dummy message"

        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } throws MessageErrorException(expectedErrorMessage)

        viewModel.getSpecifications("")
        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        val result = viewModel.errorMessage.getOrAwaitValue()
        assertEquals(expectedErrorMessage, result)
    }

    @Test
    fun `updateProductInputModelSpecifications should return filtered list`() = runBlocking {
        val specificationInputModel = listOf(
                SpecificationInputModel("1", "Indomie"),
                SpecificationInputModel("", "Indomie"),
                SpecificationInputModel("2", ""),
                SpecificationInputModel("", ""),
                SpecificationInputModel("", "", isTextInput = true)
        )

        // test without setting productInputValue
        viewModel.updateProductInputModelSpecifications(specificationInputModel)

        // test with setting productInputValue before
        setProductInputModel(emptyList())
        viewModel.updateProductInputModelSpecifications(specificationInputModel)

        val spec = viewModel.productInputModel.getOrAwaitValue().detailInputModel.specifications.orEmpty()
        assertEquals(3, spec.size)
    }

    @Test
    fun `removeSpecification should clear specification`() = runBlocking {
        setProductInputModel(emptyList())
        viewModel.removeSpecification()

        val spec = viewModel.productInputModel.getOrAwaitValue().detailInputModel.specifications.orEmpty()
        assert(spec.isEmpty())
    }

    @Test
    fun `validateSpecificationInputModel should return validated data`() {
        viewModel.validateSpecificationInputModel(listOf())
        val emptyListResult = viewModel.validateSpecificationInputModelResult.getOrAwaitValue()

        viewModel.validateSpecificationInputModel(listOf(SpecificationInputModel(required = true)))
        val requiredResult = viewModel.validateSpecificationInputModelResult.getOrAwaitValue()

        viewModel.validateSpecificationInputModel(listOf(SpecificationInputModel(required = false)))
        val notRequiredResult = viewModel.validateSpecificationInputModelResult.getOrAwaitValue()

        assert(emptyListResult.first)
        assertFalse(requiredResult.first)
        assert(notRequiredResult.first)
    }

    private fun setProductInputModel(specificationInputModel: List<SpecificationInputModel>?) {
        val expectedId = 12222L
        viewModel.setProductInputModel(ProductInputModel(
                productId = expectedId,
                detailInputModel = DetailInputModel(
                        specifications = specificationInputModel
                )))
        val productInputModel = viewModel.productInputModel.getOrAwaitValue()
        assertEquals(expectedId, productInputModel.productId)
    }
}
