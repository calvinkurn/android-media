package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper.mapToEditTemplateUiModel
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.uimodel.CreateEditTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel.Companion.ERROR_EDIT_TEMPLATE
import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class EditTemplateViewModelTest: BaseEditTemplateViewModelTest() {

    private val testString = "testString"
    private val testExistingString = "test1"
    private val testList = listOf("test1", "test2", "test3")
    private val testResultList = listOf("testString", "test2", "test3")

    @Test
    fun should_get_template_data_when_success_edit_template_buyer() {
        val expectedResponse = TemplateData(
            isIsEnable = true,
            isSuccess = true,
            templates = testResultList
        )
        val expectedResult = CreateEditTemplateResultModel(
            editTemplateResultModel = expectedResponse.mapToEditTemplateUiModel(),
            index = 0,
            text = testString
        )
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } returns expectedResponse

        //When
        viewModel.submitText(testString, testExistingString, testList, false)

        //Then
        Assert.assertEquals(
            expectedResult.editTemplateResultModel.listTemplate,
            viewModel.createEditTemplate.value?.editTemplateResultModel?.listTemplate
        )
    }

    @Test
    fun should_get_template_data_when_success_edit_template_seller() {
        val expectedResponse = TemplateData(
            isIsEnable = true,
            isSuccess = true,
            templates = testResultList
        )
        val expectedResult = CreateEditTemplateResultModel(
            editTemplateResultModel = expectedResponse.mapToEditTemplateUiModel(),
            index = 0,
            text = testString
        )
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } returns expectedResponse

        //When
        viewModel.submitText(testString, testExistingString, testList, true)

        //Then
        Assert.assertEquals(
            expectedResult.editTemplateResultModel.listTemplate,
            viewModel.createEditTemplate.value?.editTemplateResultModel?.listTemplate
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_edit_template_buyer() {
        val expectedResponse = TemplateData(
            isIsEnable = true,
            isSuccess = false,
            templates = testList
        )
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } returns expectedResponse

        //When
        viewModel.submitText(testString, testExistingString, testList, false)

        //Then
        Assert.assertEquals(
            ERROR_EDIT_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_edit_template_seller() {
        val expectedResponse = TemplateData(
            isIsEnable = true,
            isSuccess = false,
            templates = testList
        )
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } returns expectedResponse

        //When
        viewModel.submitText(testString, testExistingString, testList, true)

        //Then
        Assert.assertEquals(
            ERROR_EDIT_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_when_error_edit_template_buyer() {
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } throws expectedThrowable

        //When
        viewModel.submitText(testString, testExistingString, testList, true)

        //Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }

    @Test
    fun should_get_error_when_error_edit_template_seller() {
        coEvery {
            editTemplateUseCase.editTemplate(any(), any(), any())
        } throws expectedThrowable

        //When
        viewModel.submitText(testString, testExistingString, testList, true)

        //Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }
}