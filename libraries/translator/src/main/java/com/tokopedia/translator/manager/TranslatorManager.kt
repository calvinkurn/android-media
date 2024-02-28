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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.google.gson.Gson
import com.tokopedia.translator.R
import com.tokopedia.translator.callback.ActivityTranslatorCallbacks
import com.tokopedia.translator.repository.model.StringPoolItem
import com.tokopedia.translator.repository.source.GetDataService
import com.tokopedia.translator.repository.source.RetrofitClientInstance
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView.*
import com.tokopedia.translator.util.ViewUtil
import com.tokopedia.translator.viewtree.ViewTreeManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class TranslatorManager() : CoroutineScope {

    private val gson = Gson()

    private var mApplication: Application? = null
    private var mSelectors = HashMap<String, String>()
    private var mStringPoolManager: StringPoolManager = StringPoolManager()
    private var origStrings: String? = null
    private val TAG_CODE: String = "code"
    private val TAG_LANG_ARRAY: String = "lang"
    private val TAG_TEXT_ARRAY: String = "text"
    private var mLayoutFloatingButton: View? = null
    private var mStartX = 48
    private var mStartY = 280
    private var API_KEY: String? = null
    private var mLangsGroup: String? = "id-en"

    private var destinationLang: String = "en"

    private val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

    constructor(application: Application, apiKey: String) : this() {
        mApplication = application
//        prepareBubbleView(application)
        API_KEY = SharedPrefsUtils.getStringPreference(application, API_KEY)
        SharedPrefsUtils.setStringPreference(application, SOURCE_LANGUAGE, "Indonesian-id")

        if (API_KEY.isNullOrBlank()) {
            API_KEY = apiKey
        }

        try {
            mLangsGroup = "${
                SharedPrefsUtils.getStringPreference(application, SOURCE_LANGUAGE)!!.split(DELIM_LANG_CODE)[1]
            }-${
                SharedPrefsUtils.getStringPreference(application, DESTINATION_LANGUAGE)!!.split(DELIM_LANG_CODE)[1]
            }"

            destinationLang = mApplication?.let { application ->
                SharedPrefsUtils.getStringPreference(application.applicationContext, DESTINATION_LANGUAGE)?.split(DELIM_LANG_CODE.toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.getOrNull(1)
            } ?: "en"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var LOCK = Any()
        val TAG = "Tkpd-TranslatorManager"
        val DELIM = "#"

        private var mCurrentActivity: WeakReference<Activity>? = null

        @JvmStatic
        private var sInstance: TranslatorManager? = null

        fun getCurrentActivity(): Activity? {
            return mCurrentActivity?.let {
                it.get()
            } ?: run {
                null
            }
        }

        fun setCurrentActivity(mCurrentActivity: WeakReference<Activity>) {
            this.mCurrentActivity = mCurrentActivity
        }

        fun clearCurrentActivity() {
            this.mCurrentActivity?.clear()
        }


        @JvmStatic
        fun init(application: Application, apiKey: String): TranslatorManager? {

            if (sInstance == null) {
                synchronized(LOCK) {
                    TranslatorManagerFragment.init(application)
                    sInstance = TranslatorManager(application, apiKey)
                    application.registerActivityLifecycleCallbacks(ActivityTranslatorCallbacks())
                }
            }

            return sInstance
        }

        internal fun getInstance(): TranslatorManager? {
            synchronized(LOCK) {
                if (sInstance == null) {
                    throw IllegalStateException("Default TranslatorManager is not initialized in this " + "process " + ". make sure to call " + "TranslatorManager.initTranslatorManager(Context) first.")
                }
                return sInstance
            }
        }
    }


    suspend fun prepareSelectors(activity: Activity) {
        val views: List<View?> = ViewUtil.getChildren(ViewUtil.getContentView(activity))

        if (views.isEmpty()) return

        for (view in views) {
            if (view is TextView && view !is EditText) {
                if (view.tag == null || (view.tag is Boolean && !view.tag.toString().toBoolean())) {
                    val selector = ViewTreeManager.createDOMIdentifier(view, activity)
                    mSelectors[selector] = selector

                    if (view.text.isNotBlank()) {
                        val stringPoolItem = mStringPoolManager.get(view.text.toString())

                        if (stringPoolItem === null || stringPoolItem.demandedText === null || stringPoolItem.demandedText.isBlank()) {
                            //prepare for translate
                            mStringPoolManager.add(view.text.toString(), "", "")
                        } else {
                            //translate
                            if (view.text != stringPoolItem.demandedText) {
                                launch(Dispatchers.Main) {
                                    view.text = stringPoolItem.demandedText
                                    view.tag = true
                                }
                            }
                        }
                    }
                }
            }

            Log.d(TAG, "Created selectors for current screen $mSelectors")
            Log.d(TAG, "current string pool $mStringPoolManager")
        }
    }

    fun clearSelectors() {
        mSelectors.clear()
    }

    suspend fun startTranslate() {
        Log.d(TAG, "Starting translation of ${getCurrentActivity()}")
        if (getCurrentActivity() == null || mApplication == null) {
            return
        }

        val currentDestLang = mApplication?.let { application ->
            SharedPrefsUtils.getStringPreference(application.applicationContext, DESTINATION_LANGUAGE)?.split(DELIM_LANG_CODE.toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.getOrNull(1)
        } ?: "en"

        if (destinationLang != currentDestLang) {
            destinationLang = currentDestLang
            mStringPoolManager.updateLangDest(destinationLang)
        }

        prepareSelectors(getCurrentActivity()!!)

        origStrings = mStringPoolManager.getQueryString()

        Log.d(TAG, "Here is eligible string for translation in current screen $origStrings")

        if (origStrings.isNullOrBlank()) {
            Log.d(TAG, "Stopping translation as no new untranslated string found in this screen, ${getCurrentActivity()}")
            return
        }

        val originStrList = origStrings.orEmpty().split(DELIM.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        fetchTranslationService(originStrList)
    }

    private fun fetchTranslationService(originStrList: Array<String>) {
        launch(coroutineContext) {
            val call = service.getTranslatedString("dict-chrome-ex", "id", destinationLang, "t", originStrList)

            call.enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        try {
                            val strJson = response.body()
                            Log.d(TranslatorManagerFragment.TAG, "Received response from server: $strJson --> ${TranslatorManagerFragment.getCurrentFragment()}")

                            val arrayStr = jsonStringToArray(strJson)

                            if (arrayStr.isNotEmpty()) {

                                mStringPoolManager.updateCache(originStrList, arrayStr, destinationLang)

                                val charCountOld = SharedPrefsUtils.getIntegerPreference(mApplication!!.applicationContext, CHARS_COUNT, 0)

                                launch {
                                    updateScreenWithTranslatedString()
                                    SharedPrefsUtils.setIntegerPreference(mApplication!!.applicationContext, CHARS_COUNT, origStrings!!.length + charCountOld)
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TranslatorManagerFragment.TAG, t.localizedMessage.orEmpty())
                    t.printStackTrace()
                }
            })
        }
    }

    fun jsonStringToArray(jsonString: String?): Array<String> {
        if (jsonString?.isBlank() == true) return emptyArray()
        return try {
            gson.fromJson(jsonString, Array<String>::class.java)
        } catch (e: Exception) {
            emptyArray()
        }
    }

    suspend fun updateScreenWithTranslatedString() {
        Log.d(TAG, "Starting screen update with translated string ${getCurrentActivity()}")
        var stringPoolItem: StringPoolItem?
        var tv: View?
        for (selector in mSelectors) {
            try {
                tv = ViewTreeManager.findViewByDOMIdentifier(selector.value, getCurrentActivity()!!)

                if (tv == null) continue

                if (tv is TextView && tv !is EditText) {

                    stringPoolItem = mStringPoolManager.get(tv.text?.toString())

                    if (stringPoolItem == null || stringPoolItem.demandedText.isEmpty()) {
                        continue
                    }

                    if (tv.text != stringPoolItem.demandedText) {
                        launch(Dispatchers.Main) {
                            tv.text = stringPoolItem.demandedText
                            tv.tag = true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + CoroutineExceptionHandler { _, _ -> }
}
