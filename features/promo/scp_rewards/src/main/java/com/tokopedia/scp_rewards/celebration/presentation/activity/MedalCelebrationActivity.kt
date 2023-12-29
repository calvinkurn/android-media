package com.tokopedia.scp_rewards.celebration.presentation.activity

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.scp_rewards.celebration.di.CelebrationComponent
import com.tokopedia.scp_rewards.celebration.di.DaggerCelebrationComponent
import com.tokopedia.scp_rewards.celebration.presentation.fragment.MedalCelebrationFragment
import com.tokopedia.unifyprinciples.Typography

class MedalCelebrationActivity : BaseSimpleActivity(), HasComponent<CelebrationComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        Typography.isFontTypeOpenSauceOne = true
    }
    override fun getNewFragment() = MedalCelebrationFragment()

    override fun getComponent(): CelebrationComponent {
        return DaggerCelebrationComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }
}
