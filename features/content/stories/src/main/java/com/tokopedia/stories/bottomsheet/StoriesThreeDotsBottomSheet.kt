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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.stories.view.model.BottomSheetType
import com.tokopedia.stories.view.model.StoriesUiState
import com.tokopedia.stories.view.showDialog
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.content.common.R as commonR
import com.tokopedia.stories.R as storiesR

/**
 * @author by astidhiyaa on 08/08/23
 */
class StoriesThreeDotsBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : BottomSheetUnify() {

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.uiState.collectAsState(initial = StoriesUiState.Empty)
                val ctx = LocalContext.current
                LaunchedEffect(Unit) {
                    lifecycleScope.launchWhenStarted {
                        viewModel.uiEvent.collectLatest { event ->
                            when (event) {
                                StoriesUiEvent.ShowDeleteDialog -> {
                                    dismiss()
                                    ctx.showDialog(
                                        title = getString(storiesR.string.stories_delete_story_title),
                                        description = getString(storiesR.string.stories_delete_story_description),
                                        primaryCTAText = getString(commonR.string.card_dialog_title_delete),
                                        secondaryCTAText = getString(commonR.string.card_dialog_title_cancel),
                                        primaryAction = {
                                            viewModel.submitAction(StoriesUiAction.DeleteStory)
                                        }
                                    )
                                }

                                else -> {}
                            }
                        }
                    }
                }
                ThreeDotsPage(menuList = state.storiesDetail.detailItems[state.storiesDetail.selectedDetailPosition].menus, onDeleteStoryClicked = { item ->
                    viewModel.submitAction(StoriesUiAction.ShowDeleteDialog)
                })
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fg: FragmentManager) {
        if (isAdded) return
        super.show(fg, TAG)
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
