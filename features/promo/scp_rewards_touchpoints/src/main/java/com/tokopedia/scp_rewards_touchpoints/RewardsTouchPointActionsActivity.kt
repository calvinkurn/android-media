package com.tokopedia.scp_rewards_touchpoints

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationFragment
import com.tokopedia.scp_rewards_touchpoints.common.di.CelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.common.di.DaggerCelebrationComponent

class RewardsTouchPointActionsActivity : BaseActivity(), HasComponent<CelebrationComponent>{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_act)
        intent.data?.apply {
            if(toString().startsWith(ApplinkConstInternalPromo.CELEBRATION_BOTTOMSHEET_BASE)){
                openCelebrationBottomSheet(pathSegments.last())
            }
            else{
                getNewFragment()
            }
        }
    }


    private fun openCelebrationBottomSheet(slug:String){
        MedalCelebrationBottomSheet.show(supportFragmentManager,slug)
    }

    fun getNewFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.test_act_con, MedalCelebrationFragment(), "getTagFragment()")
            .commit()
    }

    override fun getComponent(): CelebrationComponent {
        return DaggerCelebrationComponent.builder()
            .baseAppComponent((this.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

}
