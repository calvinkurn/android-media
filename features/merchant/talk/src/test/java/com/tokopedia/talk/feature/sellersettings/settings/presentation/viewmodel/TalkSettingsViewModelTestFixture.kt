package com.tokopedia.talk.feature.sellersettings.settings.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfig
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}
