package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.topchat.chattemplate.domain.pojo.ChatAddTemplateResponse
import com.tokopedia.topchat.chattemplate.view.uimodel.CreateEditTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class CreateTemplateViewModelTest : BaseEditTemplateViewModelTest() {

    private val testString = "testString"

    @Test
    fun should_get_template_data_when_success_create_template_buyer() {
        val expectedResponse = ChatAddTemplateResponse().apply {
            this.chatAddTemplate.success = 1
        }
        val expectedResult = CreateEditTemplateResultModel(
            index = 0,
            text = testString
        )
        coEvery {
            createTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, "", listOf(), false)

        // Then
        Assert.assertEquals(
            expectedResult.text,
            viewModel.createEditTemplate.value?.text
        )
    }

    @Test
    fun should_get_template_data_when_success_create_template_seller() {
        val expectedResponse = ChatAddTemplateResponse().apply {
            this.chatAddTemplate.success = 1
        }
        val expectedResult = CreateEditTemplateResultModel(
            index = 0,
            text = testString
        )
        coEvery {
            createTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, "", listOf(), true)

        // Then
        Assert.assertEquals(
            expectedResult.text,
            viewModel.createEditTemplate.value?.text
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_create_template_buyer() {
        val expectedResponse = ChatAddTemplateResponse().apply {
            this.chatAddTemplate.success = 0
        }
        coEvery {
            createTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, "", listOf(), false)

        // Then
        Assert.assertEquals(
            EditTemplateViewModel.ERROR_CREATE_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_create_template_seller() {
        val expectedResponse = ChatAddTemplateResponse().apply {
            this.chatAddTemplate.success = 0
        }

        coEvery {
            createTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, "", listOf(), true)

        // Then
        Assert.assertEquals(
            EditTemplateViewModel.ERROR_CREATE_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_when_error_create_template_buyer() {
        coEvery {
            createTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.submitText(testString, "", listOf(), false)

        // Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }

    @Test
    fun should_get_error_when_error_create_template_seller() {
        coEvery {
            createTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.submitText(testString, "", listOf(), true)

        // Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }
}
