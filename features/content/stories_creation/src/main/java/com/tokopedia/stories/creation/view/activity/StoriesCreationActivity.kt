package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.databinding.ActivityStoriesCreationBinding
import com.tokopedia.stories.creation.di.DaggerStoriesCreationComponent
import com.tokopedia.stories.creation.di.StoriesCreationModule
import com.tokopedia.stories.creation.view.screen.StoriesCreationScreen
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
class StoriesCreationActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var videoSnapshotHelper: VideoSnapshotHelper

    private lateinit var binding: ActivityStoriesCreationBinding

    private val viewModel by viewModels<StoriesCreationViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()

        super.onCreate(savedInstanceState)

        setupContentView()
    }

    private fun inject() {
        DaggerStoriesCreationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .storiesCreationModule(StoriesCreationModule(this))
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContentView() {
        setContent {
            NestTheme {
                Surface {

                    val context = LocalContext.current
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    StoriesCreationScreen(
                        uiState = uiState,
                        onLoadMediaPreview = { mediaFilePath ->
                            videoSnapshotHelper.snapVideoBitmap(context, mediaFilePath)
                        },
                        onBackPressed = {

                        },
                        onClickChangeAccount = {

                        },
                        onClickAddProduct = {

                        },
                        onClickUpload = {

                        }
                    )
                }
            }
        }
    }
}
