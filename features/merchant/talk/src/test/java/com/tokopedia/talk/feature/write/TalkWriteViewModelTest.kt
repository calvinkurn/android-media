package com.tokopedia.talk.feature.write

import android.accounts.NetworkErrorException
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingForm
import com.tokopedia.talk.feature.write.data.model.DiscussionGetWritingFormResponseWrapper
import com.tokopedia.talk.feature.write.data.model.DiscussionSubmitForm
import com.tokopedia.talk.feature.write.data.model.DiscussionSubmitFormResponseWrapper
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteButtonState
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.talk.util.unselectedCategories
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class TalkWriteViewModelTest : TalkWriteViewModelTestFixture() {

    @Test
    fun `when getWriteFormData success should execute expected use case and get expected data`() {
        val productId = "0"
        val expectedResponse = DiscussionGetWritingFormResponseWrapper()

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingFormSuccessEquals(Success(expectedResponse.discussionGetWritingForm))
    }

    @Test
    fun `when getWriteFormData fail should execute expected use case and fail with expected error`() {
        val productId = "0"
        val expectedError = NetworkErrorException()

        onGetDiscussionWritingFormError_shouldReturn(expectedError)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyDiscussionGetWritingErrorEquals(Fail(expectedError))
    }

    @Test
    fun `when toggleCategory should select category if not selected and vice versa`() {
        val productId = "0"
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
        verifyTextFieldStateEquals(expectedSelectedCategories.any { it.isSelected })

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
        verifyTextFieldStateEquals(expectedUnselectedCategories.any { it.isSelected })
    }

    @Test
    fun `when getShopId should get expected shopId`() {
        val expectedShopId = "12345"
        val productId = "0"
        val expectedResponse = DiscussionGetWritingFormResponseWrapper(DiscussionGetWritingForm(shopId = expectedShopId))

        onGetDiscussionWritingForm_shouldReturn(expectedResponse)

        viewModel.setProductId(productId)

        verifyDiscussionGetWritingFormUseCaseCalled()
        verifyShopIdEquals(expectedShopId)
    }

    @Test
    fun `when getProductId should return expected productId`() {
        val expectedProductId = "0"

        viewModel.setProductId(expectedProductId)

        verifyProductIdEquals(expectedProductId)
    }

    @Test
    fun `when refresh should trigger load form again`() {
        viewModel.setProductId("0")
        viewModel.refresh()

        verifyDiscussionGetWritingFormUseCaseCalled()
    }

    @Test
    fun `when updateIsTextNotEmpty should set buttonState's isNotTextEmpty to expected value`() {
        val expectedValue = TalkWriteButtonState(false, true)

        viewModel.updateIsTextNotEmpty(expectedValue.isNotTextEmpty)

        verifyButtonStateIsTextNotEmptyEquals(expectedValue)
    }


    @Test
    fun `when submitWriteForm should execute expected use case and get expected data`() {
        val response = DiscussionSubmitFormResponseWrapper()
        val text = "Ready stock?"

        onTalkCreateNewTalk_thenReturn(response)

        viewModel.submitForm(text)

        val expectedResponse = Success(response.discussionSubmitForm)

        verifyTalkCreateNewTalkUseCaseCalled()
        verifyTalkCreateNewTalkSuccessEquals(expectedResponse)
    }

    @Test
    fun `when submitWriteForm fails due to network issue should execute expected use case and fail with expected error`() {
        val exception = NetworkErrorException()
        val text = "Ready stock?"

        onTalkCreateNewTalkNetworkFail_thenReturn(exception)

        viewModel.submitForm(text)

        verifyTalkCreateNewTalkUseCaseCalled()
        verifyTalkDeleteTalkErrorEquals(Fail(exception))
    }

    @Test
    fun `when set isVariantSelected should get expected value`() {
        val expectedIsVariantSelected = true

        viewModel.isVariantSelected = expectedIsVariantSelected

        Assert.assertEquals(expectedIsVariantSelected, viewModel.isVariantSelected)
    }

    @Test
    fun `when set availableVariants should get expected value`() {
        val availableVariants = "available variants"

        viewModel.availableVariants = availableVariants

        Assert.assertEquals(availableVariants, viewModel.availableVariants)
    }

    @Test
    fun `when getUserId should get expected userId`() {
        val expectedUserId = "102131"

        onGetUserId_thenReturn(expectedUserId)

        Assert.assertEquals(expectedUserId, viewModel.getUserId())
    }

    private fun verifyDiscussionGetWritingFormUseCaseCalled() {
        coVerify { discussionGetWritingFormUseCase.executeOnBackground() }
    }

    private fun verifyTalkCreateNewTalkUseCaseCalled() {
        coVerify { discussionSubmitFormUseCase.executeOnBackground() }
    }

    private fun onGetDiscussionWritingForm_shouldReturn(response: DiscussionGetWritingFormResponseWrapper) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } returns response
    }

    private fun onGetDiscussionWritingFormError_shouldReturn(exception: Exception) {
        coEvery { discussionGetWritingFormUseCase.executeOnBackground() } throws exception
    }

    private fun onTalkCreateNewTalk_thenReturn(talkCreateNewTalkResponseWrapper: DiscussionSubmitFormResponseWrapper) {
        coEvery { discussionSubmitFormUseCase.executeOnBackground() } returns talkCreateNewTalkResponseWrapper
    }

    private fun onTalkCreateNewTalkNetworkFail_thenReturn(exception: Exception) {
        coEvery { discussionSubmitFormUseCase.executeOnBackground() } throws exception
    }

    private fun onGetUserId_thenReturn(userId: String) {
        every { userSession.userId } returns userId
    }

    private fun verifyTalkCreateNewTalkSuccessEquals(expectedResponse: Success<DiscussionSubmitForm>) {
        viewModel.submitFormResult.verifySuccessEquals(expectedResponse)
    }

    private fun verifyTalkDeleteTalkErrorEquals(expectedError: Fail) {
        viewModel.submitFormResult.verifyErrorEquals(expectedError)
    }

    private fun verifyDiscussionGetWritingFormSuccessEquals(expectedResponse: Success<DiscussionGetWritingForm>) {
        viewModel.writeFormData.verifySuccessEquals(expectedResponse)
    }

    private fun verifyDiscussionGetWritingErrorEquals(expectedError: Fail) {
        viewModel.writeFormData.verifyErrorEquals(expectedError)
    }

    private fun verifyProductIdEquals(expectedProductId: String) {
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

    private fun verifyTextFieldStateEquals(isAnyCategorySelected: Boolean) {
        viewModel.textFieldState.verifyValueEquals(isAnyCategorySelected)
    }
}