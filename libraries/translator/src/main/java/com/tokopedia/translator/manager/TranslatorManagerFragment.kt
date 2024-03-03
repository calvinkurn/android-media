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
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class TranslatorManagerFragment() : CoroutineScope {

    private val gson = Gson()

    private var mApplication: Application? = null
    private var mSelectors = HashMap<String, String>()
    private var mStringPoolManager: StringPoolManager = StringPoolManager()

    private val service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
    private val updateViewList = mutableListOf<TextViewUpdateModel>()

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


    fun prepareSelectors(views: List<TextView>) {

        if (views.isEmpty()) return

        updateViewList.clear()

        for (view in views) {
            if (view.tag == null || (view.tag !is Boolean && !view.tag.toString().toBoolean())) {

                val viewText = view.text?.toString()

                if (viewText?.isNotBlank() == true) {
                    val stringPoolItem = mStringPoolManager.get(view.text.toString())

                    if (stringPoolItem === null || stringPoolItem.demandedText.isBlank() ||
                        (stringPoolItem.requestedLocale != destinationLang)
                    ) {
                        //prepare for translate
                        mStringPoolManager.add(view.text.toString(), "", "")
                    } else {
                        //translate
                        if (viewText != stringPoolItem.demandedText) {
                            updateViewList.add(TextViewUpdateModel(view, stringPoolItem.demandedText))
                        }
                    }
                }
            }
        }



        Log.d(TAG, "Created selectors for current screen $mSelectors")
        Log.d(TAG, "current string pool $mStringPoolManager")
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

    fun clearSelectors() {
        mSelectors.clear()
    }

    suspend fun startTranslate() {
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
        }

        val views = withContext(coroutineContext) {
            ViewUtil.getChildren(ViewUtil.getContentView(getCurrentFragment()))
        }

        prepareSelectors(views)

        updateViewList()

        val origStrings = mStringPoolManager.getQueryStrList()

        if (origStrings.isEmpty()) {
            return
        }

        fetchTranslationService(origStrings, views)
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

                        updateScreenWithTranslatedString(views)
                        SharedPrefsUtils.setIntegerPreference(
                            mApplication!!.applicationContext,
                            CHARS_COUNT,
                            originStrList.size + charCountOld
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun jsonStringToArray(jsonString: String?): Array<String> {
        if (jsonString?.isBlank() == true) return emptyArray()
        return gson.fromJson(jsonString, Array<String>::class.java)
    }

    private suspend fun updateScreenWithTranslatedString(views: List<TextView>) {

        val updateViewList = mutableListOf<TextViewUpdateModel>()
        var stringPoolItem: StringPoolItem?

        try {
            for (tv in views) {
                val tvText = tv.text?.toString().orEmpty()

                stringPoolItem = mStringPoolManager.get(tv.text?.toString())

                if (stringPoolItem == null || stringPoolItem.demandedText.isEmpty()) {
                    continue
                }

                if (!TextUtils.equals(tvText, stringPoolItem.demandedText)) {
                    updateViewList.add(TextViewUpdateModel(tv, stringPoolItem.demandedText))
                }
            }

            // Batch UI Updates
            withContext(Dispatchers.Main) {
                for (updateView in updateViewList) {
                    updateView.textView.text = updateView.newText
                    if (updateView.textView.tag != true) {
                        updateView.textView.tag = true
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
