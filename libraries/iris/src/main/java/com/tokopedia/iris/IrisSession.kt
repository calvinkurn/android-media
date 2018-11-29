package com.tokopedia.iris

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.util.*

class IrisSession(val context: Context) : Session {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    override fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "mockId")
    }

    override fun getSessionId(): String {
        val currentTimeStamp = Calendar.getInstance().timeInMillis.toString()
        val previousTimeStamp = sharedPreferences.getString(KEY_SESSION_ID, currentTimeStamp)
        setSessionId(currentTimeStamp)
        return "$currentTimeStamp:$previousTimeStamp"
    }

    override fun setUserId(id: String) {
        editor.putString(KEY_USER_ID, id)
        editor.commit()
    }

    override fun setSessionId(id: String) {
        editor.putString(KEY_SESSION_ID, id)
        editor.commit()
    }
}
