package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.view.compose.LiveStatsLayout
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 01 March 2024
 */
class PlayBroadcastLiveStatsBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
) : BottomSheetUnify() {

    private var mListener: Listener? = null

    private val parentViewModel by activityViewModels<PlayBroadcastViewModel> {
        parentViewModelFactoryCreator.create(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        clearContentPadding = true

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {

                val uiState by parentViewModel.uiState.collectAsStateWithLifecycle()
                NestTheme(
                    isOverrideStatusBarColor = false,
                ) {
                    Surface {
                        LiveStatsLayout(
                            liveStats = uiState.liveStatsList,
                            onEstimatedIncomeClicked = {
                                mListener?.onEstimatedIncomeClicked()
                                dismiss()
                            }
                        )
                    }
                }
            }
        }

        setChild(composeView)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    interface Listener {
        fun onEstimatedIncomeClicked()
    }

    companion object {
        private const val TAG = "PlayBroadcastLiveStatsBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroadcastLiveStatsBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroadcastLiveStatsBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroadcastLiveStatsBottomSheet::class.java.name
            ) as PlayBroadcastLiveStatsBottomSheet
        }
    }
}
