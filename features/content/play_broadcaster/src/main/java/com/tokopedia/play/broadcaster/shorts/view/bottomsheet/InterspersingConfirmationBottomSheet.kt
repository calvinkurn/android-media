package com.tokopedia.play.broadcaster.shorts.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.shorts.view.compose.InterspersingConfirmationLayout
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
class InterspersingConfirmationBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null

    var data: Data = Data.Empty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet() {
        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                NestTheme(isOverrideStatusBarColor = false) {
                    Surface {
                        InterspersingConfirmationLayout(
                            newCoverUrl = data.newCoverUrl,
                            oldCoverUrl = data.oldCoverUrl,
                            onClickBack = {
                                dismiss()
                            },
                            onClickNext = {
                                listener?.clickNext()
                                dismiss()
                            },
                            onClickPdpVideo = {
                                listener?.clickPdpVideo()
                            }
                        )
                    }
                }
            }
        }
        setChild(composeView)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    data class Data(
        val newCoverUrl: String,
        val oldCoverUrl: String,
    ) {
        companion object {
            val Empty: Data
                get() = Data(
                    newCoverUrl = "",
                    oldCoverUrl = "",
                )
        }
    }

    interface Listener {
        fun clickPdpVideo()

        fun clickNext()
    }

    companion object {
        private const val TAG = "InterspersingConfirmationBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): InterspersingConfirmationBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? InterspersingConfirmationBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                InterspersingConfirmationBottomSheet::class.java.name
            ) as InterspersingConfirmationBottomSheet
        }
    }
}
