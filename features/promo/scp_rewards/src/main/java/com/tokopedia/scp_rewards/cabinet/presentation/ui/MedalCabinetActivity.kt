package com.tokopedia.scp_rewards.cabinet.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.scp_rewards.cabinet.di.DaggerMedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent

class MedalCabinetActivity : BaseSimpleActivity(), HasComponent<MedalCabinetComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun getNewFragment(): Fragment  = MedalCabinetFragment()

    override fun getComponent(): MedalCabinetComponent {
        return DaggerMedalCabinetComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }

}
