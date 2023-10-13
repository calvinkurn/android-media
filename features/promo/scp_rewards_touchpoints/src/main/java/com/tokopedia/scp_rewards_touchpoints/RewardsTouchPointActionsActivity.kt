package com.tokopedia.scp_rewards_touchpoints

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.common.di.CelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.common.di.DaggerCelebrationComponent

class RewardsTouchPointActionsActivity : BaseActivity(), HasComponent<CelebrationComponent>{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.apply {
            openCelebrationBottomSheet(pathSegments.last())
        }
    }

    private fun openCelebrationBottomSheet(slug:String){
        MedalCelebrationBottomSheet.show(supportFragmentManager,slug)
    }

    override fun getComponent(): CelebrationComponent {
        return DaggerCelebrationComponent.builder()
            .baseAppComponent((this.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

}
