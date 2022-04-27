package com.tokopedia.liveness.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.liveness.di.DaggerLivenessDetectionComponent
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.fragment.LivenessErrorFragment

class LivenessFailedActivity : BaseSimpleActivity(), HasComponent<LivenessDetectionComponent> {

    override fun getComponent(): LivenessDetectionComponent {
        return DaggerLivenessDetectionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    fun updateToolbarTitle(strId: Int){
        supportActionBar?.setTitle(strId)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
        if (fragment != null && fragment is OnBackListener) {
            (fragment as OnBackListener).trackOnBackPressed()
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = intent?.extras ?: Bundle()
        return LivenessErrorFragment.newInstance(bundle)
    }
}
