package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatUpdateTemplateResponse
import com.tokopedia.topchat.chattemplate.view.uimodel.CreateEditTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel.Companion.ERROR_EDIT_TEMPLATE
import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class EditTemplateViewModelTest : BaseEditTemplateViewModelTest() {

    private val testString = "testString"
    private val testExistingString = "test1"
    private val testList = listOf("test1", "test2", "test3")

    @Test
    fun should_get_template_data_when_success_edit_template_buyer() {
        val expectedResponse = ChatUpdateTemplateResponse().apply {
            this.chatUpdateTemplate.success = 1
        }
        val expectedResult = CreateEditTemplateResultModel(
            index = 0,
            text = testString
        )
        coEvery {
            updateTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, testExistingString, testList, false)

        // Then
        Assert.assertEquals(
            expectedResult.text,
            viewModel.createEditTemplate.value?.text
        )
    }

    @Test
    fun should_get_template_data_when_success_edit_template_seller() {
        val expectedResponse = ChatUpdateTemplateResponse().apply {
            this.chatUpdateTemplate.success = 1
        }
        val expectedResult = CreateEditTemplateResultModel(
            index = 0,
            text = testString
        )
        coEvery {
            updateTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, testExistingString, testList, true)

        // Then
        Assert.assertEquals(
            expectedResult.text,
            viewModel.createEditTemplate.value?.text
        )
    }

    @Test
    fun should_get_error_when_not_success_update_template() {
        val expectedResponse = ChatUpdateTemplateResponse().apply {
            this.chatUpdateTemplate.success = 0
        }
        val expectedException = MessageErrorException(ERROR_EDIT_TEMPLATE)
        coEvery {
            updateTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.submitText(testString, "test2", listOf("test1", "test2"), false)

        // Then
        Assert.assertEquals(
            expectedException.message,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_edit_template_buyer() {
        coEvery {
            updateTemplateUseCase(any())
        } throws Throwable(ERROR_EDIT_TEMPLATE)

        // When
        viewModel.submitText(testString, testExistingString, testList, false)

        // Then
        Assert.assertEquals(
            ERROR_EDIT_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_edit_template_seller() {
        coEvery {
            updateTemplateUseCase(any())
        } throws Throwable(ERROR_EDIT_TEMPLATE)

        // When
        viewModel.submitText(testString, testExistingString, testList, true)

        // Then
        Assert.assertEquals(
            ERROR_EDIT_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_when_error_edit_template_buyer() {
        coEvery {
            updateTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.submitText(testString, testExistingString, testList, true)

        // Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }

    @Test
    fun should_get_error_when_error_edit_template_seller() {
        coEvery {
            updateTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.submitText(testString, testExistingString, testList, true)

        // Then
        Assert.assertEquals(
            expectedThrowable,
            viewModel.errorAction.value
        )
    }
}
