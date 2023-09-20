package com.tokopedia.stories.creation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.creation.common.presentation.components.ContentCreationFailView
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on September 20, 2023
 */
class StoriesCreationErrorBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        showCloseIcon = false
        isDragable = true
        isSkipCollapseState = true
        isHideable = true
        isCancelable = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        setOnDismissListener {
            listener?.onDismiss()
        }

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ContentCreationFailView {
                    listener?.onRetry()
                }
            }
        }

        setChild(composeView)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager,) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    interface Listener {
        fun onRetry()

        fun onDismiss()
    }

    companion object {
        private const val TAG = "StoriesCreationErrorBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesCreationErrorBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? StoriesCreationErrorBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationErrorBottomSheet::class.java.name
            ) as StoriesCreationErrorBottomSheet
        }
    }
}
