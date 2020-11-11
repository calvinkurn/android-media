package com.tokopedia.report.view.viewmodel

import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.report.coroutine.TestCoroutineDispatchers
import com.tokopedia.report.domain.interactor.SubmitReportUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

abstract class ProductReportSubmitViewModelTestFixture {

    @RelaxedMockK
    lateinit var submitReportUseCase: SubmitReportUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    protected lateinit var viewModel: ProductReportSubmitViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductReportSubmitViewModel(submitReportUseCase, uploaderUseCase, TestCoroutineDispatchers)
    }
}