package com.tokopedia.iris

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Base64
import java.nio.charset.Charset
import java.util.*


class IrisSession(val context: Context) : Session {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    override fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "")
    }

    override fun getDeviceId(): String {
        return sharedPreferences.getString(KEY_DEVICE_ID, "")
    }

    override fun getSessionId(): String {

        val beginningCurrent = Calendar.getInstance().timeInMillis
        val beginningPrevious = sharedPreferences.getString(KEY_TIMESTAMP_PREVIOUS, beginningCurrent.toString())

        var sessionId = sharedPreferences.getString(KEY_SESSION_ID, "")
        if (sessionId.isBlank() || shouldGenerateSession(beginningPrevious.toLong(), beginningCurrent)) {
            sessionId = generateSessionId(beginningCurrent)
            setSessionId(sessionId)
        }

        return sessionId
    }

    private fun generateSessionId(bc: Long) : String {

        val uuid = sharedPreferences.getString(KEY_UUID, generateUuid())
        val initialVisit = sharedPreferences.getString(KEY_INITIAL_VISIT, bc.toString())
        val domainHash = sharedPreferences.getString(KEY_DOMAIN_HASH, generateDomainHash())

        setDomainHash(domainHash)
        setTimestampPrevious(bc.toString())
        setInitialVisit(initialVisit)
        setUuid(uuid)

        return "$domainHash:$uuid:$initialVisit"
    }

    private fun shouldGenerateSession(bp: Long, bc: Long) : Boolean {
        return isExpired(bp, bc) || isDayChanged(bp, bc)
    }

    private fun isExpired(bp: Long, bc: Long) : Boolean {
        val thirtyMinutes = 1800
        return (bp+thirtyMinutes) < bc
    }

    private fun isDayChanged(bp: Long, bc: Long) : Boolean {
        val cal = Calendar.getInstance()
        cal.time = Date(bp)
        val dayBp = cal.get(Calendar.DAY_OF_YEAR)

        cal.time = Date(bc)
        val dayBc = cal.get(Calendar.DAY_OF_YEAR)
        return dayBp != dayBc
    }

    private fun generateDomainHash() : String {
        val data: ByteArray = DOMAIN_HASH.toByteArray(Charset.defaultCharset())
        return Base64.encodeToString(data, Base64.NO_WRAP).trim()
    }

    private fun generateUuid() : String {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase()
    }

    override fun setUserId(id: String) {
        editor.putString(KEY_USER_ID, id)
        editor.commit()
    }

    override fun setDeviceId(id: String) {
        editor.putString(KEY_DEVICE_ID, id)
        editor.commit()
    }

    private fun setTimestampPrevious(timestamp: String) {
        editor.putString(KEY_TIMESTAMP_PREVIOUS, timestamp)
        editor.commit()
    }

    override fun setSessionId(id: String) {
        editor.putString(KEY_SESSION_ID, id)
        editor.commit()
    }

    private fun setDomainHash(domainHash: String) {
        editor.putString(KEY_DOMAIN_HASH, domainHash)
        editor.commit()
    }

    private fun setUuid(uuid: String) {
        editor.putString(KEY_SESSION_ID, uuid)
        editor.commit()
    }

    private fun setInitialVisit(initialVisit: String) {
        editor.putString(KEY_INITIAL_VISIT, initialVisit)
        editor.commit()
    }
}
