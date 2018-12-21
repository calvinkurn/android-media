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
        return sharedPreferences.getString(KEY_USER_ID, "mockId")
    }

    override fun getSessionId(): String {
        val beginningCurrent = Calendar.getInstance().timeInMillis.toString()
        val beginningPrevious = sharedPreferences.getString(KEY_SESSION_ID, beginningCurrent)

        var uuid = sharedPreferences.getString(KEY_UUID, uuid())
        var initialVisit = sharedPreferences.getString(KEY_INITIAL_VISIT, beginningCurrent)
        val domainHash = sharedPreferences.getString(KEY_DOMAIN_HASH, domainHash())

        setDomainHash(domainHash)
        setSessionId(beginningCurrent)

        if (isExpired(beginningPrevious, beginningCurrent)) {
            uuid = uuid()
            initialVisit = Calendar.getInstance().timeInMillis.toString()
            setUuid(uuid)
            setInitialVisit(initialVisit)
        }

        return "$domainHash:$uuid:$initialVisit"
    }

    private fun uuid() : String {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase()
    }

    private fun isExpired(bp: String, bc: String) : Boolean {
        val thirtyMinutes = 1800
        val beginningPrevious : Int? = bp.toIntOrNull()
        val beginningCurrent : Int? = bc.toIntOrNull()
        if (beginningPrevious != null && beginningCurrent != null) {
            return (beginningPrevious+thirtyMinutes) < beginningCurrent
        }
        return false
    }

    override fun setUserId(id: String) {
        editor.putString(KEY_USER_ID, id)
        editor.commit()
    }

    override fun setSessionId(id: String) {
        editor.putString(KEY_SESSION_ID, id)
        editor.commit()
    }

    private fun domainHash() : String {
        val data: ByteArray = DOMAIN_HASH.toByteArray(Charset.defaultCharset())
        return Base64.encodeToString(data, Base64.NO_WRAP).trim()
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
