package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.home_component.util.HomeComponentFeatureFlag
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by frenzel on 09/05/22.
 */
object HomeRollenceController {
    private const val EMPTY_VALUE = ""

    var rollenceAtfValue: String = ""
    var rollenceLoadTime: String = ""
    var rollenceLoadAtfCache: String = RollenceKey.HOME_LOAD_ATF_CACHE_ROLLENCE_CONTROL
    var iconJumperValue: String = RollenceKey.ICON_JUMPER_DEFAULT
    var shouldGlobalComponentRecomEnabled: Boolean = false
    var iconJumperSREValue: String = ""
    var isMegaTabEnabled = false

    fun fetchHomeRollenceValue() {
        fetchAtfRollenceValue()
        fetchLoadTimeRollenceValue()
        fetchAtfCacheRollenceValue()
        fetchHomeMegaTabRollenceValue()
        HomeComponentFeatureFlag.fetchMissionRollenceValue()
    }

    @JvmStatic
    fun fetchIconJumperValue() {
        iconJumperValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.ICON_JUMPER,
            RollenceKey.ICON_JUMPER_DEFAULT
        )
        iconJumperSREValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.ICON_JUMPER_SRE_KEY)
    }

    private fun fetchAtfRollenceValue() {
        rollenceAtfValue = try {
            val rollenceAtf = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.HOME_COMPONENT_ATF)
            if (rollenceAtf == RollenceKey.HOME_COMPONENT_ATF_3) {
                rollenceAtf
            } else {
                RollenceKey.HOME_COMPONENT_ATF_2
            }
        } catch (_: Exception) {
            EMPTY_VALUE
        }
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

    private fun fetchAtfCacheRollenceValue() {
        // set the default value to exp variant so that users that are not included
        // in the experiment still get the new caching mechanism
        rollenceLoadAtfCache = try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.HOME_LOAD_ATF_CACHE_ROLLENCE_KEY,
                RollenceKey.HOME_LOAD_ATF_CACHE_ROLLENCE_EXP
            )
        } catch (_: Exception) {
            RollenceKey.HOME_LOAD_ATF_CACHE_ROLLENCE_EXP
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

    fun isLoadAtfFromCache(): Boolean {
        return rollenceLoadAtfCache == RollenceKey.HOME_LOAD_ATF_CACHE_ROLLENCE_EXP
    }

    fun isUsingAtf3Variant(forceAtf3: Boolean): Boolean {
        return forceAtf3 || rollenceAtfValue == RollenceKey.HOME_COMPONENT_ATF_3
    }

    fun getAtfRollence(forceAtf3: Boolean): String {
        return if (forceAtf3) {
            RollenceKey.HOME_COMPONENT_ATF_3
        } else {
            rollenceAtfValue
        }
    }

    @JvmStatic
    fun isIconJumper(): Boolean {
        return iconJumperValue == RollenceKey.ICON_JUMPER_EXP ||
            iconJumperSREValue == RollenceKey.ICON_JUMPER_SRE_VALUE
    }

    @JvmStatic
    fun isIconJumperSRE(): Boolean {
        return iconJumperSREValue == RollenceKey.ICON_JUMPER_SRE_VALUE
    }
}
