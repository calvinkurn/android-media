package com.tokopedia.scp_rewards.presentation.ui

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.scp_rewards.detail.di.DaggerMedalDetailComponent
import com.tokopedia.scp_rewards.detail.di.MedalDetailComponent
import com.tokopedia.scp_rewards.detail.presentation.ui.MedalDetailFragment

class MedalDetailActivity : BaseSimpleActivity(), HasComponent<MedalDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }
    override fun getNewFragment() = MedalDetailFragment()

    override fun getComponent(): MedalDetailComponent {
        return DaggerMedalDetailComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }
}
