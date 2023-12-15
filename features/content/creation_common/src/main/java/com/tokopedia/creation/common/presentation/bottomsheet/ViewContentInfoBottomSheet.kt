package com.tokopedia.creation.common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.creation.common.presentation.screen.ContentInfoScreen
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.upload.uploader.activity.ContentCreationPostUploadActivity

/**
 * Created By : Jonathan Darwin on November 27, 2023
 */
class ViewContentInfoBottomSheet : BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isDragable = true
        isHideable = true
        isSkipCollapseState = true
        showCloseIcon = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                ContentInfoScreen(
                    imageUrl = stringResource(R.string.img_content_creation_download_app),
                    title = stringResource(R.string.content_creation_check_content_in_tokopedia_feed_title),
                    subtitle = stringResource(R.string.content_creation_check_content_in_tokopedia_feed_desc),
                    primaryText = stringResource(R.string.content_creation_check_content_in_tokopedia_feed_action_text),
                    secondaryText = "",
                    onPrimaryButtonClicked = {
                        val intent = ContentCreationPostUploadActivity.getIntent(
                            context = requireContext(),
                            channelId = "",
                            authorId = "",
                            authorType = "",
                            uploadType = "",
                            appLink = ApplinkConst.FEED
                        )

                        requireActivity().startActivity(intent)

                        dismiss()
                    },
                    onSecondaryButtonClicked = {  }
                )
            }
        }
        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "ViewContentInfoBottomSheet"

        fun getOrCreateFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ViewContentInfoBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? ViewContentInfoBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ViewContentInfoBottomSheet::class.java.name
            ) as ViewContentInfoBottomSheet
        }
    }
}
