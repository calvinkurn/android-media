package com.tokopedia.topads.edit.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.view.fragment.edit.EditFormWithoutGroupFragment

/**
 * Created by Pika on 1/6/20.
 */

class EditFormWithoutGroupActivity : BaseSimpleActivity(), HasComponent<TopAdsEditComponent> {

    override fun getNewFragment(): Fragment? {
        return EditFormWithoutGroupFragment.newInstance(intent.extras)
    }

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.subtitle = intent?.extras?.getString(Constants.groupName)
    }
}