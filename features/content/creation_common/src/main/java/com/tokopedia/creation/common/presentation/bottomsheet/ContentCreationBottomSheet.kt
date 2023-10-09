package com.tokopedia.creation.common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.di.ContentCreationComponent
import com.tokopedia.creation.common.di.DaggerContentCreationComponent
import com.tokopedia.creation.common.presentation.components.ContentCreationComponent
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.viewmodel.ContentCreationViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle

/**
 * Created By : Muhammad Furqan on 06/09/23
 */
class ContentCreationBottomSheet : BottomSheetUnify() {

    private var factory: ViewModelProvider.Factory? = null
    private var viewModel: ContentCreationViewModel? = null

    @StringRes
    private var title: Int = R.string.content_creation_bottom_sheet_title

    var shouldShowPerformanceAction: Boolean = false
    var listener: ContentCreationBottomSheetListener? = null
    private var creationConfig: ContentCreationConfigModel = ContentCreationConfigModel.Empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = createComponent().contentCreationFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isDragable = true
        isHideable = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val selectedCreation =
                    viewModel?.selectedCreationType?.collectAsStateWithLifecycle()
                val creationList = viewModel?.creationConfig?.collectAsStateWithLifecycle()

                ContentCreationComponent(
                    creationConfig = creationList?.value,
                    selectedItem = selectedCreation?.value,
                    onSelectItem = {
                        viewModel?.selectCreationItem(it)
                        listener?.onCreationItemSelected(it)
                    },
                    onNextClicked = {
                        selectedCreation?.value?.let {
                            listener?.onCreationNextClicked(it)
                            RouteManager.route(context, it.applink)
                        }
                    },
                    onRetryClicked = {
                        viewModel?.fetchConfig()
                    }
                )
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            if (factory == null) {
                factory = createComponent().contentCreationFactory()
            }

            viewModel = ViewModelProvider(it, factory!!)[ContentCreationViewModel::class.java]
        }

        renderHeaderView()
    }

    private fun renderHeaderView() {
        context?.let {
            setTitle(it.getString(title))

            if (shouldShowPerformanceAction) {
                setAction(it.getString(R.string.content_creation_bottom_sheet_performance_action)) { _ ->
                    listener?.trackViewPerformanceClicked()
                    RouteManager.route(
                        it,
                        viewModel?.getPerformanceDashboardApplink()
                    )
                }
            }

            viewModel?.fetchConfig(creationConfig)
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        @StringRes title: Int? = null,
        showPerformanceAction: Boolean = false,
        creationConfig: ContentCreationConfigModel = ContentCreationConfigModel.Empty
    ) {
        title?.let {
            this.title = it
        }
        this.shouldShowPerformanceAction = showPerformanceAction
        this.creationConfig = creationConfig

        if (!isAdded) show(fragmentManager, TAG)
    }

    private fun createComponent(): ContentCreationComponent =
        DaggerContentCreationComponent.builder()
            .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()

    interface ContentCreationBottomSheetListener {
        fun onCreationItemSelected(data: ContentCreationItemModel)
        fun onCreationNextClicked(data: ContentCreationItemModel)
        fun trackViewPerformanceClicked()
    }

    companion object {
        private const val TAG = "ContentCreationBottomSheet"

        fun getFragment(
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
