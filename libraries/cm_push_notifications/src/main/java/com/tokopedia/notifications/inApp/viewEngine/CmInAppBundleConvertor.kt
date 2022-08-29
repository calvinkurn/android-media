package com.tokopedia.notifications.inApp.viewEngine

import android.text.TextUtils
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.SerializedCMInAppData
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMLayout
import com.tokopedia.notifications.inApp.ruleEngine.RulesUtil.Constants.Payload
import com.tokopedia.notifications.model.PayloadExtra
import java.lang.Exception
import java.lang.StringBuilder

object CmInAppBundleConvertor {

    const val HOURS_24_IN_MILLIS = 24 * 60 * 60 * 1000L

    fun getCmInApp(serializedCMInAppData: SerializedCMInAppData): CMInApp? {
        return try {
            val cmInApp = CMInApp()

            if(serializedCMInAppData.id == null || serializedCMInAppData.id == 0L){
                return null
            }else {
                //already checked for null show !! is safe check...
                cmInApp.id = serializedCMInAppData.id!!
            }

            if (serializedCMInAppData.parentId != null)
                cmInApp.setParentId(serializedCMInAppData.parentId)

            if (serializedCMInAppData.campaignId != null)
                cmInApp.setCampaignId(serializedCMInAppData.campaignId)

            if (!TextUtils.isEmpty(serializedCMInAppData.campaignCode))
                cmInApp.setCampaignCode(serializedCMInAppData.campaignCode)

            if (!TextUtils.isEmpty(serializedCMInAppData.campaignUserToken))
                cmInApp.setCampaignUserToken(serializedCMInAppData.campaignUserToken)

            setStartTime(cmInApp, serializedCMInAppData.startTime)

            setEndTime(cmInApp, serializedCMInAppData.endTime)

            cmInApp.setFreq(serializedCMInAppData.freq ?: 0)

            cmInApp.isCancelable = serializedCMInAppData.isCancelable ?: false

            cmInApp.isTest = serializedCMInAppData.isTest ?: false

            cmInApp.isPersistentToggle = serializedCMInAppData.isPersistentToggle ?: true

            cmInApp.setType(serializedCMInAppData.type ?: "")

            cmInApp.customValues = serializedCMInAppData.customValues ?: ""

            val screenNameIsPresent =
                serializedCMInAppData.screen != null || serializedCMInAppData.ss != null

            if (!screenNameIsPresent) {
                return null
            }

            setScreenNames(
                cmInApp,
                serializedCMInAppData.screen,
                serializedCMInAppData.ss
            )


            if (serializedCMInAppData.cmLayout == null) {
                return null
            }

            val cmLayout = serializedCMInAppData.cmLayout
            cmInApp.setCmLayout(cmLayout)

            cmInApp.shopId = serializedCMInAppData.shopId

            cmInApp.payloadExtra = getPayloadExtra(serializedCMInAppData)

            return cmInApp
        } catch (e: Exception) {
            null
        }
    }

    fun getCmInAppModel(gson: Gson, map: Map<String, String>): CMInApp? {

        val cmInApp = CMInApp()
        if (!map.containsKey(Payload.NOTIFICATION_ID)) return null

        cmInApp.setId(getLongFromStr(map[Payload.NOTIFICATION_ID]))

        if (map.containsKey(Payload.PARENT_ID))
            cmInApp.setParentId(map[Payload.PARENT_ID])

        if (map.containsKey(Payload.CAMPAIGN_ID))
            cmInApp.setCampaignId(map[Payload.CAMPAIGN_ID])

        if (map.containsKey(Payload.CAMPAIGN_CODE))
            cmInApp.setCampaignCode(map[Payload.CAMPAIGN_CODE])

        if (map.containsKey(Payload.CAMPAIGN_USER_TOKEN))
            cmInApp.setCampaignUserToken(map[Payload.CAMPAIGN_USER_TOKEN])


        setStartTime(cmInApp, getLongFromStr(map[Payload.START_TIME]))

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
            campaignName = map[CMConstant.PayloadKeys.PayloadExtraDataKey.CAMPAIGN_NAME],
            journeyId = map[CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_ID],
            journeyName = map[CMConstant.PayloadKeys.PayloadExtraDataKey.JOURNEY_NAME],
            sessionId = map[CMConstant.PayloadKeys.PayloadExtraDataKey.SESSION_ID],
        )
    }

    private fun getPayloadExtra(serializedCMInAppData: SerializedCMInAppData): PayloadExtra {
        return PayloadExtra(
            campaignName = serializedCMInAppData.campaignName,
            journeyId = serializedCMInAppData.journeyId,
            journeyName = serializedCMInAppData.journeyName,
            sessionId = serializedCMInAppData.sessionId,
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
            strInt?.toIntOrZero() ?: 0
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