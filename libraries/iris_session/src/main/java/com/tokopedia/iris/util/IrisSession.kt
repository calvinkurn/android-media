package com.tokopedia.iris.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Base64
import java.nio.charset.Charset
import java.util.UUID

class IrisSession(context: Context) : Session {

    val appContext: Context

    init {
        this.appContext = context.applicationContext
    }

    companion object {
        private val LOCK = Any()
        const val THRESHOLD_EXPIRED_IF_NO_ACTIVITY = 1_800_000L //30 minutes
        const val ONE_DAY_MILLIS = 86_400_000L
        const val GMT_MILLIS = 25_200_000L // 7 hours
        const val THRESHOLD_UPDATE_LAST_ACTIVITY =
            10_000L // 10 seconds to prevent burst update shared pref

        private var sharedPreferences: SharedPreferences? = null

        // variable to hold current session Id
        private var sessionId: String? = null

        // variable to hold first time Iris Session is created
        private var initialVisit: Long = 0L
        private var timestampOfDayChanged: Long = 0L

        // variable to hold last time Iris Session is accessed
        private var lastTrackingActivity: Long = 0L

        // variable to hold last time Iris Session is accessed (from shared Preference)
        private var lastTrackingActivityPref: Long = 0L
    }

    private fun getSharedPref(): SharedPreferences {
        val sp = sharedPreferences
        return if (sp == null) {
            val spTemp = appContext.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).also {
                sharedPreferences = it
            }
            spTemp
        } else {
            sp
        }
    }

    /**
     * Session id:
     * The session is created when there is no existing session
     * The session will be expired if there is no tracking activity for more than 30 minutes
     * The session will be renew if the day changed (GMT +7)
     */
    override fun getSessionId(): String {
        if (sessionId.isNullOrEmpty()) {
            // only if the session is empty
            // we want to synchronized generatedSessionId, so no multiple session will be created in different thread.
            synchronized(LOCK) {
                // check if sessionId already not null this time (might be initialized in different thread)
                val sessionIdNow = sessionId
                if (!sessionIdNow.isNullOrEmpty()) {
                    return sessionIdNow
                }
                val sessionIdFromPref = getSharedPref().getString(KEY_SESSION_ID, "")
                if (sessionIdFromPref.isNullOrEmpty()) {
                    // The session is created when there is no existing session
                    return generateSessionId(System.currentTimeMillis())
                } else {
                    sessionId = sessionIdFromPref
                }
            }
        }

        val currentTimeStamp = System.currentTimeMillis()
        if (lastTrackingActivity == 0L) {
            lastTrackingActivity = getSharedPref().getLong(KEY_TIMESTAMP_LAST_ACTIVITY, 0L)
            lastTrackingActivityPref = lastTrackingActivity
        }
        if (currentTimeStamp - lastTrackingActivity > THRESHOLD_EXPIRED_IF_NO_ACTIVITY) {
            // The session will be expired if there is no tracking activity for more than 30 minutes
            return generateSessionId(currentTimeStamp)
        }

        if (initialVisit == 0L) {
            initialVisit = getSharedPref().getLong(KEY_INITIAL_VISIT, 0L)
            timestampOfDayChanged = generateNextDayGMT7(initialVisit)
        }
        if (currentTimeStamp > timestampOfDayChanged) {
            // The session will be renew if the day changed (GMT +7)
            return generateSessionId(currentTimeStamp)
        }

        setPrefTrackingTimeStamp(currentTimeStamp)
        return sessionId ?: ""
    }

    /**
     * generate and store the result to cache and temp variable
     */
    private fun generateSessionId(timestamp: Long): String {
        val uuid = generateUuid()
        val domainHash = generateDomainHash()

        setInitialVisit(timestamp)

        val generatedSessionIdResult = "$domainHash:$uuid:$timestamp"
        setPrefSessionId(generatedSessionIdResult)
        sessionId = generatedSessionIdResult
        setPrefTrackingTimeStamp(timestamp)
        return generatedSessionIdResult
    }

    private fun generateNextDayGMT7(utcTimestamp: Long): Long {
        val beginningDayTimeStampUTC = utcTimestamp - (utcTimestamp % ONE_DAY_MILLIS)
        val nextDayTimeStampUTC = beginningDayTimeStampUTC + ONE_DAY_MILLIS
        var gmtNextDay = nextDayTimeStampUTC - GMT_MILLIS
        if (gmtNextDay < utcTimestamp) {
            gmtNextDay += ONE_DAY_MILLIS
        }
        return gmtNextDay
    }

    private fun generateDomainHash(): String {
        val data: ByteArray = DOMAIN_HASH.toByteArray(Charset.defaultCharset())
        return Base64.encodeToString(data, Base64.NO_WRAP).trim()
    }

    private fun generateUuid(): String {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase()
    }

    private fun setPrefSessionId(id: String) {
        val editor = getSharedPref().edit()
        editor.putString(KEY_SESSION_ID, id)
        editor.apply()
    }

    /**
     * Update timestamp of last tracking activity
     */
    private fun setPrefTrackingTimeStamp(timestamp: Long) {
        lastTrackingActivity = timestamp
        if (timestamp - lastTrackingActivityPref > THRESHOLD_UPDATE_LAST_ACTIVITY) {
            lastTrackingActivityPref = timestamp
            val editor = getSharedPref().edit()
            editor.putLong(KEY_TIMESTAMP_LAST_ACTIVITY, timestamp)
            editor.apply()
        }
    }

    private fun setInitialVisit(initialVisitParam: Long) {
        initialVisit = initialVisitParam
        timestampOfDayChanged = generateNextDayGMT7(initialVisitParam)
        val editor = getSharedPref().edit()
        editor.putLong(KEY_INITIAL_VISIT, initialVisitParam)
        editor.apply()
    }
}
