package com.tokopedia.topchat.chattemplate.viewmodel.chat_template

import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import com.tokopedia.topchat.chattemplate.viewmodel.chat_template.base.BaseChatTemplateViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class ArrangeTemplateViewModelTest : BaseChatTemplateViewModelTest()  {

    private var arrangedList = listOf(1, 2, 3, 4, 5)

    @Test
    fun should_give_chat_template_response_when_success_arrange_template_buyer() {
        //Given
        val isSeller = false
        val isEnable = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.setArrange(isSeller, isEnable, arrangedList, 0, 1)

        //Then
        Assert.assertEquals(
            expectedResponse.data.isSuccess,
            (viewModel.arrangeTemplate.value?.templateResult as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_chat_template_response_when_success_arrange_template_seller() {
        //Given
        val isSeller = true
        val isEnable = true
        val expectedResponse = TemplateDataWrapper(
            data = TemplateData(
                isSuccess = true,
                isIsEnable = isEnable
            )
        )
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } returns expectedResponse.data

        //When
        viewModel.setArrange(isSeller, isEnable, arrangedList, 0, 1)

        //Then
        Assert.assertEquals(
            expectedResponse.data.isSuccess,
            (viewModel.arrangeTemplate.value?.templateResult as Success).data.isSuccess
        )
    }

    @Test
    fun should_give_error_when_fail_arrange_template_buyer() {
        //Given
        val isSeller = false
        val isEnable = true
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } throws expectedThrowable

        //When
        viewModel.setArrange(isSeller, isEnable, arrangedList, 0, 1)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.arrangeTemplate.value?.templateResult as Fail).throwable.message
        )
    }

    @Test
    fun should_give_error_when_fail_arrange_template_seller() {
        //Given
        val isSeller = true
        val isEnable = true
        coEvery {
            setAvailabilityTemplateUseCase.setAvailability(any())
        } throws expectedThrowable

        //When
        viewModel.setArrange(isSeller, isEnable, arrangedList, 0, 1)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.arrangeTemplate.value?.templateResult as Fail).throwable.message
        )
    }
}