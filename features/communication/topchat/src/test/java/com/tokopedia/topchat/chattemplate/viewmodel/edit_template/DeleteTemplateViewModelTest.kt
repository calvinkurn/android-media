package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest

class DeleteTemplateViewModelTest : BaseEditTemplateViewModelTest() {

    private val testResultList = listOf("test1")
    private val testIndex = 1

//    @Test
//    fun should_get_template_data_when_success_delete_template_buyer() {
//        // Given
//        val expectedResponse = TemplateData(
//            isSuccess = true,
//            templates = testResultList,
//            isIsEnable = true
//        )
//        val expectedResult = Pair(expectedResponse.mapToEditTemplateUiModel(), testIndex)
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.deleteTemplate(testIndex, false)
//
//        // Then
//        Assert.assertEquals(
//            expectedResult.first.listTemplate,
//            viewModel.deleteTemplate.value?.first?.listTemplate
//        )
//        Assert.assertEquals(
//            expectedResult.second,
//            viewModel.deleteTemplate.value?.second
//        )
//    }
//
//    @Test
//    fun should_get_template_data_when_success_delete_template_seller() {
//        // Given
//        val expectedResponse = TemplateData(
//            isSuccess = true,
//            templates = testResultList,
//            isIsEnable = true
//        )
//        val expectedResult = Pair(expectedResponse.mapToEditTemplateUiModel(), testIndex)
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.deleteTemplate(testIndex, true)
//
//        // Then
//        Assert.assertEquals(
//            expectedResult.first.listTemplate,
//            viewModel.deleteTemplate.value?.first?.listTemplate
//        )
//        Assert.assertEquals(
//            expectedResult.second,
//            viewModel.deleteTemplate.value?.second
//        )
//    }
//
//    @Test
//    fun should_get_error_message_when_fail_to_delete_template_buyer() {
//        // Given
//        val expectedResponse = TemplateData(
//            isSuccess = false,
//            templates = testResultList,
//            isIsEnable = true
//        )
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.deleteTemplate(testIndex, false)
//
//        // Then
//        Assert.assertEquals(
//            ERROR_DELETE_TEMPLATE,
//            viewModel.errorAction.value?.message
//        )
//    }
//
//    @Test
//    fun should_get_error_message_when_fail_to_delete_template_seller() {
//        val expectedResponse = TemplateData(
//            isSuccess = false,
//            templates = testResultList,
//            isIsEnable = true
//        )
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.deleteTemplate(testIndex, true)
//
//        // Then
//        Assert.assertEquals(
//            ERROR_DELETE_TEMPLATE,
//            viewModel.errorAction.value?.message
//        )
//    }
//
//    @Test
//    fun should_get_error_when_error_to_delete_template_buyer() {
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } throws expectedThrowable
//
//        // When
//        viewModel.deleteTemplate(testIndex, false)
//
//        // Then
//        Assert.assertEquals(
//            expectedThrowable.message,
//            viewModel.errorAction.value?.message
//        )
//    }
//
//    @Test
//    fun should_get_error_when_error_to_delete_template_seller() {
//        coEvery {
//            deleteTemplateUseCase.deleteTemplate(any(), any())
//        } throws expectedThrowable
//
//        // When
//        viewModel.deleteTemplate(testIndex, true)
//
//        // Then
//        Assert.assertEquals(
//            expectedThrowable.message,
//            viewModel.errorAction.value?.message
//        )
//    }
}
