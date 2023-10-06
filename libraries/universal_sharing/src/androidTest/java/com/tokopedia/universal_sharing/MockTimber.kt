package com.tokopedia.universal_sharing

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import timber.log.Timber

class MockTimber : Timber.Tree() {
    val logs = mutableListOf<Log>()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logs.add(Log(priority, tag, message, t))
    }

    fun lastLogMessage(): String {
        return logs.last().message
    }

    data class Log(val priority: Int, val tag: String?, val message: String, val t: Throwable?)
}

internal fun hasMediaName(id: String): Matcher<MockTimber> = object : TypeSafeMatcher<MockTimber>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Share channel clicked has the correct name")
    }

    override fun matchesSafely(item: MockTimber?): Boolean {
        if (item == null) return false
        val l = item.logs.findLast { it.message.contains("shareChannelClicked") } ?: return false
        return l.message.split("=")[1] == id
    }
}
