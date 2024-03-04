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

import android.app.Activity
import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.TextView
import com.google.gson.Gson
import com.tokopedia.translator.callback.ActivityTranslatorCallbacks
import com.tokopedia.translator.repository.model.StringPoolItem
import com.tokopedia.translator.repository.model.TextViewUpdateModel
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
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class TranslatorManager() : CoroutineScope {

    private val gson = Gson()

    private var mApplication: Application? = null
    private var mSelectors = HashMap<String, String>()
    private var mStringPoolManager: StringPoolManager = StringPoolManager()
    private var origStrings: String? = null
    private var API_KEY: String? = null
    private var mLangsGroup: String? = "id-en"

    private var destinationLang: String = "en"

    private val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

    private val updateViewList = mutableListOf<TextViewUpdateModel>()
    private val updateScreenViewList = mutableListOf<TextViewUpdateModel>()

    constructor(application: Application, apiKey: String) : this() {
        mApplication = application
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

            destinationLang = mApplication?.let { app ->
                SharedPrefsUtils.getStringPreference(app.applicationContext, DESTINATION_LANGUAGE)?.split(DELIM_LANG_CODE.toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.getOrNull(1)
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
            return mCurrentActivity?.get() ?: run {
                null
            }
        }

        fun setCurrentActivity(mCurrentActivity: WeakReference<Activity>) {
            this.mCurrentActivity = mCurrentActivity
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


    private fun prepareSelectors(views: List<View?>) {

        if (views.isEmpty()) return

        updateViewList.clear()

        for (view in views) {
            if (view is TextView) {
                if (view.tag == null || (view.tag !is Boolean && !view.tag.toString().toBoolean())) {

                    val viewText = view.text.trim().toString()
                    if (viewText.isNotBlank()) {

                        val stringPoolItem = mStringPoolManager.get(viewText)

                        if (stringPoolItem == null || stringPoolItem.demandedText.isBlank() || (stringPoolItem.requestedLocale != destinationLang)) {
                            //prepare for translate
                            mStringPoolManager.add(viewText, "", "")
                        } else {
                            //translate
                            if (!TextUtils.equals(viewText, stringPoolItem.demandedText)) {
                                updateViewList.add(TextViewUpdateModel(view, stringPoolItem.demandedText))
                            }
                        }
                    }
                }
            }

            Log.d(TAG, "Created selectors for current screen $mSelectors")
            Log.d(TAG, "current string pool $mStringPoolManager")
        }
    }

    private suspend fun updateViewList() {
        withContext(Dispatchers.Main) {
            for (view in updateViewList) {
                if (view.newText != view.textView.text) {
                    view.textView.text = view.newText
                }
                if (view.textView.tag != true) {
                    view.textView.tag = true
                }
            }
        }
    }

    private suspend fun updateScreenViewList() {
        withContext(Dispatchers.Main) {
            for (view in updateScreenViewList) {
                if (view.newText != view.textView.text) {
                    view.textView.text = view.newText
                }
                if (view.textView.tag != true) {
                    view.textView.tag = true
                }
            }
        }
    }

    fun clearSelectors() {
        mSelectors.clear()
    }

    suspend fun startTranslate() {
        if (getCurrentActivity() == null || mApplication == null) {
            return
        }

        val currentDestLang = mApplication?.let { application ->
            SharedPrefsUtils.getStringPreference(application.applicationContext, DESTINATION_LANGUAGE)?.split(DELIM_LANG_CODE.toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.getOrNull(1)
        } ?: "en"

        if (destinationLang != currentDestLang) {
            destinationLang = currentDestLang
        }

        val views = ViewUtil.getChildrenViews(ViewUtil.getContentView(getCurrentActivity()))

        getCurrentActivity()?.let {
            prepareSelectors(views).run {
                updateViewList()
            }
        }

        mStringPoolManager.getQueryStrList().run {
            if (this.isNotEmpty()) {
                fetchTranslationService(this, views)
            }
        }
    }

    private suspend fun fetchTranslationService(originStrList: List<String>, views: List<TextView>) {

        val call = service.getTranslatedString("dict-chrome-ex", "id", destinationLang, "t", originStrList)

        try {
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }

            if (response.isSuccessful) {
                try {
                    val strJson = response.body()

                    val arrayStr = jsonStringToArray(strJson)

                    if (arrayStr.isNotEmpty()) {

                        mStringPoolManager.updateCache(originStrList, arrayStr, destinationLang)

                        val charCountOld = SharedPrefsUtils.getIntegerPreference(mApplication!!.applicationContext, CHARS_COUNT, 0)

                        updateScreenWithTranslatedString(views).run {
                            updateScreenViewList()
                        }

                        SharedPrefsUtils.setIntegerPreference(mApplication!!.applicationContext, CHARS_COUNT, origStrings!!.length + charCountOld)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun jsonStringToArray(jsonString: String?): Array<String> {
        if (jsonString?.isBlank() == true) return emptyArray()
        return try {
            gson.fromJson(jsonString, Array<String>::class.java)
        } catch (e: Exception) {
            emptyArray()
        }
    }

    private fun updateScreenWithTranslatedString(views: List<TextView>) {

        updateScreenViewList.clear()

        try {
            for (view in views) {

                val tvText = view.text?.trim().toString()

                val stringPoolItem = mStringPoolManager.get(tvText)

                if ((stringPoolItem?.demandedText?.isNotBlank() == true) && !TextUtils.equals(tvText, stringPoolItem.demandedText)) {
                    updateScreenViewList.add(TextViewUpdateModel(view, stringPoolItem.demandedText))
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + CoroutineExceptionHandler { _, _ -> }
}
