package com.tokopedia.purchase_platform.common.base

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent

/**
 * @author anggaprasetiyo on 18/04/18.
 */
abstract class BaseCheckoutActivity : BaseSimpleActivity(), HasComponent<BaseAppComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.extras != null) {
            setupBundlePass(intent.extras)
        }
        super.onCreate(savedInstanceState)
        initView()
    }

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
