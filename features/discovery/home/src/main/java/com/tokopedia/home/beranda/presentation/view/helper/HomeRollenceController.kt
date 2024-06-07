package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.home_component.util.HomeComponentFeatureFlag
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.searchbar.navigation_component.util.SearchRollenceController

/**
 * Created by frenzel on 09/05/22.
 */
object HomeRollenceController {
    private const val EMPTY_VALUE = ""

    var rollenceLoadTime: String = ""
    var iconJumperValue: String = RollenceKey.ICON_JUMPER_DEFAULT
    var iconJumperSREValue: String = ""
    var shouldGlobalComponentRecomEnabled: Boolean = false
    var isMegaTabEnabled = false

    fun fetchHomeRollenceValue() {
        fetchLoadTimeRollenceValue()
        fetchHomeMegaTabRollenceValue()
        HomeComponentFeatureFlag.fetchMissionRollenceValue()
        SearchRollenceController.fetchInboxNotifTopNavValue()
    }

    @JvmStatic
    fun fetchIconJumperValue() {
        iconJumperValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.ICON_JUMPER,
            RollenceKey.ICON_JUMPER_DEFAULT
        )
    }

    private fun fetchLoadTimeRollenceValue() {
        rollenceLoadTime = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_LOAD_TIME_KEY,
                RollenceKey.HOME_LOAD_TIME_CONTROL
            )
        } catch (_: Exception) {
            EMPTY_VALUE
        }
    }

    private fun fetchHomeMegaTabRollenceValue() {
        // set the default value to exp variant so that users that are not included
        // in the experiment still get the new caching mechanism
        val megaTab = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_MEGATAB,
                ""
            )
        } catch (_: Exception) {
            ""
        }

        isMegaTabEnabled = megaTab.isNotEmpty()
    }

    fun shouldCombineInboxNotif(): Boolean {
        return SearchRollenceController.shouldCombineInboxNotif()
    }

    @JvmStatic
    fun isIconJumper(): Boolean {
        return iconJumperValue == RollenceKey.ICON_JUMPER_EXP
    }
}
