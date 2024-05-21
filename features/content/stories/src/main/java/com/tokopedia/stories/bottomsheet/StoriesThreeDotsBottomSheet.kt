package com.tokopedia.stories.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.showDialog
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.content.common.report_content.ThreeDotsPage
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType
import com.tokopedia.stories.view.viewmodel.state.StoriesUiState
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.stories.R as storiesR

/**
 * @author by astidhiyaa on 08/08/23
 */
class StoriesThreeDotsBottomSheet @Inject constructor() : BottomSheetUnify() {

    private var mListener: Listener? = null

    private val viewModel by activityViewModels<StoriesViewModel> { (requireParentFragment() as StoriesDetailFragment).viewModelProvider }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.storiesState.collectAsState(initial = StoriesUiState.Empty)
                val ctx = LocalContext.current

                LaunchedEffect(Unit) {
                    lifecycleScope.launchWhenStarted {
                        viewModel.storiesEvent.collectLatest { event ->
                            when (event) {
                                StoriesUiEvent.ShowDeleteDialog -> {
                                    ctx.showDialog(
                                        title = getString(storiesR.string.stories_delete_story_title),
                                        description = getString(storiesR.string.stories_delete_story_description),
                                        primaryCTAText = getString(contentcommonR.string.card_dialog_title_delete),
                                        secondaryCTAText = getString(contentcommonR.string.card_dialog_title_cancel),
                                        primaryAction = {
                                            viewModel.submitAction(StoriesUiAction.DeleteStory)
                                            dismiss()
                                        }
                                    )
                                }

                                else -> {}
                            }
                        }
                    }
                }

                val selectedGroup =
                    state.storiesMainData.groupItems.getOrNull(state.storiesMainData.selectedGroupPosition)
                        ?: return@setContent
                val currentItem =
                    selectedGroup.detail.detailItems.getOrNull(selectedGroup.detail.selectedDetailPosition)?.menus
                        ?: return@setContent


               ThreeDotsPage(menuList = currentItem, onMenuClicked = ::onMenuClicked)
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        if (!isAdded) return
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Kebab))
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.submitAction(StoriesUiAction.DismissSheet(BottomSheetType.Kebab))
        super.onCancel(dialog)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mListener = null
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, TAG)
    }

    private fun onMenuClicked(item: ContentMenuItem) {
        when (item.type) {
            ContentMenuIdentifier.Delete -> {
                mListener?.onRemoveStory(this@StoriesThreeDotsBottomSheet)
                viewModel.submitAction(StoriesUiAction.ShowDeleteDialog)
            }

            ContentMenuIdentifier.Report -> {
                mListener?.onReportStoryClicked(this)
            }

            ContentMenuIdentifier.SeePerformance -> {
                mListener?.onSeePerformance(this)
            }

            else -> {}
        }
    }

    interface Listener {
        fun onReportStoryClicked(view: StoriesThreeDotsBottomSheet)
        fun onRemoveStory(view: StoriesThreeDotsBottomSheet)
        fun onSeePerformance(view: StoriesThreeDotsBottomSheet)
    }

    companion object {
        const val TAG = "StoriesThreeDotsBottomSheet"

        fun get(fragmentManager: FragmentManager): StoriesThreeDotsBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? StoriesThreeDotsBottomSheet
        }

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesThreeDotsBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesThreeDotsBottomSheet::class.java.name
            ) as StoriesThreeDotsBottomSheet
        }
    }
}
