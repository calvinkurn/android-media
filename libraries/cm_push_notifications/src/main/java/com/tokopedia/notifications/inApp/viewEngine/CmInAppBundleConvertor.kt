package com.tokopedia.notifications.inApp.viewEngine

import android.text.TextUtils
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.AmplificationCMInApp
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout
import com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.Payload
import com.tokopedia.notifications.model.PayloadExtra
import java.lang.Exception
import java.lang.StringBuilder

object CmInAppBundleConvertor {

    const val HOURS_24_IN_MILLIS = 24 * 60 * 60 * 1000L

    fun getCmInApp(remoteMessage: RemoteMessage): CMInApp? {
        return try {
            val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            val map = remoteMessage.data
            getCmInAppModel(gson, map)
        } catch (e: Exception) {
            null
        }
    }

    fun getCmInApp(amplificationCMInApp: AmplificationCMInApp): CMInApp? {
        return try {
            val cmInApp = CMInApp()

            amplificationCMInApp.id?.let {
                if (it == 0L)
                    return null
                cmInApp.id = it
            } ?: run {
                return null
            }

            if (amplificationCMInApp.parentId != null)
                cmInApp.setParentId(amplificationCMInApp.parentId)

            if (amplificationCMInApp.campaignId != null)
                cmInApp.setCampaignId(amplificationCMInApp.campaignId)

            if (!TextUtils.isEmpty(amplificationCMInApp.campaignCode))
                cmInApp.setCampaignCode(amplificationCMInApp.campaignCode)

            if (!TextUtils.isEmpty(amplificationCMInApp.campaignUserToken))
                cmInApp.setCampaignUserToken(amplificationCMInApp.campaignUserToken)

            setStartTime(cmInApp, amplificationCMInApp.startTime)

            setEndTime(cmInApp, amplificationCMInApp.endTime)

            cmInApp.setFreq(amplificationCMInApp.freq ?: 1)

            cmInApp.isCancelable = amplificationCMInApp.isCancelable ?: false

            cmInApp.isTest = amplificationCMInApp.isTest ?: false

            cmInApp.isPersistentToggle = amplificationCMInApp.isPersistentToggle ?: true

            cmInApp.setType(amplificationCMInApp.type ?: "")

            cmInApp.customValues = amplificationCMInApp.customValues ?: ""

            val screenNameIsPresent =
                amplificationCMInApp.screen != null || amplificationCMInApp.ss != null

            if (!screenNameIsPresent) {
                return null
            }

            setScreenNames(
                cmInApp,
                amplificationCMInApp.screen,
                amplificationCMInApp.ss
            )


            if (amplificationCMInApp.cmLayout == null) {
                return null
            }

            val cmLayout = amplificationCMInApp.cmLayout
            cmInApp.setCmLayout(cmLayout)

            cmInApp.shopId = amplificationCMInApp.shopId

            cmInApp.payloadExtra = getPayloadExtra(amplificationCMInApp)

            return cmInApp
        } catch (e: Exception) {
            null
        }
    }

    private fun getCmInAppModel(gson: Gson, map: Map<String, String>): CMInApp? {

        val cmInApp = CMInApp()
        if (!map.containsKey(Payload.NOTIFICATION_ID)) return null

        cmInApp.setId(getLongFromStr(map[Payload.NOTIFICATION_ID]))

        if (map.containsKey(Payload.CAMPAIGN_ID))
            cmInApp.setCampaignId(map[Payload.CAMPAIGN_ID])

        if (map.containsKey(Payload.CAMPAIGN_CODE))
            cmInApp.setCampaignCode(map[Payload.CAMPAIGN_CODE])

        if (map.containsKey(Payload.CAMPAIGN_USER_TOKEN))
            cmInApp.setCampaignUserToken(map[Payload.CAMPAIGN_USER_TOKEN])

        if (map.containsKey(Payload.PARENT_ID))
            cmInApp.setParentId(map[Payload.PARENT_ID])

        setStartTime(cmInApp,getLongFromStr(map[Payload.START_TIME]))

        setEndTime(cmInApp, getLongFromStr(map[Payload.END_TIME]))

        if (map.containsKey(Payload.FREQUENCY))
            cmInApp.setFreq(getIntFromStr(map[Payload.FREQUENCY]))


        if (map.containsKey(Payload.CANCELLABLE))
            cmInApp.isCancelable = getBooleanFromString(map[Payload.CANCELLABLE])

        if (map.containsKey(Payload.IS_TEST))
            cmInApp.isTest = getBooleanFromString(map[Payload.IS_TEST])

        if (map.containsKey(Payload.PERST_ON))
            cmInApp.isPersistentToggle = getBooleanFromString(map[Payload.PERST_ON])

        if (map.containsKey(Payload.NOTIFICATION_TYPE))
            cmInApp.setType(map[Payload.NOTIFICATION_TYPE])

        if (map.containsKey(Payload.CUSTOM_VALUES))
            cmInApp.customValues = map[Payload.CUSTOM_VALUES]

        val screenNameIsPresent = map.containsKey(Payload.SCREEN_NAME) ||
                map.containsKey(Payload.MULTIPLE_SCREEN_NAME)
        if (!screenNameIsPresent) {
            return null
        }

        setScreenNames(cmInApp, map[Payload.SCREEN_NAME], map[Payload.MULTIPLE_SCREEN_NAME])

        if (!map.containsKey(Payload.UI)) {
            return null
        }

        val ui = map[Payload.UI]
        val cmLayout = gson.fromJson(ui, CMLayout::class.java)
        cmInApp.setCmLayout(cmLayout)

        if (map.containsKey(Payload.SHOP_ID)) cmInApp.shopId = map[Payload.SHOP_ID]

        cmInApp.payloadExtra = getPayloadExtra(map)

        return cmInApp
    }

    private fun getPayloadExtra(map: Map<String, String>): PayloadExtra {
        return PayloadExtra(
            sessionId = map[CMConstant.PayloadKeys.PayloadExtraDataKey.SESSION_ID],
            campaignName = map[CMConstant.PayloadKeys.PayloadExtraDataKey.CAMPAIGN_NAME],
            journeyId = map[CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_ID],
            journeyName = map[CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_NAME],
        )
    }

    private fun getPayloadExtra(amplificationCMInApp: AmplificationCMInApp): PayloadExtra {
        return PayloadExtra(
            sessionId = amplificationCMInApp.sessionId,
            campaignName = amplificationCMInApp.campaignName,
            journeyId = amplificationCMInApp.journeyId,
            journeyName = amplificationCMInApp.journeyName,
        )
    }

    private fun getBooleanFromString(strBoolean: String?): Boolean {
        return try {
            strBoolean?.toBoolean() ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun getIntFromStr(strInt: String?): Int {
        return try {
            strInt?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun getLongFromStr(strLong: String?): Long {
        return try {
            strLong?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun setStartTime(cmInApp: CMInApp, startTime: Long?) {
        if (startTime == null || startTime == 0L)
            cmInApp.startTime = System.currentTimeMillis()
        else
            cmInApp.startTime = startTime
    }

    private fun setEndTime(cmInApp: CMInApp, endTime: Long?) {
        if (endTime == null || endTime == 0L)
            cmInApp.endTime = System.currentTimeMillis() +
                    CMInAppManager.getInstance().cmInAppEndTimeInterval
        else
            cmInApp.endTime = endTime
    }

    private fun setScreenNames(cmInApp: CMInApp, screenName: String?, multiScreenName: String?) {
        var tempScreenName: String? = ""
        if (screenName != null) {
            tempScreenName = screenName
            cmInApp.setScreen(tempScreenName)
        }

        if (multiScreenName != null) {
            if (!TextUtils.isEmpty(multiScreenName)) {
                val sb = StringBuilder(multiScreenName)
                if (!TextUtils.isEmpty(tempScreenName)) {
                    sb.append(",")
                    sb.append(tempScreenName)
                }
                cmInApp.setScreen(sb.toString())
            }
        }
    }

}