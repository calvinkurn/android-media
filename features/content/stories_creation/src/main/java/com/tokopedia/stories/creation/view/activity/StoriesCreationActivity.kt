package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.util.Router
import com.tokopedia.nest.components.NestBottomSheetScreen
import com.tokopedia.nest.components.rememberNestBottomSheetState
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
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
                    val scope = rememberCoroutineScope()
                    val sheetState = rememberNestBottomSheetState()

                    LaunchedEffect(Unit) {
                        observeUiEvent(scope, sheetState)
                    }

                    LaunchedEffect(Unit) {
                        openMediaPicker()
                    }

                    NestBottomSheetScreen(
                        state = sheetState,
                        showCloseIcon = true,
                        isHideable = true,
                        bottomSheetContent = {
                            Text("Testing")
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

    @OptIn(ExperimentalMaterialApi::class)
    private fun observeUiEvent(
        scope: CoroutineScope,
        sheetState: BottomSheetScaffoldState
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is StoriesCreationUiEvent.ShowTooManyStoriesReminder -> {
                        showTooManyStoriesReminder(scope, sheetState)
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

    @OptIn(ExperimentalMaterialApi::class)
    private fun showTooManyStoriesReminder(
        scope: CoroutineScope,
        sheetState: BottomSheetScaffoldState
    ) {
        scope.launch {
            sheetState.bottomSheetState.expand()
        }
    }
}
