package com.tokopedia.scp_rewards_touchpoints

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationBottomSheet
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.view.MedalCelebrationFragment
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.viewmodel.MedalCelebrationViewModel
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Loading
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.common.di.CelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.common.di.DaggerCelebrationComponent
import com.tokopedia.scp_rewards_touchpoints.toaster.model.ScpRewardsToasterModel
import com.tokopedia.scp_rewards_touchpoints.toaster.viewmodel.ScpToasterViewModel
import com.tokopedia.scp_rewards_touchpoints.view.toaster.ScpRewardsToaster
import javax.inject.Inject

class TestActivity : BaseSimpleActivity(), HasComponent<CelebrationComponent>{
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_test)
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        return MedalCelebrationFragment()
    }

    override fun getComponent(): CelebrationComponent {
        return DaggerCelebrationComponent.builder()
            .baseAppComponent((this.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

}
