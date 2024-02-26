/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.translator.manager

import android.app.Application
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.tokopedia.translator.callback.ActivityTranslatorCallbacks
import com.tokopedia.translator.repository.model.StringPoolItem
import com.tokopedia.translator.repository.source.GetDataService
import com.tokopedia.translator.repository.source.RetrofitClientInstance
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView.*
import com.tokopedia.translator.util.ViewUtil
import com.tokopedia.translator.viewtree.ViewTreeManagerFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class TranslatorManagerFragment() {

    private val gson = Gson()

    private var mApplication: Application? = null
    private var mSelectors = HashMap<String, String>()
    private var mStringPoolManager: StringPoolManager = StringPoolManager()
    private var origStrings: String? = null

    private val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

    private var destinationLang: String = "en"

    constructor(application: Application) : this() {
        mApplication = application
        SharedPrefsUtils.setStringPreference(application, SOURCE_LANGUAGE, "Indonesian-id")

        try {
            destinationLang = mApplication?.let { application ->
                SharedPrefsUtils.getStringPreference(application.applicationContext, DESTINATION_LANGUAGE)
                    ?.split(DELIM_LANG_CODE.toRegex())
                    ?.dropLastWhile { it.isEmpty() }
                    ?.toTypedArray()?.getOrNull(1)
            } ?: "en"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var LOCK = Any()
        val TAG = "Tkpd-TranslatorManager"
        val DELIM = "#"

        private var mCurrentFragment: WeakReference<Fragment>? = null

        @JvmStatic
        private var sInstance: TranslatorManagerFragment? = null

        fun getCurrentFragment(): Fragment? {
            return mCurrentFragment?.let {
                it.get()
            } ?: run {
                null
            }
        }

        fun setCurrentFragment(mCurrentFragment: WeakReference<Fragment>) {
            this.mCurrentFragment = mCurrentFragment
        }

        fun clearCurrentFragment() {
            this.mCurrentFragment?.clear()
        }

        @JvmStatic
        fun init(application: Application): TranslatorManagerFragment? {

            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = TranslatorManagerFragment(application)
                }
            }

            return sInstance
        }

        internal fun getInstance(): TranslatorManagerFragment? {
            synchronized(LOCK) {
                if (sInstance == null) {
                    throw IllegalStateException(
                        "Default TranslatorManagerFragment is not initialized in this "
                                + "process "
                                + ". make sure to call "
                                + "TranslatorManagerFragment.initTranslatorManager(Context) first."
                    )
                }
                return sInstance
            }
        }
    }


    fun prepareSelectors(fragment: Fragment) {
        val views: List<View?> = ViewUtil.getChildren(ViewUtil.getContentView(fragment))

        for (view in views) {
            view?.run {
                if (view is TextView && view !is EditText) {
                    if (view.tag == null || (view.tag is Boolean && !view.tag.toString().toBoolean())) {
                        val selector = ViewTreeManagerFragment.createDOMIdentifier(view, fragment)
                        mSelectors[selector] = selector

                        view.text?.apply {
                            if (view.text.isNotBlank()) {
                                val stringPoolItem = mStringPoolManager.get(view.text.toString())

                                if (stringPoolItem === null || stringPoolItem.demandedText === null || stringPoolItem.demandedText.isBlank()) {
                                    //prepare for translate
                                    mStringPoolManager.add(view.text.toString(), "", "")
                                } else {
                                    //translate
                                    if (view.text != stringPoolItem.demandedText) {
                                        view.text = stringPoolItem.demandedText
                                        view.tag = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Log.d(TAG, "Created selectors for current screen $mSelectors")
        Log.d(TAG, "current string pool $mStringPoolManager")
    }

    fun clearSelectors() {
        mSelectors.clear()
    }

    fun startTranslate() {
        Log.d(TAG, "Starting translation of ${getCurrentFragment()}")
        if (getCurrentFragment() == null || mApplication == null) {
            return
        }

        val currentDestLang = mApplication?.let { application ->
            SharedPrefsUtils.getStringPreference(application.applicationContext, DESTINATION_LANGUAGE)
                ?.split(DELIM_LANG_CODE.toRegex())
                ?.dropLastWhile { it.isEmpty() }
                ?.toTypedArray()?.getOrNull(1)
        } ?: "en"

        if (destinationLang != currentDestLang) {
            destinationLang = currentDestLang
            mStringPoolManager.updateLangDest(destinationLang)
        }

        getCurrentFragment()?.let { prepareSelectors(it) }
        origStrings = mStringPoolManager.getQueryString()
        Log.d(TAG, "Here is eligible string for translation in current screen $origStrings")

        if (origStrings.isNullOrBlank()) {
            Log.d(
                TAG,
                "Stopping translation as no new untranslated string found in this screen, ${getCurrentFragment()}"
            )
            return
        }

        val originStrList = origStrings.orEmpty().split(DELIM.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        fetchTranslationService(originStrList)
    }

    private fun fetchTranslationService(originStrList: Array<String>) {
        val call = service.getTranslatedString("dict-chrome-ex", "id", destinationLang, "t", originStrList)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    try {
                        val strJson = response.body()
                        Log.d(
                            TAG,
                            "Received response from server: $strJson --> ${getCurrentFragment()}"
                        )

                        val arrayStr = jsonStringToArray(strJson)

                        if (arrayStr.isNotEmpty()) {

                            mStringPoolManager.updateCache(
                                originStrList,
                                arrayStr,
                                destinationLang
                            )

                            val charCountOld =
                                SharedPrefsUtils.getIntegerPreference(
                                    mApplication!!.applicationContext,
                                    CHARS_COUNT,
                                    0
                                )

                            updateScreenWithTranslatedString()
                            SharedPrefsUtils.setIntegerPreference(
                                mApplication!!.applicationContext,
                                CHARS_COUNT,
                                origStrings!!.length + charCountOld
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.localizedMessage.orEmpty())
                t.printStackTrace()
            }
        })
    }

    fun jsonStringToArray(jsonString: String?): Array<String> {
        if (jsonString?.isBlank() == true) return emptyArray()
        return gson.fromJson(jsonString, Array<String>::class.java)
    }

    fun updateScreenWithTranslatedString() {
        Log.d(TAG, "Starting screen update with translated string ${getCurrentFragment()}")
        var stringPoolItem: StringPoolItem?
        var tv: View?
        for (selector in mSelectors) {
            try {
                tv = ViewTreeManagerFragment.findViewByDOMIdentifier(
                        selector.value,
                        getCurrentFragment()!!
                )

                if (tv is TextView && tv !is EditText) {

                    stringPoolItem = mStringPoolManager.get(tv.text?.toString())

                    if (stringPoolItem == null || stringPoolItem.demandedText.isEmpty()) {
                        continue
                    }

                    if (tv.text != stringPoolItem.demandedText) {
                        tv.text = stringPoolItem.demandedText
                        tv.tag = true
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}
