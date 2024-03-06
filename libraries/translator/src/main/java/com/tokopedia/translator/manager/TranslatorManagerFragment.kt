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
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.tokopedia.translator.repository.model.TextViewUpdateModel
import com.tokopedia.translator.repository.source.GetDataService
import com.tokopedia.translator.repository.source.RetrofitClientInstance
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView.*
import com.tokopedia.translator.util.ViewUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class TranslatorManagerFragment() : CoroutineScope {

    private val gson = Gson()

    private var mApplication: Application? = null
    private var mSelectors = HashMap<String, String>()
    private var mStringPoolManager: StringPoolManager = StringPoolManager()

    private val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)

    private var destinationLang: String = "en"

    private var isTranslationInProgress = false

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
        val TAG = "Tkpd-TranslatorManagerFragment"

        private var mCurrentFragment: WeakReference<Fragment>? = null

        @JvmStatic
        private var sInstance: TranslatorManagerFragment? = null

        fun getCurrentFragment(): Fragment? {
            return mCurrentFragment?.get() ?: run {
                null
            }
        }

        fun setCurrentFragment(mCurrentFragment: WeakReference<Fragment>) {
            this.mCurrentFragment = mCurrentFragment
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


    private fun prepareSelectors(views: List<View?>) {

        if (views.isEmpty()) return

        val updateViewList = mutableListOf<TextViewUpdateModel>()

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

        getCurrentFragment()?.activity?.runOnUiThread {
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

    fun clearSelectors() {
        mSelectors.clear()
    }

    suspend fun startTranslate() {
        if (getCurrentFragment() == null || mApplication == null || isTranslationInProgress) {
            return
        }

        isTranslationInProgress = true

        try {
            val views = coroutineScope {
                async {
                    ViewUtil.getChildren(ViewUtil.getContentView(getCurrentFragment()))
                }.await()
            }

            prepareSelectors(views)

            val strList = mStringPoolManager.getQueryStrList()

            if (strList.isNotEmpty()) {
                fetchTranslationService(strList, views)
            }
        } finally {
            isTranslationInProgress = false
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

                        updateScreenWithTranslatedString(views)

                        mApplication?.applicationContext?.let {
                            SharedPrefsUtils.setIntegerPreference(
                                it, CHARS_COUNT,
                                originStrList.size + charCountOld
                            )
                        }
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
        return gson.fromJson(jsonString, Array<String>::class.java)
    }

    private fun updateScreenWithTranslatedString(views: List<TextView>) {

        val updateScreenViewList = mutableListOf<TextViewUpdateModel>()

        try {
            for (view in views) {

                val tvText = view.text?.trim().toString()

                val stringPoolItem = mStringPoolManager.get(tvText)

                if ((stringPoolItem?.demandedText?.isNotBlank() == true) && !TextUtils.equals(tvText, stringPoolItem.demandedText)) {
                    updateScreenViewList.add(TextViewUpdateModel(view, stringPoolItem.demandedText))
                }
            }

            getCurrentFragment()?.activity?.runOnUiThread {
                for (view in updateScreenViewList) {
                    if (view.newText != view.textView.text) {
                        view.textView.text = view.newText
                    }
                    if (view.textView.tag != true) {
                        view.textView.tag = true
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + CoroutineExceptionHandler { _, _ -> }
}
