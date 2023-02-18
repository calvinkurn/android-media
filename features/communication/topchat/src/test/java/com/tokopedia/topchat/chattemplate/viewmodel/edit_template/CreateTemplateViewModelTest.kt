package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest

class CreateTemplateViewModelTest : BaseEditTemplateViewModelTest() {

    private val testString = "testString"
    private val testResultList = listOf("testString")

//    @Test
//    fun should_get_template_data_when_success_create_template_buyer() {
//        val expectedResponse = TemplateData(
//            isIsEnable = true,
//            isSuccess = true,
//            templates = testResultList
//        )
//        val expectedResult = CreateEditTemplateResultModel(
//            editTemplateResultModel = expectedResponse.mapToEditTemplateUiModel(),
//            index = 0,
//            text = testString
//        )
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.submitText(testString, "", listOf(), false)
//
//        // Then
//        Assert.assertEquals(
//            expectedResult.editTemplateResultModel.listTemplate,
//            viewModel.createEditTemplate.value?.editTemplateResultModel?.listTemplate
//        )
//    }
//
//    @Test
//    fun should_get_template_data_when_success_create_template_seller() {
//        val expectedResponse = TemplateData(
//            isIsEnable = true,
//            isSuccess = true,
//            templates = testResultList
//        )
//        val expectedResult = CreateEditTemplateResultModel(
//            editTemplateResultModel = expectedResponse.mapToEditTemplateUiModel(),
//            index = 0,
//            text = testString
//        )
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.submitText(testString, "", listOf(), true)
//
//        // Then
//        Assert.assertEquals(
//            expectedResult.editTemplateResultModel.listTemplate,
//            viewModel.createEditTemplate.value?.editTemplateResultModel?.listTemplate
//        )
//    }
//
//    @Test
//    fun should_get_error_message_when_fail_to_create_template_buyer() {
//        val expectedResponse = TemplateData(
//            isIsEnable = true,
//            isSuccess = false,
//            templates = arrayListOf()
//        )
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.submitText(testString, "", listOf(), false)
//
//        // Then
//        Assert.assertEquals(
//            EditTemplateViewModel.ERROR_CREATE_TEMPLATE,
//            viewModel.errorAction.value?.message
//        )
//    }
//
//    @Test
//    fun should_get_error_message_when_fail_to_create_template_seller() {
//        val expectedResponse = TemplateData(
//            isIsEnable = true,
//            isSuccess = false,
//            templates = arrayListOf()
//        )
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } returns expectedResponse
//
//        // When
//        viewModel.submitText(testString, "", listOf(), true)
//
//        // Then
//        Assert.assertEquals(
//            EditTemplateViewModel.ERROR_CREATE_TEMPLATE,
//            viewModel.errorAction.value?.message
//        )
//    }
//
//    @Test
//    fun should_get_error_when_error_create_template_buyer() {
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } throws expectedThrowable
//
//        // When
//        viewModel.submitText(testString, "", listOf(), false)
//
//        // Then
//        Assert.assertEquals(
//            expectedThrowable,
//            viewModel.errorAction.value
//        )
//    }
//
//    @Test
//    fun should_get_error_when_error_create_template_seller() {
//        coEvery {
//            createTemplateUseCase.createTemplate(any(), any())
//        } throws expectedThrowable
//
//        // When
//        viewModel.submitText(testString, "", listOf(), true)
//
//        // Then
//        Assert.assertEquals(
//            expectedThrowable,
//            viewModel.errorAction.value
//        )
//    }
}
