package com.tokopedia.talk.feature.write

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.coroutines.TestCoroutineDispatchers
import com.tokopedia.talk.feature.write.domain.usecase.DiscussionGetWritingFormUseCase
import com.tokopedia.talk.feature.write.presentation.viewmodel.TalkWriteViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkWriteViewModelTestFixture {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var discussionGetWritingFormUseCase: DiscussionGetWritingFormUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkWriteViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkWriteViewModel(TestCoroutineDispatchers,
                discussionGetWritingFormUseCase,
                userSession)
        viewModel.writeFormData.observeForever{}
    }


}