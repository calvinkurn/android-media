package com.tokopedia.logisticaddaddress.features.pinpoint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.di.addeditaddress.AddEditAddressComponent
import com.tokopedia.logisticaddaddress.di.addeditaddress.DaggerAddEditAddressComponent
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_BUNDLE

class PinpointPageActivity : BaseSimpleActivity(), HasComponent<AddEditAddressComponent> {

    var fragment: PinpointFragment? = null
    companion object {
        fun createIntent(content: Context, bundle: Bundle): Intent {
            return Intent(content, PinpointPageActivity::class.java).putExtra(EXTRA_BUNDLE, bundle)
        }
    }

    override fun getComponent(): AddEditAddressComponent {
        return DaggerAddEditAddressComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): PinpointFragment? {
        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        if (bundle != null) {
            fragment = PinpointFragment.newInstance(bundle)
        }
        return fragment
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent?.getBundleExtra(EXTRA_BUNDLE)
        bundle?.run { fragment?.onNewIntent(this) }
    }
}
