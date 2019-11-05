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
package com.tokopedia.translator.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.tokopedia.translator.manager.TranslatorManager
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView
import java.lang.ref.WeakReference


class ActivityTranslatorCallbacks : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }


    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if(SharedPrefsUtils.getBooleanPreference(activity, TranslatorSettingView.IS_ENABLE, false)) {
            Log.i(TAG, "onActivityResumed() invoked of :" + activity.localClassName)
            val weakActivity = WeakReference<Activity>(activity)
            val translatorManager = TranslatorManager.getInstance()
            translatorManager?.clearSelectors()
            translatorManager?.prepareSelectors(activity)
            translatorManager?.attachBubbleViewInstanceHandler(activity)
            TranslatorManager.setCurrentActivity(weakActivity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    companion object {

        private val TAG = ActivityTranslatorCallbacks::class.java.simpleName
    }
}