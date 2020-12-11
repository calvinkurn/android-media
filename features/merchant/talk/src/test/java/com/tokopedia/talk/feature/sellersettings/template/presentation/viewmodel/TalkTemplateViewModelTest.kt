package com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel

import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.*

class TalkTemplateViewModelTest : TalkTemplateViewModelTestFixture() {

    @Test
    fun `when addTemplate success should execute expected use case`() {
        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
    }

    @Test
    fun `when addTemplate fail due to BE error should execute expected use case and return fail`() {
        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
    }

    @Test
    fun `when addTemplate fail due to network error should execute expected use case and return fail`() {
        viewModel.addTemplate(anyBoolean(), anyString())

        verifyAddTemplateUseCaseCalled()
    }

    @Test
    fun `when arrangeTemplate success should execute expected use case`() {
        viewModel.arrangeTemplate(anyInt(), anyInt(), anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
    }

    @Test
    fun `when arrangeTemplate fail due to BE error should execute expected use case and return fail`() {
        viewModel.arrangeTemplate(anyInt(), anyInt(), anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
    }

    @Test
    fun `when arrangeTemplate fail due to network error should execute expected use case and return fail`() {
        viewModel.arrangeTemplate(anyInt(), anyInt(), anyBoolean())

        verifyArrangeTemplateUseCaseCalled()
    }

    @Test
    fun `when deleteSpecificTemplate success should execute expected use case`() {
        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
    }

    @Test
    fun `when deleteSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
    }

    @Test
    fun `when deleteSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        viewModel.deleteSpecificTemplate(anyInt(), anyBoolean())

        verifyDeleteTemplateUseCaseCalled()
    }

    @Test
    fun `when enableTemplate success should execute expected use case`() {
        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
    }

    @Test
    fun `when enableTemplate fail due to BE error should execute expected use case and return fail`() {
        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
    }

    @Test
    fun `when enableTemplate fail due to network error should execute expected use case and return fail`() {
        viewModel.enableTemplate(anyBoolean())

        verifyEnableTemplateUseCaseCalled()
    }

    @Test
    fun `when getTemplateList success should execute expected use case`() {
        viewModel.getTemplateList(anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
    }

    @Test
    fun `when getTemplateList fail due to BE error should execute expected use case and return fail`() {
        viewModel.getTemplateList(anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
    }

    @Test
    fun `when getTemplateList fail due to network error should execute expected use case and return fail`() {
        viewModel.getTemplateList(anyBoolean())

        verifyGetAllTemplatesUseCaseCalled()
    }

    @Test
    fun `when updateSpecificTemplate success should execute expected use case`() {
        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
    }

    @Test
    fun `when updateSpecificTemplate fail due to BE error should execute expected use case and return fail`() {
        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
    }

    @Test
    fun `when updateSpecificTemplate fail due to network error should execute expected use case and return fail`() {
        viewModel.updateSpecificTemplate(anyBoolean(), anyString(), anyInt())

        verifyUpdateSpecificTemplateUseCaseCalled()
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
}