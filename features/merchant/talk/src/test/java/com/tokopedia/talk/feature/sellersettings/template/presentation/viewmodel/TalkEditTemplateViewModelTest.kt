package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import com.tokopedia.talk.feature.sellersettings.template.data.*
import com.tokopedia.talk.util.verifyTemplateMutationErrorEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers

class TalkEditTemplateViewModelTest : TalkEditTemplateViewModelTestFixture() {

    @Test
    fun `when addTemplate success should execute expected use case`() {
        val expectedResponse = AddTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString())

        val expectedValue = TalkTemplateMutationResults.TemplateMutationSuccess

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when addTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = AddTemplateResponseWrapper(TemplateMutationResult(success = 0))

        onSuccessAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString())

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.MutationFailed())
    }

    @Test
    fun `when addTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString())

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.MutationFailed())
    }

    @Test
    fun `when deleteSpecificTemplate success should execute expected use case`() {
        val expectedResponse = DeleteSpecificTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean())

        val expectedValue = TalkTemplateMutationResults.DeleteTemplate

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when deleteSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = DeleteSpecificTemplateResponseWrapper(TemplateMutationResult(success = 0))

        onSuccessDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.DeleteTemplateFailed())
    }

    @Test
    fun `when deleteSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(ArgumentMatchers.anyInt(), ArgumentMatchers.anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.DeleteTemplateFailed())
    }

    @Test
    fun `when updateSpecificTemplate success should execute expected use case`() {
        val expectedResponse = UpdateSpecificTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())

        val expectedValue = TalkTemplateMutationResults.TemplateMutationSuccess

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when updateSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = UpdateSpecificTemplateResponseWrapper(TemplateMutationResult(success = 0))

        onSuccessUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.MutationFailed())
    }

    @Test
    fun `when updateSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.MutationFailed())
    }

    private fun verifyAddTemplateUseCaseCalled() {
        coVerify { addTemplateUseCase.executeOnBackground() }
    }

    private fun verifyDeleteTemplateUseCaseCalled() {
        coVerify { deleteSpecificTemplateUseCase.executeOnBackground() }
    }

    private fun verifyUpdateSpecificTemplateUseCaseCalled() {
        coVerify { updateSpecificTemplateUseCase.executeOnBackground() }
    }

    private fun onSuccessAddTemplate_thenReturn(addTemplateResponseWrapper: AddTemplateResponseWrapper) {
        coEvery { addTemplateUseCase.executeOnBackground() } returns addTemplateResponseWrapper
    }

    private fun onFailAddTemplate_thenReturn(throwable: Throwable) {
        coEvery { addTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun onSuccessDeleteSpecificTemplate_thenReturn(deleteSpecificTemplateResponseWrapper: DeleteSpecificTemplateResponseWrapper) {
        coEvery { deleteSpecificTemplateUseCase.executeOnBackground() } returns deleteSpecificTemplateResponseWrapper
    }

    private fun onFailDeleteSpecificTemplate_thenReturn(throwable: Throwable) {
        coEvery { deleteSpecificTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun onSuccessUpdateSpecificTemplate_thenReturn(updateSpecificTemplateResponseWrapper: UpdateSpecificTemplateResponseWrapper) {
        coEvery { updateSpecificTemplateUseCase.executeOnBackground() } returns updateSpecificTemplateResponseWrapper
    }

    private fun onFailUpdateSpecificTemplate_thenReturn(throwable: Throwable) {
        coEvery { updateSpecificTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyTemplateMutationSuccess(talkTemplateMutationResults: TalkTemplateMutationResults) {
        viewModel.templateMutation.verifyValueEquals(talkTemplateMutationResults)
    }

    private fun verifyTemplateMutationFail(talkTemplateMutationFailed: TalkTemplateMutationResults.MutationFailed) {
        viewModel.templateMutation.verifyTemplateMutationErrorEquals(talkTemplateMutationFailed)
    }

    private fun verifyTemplateMutationFail(talkTemplateMutationResults: TalkTemplateMutationResults) {
        viewModel.templateMutation.verifyTemplateMutationErrorEquals(talkTemplateMutationResults)
    }

}