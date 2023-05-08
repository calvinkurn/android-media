package com.tokopedia.review.feature.reviewreminder.view.viewmodel

import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderCounterResponseWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderListResponseWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevGetReminderTemplateResponseWrapper
import com.tokopedia.review.feature.reviewreminder.data.ProductrevSendReminder
import com.tokopedia.review.feature.reviewreminder.data.ProductrevSendReminderResponseWrapper
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        coVerify { productrevGetReminderCounterUseCase.executeOnBackground() }
        viewModel.getEstimation().verifyValueEquals(responseWrapper.productrevGetReminderCounter)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when fetch reminder counter fail should update error value`() {
        val throwable = Throwable()
        coEvery { productrevGetReminderCounterUseCase.executeOnBackground() } throws throwable
        viewModel.fetchReminderCounter()
        coVerify { productrevGetReminderCounterUseCase.executeOnBackground() }
        viewModel.getError().verifyValueEquals(throwable)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when fetch reminder template success should update template value`() {
        val responseWrapper = ProductrevGetReminderTemplateResponseWrapper()
        coEvery { productrevGetReminderTemplateUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchReminderTemplate()
        coVerify { productrevGetReminderTemplateUseCase.executeOnBackground() }
        viewModel.getTemplate().verifyValueEquals(responseWrapper.productrevGetReminderTemplate)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when fetch reminder template fail should update error value`() {
        val throwable = Throwable()
        coEvery { productrevGetReminderTemplateUseCase.executeOnBackground() } throws throwable
        viewModel.fetchReminderTemplate()
        coVerify { productrevGetReminderTemplateUseCase.executeOnBackground() }
        viewModel.getError().verifyValueEquals(throwable)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when fetch product list success should update products value`() {
        val responseWrapper = ProductrevGetReminderListResponseWrapper()
        coEvery { productrevGetReminderListUseCase.executeOnBackground() } returns responseWrapper
        viewModel.fetchProductList()
        coVerify { productrevGetReminderListUseCase.executeOnBackground() }
        viewModel.getProducts().verifyValueEquals(responseWrapper.productrevGetReminderList)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when fetch product list fail should update error value`() {
        val throwable = Throwable()
        coEvery { productrevGetReminderListUseCase.executeOnBackground() } throws throwable
        viewModel.fetchProductList()
        coVerify { productrevGetReminderListUseCase.executeOnBackground() }
        viewModel.getError().verifyValueEquals(throwable)
        viewModel.getFetchingStatus().verifyValueEquals(false)
    }

    @Test
    fun `when send reminder should call usecase`() {
        viewModel.sendReminder(anyString())
        coVerify { productrevSendReminderUseCase.executeOnBackground() }
    }

    @Test
    fun `when send reminder success should update result live data`() {
        val responseWrapper = ProductrevSendReminderResponseWrapper(
            ProductrevSendReminder(success = true)
        )
        coEvery { productrevSendReminderUseCase.executeOnBackground() } returns responseWrapper
        viewModel.sendReminder(anyString())
        coVerify { productrevSendReminderUseCase.executeOnBackground() }
        viewModel.getSendReminderResult().verifySuccessEquals(Success(true))
    }

    @Test
    fun `when send reminder error should update result live data with the throwable`() {
        val throwable = Throwable()
        coEvery { productrevSendReminderUseCase.executeOnBackground() } throws throwable
        viewModel.sendReminder(anyString())
        coVerify { productrevSendReminderUseCase.executeOnBackground() }
        viewModel.getSendReminderResult().verifyErrorEquals(Fail(throwable))
    }
}
