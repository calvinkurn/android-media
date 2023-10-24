package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.picker.ProductSetupFragment
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.view.screen.StoriesCreationScreen
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.stories.creation.R
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalytic
import com.tokopedia.stories.creation.di.StoriesCreationInjector
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationErrorBottomSheet
import com.tokopedia.stories.creation.view.bottomsheet.StoriesCreationInfoBottomSheet
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.exception.AccountNotEligibleException
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
    lateinit var storiesCreationAnalytic: StoriesCreationAnalytic

    private val viewModel by viewModels<StoriesCreationViewModel> { viewModelFactory }

    private val mediaPickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val data = it.data
            if (data != null) {
                val mediaFilePath = MediaPicker.result(data).originalPaths.getOrNull(0).orEmpty()
                val mediaType = ContentMediaType.parse(mediaFilePath)

                viewModel.submitAction(StoriesCreationAction.SetMedia(mediaFilePath, mediaType))
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupAttachFragmentListener()
        setupContentView()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ProductSetupFragment -> {
                fragment.setDataSource(object : ProductSetupFragment.DataSource {
                        override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                            return viewModel.productTag
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
        }
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
                is StoriesCreationErrorBottomSheet -> {
                    fragment.listener = object : StoriesCreationErrorBottomSheet.Listener {
                        override fun onRetry(throwable: Throwable) {
                            fragment.dismiss()

                            when (throwable) {
                                is AccountNotEligibleException -> {
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
                            videoSnapshotHelper.snapVideoBitmap(context, mediaFilePath)
                        },
                        onBackPressed = {
                            finish()
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
            pageSource(PageSource.Stories)
            minVideoDuration(viewModel.minVideoDuration)
            maxVideoDuration(viewModel.maxVideoDuration)
            pageType(PageType.GALLERY)
            modeType(ModeType.COMMON)
            singleSelectionMode()
            withImmersiveEditor()
            previewActionText(getString(R.string.stories_creation_continue))
        }

        router.route(mediaPickerResult, intent)
    }
}
