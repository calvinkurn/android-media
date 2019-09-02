package com.tokopedia.analytics.cashshield

import android.content.Context
import com.cashshield.android.cashshieldclient
import com.tokopedia.analytics.R
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 2019-07-11.
 */
class CashShieldScope(private val context: Context): CoroutineScope {
    val CASHSHIELD = "CASHSHIELD"
    val SESSION = "session"
    val job = Job()
    val userSession: UserSessionInterface = UserSession(context)
    var sharedPref = context.getSharedPreferences(CASHSHIELD, Context.MODE_PRIVATE)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun send() {
        if(context != null) {
            try {
                launch {
                    val cs = cashshieldclient()
                    cs.dedi = true
                    cs.sendSignatureFDS(context, getSessionId(), context.getString(R.string.cashshield_token))
                }
            } catch (ignored: Exception) {

            }
        }
    }

    fun getSessionId(): String {
        return sharedPref.getString(SESSION, "")?: ""
    }

    fun refreshSession() {
        val session = UUID.randomUUID().toString()
        sharedPref.edit().apply {
            putString(SESSION, session)
            apply()
        }
    }

    fun cancel() {
        if(!job.isCancelled) {
            job.children.forEach {
                it.cancel()
            }
        }
    }
}
