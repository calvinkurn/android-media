package com.tokopedia.play.broadcaster.shorts.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.play.broadcaster.shorts.ui.model.ShortsCoverState
import com.tokopedia.play.broadcaster.shorts.view.compose.InterspersingConfirmationLayout
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
class InterspersingConfirmationBottomSheet @Inject constructor(
    private val videoSnapshotHelper: VideoSnapshotHelper,
) : BottomSheetUnify() {

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

    override fun onDestroyView() {
        super.onDestroyView()
        videoSnapshotHelper.deleteLocalFile()
    }

    private fun setupBottomSheet() {
        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
        setCloseClickListener {
            listener?.clickClose()
            dismiss()
        }

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {

                var newCoverState: ShortsCoverState by remember { mutableStateOf(ShortsCoverState.Unknown) }

                if (data.needSnapNewCover) {
                    LaunchedEffect(Unit) {
                        newCoverState = ShortsCoverState.Loading
                        val filePath = videoSnapshotHelper.snapVideo(requireContext(), data.newCoverUri)
                        newCoverState = if (filePath != null) {
                            ShortsCoverState.Success(filePath.absolutePath)
                        } else {
                            ShortsCoverState.Error
                        }
                    }
                }

                NestTheme(isOverrideStatusBarColor = false) {
                    Surface {
                        InterspersingConfirmationLayout(
                            newCoverState = newCoverState,
                            oldCoverState = ShortsCoverState.Success(data.oldCoverUri),
                            onClickBack = {
                                listener?.clickBack()
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
        val newCoverUri: String,
        val oldCoverUri: String,
        val needSnapNewCover: Boolean,
    ) {
        companion object {
            val Empty: Data
                get() = Data(
                    newCoverUri = "",
                    oldCoverUri = "",
                    needSnapNewCover = false,
                )
        }
    }

    interface Listener {
        fun clickClose()

        fun clickBack()

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
