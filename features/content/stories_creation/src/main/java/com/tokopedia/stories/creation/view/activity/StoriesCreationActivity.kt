package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.util.Router
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.di.DaggerStoriesCreationComponent
import com.tokopedia.stories.creation.di.StoriesCreationModule
import com.tokopedia.stories.creation.view.screen.StoriesCreationScreen
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationErrorBottomSheet
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationInfoBottomSheet
import com.tokopedia.stories.creation.view.model.StoriesMediaType
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
class StoriesCreationActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var videoSnapshotHelper: VideoSnapshotHelper

    @Inject
    lateinit var router: Router

    private val viewModel by viewModels<StoriesCreationViewModel> { viewModelFactory }

    private val mediaPickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val data = it.data
            if (data != null) {
                val mediaFilePath = MediaPicker.result(data).originalPaths.getOrNull(0).orEmpty()
                val mediaType = StoriesMediaType.parse(mediaFilePath)

                viewModel.submitAction(StoriesCreationAction.SetMedia(mediaFilePath, mediaType))
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setupAttachFragmentListener()
        setupContentView()
    }

    private fun inject() {
        DaggerStoriesCreationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .storiesCreationModule(StoriesCreationModule(this))
            .build()
            .inject(this)
    }

    private fun setupAttachFragmentListener() {
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            when (fragment) {
                is StoriesCreationErrorBottomSheet -> {
                    fragment.listener = object : StoriesCreationErrorBottomSheet.Listener {
                        override fun onRetry() {
                            fragment.dismiss()
                            viewModel.submitAction(StoriesCreationAction.Prepare)
                        }

                        override fun onClose() {
                            fragment.dismiss()
                            finish()
                        }
                    }
                }

                is StoriesCreationInfoBottomSheet -> {
                    fragment.info = StoriesCreationInfoBottomSheet.Info(
                        imageUrl = viewModel.maxStoriesConfig.imageUrl,
                        title = viewModel.maxStoriesConfig.title,
                        subtitle = viewModel.maxStoriesConfig.description,
                        primaryText = viewModel.maxStoriesConfig.primaryText,
                        secondaryText = viewModel.maxStoriesConfig.secondaryText
                    )

                    fragment.listener = object : StoriesCreationInfoBottomSheet.Listener {
                        override fun onClose() {
                            fragment.dismiss()
                            finish()
                        }

                        override fun onPrimaryButtonClick() {
                            fragment.dismiss()
                            openMediaPicker()
                        }

                        override fun onSecondaryButtonClick() {
                            fragment.dismiss()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun setupContentView() {
        setContent {
            LaunchedEffect(Unit) {
                observeUiEvent()
            }

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
                            finish()
                        },
                        onClickChangeAccount = {
                            /** Won't handle for now since UGC is not supported yet */
                        },
                        onClickAddProduct = {
                            /** TODO JOE: handle this later */
                            viewModel.submitAction(StoriesCreationAction.ClickAddProduct(emptyList()))
                        },
                        onClickUpload = {
                            viewModel.submitAction(StoriesCreationAction.ClickUpload)
                        }
                    )
                }
            }
        }

        viewModel.submitAction(StoriesCreationAction.Prepare)
    }

    private fun observeUiEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesCreationUiEvent.OpenMediaPicker -> {
                        openMediaPicker()
                    }
                    is StoriesCreationUiEvent.ErrorPreparePage -> {
                        StoriesCreationErrorBottomSheet
                            .getFragment(supportFragmentManager, classLoader)
                            .show(supportFragmentManager, event.throwable)
                    }
                    is StoriesCreationUiEvent.ShowTooManyStoriesReminder -> {
                        StoriesCreationInfoBottomSheet
                            .getFragment(supportFragmentManager, classLoader)
                            .show(supportFragmentManager)
                    }
                }
            }
        }
    }

    private fun openMediaPicker() {
        val intent = MediaPicker.intent(this) {
            /** TODO JOE: setup media picker for stories */
            pageSource(PageSource.Stories)
            minVideoDuration(1000)
            maxVideoDuration(90000)
            pageType(PageType.GALLERY)
            modeType(ModeType.COMMON)
            singleSelectionMode()
            previewActionText(getString(R.string.stories_creation_continue))
        }

        router.route(mediaPickerResult, intent)
    }
}
