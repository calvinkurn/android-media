package com.ilhamsuaib.darkmodeconfig.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import com.ilhamsuaib.darkmodeconfig.model.UiMode
import com.ilhamsuaib.darkmodeconfig.view.screen.DarkModeIntroScreen
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

internal class DarkModeIntroBottomSheet : BottomSheetUnify() {

    private var setOnApplyConfig: ((UiMode) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity
        if (activity != null) {
            setChild(createBottomSheetContent(activity))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetTitle.gone()
    }

    fun show(fm: FragmentManager, onApplyConfig: (UiMode) -> Unit) {
        if (fm.isStateSaved || isAdded) return

        this.setOnApplyConfig = onApplyConfig
        show(fm, TAG)
    }

    private fun createBottomSheetContent(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            addView(ComposeView(context).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    NestTheme {
                        DarkModeIntroScreen(
                            onPrimaryClicked = {
                                applyFollowSystemSetting()
                            },
                            onSecondaryClicked = {
                                dismissBottomSheet()
                            }
                        )
                    }
                }
            })
        }
    }

    private fun applyFollowSystemSetting() {
        showToaster()
        setOnApplyConfig?.invoke(UiMode.FollowSystemSetting())
        dismissBottomSheet()
    }

    private fun showToaster() {
        val view = view ?: return
        Toaster.build(view.rootView, "")
    }

    private fun dismissBottomSheet() {
        dismiss()
    }

    companion object {
        private const val TAG = "DarkModeInfoBottomSheet"

        fun getInstance(fm: FragmentManager): DarkModeIntroBottomSheet {
            val fragment = (fm.findFragmentByTag(TAG) as? DarkModeIntroBottomSheet)
            return fragment ?: DarkModeIntroBottomSheet().apply {
                isDragable = true
                showKnob = true
                isHideable = true
                isSkipCollapseState = true
                showCloseIcon = false
                clearContentPadding = true
            }
        }
    }
}