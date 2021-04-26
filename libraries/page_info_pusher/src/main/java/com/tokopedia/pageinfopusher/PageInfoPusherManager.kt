 pupackage com.tokopedia.pageinfopusher

import android.app.Activity
import android.util.Log
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.track.TrackApp
import org.json.JSONObject
import timber.log.Timber

class PageInfoPusherManager(val activity: Activity) {

    val MAINAPP_GENERAL_INFO = "android_mainapp_general_info"
    val SELLERAPP_GENERAL_INFO = "android_sellerapp_general_info"
    val PRO_GENERAL_INFO = "android_pro_general_info"

    val EVENT = "event"
    val EVENT_CATEGORY = "eventCategory"
    val EVENT_ACTION = "eventAction"
    val EVENT_LABEL = "eventLabel"
    val USER_ID = "userId"

    val remoteConfig : RemoteConfig

    init {
        remoteConfig = FirebaseRemoteConfigImpl(activity)
    }

    fun showGeneralInfoMessage() {
        try {
            val rawConfig = if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
                remoteConfig.getString(SELLERAPP_GENERAL_INFO)
            } else if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION) {
                remoteConfig.getString(MAINAPP_GENERAL_INFO)
            } else if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_PRO_APPLICATION) {
                remoteConfig.getString(PRO_GENERAL_INFO)
            } else { "" }

            if (rawConfig.isNullOrBlank()) return

            val configList: org.json.JSONArray = org.json.JSONArray(rawConfig)
            for (i in 0 until configList.length()) {
                val config: JSONObject? = configList.optJSONObject(i)
                config?.let { handleConfig(config) }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    private fun handleConfig(config: JSONObject) {
        try {
            val className: String? = activity.javaClass.canonicalName
            val id: String = config.optString("id")
            val isHitGa: Boolean = config.optBoolean("hit_to_ga")
            val isHitTimber: Boolean = config.optBoolean("hit_to_timber")
            val pages: String = config.optString("pages")
            val environment: String = config.optString("environment")
            val minAppVer: String = config.optString("min_app_ver_code")
            val maxAppVer: String = config.optString("max_app_ver_code")
            val manufacturers: String = config.optString("device_manufacturers")
            val models: String = config.optString("device_models")
            val minOsVer: String = config.optString("min_os_ver")
            val maxOsVer: String = config.optString("max_os_ver")
            val title: String = config.optString("title")
            val buttonText: String = config.optString("button_text")
            val actionApplink: String = config.optString("action_applink")
            val message: String = config.optString("message")

            if (!isEligibleForGeneralInfo(pages, className)) return
            if (!isEligibleForGeneralInfo(manufacturers, android.os.Build.MANUFACTURER)) return
            if (!isEligibleForGeneralInfo(models, android.os.Build.MODEL)) return

            if (!isRangeEligibleForGeneralInfo(minRange = minAppVer, maxRange = maxAppVer, value = GlobalConfig.VERSION_CODE)) return
            if (!isRangeEligibleForGeneralInfo(minRange = minOsVer, maxRange = maxOsVer, value = android.os.Build.VERSION.SDK_INT)) return

            if ("all" != environment && GlobalConfig.isAllowDebuggingTools() && "dev" != environment) return
            if ("all" != environment && !GlobalConfig.isAllowDebuggingTools() && "prod" != environment) return

            showPopUp(message = message, title = title, buttonText = buttonText, actionApplink =  actionApplink)

            if (isHitTimber) {
                Timber.w("P1#DISPLAY_GENERAL_INFO#'" + className
                        + "';id='" + id
                        + "';dev='" + GlobalConfig.isAllowDebuggingTools() + "'")
            }

            if (isHitGa) {
                eventDisplayGeneralInfo(id)
            }

        } catch (e: java.lang.Exception) {
        }
    }

    private fun eventDisplayGeneralInfo(infoId: String?) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, "viewGeneralInfo",
                        EVENT_CATEGORY, "android - tools",
                        EVENT_ACTION, "general info - impression",
                        EVENT_LABEL, "$infoId"
                )
        )
    }

    private fun isEligibleForGeneralInfo(requirements: String, value: String?): Boolean {
        if (requirements.isBlank()) return false

        val requirementList = requirements.split("\\s*,\\s*").map { s -> s.trim() }
        return ("all" == requirementList[0] || requirementList.contains(value))
    }

    private fun isRangeEligibleForGeneralInfo(minRange: String, maxRange: String, value: Int): Boolean {
        if (minRange == "all" || maxRange == "all") return true

        if (minRange.isBlank() || maxRange.isBlank()) return false

        val min = minRange.toIntOrNull()
        val max = maxRange.toIntOrNull()

        if (min == null || max == null) return false

        return (min <= value && value <= max)
    }

    private fun showPopUp(message: String, title: String, buttonText: String, actionApplink: String) {
        DialogUnify(context = activity,
                actionType = DialogUnify.SINGLE_ACTION,
                imageType = DialogUnify.NO_IMAGE).apply {
            setTitle(title)
            setDescription(message)
            setPrimaryCTAText(buttonText)
            setPrimaryCTAClickListener {
                this.dismiss()
            }
            setOnDismissListener {
                if (actionApplink == ApplinkConst.GeneralInfo.GENERAL_INFO_FORCE_CLOSE_PAGE) {
                    activity.finish()
                } else if (actionApplink.isNotBlank()) {
                    RouteManager.route(activity, actionApplink)
                    activity.finish()
                }
            }
            setOverlayClose(false)
            show()
        }
    }
}
