package com.tokopedia.oneclickcheckout.preference.edit.view

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.TestPreferenceEditModule

// For running PreferenceEditActivity with TestPreferenceEditModule
class TestPreferenceEditActivity : PreferenceEditActivity() {

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(TestPreferenceEditModule(this))
                .build()
    }
}