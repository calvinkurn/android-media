package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.seller.feedback.domain.SubmitFeedbackUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations.init
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class SellerFeedbackViewKmpModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: SellerFeedbackKmpViewModel

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @RelaxedMockK
    lateinit var submitFeedbackUseCase: SubmitFeedbackUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @Before
    fun setup() {
        init(this)
        viewModel = SellerFeedbackKmpViewModel(
            CoroutineTestDispatchersProvider,
            uploaderUseCase,
            submitFeedbackUseCase,
            userSession
        )
    }
}
