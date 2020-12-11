package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import com.tokopedia.talk.feature.sellersettings.template.data.*
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
    fun `when addTemplate success should execute expected use case`() {
        val expectedResponse = AddTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationSuccess()
    }

    @Test
    fun `when addTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = AddTemplateResponseWrapper()

        onSuccessAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when addTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailAddTemplate_thenReturn(expectedResponse)

        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when arrangeTemplate success should execute expected use case`() {
        val expectedResponse = ArrangeTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessArrangeTemplate_thenReturn(expectedResponse)

        viewModel.arrangeTemplate(0, 1, anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
        verifyTemplateMutationSuccess()
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
        verifyTemplateMutationFail()
    }

    @Test
    fun `when arrangeTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailArrangeTemplate_thenReturn(expectedResponse)

        viewModel.arrangeTemplate(0, 1, anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when deleteSpecificTemplate success should execute expected use case`() {
        val expectedResponse = DeleteSpecificTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationSuccess()
    }

    @Test
    fun `when deleteSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = DeleteSpecificTemplateResponseWrapper()

        onSuccessDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when deleteSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailDeleteSpecificTemplate_thenReturn(expectedResponse)

        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when enableTemplate success should execute expected use case`() {
        val expectedResponse = EnableTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessEnableTemplate_thenReturn(expectedResponse)

        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
        verifyTemplateMutationSuccess()
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
        val expectedResponse = Throwable()

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

    @Test
    fun `when updateSpecificTemplate success should execute expected use case`() {
        val expectedResponse = UpdateSpecificTemplateResponseWrapper(TemplateMutationResult(success = 1))

        onSuccessUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationSuccess()
    }

    @Test
    fun `when updateSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        val expectedResponse = UpdateSpecificTemplateResponseWrapper()

        onSuccessUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    @Test
    fun `when updateSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        val expectedResponse = Throwable()

        onFailUpdateSpecificTemplate_thenReturn(expectedResponse)

        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
        verifyTemplateMutationFail()
    }

    private fun verifyAddTemplateUseCaseCalled() {
        coVerify { addTemplateUseCase.executeOnBackground() }
    }

    private fun verifyArrangeTemplateUseCaseCalled() {
        coVerify { arrangeTemplateUseCase.executeOnBackground() }
    }

    private fun verifyDeleteTemplateUseCaseCalled() {
        coVerify { deleteSpecificTemplateUseCase.executeOnBackground() }
    }

    private fun verifyEnableTemplateUseCaseCalled() {
        coVerify { enableTemplateUseCase.executeOnBackground() }
    }

    private fun verifyGetAllTemplatesUseCaseCalled() {
        coVerify { getAllTemplatesUseCase.executeOnBackground() }
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

    private fun onSuccessArrangeTemplate_thenReturn(arrangeTemplateResponseWrapper: ArrangeTemplateResponseWrapper) {
        coEvery { arrangeTemplateUseCase.executeOnBackground() } returns arrangeTemplateResponseWrapper
    }

    private fun onFailArrangeTemplate_thenReturn(throwable: Throwable) {
        coEvery { arrangeTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun onSuccessDeleteSpecificTemplate_thenReturn(deleteSpecificTemplateResponseWrapper: DeleteSpecificTemplateResponseWrapper) {
        coEvery { deleteSpecificTemplateUseCase.executeOnBackground() } returns deleteSpecificTemplateResponseWrapper
    }

    private fun onFailDeleteSpecificTemplate_thenReturn(throwable: Throwable) {
        coEvery { deleteSpecificTemplateUseCase.executeOnBackground() } throws throwable
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

    private fun onSuccessUpdateSpecificTemplate_thenReturn(updateSpecificTemplateResponseWrapper: UpdateSpecificTemplateResponseWrapper) {
        coEvery { updateSpecificTemplateUseCase.executeOnBackground() } returns updateSpecificTemplateResponseWrapper
    }

    private fun onFailUpdateSpecificTemplate_thenReturn(throwable: Throwable) {
        coEvery { updateSpecificTemplateUseCase.executeOnBackground() } throws throwable
    }

    private fun verifyGetTemplateListSuccess(chatTemplatesAll: ChatTemplatesAll) {
        viewModel.templateList.verifySuccessEquals(Success(chatTemplatesAll))
    }

    private fun verifyGetTemplateListFail(throwable: Throwable) {
        viewModel.templateList.verifyErrorEquals(Fail(throwable))
    }

    private fun verifyTemplateMutationSuccess() {
        viewModel.templateMutation.verifyValueEquals(true)
    }

    private fun verifyTemplateMutationFail() {
        viewModel.templateMutation.verifyValueEquals(false)
    }
}