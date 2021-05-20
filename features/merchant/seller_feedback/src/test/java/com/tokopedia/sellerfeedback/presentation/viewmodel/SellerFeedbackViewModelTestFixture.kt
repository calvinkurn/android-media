package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.sellerfeedback.domain.SubmitGlobalFeedbackUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class SellerFeedbackViewModelTestFixture {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: SellerFeedbackViewModel

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @RelaxedMockK
    lateinit var submitGlobalFeedbackUseCase: SubmitGlobalFeedbackUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SellerFeedbackViewModel(
                CoroutineTestDispatchersProvider,
                uploaderUseCase,
                submitGlobalFeedbackUseCase,
                userSession
        )
    }
}