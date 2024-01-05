package com.tokopedia.stories.creation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.creation.common.presentation.screen.ContentInfoScreen
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on September 20, 2023
 */
class StoriesCreationInfoBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null

    var info: Info = Info()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isDragable = false
        isSkipCollapseState = true
        isHideable = false
        isCancelable = false
        overlayClickDismiss = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        setCloseClickListener {
            listener?.onClose()
        }

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ContentInfoScreen(
                    imageUrl = info.imageUrl,
                    title = info.title,
                    subtitle = info.subtitle,
                    primaryText = info.primaryText,
                    secondaryText = info.secondaryText,
                    onPrimaryButtonClicked = {
                        listener?.onPrimaryButtonClick()
                    },
                    onSecondaryButtonClicked = {
                        listener?.onSecondaryButtonClick()
                    }
                )
            }
        }

        setChild(composeView)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    data class Info(
        val imageUrl: String = "",
        val title: String = "",
        val subtitle: String = "",
        val primaryText: String = "",
        val secondaryText: String = "",
    )

    interface Listener {

        fun onClose()

        fun onPrimaryButtonClick()

        fun onSecondaryButtonClick()
    }

    companion object {
        private const val TAG = "StoriesCreationInfoBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesCreationInfoBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? StoriesCreationInfoBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationInfoBottomSheet::class.java.name
            ) as StoriesCreationInfoBottomSheet
        }
    }
}
