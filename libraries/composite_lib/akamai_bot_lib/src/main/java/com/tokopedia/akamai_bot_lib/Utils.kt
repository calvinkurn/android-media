package com.tokopedia.akamai_bot_lib

import android.app.Application
import android.os.Build
import com.akamai.botman.CYFMonitor
import com.tokopedia.config.GlobalConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

private val getAnyPattern = Pattern.compile("\\{.*?([a-zA-Z_][a-zA-Z0-9_\\s]+)((?=\\()|(?=\\{)).*(?=\\{)")
val getMutationPattern: Pattern = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()")

val map = ConcurrentHashMap<String, String>()

fun initAkamaiBotManager(app:Application?){
    app?.let { CYFMonitor.initialize(it) }
}

fun getSensorData() = CYFMonitor.getSensorData()

val registeredGqlFunctions = mapOf(
        "login_token" to "login",
        "register" to "register",
        "pdpGetLayout" to "pdpGetLayout",
        "checkout_general" to "checkout",
        "atcOCS" to "atconeclickshipment",
        "getPDPInfo" to "product_info",
        "shopInfoByID" to "shop_info",
        "followShop" to "followshop",
        "validate_use_promo_revamp" to	"promorevamp",
        "crackResult" to	"crackresult",
        "gamiCrack" to	"gamicrack",
        "add_to_cart_occ" to	"atcocc",
        "one_click_checkout" to	"checkoutocc",
        "add_to_cart_transactional" to "atc",
        "add_to_cart" to "atc
)

fun isAkamai(query: String): Boolean {
    val isAkamai = getAny(query)
            .asSequence()
            .filter {
                registeredGqlFunctions.containsKey(it)
            }.take(1).map {
                registeredGqlFunctions[it]
            }.firstOrNull()
    return !isAkamai.isNullOrEmpty()
}

fun getMutation(input: String, match:String) : Boolean{
    val input2 = input.replace("\n", "")
    val input3 = input2.replace("\\s+", " ")
    val m: Matcher = getMutationPattern.matcher(input3)
    while (m.find()) {
        if( m.group(0).equals(match, ignoreCase = true))
            return true
    }
    return false
}

fun getAny(input:String) : MutableList<String>{
    if(map[input]?.isEmpty() == true) {
        return map[input]?.let { mutableListOf(it) } ?: mutableListOf()
    }

    val m = getAnyPattern.matcher(input.replace("\n", " "))
    val any = mutableListOf<String>()

    while (m.find()) {
        any.add(m.group(1))
        map.putIfAbsent(input, m.group(1))
    }

    return any
}
