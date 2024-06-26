package com.tokopedia.akamai_bot_lib

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.akamai.botman.CYFMonitor
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

const val ERROR_CODE = 403
const val HEADER_AKAMAI_KEY = "server"
const val HEADER_AKAMAI_VALUE = "akamai"
const val ERROR_MESSAGE_AKAMAI =
    "Oops, ada kendala pada akunmu. Silakan coba kembali atau hubungi Tokopedia Care untuk bantuan lanjutan."

private val getAnyPattern =
    Pattern.compile("\\{.*?([a-zA-Z_][a-zA-Z0-9_\\s]+)((?=\\()|(?=\\{)).*(?=\\{)")
val getMutationPattern: Pattern = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()")

val map = ConcurrentHashMap<String, String>()

fun initAkamaiBotManager(app: Application?) {
    app?.let { CYFMonitor.initialize(it) }
}

fun getSensorData() = CYFMonitor.getSensorData()

val registeredGqlFunctions = mapOf(
    "login_token" to "login",
    "register" to "register",
    "login_token_v2" to "login",
    "register_v2" to "register",
    "OTPValidate" to "otp",
    "OTPRequest" to "otp",
    "richieSubmitWithdrawal" to "ttwdl",
    "pdpGetLayout" to "pdpGetLayout",
    "pdpGetData" to "pdpGetData",
    "pdpGetDetailBottomSheet" to "pdpGetDetailBottomSheet",
    "atcOCS" to "atconeclickshipment",
    "getPDPInfo" to "product_info",
    "shopInfoByID" to "shop_info",
    "followShop" to "followshop",
    "validate_use_promo_revamp" to "promorevamp",
    "crackResult" to "crackresult",
    "gamiCrack" to "gamicrack",
    "add_to_cart_occ_multi" to "atcoccmulti",
    "one_click_checkout" to "checkoutocc",
    "add_to_cart_v2" to "atc",
    "add_to_cart_bundle" to "atc",
    "checkout" to "checkout",
    "coupon_list_recommendation" to "clrecom",
    "hachikoRedeem" to "claimcoupon",
    "registerCheck" to "rgsc",
    "rechargeCheckVoucher" to "rcv",
    "playInteractiveUserTapSession" to "PlayTap",
    "ValidateInactivePhoneResponse" to "rgsc",
    "GetStatusInactivePhoneNumber" to "rgsc",
    "createAffiliateCookie" to "cac",
    "playInteractiveAnswerQuiz" to "piaq",
    "checkout_general_v2" to "cogn",
    "checkout_general_v2_instant" to "cogn",
    "PostATCLayout" to "PostATCLayout",
    "cart_general_add_to_cart" to "cagn",
    "cart_general_update_cart_quantity" to "cagn",
    "cart_general_cart_list" to "cagn",
    "cart_general_remove_cart" to "cagn",
    "cart_general_promo_list" to "cagn",
    "checkout_cart_general" to "cogn",
    "rechargeCheckoutV3" to "rcgco",
    "cart_general_add_to_cart_instant" to "cagn",
    )

fun getAkamaiQuery(query: String): String? {
    return getAkamaiQuery(getQueryListFromQueryString(query))
}

fun getAkamaiQuery(queryNameList: List<String>): String? {
    return queryNameList.asSequence()
        .filter {
            registeredGqlFunctions.containsKey(it)
        }.take(1).map {
            registeredGqlFunctions[it]
        }.firstOrNull()
}

fun getMutation(input: String, match: String): Boolean {
    val input2 = input.replace("\n", "")
    val input3 = input2.replace("\\s+", " ")
    val m: Matcher = getMutationPattern.matcher(input3)
    while (m.find()) {
        if (m.group(0).equals(match, ignoreCase = true)) {
            return true
        }
    }
    return false
}

const val KEY_AKAMAI_EXPIRED_TIME = "KEY_AKAMAI_EXPIRED_TIME"
const val KEY_VALUE_AKAMAI_EXPIRED_TIME = "KEY_AKAMAI_EXPIRED_TIME"
const val KEY_REAL_VALUE_AKAMAI = "KEY_REAL_VALUE_AKAMAI_EXPIRED_TIME"

const val sdValidTime = 10000

fun Context.setExpiredTime(expiredTime: Long) {
    val sharedPreferences = this.getSharedPreferences(
        KEY_AKAMAI_EXPIRED_TIME,
        MODE_PRIVATE
    ).edit()

    sharedPreferences.putLong(KEY_VALUE_AKAMAI_EXPIRED_TIME, expiredTime)
    sharedPreferences.apply()
}

fun Context.getExpiredTime(): Long {
    return this.getSharedPreferences(
        KEY_AKAMAI_EXPIRED_TIME,
        MODE_PRIVATE
    ).getLong(KEY_VALUE_AKAMAI_EXPIRED_TIME, -1L)
}

fun Context.setAkamaiValue(realAkamaiValue: String) {
    val sharedPreferences = this.getSharedPreferences(
        KEY_AKAMAI_EXPIRED_TIME,
        MODE_PRIVATE
    ).edit()
    sharedPreferences.putString(KEY_REAL_VALUE_AKAMAI, realAkamaiValue)
    sharedPreferences.apply()
}

fun Context.getAkamaiValue(): String {
    return this.getSharedPreferences(
        KEY_AKAMAI_EXPIRED_TIME,
        MODE_PRIVATE
    ).getString(KEY_REAL_VALUE_AKAMAI, "") ?: ""
}

fun <E> setExpire(
    currentTime: () -> Long,
    alreadyNotedTime: () -> Long,
    saveTime: (param: Long) -> Unit,
    setValue: () -> Unit,
    getValue: () -> E
): E {
    val currTime = currentTime.invoke()
    val savedTime = alreadyNotedTime.invoke()

    if ((currTime - savedTime) >= sdValidTime) {
        saveTime(currTime)
        val previousValue = getValue.invoke()
        setValue()
        val currentValue = getValue.invoke()
        val valueChanged = currentValue?.equals(previousValue) ?: false
        if (valueChanged) {
            ServerLogger.log(
                Priority.P1,
                "AKAMAI_SENSOR_SAME",
                mapOf(
                    "type" to "shared_pref",
                    "expired" to "true",
                    "value_changed" to valueChanged.toString(),
                    "expired_time" to (savedTime + sdValidTime).toString(),
                    "current_time" to currTime.toString()
                )
            )
        }
        return getValue.invoke()
    } else {
        return getValue.invoke()
    }
}

fun getHashKey(input: String): String {
    return input.hashCode().toString() + "-" + input.length
}

fun getQueryListFromQueryString(input: String): MutableList<String> {
    val hashStr = getHashKey(input)
    map[hashStr]?.let {
        return mutableListOf(it)
    }

    val m = getAnyPattern.matcher(input.replace("\n", " "))
    val any = mutableListOf<String>()

    while (m.find()) {
        m.group(1)?.let {
            any.add(it)
            map.putIfAbsent(hashStr, it)
        }
    }
    return any
}
