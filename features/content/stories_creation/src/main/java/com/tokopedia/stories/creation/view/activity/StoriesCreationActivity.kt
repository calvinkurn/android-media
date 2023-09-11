package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.util.Router
import com.tokopedia.nest.components.NestBottomSheetScreen
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
import com.tokopedia.stories.creation.view.model.StoriesCreationBottomSheetType
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.screen.StoriesCreationInfoLayout
import com.tokopedia.stories.creation.view.stateholder.StoriesCreationBottomSheetStateHolder
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

    @Inject
    lateinit var bottomSheetStateHolder: StoriesCreationBottomSheetStateHolder

    private val viewModel by viewModels<StoriesCreationViewModel> { viewModelFactory }

    private val mediaPickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val data = it.data
            if (data != null) {
                val mediaFilePath = MediaPicker.result(data).originalPaths.getOrNull(0).orEmpty()
                viewModel.submitAction(StoriesCreationAction.SetMedia(mediaFilePath))
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
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

    @OptIn(ExperimentalMaterialApi::class)
    private fun setupContentView() {
        setContent {
            NestTheme {
                Surface {

                    val context = LocalContext.current
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    bottomSheetStateHolder.initState()

                    LaunchedEffect(Unit) {
                        observeUiEvent()
                    }

                    LaunchedEffect(Unit) {
                        openMediaPicker()
                    }

                    NestBottomSheetScreen(
                        state = bottomSheetStateHolder.sheetState,
                        showCloseIcon = true,
                        isHideable = true,
                        bottomSheetContent = {
                            when (bottomSheetStateHolder.bottomSheetType) {
                                is StoriesCreationBottomSheetType.TooMuchStories -> {

                                    /** TODO JOE: the content will be coming from BE */
                                    StoriesCreationInfoLayout(
                                        imageUrl = uiState.config.maxStoriesConfig.imageUrl,
                                        title = uiState.config.maxStoriesConfig.title,
                                        subtitle = uiState.config.maxStoriesConfig.description,
                                        primaryText = uiState.config.maxStoriesConfig.primaryText,
                                        secondaryText = uiState.config.maxStoriesConfig.secondaryText,
                                        onPrimaryButtonClicked = {

                                        },
                                        onSecondaryButtonClicked = {
                                            bottomSheetStateHolder.dismissBottomSheet()
                                        },
                                    )
                                }
                                else -> {}
                            }
                        }
                    ) {
                        StoriesCreationScreen(
                            uiState = uiState,
                            onLoadMediaPreview = { mediaFilePath ->
                                videoSnapshotHelper.snapVideoBitmap(context, mediaFilePath)
                            },
                            onBackPressed = {

                            },
                            onClickChangeAccount = {
                                /** Won't handle for now since UGC is not supported yet */
                            },
                            onClickAddProduct = {
                                viewModel.submitAction(StoriesCreationAction.ClickAddProduct)
                            },
                            onClickUpload = {
                                viewModel.submitAction(StoriesCreationAction.ClickUpload)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun observeUiEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesCreationUiEvent.ShowTooManyStoriesReminder -> {
                        bottomSheetStateHolder.showBottomSheet(StoriesCreationBottomSheetType.TooMuchStories)
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
            modeType(ModeType.VIDEO_ONLY)
            singleSelectionMode()
            previewActionText(getString(R.string.stories_creation_continue))
        }

        router.route(mediaPickerResult, intent)
    }
}
