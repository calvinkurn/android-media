package com.tokopedia.creation.common.presentation.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import com.tokopedia.stories.widget.settings.StoriesSettingsFactory
import com.tokopedia.stories.widget.settings.StoriesSettingsFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

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

        @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    @Inject
    lateinit var factory: StoriesSettingsFactory.Creator

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    StoriesSettingsFragment::class.java.name -> StoriesSettingsFragment(
                        factory,
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isDragable = true
        isHideable = true
        isSkipCollapseState = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val selectedCreation =
                    viewModel.selectedCreationType.collectAsStateWithLifecycle()
                val creationList = viewModel.creationConfig.collectAsStateWithLifecycle()

                ContentCreationView(
                    creationConfig = creationList.value,
                    selectedItem = selectedCreation.value,
                    onImpressBottomSheet = {
                        analytics?.eventImpressionContentCreationBottomSheet(
                            viewModel.authorType,
                            widgetSource
                        )
                    },
                    onSelectItem = {
                        viewModel.selectCreationItem(it)
                    },
                    onNextClicked = {},
                    onRetryClicked = {
                        viewModel.fetchConfig(widgetSource)
                    }
                )
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderHeaderView()
    }

    private fun renderHeaderView() {
        // TODO: custom header
        context?.let {
            setTitle(it.getString(R.string.content_creation_bottom_sheet_title))

            if (shouldShowPerformanceAction) {
                setAction(it.getString(R.string.content_creation_bottom_sheet_performance_action)) { _ ->
                    analytics?.eventClickPerformanceDashboard(
                        viewModel.authorType,
                        widgetSource
                    )
                    RouteManager.route(
                        it,
                        viewModel.getPerformanceDashboardApplink()
                    )
                }
            }
            setAction(it.getDrawable(com.tokopedia.resources.common.R.drawable.bg_animated_action_counter_plus_24)) { _ ->
                Log.d("hello", "helo")
            }

            viewModel.fetchConfig(widgetSource, creationConfig)
        }
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
