package com.tokopedia.config.url

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.GuardedBy

/**
 * @author okasurya on 4/8/19.
 */
class TokopediaUrl {

    companion object {

        /**
         *  volatile gunanya untuk kasih tau kalau varibale ini mungkin
         *  akan berubah di thread yg lain, buat make sure value nya
         *  ke-refresh thread itu update local cache
         *
         *  ku hapus GuardedBy(lock) karna kayaknya variable ini ngga di akses sama luar.
         */
        @Volatile private var sampleUrl: Url? = null
        @Volatile private var sharedPref: SharedPreferences? = null

        /**
         * 1. passing context karena sharedpreference
         * 2. synchronize hanya dilakukan saat initialize saja,
         * agar instance di-create hanya sekali walaupun ada beberapa thread (multi-thread),
         * dan apapun yg ada didalam syncronize akan memperlambat kinerja code didalamnya, jd buat lebih efisien.
         * 3.
         */
        fun getInstance(context: Context) : Url {
            return sampleUrl?: synchronized(this) {
                getEnvironment(context).also {
                   sampleUrl = it
                }
            }
        }

        private fun getSharedPreferences(context: Context) : SharedPreferences {
            return sharedPref?: synchronized(this) {
                context.getSharedPreferences(KEY_ENV_PREFERENCES,
                        Context.MODE_PRIVATE).also {
                    sharedPref = it
                }
            }
        }

        fun deleteInstance() {
            synchronized(this) {
                sampleUrl = null
            }
        }

        private fun getEnvironment(context: Context) : Url {
            return selectInstance(getSharedPreferences(context).getString(KEY_ENV, Env.LIVE.value));
        }

//        private const val LOCK = "LOCK"
//
//        @GuardedBy(LOCK)
//        @Volatile
//        private var tokopediaUrl: Url? = null
//
//        fun init(context: Context?) {
//            synchronized(LOCK) {
//                if (tokopediaUrl == null) {
//                    val sharedPref = context?.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
//                    tokopediaUrl = selectInstance(sharedPref?.getString(KEY_ENV, Env.LIVE.value))
//                }
//            }
//        }

        private fun selectInstance(env: String?): Url {
            return when(env) {
                Env.STAGING.value -> staging
                else ->  live
            }
        }

//        val url: Url
//            get() = synchronized(LOCK) {
//                if (tokopediaUrl == null) {
//                    // if not initialized, force to Live Url
//                    tokopediaUrl = live
//                    return tokopediaUrl as Url
//                }
//                return tokopediaUrl as Url
//            }

        fun setEnvironment(context: Context, env: Env) {
            getSharedPreferences(context).edit().putString(KEY_ENV, env.value)?.apply()
        }
    }
}
