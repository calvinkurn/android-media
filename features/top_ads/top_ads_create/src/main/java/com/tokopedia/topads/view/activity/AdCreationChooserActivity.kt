package com.tokopedia.topads.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.AdCreationChooserFragment

class AdCreationChooserActivity : BaseSimpleActivity(), HasComponent<CreateAdsComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.get(TopAdsCommonConstant.DIRECTED_FROM_MANAGE_OR_PDP) == true) {
            updateTitle(getString(R.string.mulai_beriklan))
        } else {
            updateTitle(getString(R.string.creat_ad))
        }
    }
    override fun getNewFragment(): Fragment? {
        return AdCreationChooserFragment.newInstance()
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}