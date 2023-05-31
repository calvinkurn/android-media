package com.tokopedia.kyc_centralized.util

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

internal fun hasProjectIdOf(id: String): Matcher<MockTimber> = object : TypeSafeMatcher<MockTimber>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Kyc Repository has the correct id")
    }

    override fun matchesSafely(item: MockTimber?): Boolean {
        if (item == null) return false
        val l = item.logs.findLast { it.message.contains("uploadProjectId") } ?: return false
        return l.message.split("=")[1] == id
    }
}

internal fun hasKycFaceMode(id: String): Matcher<MockTimber> = object : TypeSafeMatcher<MockTimber>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Kyc selfie or liveness mode when uploading face images")
    }

    override fun matchesSafely(item: MockTimber?): Boolean {
        if (item == null) return false
        val l = item.logs.findLast { it.message.contains("uploadFaceImage") } ?: return false
        return l.message.split("=")[1] == id
    }
}

internal fun hasRedirectUrlFinal(id: String): Matcher<MockTimber> = object : TypeSafeMatcher<MockTimber>() {
    override fun describeTo(description: Description?) {
        description?.appendText("Kyc ala carte redirect url")
    }

    override fun matchesSafely(item: MockTimber?): Boolean {
        if (item == null) return false
        val l = item.logs.findLast { it.message.contains("redirectUrlFinal") } ?: return false
        return l.message.split("=")[1] == id
    }
}
