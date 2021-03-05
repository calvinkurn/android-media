package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.feature.sellersettings.template.data.*
import com.tokopedia.talk.util.verifyTemplateMutationErrorEquals
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import kotlin.math.exp

class TalkTemplateViewModelTest : TalkTemplateViewModelTestFixture() {

    @Test
    fun `when arrangeTemplate success should execute expected use case`() {
        val expectedResponse = ArrangeTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessArrangeTemplate_thenReturn(expectedResponse)

        viewModel.arrangeTemplate(0, 1, anyBoolean())

        val expectedValue = TalkTemplateMutationResults.TemplateMutationSuccess

        verifyArrangeTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when arrangeTemplate has the same index should not execute use case`() {
        viewModel.arrangeTemplate(0, 0, anyBoolean())
        coVerify {
            arrangeTemplateUseCase.executeOnBackground() wasNot Called
        }
    }

    @Test
    fun `when arrangeTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = ArrangeTemplateResponseWrapper()

        onSuccessArrangeTemplate_thenReturn(expectedResponse)

        viewModel.arrangeTemplate(0, 1, anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.RearrangeTemplateFailed())
    }

    @Test
    fun `when arrangeTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = MessageErrorException()

        onFailArrangeTemplate_thenReturn(expectedResponse)

        viewModel.arrangeTemplate(0, 1, anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
        verifyTemplateMutationFail(TalkTemplateMutationResults.RearrangeTemplateFailed())
    }

    @Test
    fun `when enableTemplate success should execute expected use case`() {
        val expectedResponse = EnableTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessEnableTemplate_thenReturn(expectedResponse)

        viewModel.enableTemplate(true)

        val expectedValue = TalkTemplateMutationResults.TemplateActivateSuccess

        verifyEnableTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when disableTemplate success should execute expected use case`() {
        val expectedResponse = EnableTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessEnableTemplate_thenReturn(expectedResponse)

        viewModel.enableTemplate(false)

        val expectedValue = TalkTemplateMutationResults.TemplateDeactivateSuccess

        verifyEnableTemplateUseCaseCalled()
        verifyTemplateMutationSuccess(expectedValue)
    }

    @Test
    fun `when enableTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = EnableTemplateResponseWrapper()

        onSuccessEnableTemplate_thenReturn(expectedResponse)

        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when enableTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = MessageErrorException()

        onFailEnableTemplate_thenReturn(expectedResponse)

        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when getTemplateList success should execute expected use case`() {
        val expectedResponse = GetAllTemplateResponseWrapper()

        onSuccessGetTemplateList_thenReturn(expectedResponse)

        viewModel.getTemplateList(anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
        verifyGetTemplateListSuccess(expectedResponse.chatTemplatesAll)
    }

    @Test
    fun `when getTemplateList fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailGetTemplateList_thenReturn(expectedResponse)

        viewModel.getTemplateList(anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
        verifyGetTemplateListFail(expectedResponse)
    }

    private fun verifyArrangeTemplateUseCaseCalled() {
        coVerify { arrangeTemplateUseCase.executeOnBackground() }
    }

    private fun verifyEnableTemplateUseCaseCalled() {
        coVerify { enableTemplateUseCase.executeOnBackground() }
    }

    private fun verifyGetAllTemplatesUseCaseCalled() {
        coVerify { getAllTemplatesUseCase.executeOnBackground() }
    }

    private fun onSuccessArrangeTemplate_thenReturn(arrangeTemplateResponseWrapper: ArrangeTemplateResponseWrapper) {
        coEvery { arrangeTemplateUseCase.executeOnBackground() } returns arrangeTemplateResponseWrapper
    }

    private fun onFailArrangeTemplate_thenReturn(throwable: Throwable) {
        coEvery { arrangeTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun onSuccessEnableTemplate_thenReturn(enableTemplateResponseWrapper: EnableTemplateResponseWrapper) {
        coEvery { enableTemplateUseCase.executeOnBackground() } returns enableTemplateResponseWrapper
    }

    private fun onFailEnableTemplate_thenReturn(throwable: Throwable) {
        coEvery { enableTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun onSuccessGetTemplateList_thenReturn(getAllTemplateResponseWrapper: GetAllTemplateResponseWrapper) {
        coEvery { getAllTemplatesUseCase.executeOnBackground() } returns getAllTemplateResponseWrapper
    }

    private fun onFailGetTemplateList_thenReturn(throwable: Throwable) {
        coEvery { getAllTemplatesUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetTemplateListSuccess(chatTemplatesAll: ChatTemplatesAll) {
        viewModel.templateList.verifySuccessEquals(Success(chatTemplatesAll))
    }

    private fun verifyGetTemplateListFail(throwable: Throwable) {
        viewModel.templateList.verifyErrorEquals(Fail(throwable))
    }

    private fun verifyTemplateMutationSuccess(talkTemplateMutationResults: TalkTemplateMutationResults) {
        viewModel.templateMutation.verifyValueEquals(talkTemplateMutationResults)
    }

    private fun verifyTemplateMutationFail() {
        viewModel.templateMutation.verifyTemplateMutationErrorEquals(TalkTemplateMutationResults.MutationFailed())
    }

    private fun verifyTemplateMutationFail(talkTemplateMutationResults: TalkTemplateMutationResults) {
        viewModel.templateMutation.verifyTemplateMutationErrorEquals(talkTemplateMutationResults)
    }
}