package com.tokopedia.play.broadcaster.view.bottomsheet.livetovod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class PlayBroLiveToVodBottomSheet @Inject constructor(
    private val router: Router,
) : BottomSheetUnify() {

    private var mData: TickerBottomSheetUiModel = TickerBottomSheetUiModel.Empty

    private fun generateLearnMoreAppLink(): String {
        return getString(
            R.string.up_webview_template,
            ApplinkConst.WEBVIEW,
            getString(com.tokopedia.content.common.R.string.ugc_get_to_know_more_link),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setChild(ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PlayBroadcasterLiveToVodBottomSheetScreen(
                    onBackPressed = { dismiss() },
                    onLearnMorePressed = {
                        router.route(
                            requireContext(),
                            generateLearnMoreAppLink(),
                        )
                    },
                )
            }
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setupData(data: TickerBottomSheetUiModel) {
        mData = data
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "PlayBroLiveToVodBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroLiveToVodBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroLiveToVodBottomSheet
            return oldInstance ?: (fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroLiveToVodBottomSheet::class.java.name,
            ) as PlayBroLiveToVodBottomSheet)
        }
    }
}
