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
    private var mListener: Listener? = null

    private fun generateInAppLink(appLink: String): String {
        return getString(
            R.string.up_webview_template,
            ApplinkConst.WEBVIEW,
            appLink,
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
                    data = mData,
                    onButtonClick = {
                        mListener?.onButtonActionPressed()
                        dismiss()
                    },
                    onActionTextPressed = { appLink ->
                        router.route(
                            requireContext(),
                            generateInAppLink(appLink),
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

    fun setupListener(listener: Listener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (isAdded) return
        showNow(fragmentManager, TAG)
    }

    interface Listener {
        fun onButtonActionPressed()
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
