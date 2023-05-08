package com.tokopedia.topchat.chattemplate.viewmodel.edit_template

import com.tokopedia.topchat.chattemplate.domain.pojo.ChatDeleteTemplateResponse
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel.Companion.ERROR_DELETE_TEMPLATE
import com.tokopedia.topchat.chattemplate.viewmodel.edit_template.base.BaseEditTemplateViewModelTest
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class DeleteTemplateViewModelTest : BaseEditTemplateViewModelTest() {

    private val testIndex = 1

    @Test
    fun should_get_template_data_when_success_delete_template_buyer() {
        // Given
        val expectedResponse = ChatDeleteTemplateResponse().apply {
            this.chatDeleteTemplate.success = 1
        }
        coEvery {
            deleteTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.deleteTemplate(testIndex, false)

        // Then
        Assert.assertEquals(
            testIndex,
            viewModel.deleteTemplate.value
        )
    }

    @Test
    fun should_get_template_data_when_success_delete_template_seller() {
        // Given
        val expectedResponse = ChatDeleteTemplateResponse().apply {
            this.chatDeleteTemplate.success = 1
        }
        coEvery {
            deleteTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.deleteTemplate(testIndex, true)

        // Then
        Assert.assertEquals(
            testIndex,
            viewModel.deleteTemplate.value
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_delete_template_buyer() {
        // Given
        val expectedResponse = ChatDeleteTemplateResponse().apply {
            this.chatDeleteTemplate.success = 0
        }
        coEvery {
            deleteTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.deleteTemplate(testIndex, false)

        // Then
        Assert.assertEquals(
            ERROR_DELETE_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_message_when_fail_to_delete_template_seller() {
        val expectedResponse = ChatDeleteTemplateResponse().apply {
            this.chatDeleteTemplate.success = 0
        }

        coEvery {
            deleteTemplateUseCase(any())
        } returns expectedResponse

        // When
        viewModel.deleteTemplate(testIndex, true)

        // Then
        Assert.assertEquals(
            EditTemplateViewModel.ERROR_DELETE_TEMPLATE,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_when_error_to_delete_template_buyer() {
        coEvery {
            deleteTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.deleteTemplate(testIndex, false)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            viewModel.errorAction.value?.message
        )
    }

    @Test
    fun should_get_error_when_error_to_delete_template_seller() {
        coEvery {
            deleteTemplateUseCase(any())
        } throws expectedThrowable

        // When
        viewModel.deleteTemplate(testIndex, true)

        // Then
        Assert.assertEquals(
            expectedThrowable.message,
            viewModel.errorAction.value?.message
        )
    }
}
