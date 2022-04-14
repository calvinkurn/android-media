package com.tokopedia.profilecompletion.di

import android.app.Activity
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class ActivityComponentFactory {

    open fun createProfileCompletionComponent(activity: Activity, application: BaseMainApplication): ProfileCompletionSettingComponent =
            DaggerProfileCompletionSettingComponent.builder()
                .baseAppComponent((application).baseAppComponent)
                .profileCompletionSettingModule(ProfileCompletionSettingModule(activity))
                .build()

    companion object {
        private var sInstance: ActivityComponentFactory? = null

        @VisibleForTesting
        var instance: ActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = ActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}