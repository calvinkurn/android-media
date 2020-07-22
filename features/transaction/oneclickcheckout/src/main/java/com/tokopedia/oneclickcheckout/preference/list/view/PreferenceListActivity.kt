package com.tokopedia.oneclickcheckout.preference.list.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.preference.list.di.DaggerPreferenceListComponent
import com.tokopedia.oneclickcheckout.preference.list.di.PreferenceListComponent

class PreferenceListActivity : BaseSimpleActivity(), HasComponent<PreferenceListComponent> {

    override fun getNewFragment(): Fragment? {
        return PreferenceListFragment()
    }

    override fun getComponent(): PreferenceListComponent {
        return DaggerPreferenceListComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }
}
