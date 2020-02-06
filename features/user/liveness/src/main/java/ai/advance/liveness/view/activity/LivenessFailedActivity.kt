package ai.advance.liveness.view.activity

import ai.advance.liveness.di.DaggerLivenessDetectionComponent
import ai.advance.liveness.di.LivenessDetectionComponent
import ai.advance.liveness.view.OnBackListener
import ai.advance.liveness.view.fragment.LivenessErrorFragment
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
        val intent = intent
        val bundle = Bundle()
        bundle.putInt("failed_type", intent.getIntExtra("failed_type", -1))
        val fragment = LivenessErrorFragment.newInstance()
        fragment.arguments = bundle

        return fragment
    }
}
