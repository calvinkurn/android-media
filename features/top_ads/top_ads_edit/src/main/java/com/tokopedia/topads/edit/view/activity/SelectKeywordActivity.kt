package com.tokopedia.topads.edit.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment

/**
 * Created by Pika on 6/4/20.
 */

class SelectKeywordActivity : BaseSimpleActivity(), HasComponent<TopAdsEditComponent> {
    override fun getNewFragment(): Fragment? {
        return KeywordAdsListFragment.createInstance(intent.extras)
    }

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
    }

}