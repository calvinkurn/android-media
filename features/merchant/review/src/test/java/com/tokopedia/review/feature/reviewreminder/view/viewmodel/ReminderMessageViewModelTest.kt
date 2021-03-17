package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounterResponseWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderListResponseWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplateResponseWrapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class ReminderMessageViewModelTest : ReminderMessageViewModelTestFixture() {
    @Test
    fun `when fetch reminder counter success should update estimation value`() {
        val responseWrapper = ProductrevGetReminderCounterResponseWrapper()
        coEvery { productrevGetReminderCounterUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchReminderCounter()
        viewModel.getEstimation().verifyValueEquals(responseWrapper.productrevGetReminderCounter)
    }

    @Test
    fun `when fetch reminder counter fail should update error value`() {
        val errorMessage = anyString()
        coEvery { productrevGetReminderCounterUseCase.executeOnBackground() } throws Throwable(errorMessage)
        viewModel.fetchReminderCounter()
        viewModel.getError().verifyValueEquals(errorMessage)
    }

    @Test
    fun `when fetch reminder template success should update template value`() {
        val responseWrapper = ProductrevGetReminderTemplateResponseWrapper()
        coEvery { productrevGetReminderTemplateUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchReminderTemplate()
        viewModel.getTemplate().verifyValueEquals(responseWrapper.productrevGetReminderTemplate)
    }

    @Test
    fun `when fetch reminder template fail should update error value`() {
        val errorMessage = anyString()
        coEvery { productrevGetReminderTemplateUseCase.executeOnBackground() } throws Throwable(errorMessage)
        viewModel.fetchReminderTemplate()
        viewModel.getError().verifyValueEquals(errorMessage)
    }

    @Test
    fun `when fetch product list success should update products value`(){
        val responseWrapper = ProductrevGetReminderListResponseWrapper()
        coEvery { productrevGetReminderListUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchProductList()
        viewModel.getProducts().verifyValueEquals(responseWrapper.productrevGetReminderList.list)
    }

    @Test
    fun `when fetch product list fail should update error value`(){
        val errorMessage = anyString()
        coEvery { productrevGetReminderListUseCase.executeOnBackground() } throws Throwable(errorMessage)
        viewModel.fetchProductList()
        viewModel.getError().verifyValueEquals(errorMessage)
    }

    @Test
    fun `when send reminder should call usecase`(){
        viewModel.sendReminder(anyString())
        coVerify { productrevSendReminderUseCase.executeOnBackground() }
    }
}