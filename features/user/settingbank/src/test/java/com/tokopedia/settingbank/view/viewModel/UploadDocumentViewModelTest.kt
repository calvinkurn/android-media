package com.tokopedia.settingbank.view.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.settingbank.domain.model.UploadDocumentPojo
import com.tokopedia.settingbank.domain.usecase.UploadDocumentUseCase
import com.tokopedia.settingbank.view.viewState.DocumentUploadError
import com.tokopedia.settingbank.view.viewState.DocumentUploaded
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber

class UploadDocumentViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var uploadUseCase: UploadDocumentUseCase

    private lateinit var viewModel: UploadDocumentViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = spyk(UploadDocumentViewModel(uploadUseCase))
    }

    @Test
    fun `uploadDocument success`() {
        val slot = slot<Subscriber<String>>()

        mockkObject(UploadDocumentUseCase)
        val uploadDocumentPojo = mockk<UploadDocumentPojo>()

        every { UploadDocumentUseCase.getParam(uploadDocumentPojo) } returns mockk()

        every {
            uploadUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onNext("Uploaded Success")
        }
        viewModel.uploadDocument(uploadDocumentPojo)
        assert(viewModel.uploadDocumentStatus.value is DocumentUploaded)
    }

    @Test
    fun `uploadDocument Failed`() {
        val slot = slot<Subscriber<String>>()

        mockkObject(UploadDocumentUseCase)
        val uploadDocumentPojo = mockk<UploadDocumentPojo>()

        every { UploadDocumentUseCase.getParam(uploadDocumentPojo) } returns mockk()

        every {
            uploadUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onError(Throwable())
        }
        viewModel.uploadDocument(uploadDocumentPojo)
        assert(viewModel.uploadDocumentStatus.value is DocumentUploadError)
    }

}