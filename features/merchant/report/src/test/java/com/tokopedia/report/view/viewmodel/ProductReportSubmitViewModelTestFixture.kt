package com.tokopedia.report.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.report.domain.interactor.SubmitReportUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ProductReportSubmitViewModelTestFixture {

    @RelaxedMockK
    lateinit var submitReportUseCase: SubmitReportUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductReportSubmitViewModel

    protected var reportInput = mapOf(
            "photo" to listOf("/storage/emulated/0/Tokopedia/Tokopedia Edit/1314740.jpg", "/storage/emulated/0/Tokopedia/Tokopedia Edit/1314147.jpg"),
            "report_reason" to "testing ajaaaaaaaaaaaaa productnya gppp kokkkkkkk"
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductReportSubmitViewModel(submitReportUseCase, uploaderUseCase, CoroutineTestDispatchersProvider)
    }
}