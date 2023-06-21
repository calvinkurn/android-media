package com.tokopedia.scp_rewards.cabinet.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.CABINET_PATH
import com.tokopedia.applink.internal.ApplinkConstInternalPromo.SEE_MORE_PATH
import com.tokopedia.scp_rewards.cabinet.di.DaggerMedalCabinetComponent
import com.tokopedia.scp_rewards.cabinet.di.MedalCabinetComponent

class MedalCabinetActivity : BaseSimpleActivity(), HasComponent<MedalCabinetComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }
    override fun getNewFragment(): Fragment {
        return when (intent.data?.pathSegments?.last()) {
            CABINET_PATH -> MedalCabinetFragment()
            SEE_MORE_PATH -> SeeMoreMedaliFragment()
            else -> MedalCabinetFragment()
        }
    }
    override fun getComponent(): MedalCabinetComponent {
        return DaggerMedalCabinetComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()
    }

}
