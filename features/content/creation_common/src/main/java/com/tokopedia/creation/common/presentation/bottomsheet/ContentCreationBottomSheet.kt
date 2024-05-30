package com.tokopedia.creation.common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.analytics.ContentCreationAnalytics
import com.tokopedia.creation.common.di.ContentCreationComponent
import com.tokopedia.creation.common.di.ContentCreationInjector
import com.tokopedia.creation.common.presentation.components.ContentCreationView
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.viewmodel.ContentCreationViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle

/**
 * Created By : Muhammad Furqan on 06/09/23
 */
class ContentCreationBottomSheet : BottomSheetUnify() {

    private val viewModel: ContentCreationViewModel by viewModels {
        createComponent().contentCreationFactory()
    }

    var shouldShowPerformanceAction: Boolean = false
    var listener: Listener? = null
    var analytics: ContentCreationAnalytics? = null
    var creationConfig: ContentCreationConfigModel = ContentCreationConfigModel.Empty

    var widgetSource: ContentCreationEntryPointSource = ContentCreationEntryPointSource.Unknown

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isDragable = true
        isHideable = true
        isSkipCollapseState = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
        showHeader = false

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val selectedCreation =
                    viewModel.selectedCreationType.collectAsStateWithLifecycle()
                val creationList = viewModel.creationConfig.collectAsStateWithLifecycle()

                ContentCreationView(
                    creationConfig = creationList.value,
                    selectedItem = selectedCreation.value,
                    isOwner = shouldShowPerformanceAction,
                    onImpressBottomSheet = {
                        analytics?.eventImpressionContentCreationBottomSheet(
                            viewModel.authorType,
                            widgetSource
                        )
                    },
                    onSelectItem = {
                        viewModel.selectCreationItem(it)
                    },
                    onNextClicked = {
                        selectedCreation.value?.let {
                            analytics?.eventClickNextButton(
                                viewModel.authorType,
                                viewModel.selectedItemTitle,
                                widgetSource
                            )
                            listener?.onCreationNextClicked(it)
                            dismiss()
                        }
                    },
                    onRetryClicked = {
                        viewModel.fetchConfig(widgetSource)
                    },
                    onCloseClicked = {
                        dismiss()
                    },
                    onSeePerformanceClicked = {
                        analytics?.eventClickPerformanceDashboard(
                            viewModel.authorType,
                            widgetSource
                        )
                        RouteManager.route(
                            requireContext(),
                            viewModel.getPerformanceDashboardApplink()
                        )
                    },
                    onSettingsClicked = {
                        RouteManager.route(
                            requireContext(),
                            ApplinkConst.CONTENT_SETTINGS
                        )
                    }
                )
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchConfig(widgetSource, creationConfig)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    private fun createComponent(): ContentCreationComponent = ContentCreationInjector.get(requireContext())

    interface Listener {
        fun onCreationNextClicked(data: ContentCreationItemModel)
    }

    companion object {
        private const val TAG = "ContentCreationBottomSheet"

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentCreationBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? ContentCreationBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentCreationBottomSheet::class.java.name
            ) as ContentCreationBottomSheet
        }
    }
}
