package com.tokopedia.mediauploader

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.mediauploader.di.DaggerMediaUploaderTestComponent
import com.tokopedia.mediauploader.ui.DebugScreen
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MediaUploaderActivity : AppCompatActivity() {

    @Inject lateinit var uploaderUseCase: UploaderUseCase
    @Inject lateinit var userSession: UserSessionInterface
    @Inject lateinit var factory: ViewModelProvider.Factory

    private val viewModel: DebugMediaUploaderHandler by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        setContent {
            NestTheme {
                DebugScreen(viewModel)
            }
        }
    }

    private fun initInjector() {
        DaggerMediaUploaderTestComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

}
