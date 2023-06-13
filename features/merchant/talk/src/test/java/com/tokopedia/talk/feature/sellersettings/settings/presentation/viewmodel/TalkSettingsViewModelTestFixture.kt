package com.tokopedia.talk.feature.sellersettings.settings.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkSettingsViewModelTestFixture {

    @RelaxedMockK
    lateinit var firebaseRemoteConfig: RemoteConfig

    protected val viewModel by lazy {
        TalkSettingsViewModel(firebaseRemoteConfig)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val taskRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}
