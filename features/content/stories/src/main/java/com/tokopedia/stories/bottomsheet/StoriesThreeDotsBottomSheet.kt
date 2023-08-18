package com.tokopedia.stories.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.report_content.model.FeedMenuIdentifier
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.view.showDialog
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.stories.R as storiesR
import com.tokopedia.content.common.R as commonR

/**
 * @author by astidhiyaa on 08/08/23
 */
class StoriesThreeDotsBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                val ctx = LocalContext.current
                LaunchedEffect(Unit) {
                    //observe  event
                }
                ThreeDotsPage(menuList = List(1) {
                    FeedMenuItem(
                        iconUnify = IconUnify.DELETE,
                        name = storiesR.string.stories_delete_story_title,
                        type = FeedMenuIdentifier.Delete,
                    )
                }, onMenuClicked = { item ->
                    //TODO() move it to event - show
                    dismiss()
                    this.context.showDialog(
                        title = getString(storiesR.string.stories_delete_story_title),
                        description = getString(storiesR.string.stories_delete_story_description),
                        primaryCTAText = getString(commonR.string.card_dialog_title_delete),
                        secondaryCTAText = getString(commonR.string.card_dialog_title_cancel),
                        primaryAction = {
                            //TODO () hit gql
                        }
                    )
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
        mListener?.onDismissEvent(this)
        super.dismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        mListener?.onDismissEvent(this)
        super.onCancel(dialog)
    }

    override fun onDestroyView() {
        mListener = null
        super.onDestroyView()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onDismissEvent(view: StoriesThreeDotsBottomSheet)
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
