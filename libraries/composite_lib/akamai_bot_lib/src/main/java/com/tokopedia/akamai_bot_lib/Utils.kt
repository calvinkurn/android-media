package com.tokopedia.akamai_bot_lib

import android.app.Application
import android.os.Build
import com.akamai.botman.CYFMonitor
import com.tokopedia.config.GlobalConfig
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

fun getAny(input:String) : MutableList<String>{
    val p = Pattern.compile("\\{.*?([a-zA-Z_][a-zA-Z0-9_]+)(?=\\().*")
    val m = p.matcher(input.replace("\n"," "))
    val any = mutableListOf<String>()
    while (m.find()) {

        println(m.group(1))
        any.add(m.group(1))
    }
    return any;
}


val p: Pattern = Pattern.compile("(?<=mutation )(\\w*)(?=\\s*\\()")