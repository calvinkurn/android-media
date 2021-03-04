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
import com.tokopedia.unifyprinciples.R

class LivenessFailedActivity : BaseSimpleActivity(), HasComponent<LivenessDetectionComponent> {

    override fun getComponent(): LivenessDetectionComponent {
        return DaggerLivenessDetectionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    fun updateToolbarTitle(strId: Int){
        supportActionBar?.setTitle(strId)
        toolbar.setTitleTextColor(MethodChecker.getColor(this, R.color.Unify_N700_96))
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
        if (fragment != null && fragment is OnBackListener) {
            (fragment as OnBackListener).trackOnBackPressed()
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putInt(LivenessConstants.ARG_FAILED_TYPE, intent.getIntExtra(LivenessConstants.ARG_FAILED_TYPE, DEFAULT_FAILED_TYPE))
        val fragment = LivenessErrorFragment.newInstance()
        fragment.arguments = bundle

        return fragment
    }

    companion object {
        const val DEFAULT_FAILED_TYPE = -1
    }
}
