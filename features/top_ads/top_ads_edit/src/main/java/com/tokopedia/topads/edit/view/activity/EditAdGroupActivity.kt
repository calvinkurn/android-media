package com.tokopedia.topads.edit.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.view.fragment.edit.EditAdGroupFragment

class EditAdGroupActivity : BaseSimpleActivity(), HasComponent<TopAdsEditComponent> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_edit_activity_edit_ad_group)
    }

    override fun getNewFragment() = EditAdGroupFragment.newInstance()

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        )
            .topAdEditModule(TopAdEditModule(this)).build()
    }
}