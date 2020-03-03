package com.tokopedia.tokopoints.view.pointhistory

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.*

class PointHistoryActivity : BaseSimpleActivity(), HasComponent<TokopointBundleComponent> {
    private val tokoPointComponent: TokopointBundleComponent by lazy { initInjector() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.tp_title_history))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = resources.getDimension(com.tokopedia.design.R.dimen.dp_0)
        }
    }

    override fun getNewFragment(): Fragment {
        return PointHistoryFragment.newInstance(intent.extras)
    }

    override fun getComponent(): TokopointBundleComponent {
        return tokoPointComponent
    }

    private fun initInjector() : TokopointBundleComponent {
        return  DaggerTokopointBundleComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, PointHistoryActivity::class.java)
            intent.putExtras(extras)
            return intent
        }
    }
}
