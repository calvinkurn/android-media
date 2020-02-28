package com.tokopedia.liveness.view.activity

import com.tokopedia.liveness.di.DaggerLivenessDetectionComponent
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.fragment.LivenessErrorFragment
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent

class LivenessFailedActivity : BaseSimpleActivity(), HasComponent<LivenessDetectionComponent> {

    override fun getComponent(): LivenessDetectionComponent {
        return DaggerLivenessDetectionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    fun updateToolbarTitle(strId: Int){
        supportActionBar?.setTitle(strId)
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
        bundle.putInt(FAILED_TYPE, intent.getIntExtra(FAILED_TYPE, DEFAULT_FAILED))
        val fragment = LivenessErrorFragment.newInstance()
        fragment.arguments = bundle

        return fragment
    }

    companion object {
        const val FAILED_TYPE = "failed_type"
        const val DEFAULT_FAILED = -1
    }
}
