package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.picker.ProductSetupFragment
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.creation.common.presentation.utils.ContentCreationRemoteConfigManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalytic
import com.tokopedia.stories.creation.di.StoriesCreationInjector
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationErrorBottomSheet
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationInfoBottomSheet
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.view.model.StoriesMediaCover
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.activityResult.MediaPickerForResult
import com.tokopedia.stories.creation.view.model.activityResult.MediaPickerIntentData
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.exception.NotEligibleException
import com.tokopedia.stories.creation.view.screen.StoriesCreationScreen
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var contentCreationRemoteConfig: ContentCreationRemoteConfigManager

    @Inject
    lateinit var storiesCreationAnalytic: StoriesCreationAnalytic

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private val viewModel by viewModels<StoriesCreationViewModel> { viewModelFactory }

    private val mediaPickerResult =
        registerForActivityResult(MediaPickerForResult()) { media ->
            if (media != StoriesMedia.Empty) {
                viewModel.submitAction(StoriesCreationAction.SetMedia(media))
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        setupAttachFragmentListener()
        super.onCreate(savedInstanceState)
        setupOnBackPressed()
        setupContentView()
    }

    override fun onPause() {
        super.onPause()
        videoSnapshotHelper.deleteLocalFile()
    }

    private fun inject() {
        StoriesCreationInjector
            .get(this)
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupAttachFragmentListener() {
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            when (fragment) {
                is ProductSetupFragment -> {
                    fragment.setDataSource(object : ProductSetupFragment.DataSource {
                        override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                            return viewModel.productTagSection
                        }

                        override fun isEligibleForPin(): Boolean = false

                        override fun getSelectedAccount(): ContentAccountUiModel {
                            return viewModel.selectedAccount
                        }

                        override fun creationId(): String {
                            return viewModel.storyId
                        }

                        override fun maxProduct(): Int {
                            return viewModel.maxProductTag
                        }

                        override fun isNumerationShown(): Boolean = false

                        override fun fetchCommissionProduct(): Boolean = false
                    }
                    )
                    fragment.setListener(object : ProductSetupFragment.Listener {
                        override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
                            viewModel.submitAction(StoriesCreationAction.SetProduct(productTagSectionList))
                        }
                    })
                }
                is StoriesCreationErrorBottomSheet -> {
                    fragment.listener = object : StoriesCreationErrorBottomSheet.Listener {

                        override fun onRetry(errorType: Int) {
                            fragment.dismiss()

                            when (errorType) {
                                GlobalError.PAGE_NOT_FOUND -> {
                                    finish()
                                }
                                else -> {
                                    viewModel.submitAction(StoriesCreationAction.Prepare)
                                }
                            }
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

    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    lifecycleScope.launchCatchError(block = {
                        withContext(dispatchers.io) {
                            FileUtil.deleteFile(viewModel.mediaFilePath)
                        }

                        finish()
                    }) {}
                }
            }
        )
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
                        onImpressScreen = {
                            storiesCreationAnalytic.openScreenCreationPage(
                                account = viewModel.selectedAccount,
                                storyId = viewModel.storyId
                            )
                        },
                        onLoadMediaPreview = { mediaFilePath ->
                            val file = videoSnapshotHelper.snapVideo(this, mediaFilePath)

                            if (file != null)
                                StoriesMediaCover.Success(file.absolutePath)
                            else
                                StoriesMediaCover.Error
                        },
                        onBackPressed = {
                            onBackPressedDispatcher.onBackPressed()
                        },
                        onClickChangeAccount = {
                            /** Won't handle for now since UGC is not supported yet */
                        },
                        onClickAddProduct = {
                            storiesCreationAnalytic.clickAddProduct(
                                account = viewModel.selectedAccount,
                            )

                            supportFragmentManager.beginTransaction()
                                .add(ProductSetupFragment::class.java, null, null)
                                .commit()
                        },
                        onClickUpload = {
                            storiesCreationAnalytic.clickUpload(
                                account = viewModel.selectedAccount,
                                storyId = viewModel.storyId
                            )

                            viewModel.submitAction(StoriesCreationAction.ClickUpload)
                        }
                    )
                }
            }
        }

        if (!contentCreationRemoteConfig.isShowingCreation()) {
            showErrorBottomSheet(NotEligibleException())
            return
        }

        viewModel.submitAction(StoriesCreationAction.Prepare)
    }

    private fun observeUiEvent() {
        lifecycleScope.launch {
            viewModel.uiEvent
                .flowWithLifecycle(lifecycle)
                .collect { event ->
                    when (event) {
                        is StoriesCreationUiEvent.OpenMediaPicker -> {
                            openMediaPicker()
                        }
                        is StoriesCreationUiEvent.ErrorPreparePage -> {
                            showErrorBottomSheet(event.throwable)
                        }
                        is StoriesCreationUiEvent.ShowTooManyStoriesReminder -> {
                            showInfoBottomSheet()
                        }
                        is StoriesCreationUiEvent.StoriesUploadQueued -> {
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
            }
        }
    }

    private fun showErrorBottomSheet(throwable: Throwable) {
        StoriesCreationErrorBottomSheet
            .getFragment(supportFragmentManager, classLoader, throwable)
            .show(supportFragmentManager)
    }

    private fun showInfoBottomSheet() {
        StoriesCreationInfoBottomSheet
            .getFragment(supportFragmentManager, classLoader)
            .show(supportFragmentManager)
    }

    private fun openMediaPicker() {
        val intentData = MediaPickerIntentData(
            storiesId = viewModel.storiesId,
            minVideoDuration = viewModel.minVideoDuration,
            maxVideoDuration = viewModel.maxVideoDuration,
            previewActionText = getString(R.string.stories_creation_continue),
        )

        router.route(mediaPickerResult, intentData)
    }
}
