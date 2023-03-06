package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplySettingsUseCase
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplyTemplateUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkSmartReplyDetailViewModelTestFixture {

    @RelaxedMockK
    lateinit var discussionSetSmartReplyTemplateUseCase: DiscussionSetSmartReplyTemplateUseCase

    @RelaxedMockK
    lateinit var discussionSetSmartReplySettingsUseCase: DiscussionSetSmartReplySettingsUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var firebaseRemoteConfig: RemoteConfig

    protected val viewModel by lazy {
        TalkSmartReplyDetailViewModel(
            discussionSetSmartReplyTemplateUseCase,
            discussionSetSmartReplySettingsUseCase,
            userSession,
            firebaseRemoteConfig,
            CoroutineTestDispatchersProvider
        )
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}
