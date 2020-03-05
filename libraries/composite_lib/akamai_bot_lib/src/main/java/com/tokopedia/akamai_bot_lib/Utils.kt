package com.tokopedia.akamai_bot_lib

import android.app.Application
import android.os.Build
import android.text.TextUtils
import com.akamai.botman.CYFMonitor
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Matcher
import java.util.regex.Pattern


fun initAkamaiBotManager(app:Application?){
    app?.let { CYFMonitor.initialize(it) }
}

fun getSensorData() = CYFMonitor.getSensorData()

private val userAgentFormat = "TkpdConsumer/%s (%s;)"

fun getUserAgent(): String {
    return String.format(userAgentFormat, GlobalConfig.VERSION_NAME, "Android " + Build.VERSION.RELEASE)
}

fun getMutation(input: String, match:String) : Boolean{
    val input2 = input.replace("\n", "")
    val input3 = input2.replace("\\s+", " ")
    val m: Matcher = p.matcher(input3)
    while (m.find()) {
        if( m.group(0).equals(match, ignoreCase = true))
            return true
    }
    return false
}

val map = ConcurrentHashMap<String, String>()

fun getAny(input:String) : MutableList<String>{
    if(map.get(input)?.isEmpty()?:false )
        return map.get(input)?.let { mutableListOf(it) } ?: mutableListOf()

    val p = Pattern.compile("\\{.*?([a-zA-Z_][a-zA-Z0-9_\\s]+)((?=\\()|(?=\\{)).*(?=\\{)")
    val m = p.matcher(input.replace("\n"," "))
    val any = mutableListOf<String>()
    while (m.find()) {
        any.add(m.group(1))
        map.putIfAbsent(input, m.group(1))
    }
    return any;
}

val p: Pattern = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()")