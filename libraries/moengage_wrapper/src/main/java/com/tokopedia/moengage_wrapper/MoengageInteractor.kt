package com.tokopedia.moengage_wrapper

import android.app.Application
import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.moe.pushlibrary.MoEHelper
import com.moe.pushlibrary.PayloadBuilder
import com.moe.pushlibrary.utils.MoEHelperConstants
import com.moengage.core.MoEngage
import com.moengage.inapp.InAppManager
import com.moengage.push.PushManager
import com.moengage.pushbase.push.MoEPushCallBacks
import com.moengage.pushbase.push.MoEngageNotificationUtils
import com.tokopedia.moengage_wrapper.cache.MoengageWrapperCacheHandler
import com.tokopedia.moengage_wrapper.constants.Constants
import com.tokopedia.moengage_wrapper.interfaces.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.time.DateFormatUtils
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.*

object MoengageInteractor {
    private lateinit var userSessionInterface: UserSession
    private var isMoengageInitialised = false
    private lateinit var context: Application
    private val moengageWrapperCacheHandler: MoengageWrapperCacheHandler by lazy { MoengageWrapperCacheHandler(context) }
    var keyMoengage: String = ""
    var smallIcon = 0
    var largeIcon = 0

    fun initInteractor(context: Application, keyMoengage: String, @DrawableRes smallIcon: Int, @DrawableRes largeIcon: Int) {
        this.context = context
        this.keyMoengage = keyMoengage
        this.smallIcon = smallIcon
        this.largeIcon = largeIcon
    }

    fun initialiseMoengage(): Boolean {
        /*
          Mandatory to set small/Large notification icon while initialising sdk
          */
        if (!isMoengageInitialised && ::context.isInitialized && keyMoengage.isNotEmpty() && smallIcon != 0 && largeIcon != 0) {
            MoengageValidator().checkIfMoengageEnabled(context, {
                val moEngage = MoEngage.Builder(context,
                        keyMoengage)
                        .setNotificationSmallIcon(smallIcon)
                        .setNotificationLargeIcon(largeIcon)
                        .optOutTokenRegistration()
                        .build()
                MoEngage.initialise(moEngage)
                isMoengageInitialised = true
                sendTokenToServerIfNotSent()
            })

        }
        return isMoengageInitialised
    }

    private fun sendTokenToServerIfNotSent() {
        val tokenSent = moengageWrapperCacheHandler.getBoolean(Constants.SharedPreference.TOKEN_SENT)
        if (!::userSessionInterface.isInitialized)
            userSessionInterface = UserSession(context)
        if (!tokenSent)
            refreshToken(userSessionInterface.deviceId)
    }

    fun sendExistingUserAndInstallTrackingEvent(isLoggedIn: Boolean) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        val checkIfMoengageUserAttributesEnabled = moengageValidator.checkIfMoengageUserAttributesEnabled(context)
        if (checkIfMoengageUserAttributesEnabled) {
            MoEHelper.getInstance(context).setExistingUser(isLoggedIn)
        }
    }

    fun setUserData(value: Map<String?, Any?>) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        val checkIfMoengageUserAttributesEnabled = moengageValidator.checkIfMoengageUserAttributesEnabled(context)
        if (checkIfMoengageUserAttributesEnabled) {
            val helper = MoEHelper.getInstance(context)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_USER_NAME]))
                helper.setFullName((value[MoEHelperConstants.USER_ATTRIBUTE_USER_NAME] as String?)!!)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_USER_FIRST_NAME]))
                helper.setFirstName((value[MoEHelperConstants.USER_ATTRIBUTE_USER_FIRST_NAME] as String?)!!)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID]))
                helper.setUniqueId((value[MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID] as String?)!!)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL]))
                helper.setEmail((value[MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL] as String?)!!)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE])) {
                var number = value[MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE] as String?
                number = normalizePhoneNumber(number)
                helper.setNumber(number)
            }
            if (!TextUtils.isEmpty(value[MoEHelperConstants.USER_ATTRIBUTE_USER_BDAY] as String?)) {
                helper.setBirthDate((value[MoEHelperConstants.USER_ATTRIBUTE_USER_BDAY] as String?)!!)
            }
            if (checkNull(value[Constants.MOENGAGE.IS_GOLD_MERCHANT]))
                helper.setUserAttribute(Constants.MOENGAGE.IS_GOLD_MERCHANT, value[Constants.MOENGAGE.IS_GOLD_MERCHANT].toString())
            if (checkNull(value[Constants.MOENGAGE.SHOP_ID]))
                helper.setUserAttribute(Constants.MOENGAGE.SHOP_ID, (value[Constants.MOENGAGE.SHOP_ID] as String?)!!)
            if (checkNull(value[Constants.MOENGAGE.SHOP_NAME]))
                helper.setUserAttribute(Constants.MOENGAGE.SHOP_NAME, (value[Constants.MOENGAGE.SHOP_NAME] as String?)!!)
            if (checkNull(value[Constants.MOENGAGE.TOTAL_SOLD_ITEM]))
                helper.setUserAttribute(Constants.MOENGAGE.TOTAL_SOLD_ITEM, (value[Constants.MOENGAGE.TOTAL_SOLD_ITEM] as String?)!!)
            if (checkNull(value[Constants.MOENGAGE.TOPADS_AMT]))
                helper.setUserAttribute(Constants.MOENGAGE.TOPADS_AMT, (value[Constants.MOENGAGE.TOPADS_AMT] as String?)!!)
            if (checkNull(value[Constants.MOENGAGE.HAS_PURCHASED_MARKETPLACE]))
                helper.setUserAttribute(Constants.MOENGAGE.HAS_PURCHASED_MARKETPLACE, value[Constants.MOENGAGE.HAS_PURCHASED_MARKETPLACE] as Boolean)
            if (checkNull(value[Constants.MOENGAGE.LAST_TRANSACT_DATE]))
                helper.setUserAttribute(Constants.MOENGAGE.LAST_TRANSACT_DATE, (value[Constants.MOENGAGE.LAST_TRANSACT_DATE] as String?)!!)
            if (checkNull(value[Constants.MOENGAGE.SHOP_SCORE]))
                helper.setUserAttribute(Constants.MOENGAGE.SHOP_SCORE, (value[Constants.MOENGAGE.SHOP_SCORE] as String?)!!)
            if (checkNull(value[MoEHelperConstants.USER_ATTRIBUTE_USER_GENDER]))
                helper.setGender(if (value[MoEHelperConstants.USER_ATTRIBUTE_USER_GENDER] == "1") "male" else "female")

        }
    }

    fun setMoengageUserProfile(vararg customerWrapper: String) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {

            if (customerWrapper.size != 3) {
                return
            }
            val customerId = customerWrapper[0]
            val fullName = customerWrapper[1]
            val emailAddress = customerWrapper[2]
            Timber.d("MoEngage check user $customerId")

            val helper = MoEHelper.getInstance(context)
            helper.setFullName(fullName)
            helper.setUniqueId(customerId)
            helper.setEmail(emailAddress)
        }
    }

    fun setPushPreference(status: Boolean) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {
            MoEHelper.getInstance(context).setUserAttribute(Constants.EventMoEngage.PUSH_PREFERENCE, status)
        }
    }

    fun setNewsletterEmailPref(status: Boolean) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {
            MoEHelper.getInstance(context).setUserAttribute(Constants.EventMoEngage.EMAIL_PREFERENCE, status)
        }
    }


    fun setUserDataGraphQL(name: String, firstName: String, userId: String, email: String,
                           phoneNumber: String, bday: String, shopName: String, shopId: String,
                           totalSoldItem: String, topAdsAmt: String, lastTransactionDate: String,
                           gender: String, hasPurchasedMarketplace: Boolean) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {
            val value: MutableMap<String?, Any?> = HashMap()
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_NAME] = name
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_FIRST_NAME] = firstName
            value[MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID] = userId
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL] = email
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE] = normalizePhoneNumber(phoneNumber)
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_BDAY] = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, extractFirstSegment(bday, "T"))
            value[Constants.MOENGAGE.SHOP_ID] = shopId
            value[Constants.MOENGAGE.SHOP_NAME] = shopName
            value[Constants.MOENGAGE.TOTAL_SOLD_ITEM] = totalSoldItem
            value[Constants.MOENGAGE.TOPADS_AMT] = topAdsAmt
            value[Constants.MOENGAGE.HAS_PURCHASED_MARKETPLACE] = hasPurchasedMarketplace
            value[Constants.MOENGAGE.LAST_TRANSACT_DATE] = DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, extractFirstSegment(lastTransactionDate, "T"))
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_GENDER] = gender

            setUserData(value)
        }
    }


    private fun extractFirstSegment(inputString: String, separator: String): String {
        var firstSegment = ""
        if (!TextUtils.isEmpty(inputString)) {
            val token = inputString.split(separator.toRegex()).toTypedArray()
            firstSegment = if (token.size > 1) {
                token[0]
            } else {
                separator
            }
        }
        return firstSegment
    }


    fun setUserDataLogin(userId: String, name: String, email: String, phoneNumber: String, isGoldMerchant: Boolean, shopName: String, shopId: String) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {
            val value: MutableMap<String?, Any?> = HashMap()
            value[MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID] = userId
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_NAME] = name
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL] = email
            value[Constants.MOENGAGE.IS_GOLD_MERCHANT] = isGoldMerchant
            value[Constants.MOENGAGE.SHOP_NAME] = shopName
            value[Constants.MOENGAGE.SHOP_ID] = shopId
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE] = phoneNumber
            setUserData(value)
        }
    }

    fun setUserDataRegister(userId: String, name: String, email: String, phoneNumber: String, isGoldMerchant: Boolean, shopName: String, shopId: String) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageUserAttributesEnabled(context)) {
            val value: MutableMap<String?, Any?> = HashMap()
            value[MoEHelperConstants.USER_ATTRIBUTE_UNIQUE_ID] = userId
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_NAME] = name
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL] = email
            value[Constants.MOENGAGE.IS_GOLD_MERCHANT] = isGoldMerchant
            value[Constants.MOENGAGE.SHOP_NAME] = shopName
            value[Constants.MOENGAGE.SHOP_ID] = shopId
            value[MoEHelperConstants.USER_ATTRIBUTE_USER_MOBILE] = phoneNumber
            setUserData(value)
        }
    }


    fun sendTrackEvent(eventName: String, eventValue: Map<String, Any>) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageEventsEnabled(context)) {
            val builder = PayloadBuilder()
            for ((key, value) in eventValue.entries) {
                builder.putAttrString(key, value.toString())
            }
            trackEvent(builder.build(), eventName)
        }
    }

    fun sendTrackEventWithAppInfo(data: Map<String, Any>, eventName: String, appVersion: String) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        if (moengageValidator.checkIfMoengageEventsEnabled(context)) {
            val builder = PayloadBuilder()
            if (data.isEmpty()) {
                return
            }
            for ((key, value) in data.entries) {
                if (value is JSONArray) {
                    builder.putAttrJSONArray(key, value)
                } else {
                    builder.putAttrString(key, value.toString())
                }
            }
            builder.putAttrString(Constants.MOENGAGE.APP_VERSION, appVersion)
            builder.putAttrString(Constants.MOENGAGE.PLATFORM, "android")
            trackEvent(builder.build(), eventName)
        }
    }

    private fun trackEvent(data: JSONObject, eventName: String) {
        MoEHelper.getInstance(context).trackEvent(eventName, data)
    }

    private fun initialiseMoengageIfEnabled(moengageValidator: MoengageValidator) {
        moengageValidator.checkIfMoengageEnabled(context, {
            if (!isMoengageInitialised)
                initialiseMoengage()
        }, {
            if (isMoengageInitialised) {
                MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(null)
                PushManager.getInstance().setMessageListener(null)
                InAppManager.getInstance().setInAppListener(null)
            }
        })
    }

    private fun checkNull(o: Any?): Boolean {
        return when (o) {
            is String? -> !TextUtils.isEmpty(o)
            is Boolean? -> o != null
            else -> o != null
        }
    }

    private fun normalizePhoneNumber(phoneNum: String?): String? {
        return if (!TextUtils.isEmpty(phoneNum)) phoneNum?.replaceFirst("^0(?!$)".toRegex(), "62") else ""
    }

    fun logoutEvent() {
        MoEHelper.getInstance(context).logoutUser()
    }

    fun refreshToken(token: String?) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        moengageValidator.checkIfMoengageEnabled(context, {
            if (!token.isNullOrEmpty()) {
                PushManager.getInstance().refreshToken(context, token)
                moengageWrapperCacheHandler.putBoolean(Constants.SharedPreference.TOKEN_SENT, true)
            } else moengageWrapperCacheHandler.putBoolean(Constants.SharedPreference.TOKEN_SENT, false)
        })
    }

    fun handlePushPayload(token: Map<String, String>) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        moengageValidator.checkIfMoengageEnabled(context, {
            PushManager.getInstance().pushHandler?.handlePushPayload(context, token)
        })
    }

    fun setMessageListener(listener: CustomPushDataListener?) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        moengageValidator.checkIfMoengageEnabled(context, {
            PushManager.getInstance().setMessageListener(if (listener == null) null else CustomPushListener(listener))
        })
    }

    fun setInAppListener(listener: MoengageInAppListener?) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        moengageValidator.checkIfMoengageEnabled(context, {
            InAppManager.getInstance().setInAppListener(if (listener == null) null else MoengageInAppListenerImpl(listener, context))
        })
    }

    fun setPushListener(listener: MoengagePushListener?) {
        val moengageValidator = MoengageValidator()
        initialiseMoengageIfEnabled(moengageValidator)
        moengageValidator.checkIfMoengageEnabled(context, {
            MoEPushCallBacks.getInstance().setOnMoEPushNavigationAction(if (listener == null) null else MoengagePushListenerImpl(listener))
        })
    }

    fun isFromMoEngagePlatform(remoteMessageData: MutableMap<String, String>): Boolean {
        return MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessageData)
    }
}