package com.tokopedia.talk.feature.write

import android.accounts.NetworkErrorException
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteButtonState
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.talk.util.unselectedCategories
import com.tokopedia.talk.util.verifyErrorEquals
import com.tokopedia.talk.util.verifySuccessEquals
import com.tokopedia.talk.util.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class TalkWriteViewModelTest : TalkWriteViewModelTestFixture() {

    @Test
    fun `when getWriteFormData success should execute expected use case and get expected data`() {
        val productId = 0
        val expectedResponse = DiscussionGetWritingFormResponseWrapper()

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingFormSuccessEquals(Success(expectedResponse.discussionGetWritingForm))
    }

    @Test
    fun `when getWriteFormData fail should execute expected use case and fail with expected error`() {
        val productId = 0
        val expectedError = NetworkErrorException()

        onGetDiscussionWritingFormError_shouldReturn(expectedError)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingErrorEquals(Fail(expectedError))
    }

    @Test
    fun `when toggleCategory should select category if not selected and vice versa`() {
        val productId = 0
        val expectedResponse = DiscussionGetWritingFormResponseWrapper(DiscussionGetWritingForm(categories = unselectedCategories))

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)
        val categoryToSelect = TalkWriteCategory("Stock", "", false)
        viewModel.toggleCategory(categoryToSelect)

        verifyDiscussionGetWritingFormUseCaseCalled()
        val expectedSelectedCategories = listOf(
                TalkWriteCategory("Stock", "", true),
                TalkWriteCategory("Varian", "", false),
                TalkWriteCategory("Deskripsi Produk", "", false),
                TalkWriteCategory("Logistik", "", false),
                TalkWriteCategory("Lainnya", "", false)
        )

        verifyCategoriesEquals(expectedSelectedCategories)
        verifySelectedCategoryEquals(categoryToSelect)

        viewModel.toggleCategory(TalkWriteCategory("Stock", "", false))

        val expectedUnselectedCategories = listOf(
                TalkWriteCategory("Stock", "", false),
                TalkWriteCategory("Varian", "", false),
                TalkWriteCategory("Deskripsi Produk", "", false),
                TalkWriteCategory("Logistik", "", false),
                TalkWriteCategory("Lainnya", "", false)
        )

        verifyCategoriesEquals(expectedUnselectedCategories)
        verifySelectedCategoryEquals(null)
    }

    @Test
    fun `when getShopId should get expected shopId`() {
        val expectedShopId = "12345"
        val productId = 0
        val expectedResponse = DiscussionGetWritingFormResponseWrapper(DiscussionGetWritingForm(shopId = expectedShopId))

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyShopIdEquals(expectedShopId)
    }

    @Test
    fun `when getProductId should return expected productId`() {
        val expectedProductId = 0

        viewModel.setProductId(expectedProductId)

        verifyProductIdEquals(expectedProductId)
    }

    @Test
    fun `when refresh should trigger load form again`() {
        viewModel.setProductId(0)
        viewModel.refresh()

        verifyDiscussionGetWritingFormUseCaseCalled()
    }

    @Test
    fun `when updateIsTextNotEmpty should set buttonState's isNotTextEmpty to expected value`() {
        val expectedValue = TalkWriteButtonState(false, true)

        viewModel.updateIsTextNotEmpty(expectedValue.isNotTextEmpty)

        verifyButtonStateIsTextNotEmptyEquals(expectedValue)
    }

    // TO DO CHANGE TO CORRECT USE CASE WHEN AVAILABLE
//    @Test
//    fun `when submitWriteForm should execute expected use case and get expected data`() {
//        val response = TalkCreateNewTalkResponseWrapper(TalkCreateNewTalk(talkMutationData = TalkMutationData(isSuccess = 1)))
//        val text = "Ready stock?"
//
//        onTalkCreateNewTalk_thenReturn(response)
//
//        viewModel.submitWriteForm(text)
//
//        val expectedResponse = Success(response.talkCreateNewTalk)
//
//        verifyTalkCreateNewTalkUseCaseCalled()
//        verifyTalkCreateNewTalkSuccessEquals(expectedResponse)
//    }
//
//    @Test
//    fun `when submitWriteForm fails due to backend should execute expected use case and fail with expected error`() {
//        val response = TalkCreateNewTalkResponseWrapper(TalkCreateNewTalk(messageError = listOf("Some Error"), talkMutationData = TalkMutationData(isSuccess = 0)))
//        val text = "Ready stock?"
//
//        onTalkCreateNewTalkFail_thenReturn(response)
//
//        viewModel.submitWriteForm(text)
//
//        val expectedResponse = Fail(Throwable(response.talkCreateNewTalk.messageError.first()))
//
//        verifyTalkCreateNewTalkUseCaseCalled()
//        verifyTalkDeleteTalkErrorEquals(expectedResponse)
//    }
//
//    @Test
//    fun `when submitWriteForm fails due to network issue should execute expected use case and fail with expected error`() {
//        val exception = NetworkErrorException()
//        val text = "Ready stock?"
//
//        onTalkCreateNewTalkNetworkFail_thenReturn(exception)
//
//        viewModel.submitWriteForm(text)
//
//        verifyTalkCreateNewTalkUseCaseCalled()
//        verifyTalkDeleteTalkErrorEquals(Fail(exception))
//    }

    private fun verifyDiscussionGetWritingFormUseCaseCalled() {
        coVerify { discussionGetWritingFormUseCase.executeOnBackground() }
    }

//    private fun verifyTalkCreateNewTalkUseCaseCalled() {
//        coVerify { talkCreateNewTalkUseCase.executeOnBackground() }
//    }

    private fun onGetDiscussionWritingForm_shouldReturn(response: DiscussionGetWritingFormResponseWrapper) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } returns response
    }

    private fun onGetDiscussionWritingFormError_shouldReturn(exception: Exception) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } throws exception
    }

//    private fun onTalkCreateNewTalk_thenReturn(talkCreateNewTalkResponseWrapper: TalkCreateNewTalkResponseWrapper) {
//        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } returns talkCreateNewTalkResponseWrapper
//    }
//
//    private fun onTalkCreateNewTalkFail_thenReturn(talkCreateNewTalkResponseWrapper: TalkCreateNewTalkResponseWrapper) {
//        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } returns talkCreateNewTalkResponseWrapper
//    }
//
//    private fun onTalkCreateNewTalkNetworkFail_thenReturn(exception: Exception) {
//        coEvery { talkCreateNewTalkUseCase.executeOnBackground() } throws exception
//    }

//    private fun verifyTalkCreateNewTalkSuccessEquals(expectedResponse: Success<TalkCreateNewTalk>) {
//        viewModel.talkCreateNewTalkResponse.verifySuccessEquals(expectedResponse)
//    }
//
//    private fun verifyTalkDeleteTalkErrorEquals(expectedError: Fail) {
//        viewModel.talkCreateNewTalkResponse.verifyErrorEquals(expectedError)
//    }

    private fun verifyDiscussionGetWritingFormSuccessEquals(expectedResponse: Success<DiscussionGetWritingForm>) {
        viewModel.writeFormData.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionGetWritingErrorEquals(expectedError: Fail) {
        viewModel.writeFormData.verifyErrorEquals(expectedError)
    }

    private fun verifyProductIdEquals(expectedProductId: Int) {
        Assert.assertEquals(expectedProductId, viewModel.getProductId())
    }

    private fun verifyButtonStateIsTextNotEmptyEquals(expectedValue: TalkWriteButtonState) {
        viewModel.buttonState.verifyValueEquals(expectedValue)
    }

    private fun verifyShopIdEquals(expectedShopId: String) {
        Assert.assertEquals(expectedShopId, viewModel.shopId)
    }

    private fun verifyCategoriesEquals(categories: List<TalkWriteCategory>) {
        viewModel.categoryChips.verifyValueEquals(categories)
    }

    private fun verifySelectedCategoryEquals(category: TalkWriteCategory?) {
        Assert.assertEquals(category, viewModel.getSelectedCategory())
    }
}