package com.tokopedia.home_account.explicitprofile.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents

class ExplicitProfileActivity : BaseSimpleActivity(), HasComponent<ExplicitProfileComponents> {

    override fun getNewFragment(): Fragment {
        return ExplicitProfileFragment.createInstance()
    }

    override fun getComponent(): ExplicitProfileComponents {
        return ActivityComponentFactory.instance.createExplicitProfileComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
}
