package com.tokopedia.home_account.explicitprofile.personalize

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents
import com.tokopedia.kotlin.extensions.view.hide

class ExplicitPersonalizeActivity: BaseSimpleActivity(), HasComponent<ExplicitProfileComponents> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment = PersonalizeQuestionFragment.createInstance()
    override fun getComponent(): ExplicitProfileComponents =
        ActivityComponentFactory.instance.createExplicitProfileComponent(application)
}
