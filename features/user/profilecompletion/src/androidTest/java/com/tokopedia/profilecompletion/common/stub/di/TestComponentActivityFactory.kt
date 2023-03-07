package com.tokopedia.profilecompletion.common.stub.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

class TestComponentActivityFactory: ActivityComponentFactory() {

    override fun createProfileCompletionComponent(activity: Activity, application: BaseMainApplication): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
                .baseAppComponent((application).baseAppComponent)
                .profileCompletionSettingModule(TestProfileCompletionSettingModule(activity))
                .build()
    }
}