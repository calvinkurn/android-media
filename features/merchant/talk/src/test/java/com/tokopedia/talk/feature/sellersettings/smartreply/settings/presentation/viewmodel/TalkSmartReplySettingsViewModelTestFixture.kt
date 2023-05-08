package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.domain.usecase.DiscussionGetSmartReplyUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkSmartReplySettingsViewModelTestFixture {

    @RelaxedMockK
    lateinit var discussionGetSmartReplyUseCase: DiscussionGetSmartReplyUseCase

    @RelaxedMockK
    lateinit var firebaseRemoteConfig: RemoteConfig

    protected val viewModel by lazy {
        TalkSmartReplySettingsViewModel(
            discussionGetSmartReplyUseCase,
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
