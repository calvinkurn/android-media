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
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.translator.R
import com.tokopedia.translator.callback.ActivityTranslatorCallbacks
import com.tokopedia.translator.repository.model.StringPoolItem
import com.tokopedia.translator.repository.source.GetDataService
import com.tokopedia.translator.repository.source.RetrofitClientInstance
import com.tokopedia.translator.ui.CommonUtil
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView.*
import com.tokopedia.translator.util.ViewUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference


class TranslatorManager() {
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

    constructor(application: Application, apiKey: String) : this() {
        mApplication = application
        prepareBubbleView(application)
        API_KEY = SharedPrefsUtils.getStringPreference(application, API_KEY)
        SharedPrefsUtils.setStringPreference(application, SOURCE_LANGUAGE, "Indonesian-id")

        if (API_KEY.isNullOrBlank()) {
            API_KEY = apiKey
        }

        try {
            mLangsGroup =
                "${SharedPrefsUtils.getStringPreference(
                    application,
                    SOURCE_LANGUAGE
                )!!.split(
                    DELIM_LANG_CODE
                )[1]}-${SharedPrefsUtils.getStringPreference(
                    application,
                    DESTINATION_LANGUAGE
                )!!.split(
                    DELIM_LANG_CODE
                )[1]}"
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

        fun setCurrentActivity(currentActivity: WeakReference<Activity>) {
            this.mCurrentActivity = currentActivity
        }

        @JvmStatic
        fun init(application: Application, apiKey: String): TranslatorManager? {

            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = TranslatorManager(application, apiKey)
                    application.registerActivityLifecycleCallbacks(ActivityTranslatorCallbacks())
                }
            }

            return sInstance
        }

        internal fun getInstance(): TranslatorManager? {
            synchronized(LOCK) {
                if (sInstance == null) {
                    throw IllegalStateException(
                        "Default TranslatorManager is not initialized in this "
                                + "process "
                                + ". make sure to call "
                                + "TranslatorManager.initTranslatorManager(Context) first."
                    )
                }
                return sInstance
            }
        }
    }


    fun prepareSelectors(activity: Activity) {
        val views: List<View?> = ViewUtil.getChildren(ViewUtil.getContentView(activity))

        for (view in views) {
            view?.apply {
                if (view is TextView) {
                    if (view.tag == null || (view.tag is Boolean && !view.tag.toString().toBoolean())) {
                        val selector = ViewTreeManager.createDOMIdentifier(view, activity)
                        mSelectors[selector] = selector

                        view.text?.apply {
                            if (view.text.isNotBlank()) {
                                val stringPoolItem = mStringPoolManager.get(view.text.toString())

                                if (stringPoolItem === null || stringPoolItem.demandedText === null || stringPoolItem.demandedText.isBlank()) {
                                    //prepare for translate
                                    mStringPoolManager.add(view.text.toString(), "")
                                } else {
                                    //translate
                                    view.text = stringPoolItem.demandedText
                                    view.tag = true
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

    public fun clearSelectors() {
        mSelectors.clear()
    }

    fun startTranslate() {
        Log.d(TAG, "Starting translation of ${getCurrentActivity()}")
        if (getCurrentActivity() == null) {
            return
        }

        prepareSelectors(getCurrentActivity()!!)
        origStrings = mStringPoolManager.getQueryString()
        Log.d(TAG, "Here is eligible string for translation in current screen $origStrings")

        if (origStrings == null || origStrings!!.isEmpty()) {
            Log.d(
                TAG,
                "Stopping translation as no new untranslated string found in this screen, ${getCurrentActivity()}"
            )
            CommonUtil.showToast(mApplication, "No translation needed");
            return
        }

        val service =
            RetrofitClientInstance.getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getTranslatedStringY(API_KEY, origStrings!!, mLangsGroup, "plain")
        CommonUtil.showToast(mApplication, "Translation starting...");
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    try {
                        val strJson = response.body()
                        Log.d(
                            TAG,
                            "Received response from server: $strJson --> ${getCurrentActivity()}"
                        )
                        val data = JSONObject(strJson)
                        val resCode = data.getInt(TAG_CODE)
                        if (resCode == 200) {
                            val arrText = data.getJSONArray(TAG_TEXT_ARRAY)

                            mStringPoolManager.updateCache(
                                origStrings!!.split(DELIM.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
                                arrText.getString(0).split(
                                    DELIM.toRegex()
                                ).dropLastWhile { it.isEmpty() }.toTypedArray()
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

                            CommonUtil.showToast(mApplication, "Translation succeeded...enjoy");

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        CommonUtil.showToast(mApplication, "Translation failed...");
                    }
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, t.localizedMessage!!)
                t.printStackTrace()
            }
        })
    }

    public fun updateScreenWithTranslatedString() {
        Log.d(TAG, "Starting screen update with translated string ${getCurrentActivity()}")
        var stringPoolItem: StringPoolItem?
        var tv: TextView?
        for (selector in mSelectors) {
            try {
                tv = ViewTreeManager.findViewByDOMIdentifier(
                        selector.value,
                        getCurrentActivity()!!
                ) as TextView
                stringPoolItem = mStringPoolManager.get(tv.text.toString())
                if (stringPoolItem == null || stringPoolItem.demandedText.isEmpty()) {
                    continue
                }

                tv.text = stringPoolItem.demandedText
                tv.tag = true
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    private fun prepareBubbleView(context: Context) {
        mLayoutFloatingButton =
            LayoutInflater.from(context).inflate(R.layout.layout_floating_button, null)
        //Add the view to the window.
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.END
        mLayoutFloatingButton!!.x = mStartX.toFloat()
        mLayoutFloatingButton!!.y = mStartY.toFloat()
        mLayoutFloatingButton!!.findViewById<View>(R.id.container)
            .setOnTouchListener(object : View.OnTouchListener {
                private var initialX: Int = 0
                private var initialY: Int = 0
                private var initialTouchX: Float = 0.toFloat()
                private var initialTouchY: Float = 0.toFloat()

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = mLayoutFloatingButton!!.x.toInt()
                            initialY = mLayoutFloatingButton!!.y.toInt()
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            val xDiff = (event.rawX - initialTouchX).toInt()
                            val yDiff = (event.rawY - initialTouchY).toInt()
                            if (xDiff < 10 && yDiff < 10) {
                                startTranslate()

                            } else {
                                mStartX = event.rawX.toInt()
                                mStartY = event.rawY.toInt()
                            }

                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mLayoutFloatingButton!!.x =
                                (initialX + (event.rawX - initialTouchX).toInt()).toFloat()
                            mLayoutFloatingButton!!.y =
                                (initialY + (event.rawY - initialTouchY).toInt()).toFloat()
                            return true
                        }
                    }
                    return false
                }
            })
    }


    fun attachBubbleViewInstanceHandler(
        next: Activity
    ) {
        if (mCurrentActivity != null) {
            (mCurrentActivity?.get()!!.window.decorView as ViewGroup).removeView(
                mLayoutFloatingButton
            )
        }

        (next.window.decorView as ViewGroup).addView(mLayoutFloatingButton)
        applyAnimation(mLayoutFloatingButton)
    }

    private fun applyAnimation(view: View?) {
        val fadeOut = ObjectAnimator.ofFloat(view, "alpha", .5f, .1f)
        fadeOut.duration = 1000
        val fadeIn = ObjectAnimator.ofFloat(view, "alpha", .1f, .5f)
        fadeIn.duration = 1000

        val mAnimationSet = AnimatorSet()

        mAnimationSet.play(fadeIn).after(fadeOut)

        mAnimationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mAnimationSet.start()
            }
        })

        mAnimationSet.start()
    }
}
