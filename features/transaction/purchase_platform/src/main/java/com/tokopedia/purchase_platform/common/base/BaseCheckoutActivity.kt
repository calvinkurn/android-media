package com.tokopedia.purchase_platform.common.base

import android.os.Bundle

import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.purchase_platform.R

/**
 * @author anggaprasetiyo on 18/04/18.
 */
abstract class BaseCheckoutActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    protected val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.parent_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.extras != null) {
            setupBundlePass(intent.extras)
        }
        initInjector()
        super.onCreate(savedInstanceState)
        initView()
    }

    protected abstract fun initInjector()

    /**
     * Kalalu memang ada bundle data dari intent, mau diapain?
     *
     * @param extras bundle extras dari intent
     */
    protected abstract fun setupBundlePass(extras: Bundle?)

    /**
     * initial wiew atau widget
     */
    protected abstract fun initView()

    override fun getComponent(): BaseAppComponent {
        return (applicationContext as BaseMainApplication).baseAppComponent
    }
}
