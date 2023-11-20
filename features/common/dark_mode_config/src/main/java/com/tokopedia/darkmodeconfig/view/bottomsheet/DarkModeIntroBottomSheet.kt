package com.tokopedia.darkmodeconfig.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.darkmodeconfig.common.DarkModeAnalytics
import com.tokopedia.darkmodeconfig.common.Utils
import com.tokopedia.darkmodeconfig.model.UiMode
import com.tokopedia.darkmodeconfig.view.screen.DarkModeIntroScreen
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

internal class DarkModeIntroBottomSheet : BottomSheetUnify() {

    private var setOnApplyConfig: (() -> Unit)? = null

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

    fun show(fm: FragmentManager, onApplyConfig: () -> Unit) {
        if (fm.isStateSaved || isAdded) return

        this.setOnApplyConfig = onApplyConfig
        show(fm, TAG)
    }

    private fun createBottomSheetContent(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            addView(
                ComposeView(context).apply {
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
                }
            )
        }
    }

    private fun applyFollowSystemSetting() {
        dismissBottomSheet()
        setOnApplyConfig?.invoke()
        applyAppTheme()
    }

    private fun applyAppTheme() {
        val mode = UiMode.FollowSystemSetting()
        sendAnalytics(mode)
        saveDarkModeState(mode)
        AppCompatDelegate.setDefaultNightMode(mode.screenMode)
    }

    private fun saveDarkModeState(mode: UiMode) {
        val applicationContext = activity?.applicationContext ?: return
        val editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
        val isDarkModeOs = applicationContext.isDarkMode()
        activity?.lifecycleScope?.launch(Dispatchers.Default) {
            editor.putInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, mode.screenMode)
            editor.putBoolean(
                TkpdCache.Key.KEY_DARK_MODE,
                Utils.getIsDarkModeStatus(mode, isDarkModeOs)
            )
            editor.apply()
        }
    }

    private fun sendAnalytics(mode: UiMode) {
        val context = activity?.applicationContext ?: return
        val isDarkModeOS = context.isDarkMode()
        DarkModeAnalytics.eventClickThemeSetting(mode, isDarkModeOS)
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
