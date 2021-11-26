package com.tokopedia.report.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.report.usecase.SubmitReportUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import java.io.File

abstract class ProductReportSubmitViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductReportSubmitViewModel

    @RelaxedMockK
    lateinit var submitReportUseCase: SubmitReportUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    private val sourceId = "OfQTGl"

    private val photos = listOf(
        "/storage/emulated/0/Tokopedia/Tokopedia Edit/1314740.jpg",
        "/storage/emulated/0/Tokopedia/Tokopedia Edit/1314147.jpg"
    )

    protected val reportInput = mapOf(
        "photo" to photos,
        "report_reason" to "testing ajaaaaaaaaaaaaa productnya gppp kokkkkkkk"
    )

    protected fun getUploadParam1() = uploaderUseCase.createParams(
        sourceId = sourceId,
        filePath = File(photos[0])
    )

    protected fun getUploadParam2() = uploaderUseCase.createParams(
        sourceId = "OfQTGl",
        filePath = File(photos[1])
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductReportSubmitViewModel(
            submitReportUseCase,
            uploaderUseCase,
            CoroutineTestDispatchersProvider
        )
    }
}