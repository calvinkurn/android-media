package com.tokopedia.updateinactivephone.features.inputoldphone.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.updateinactivephone.common.analytics.InputOldPhoneNumberAnalytics
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.di.InactivePhoneComponentBuilder
import com.tokopedia.updateinactivephone.features.inputoldphone.fragment.InputOldPhoneNumberFragment
import javax.inject.Inject

class InputOldPhoneNumberActivity : BaseSimpleActivity(),
    HasComponent<InactivePhoneComponent> {

    @Inject
    lateinit var analytics: InputOldPhoneNumberAnalytics

    private var inactivePhoneNumberComponent: InactivePhoneComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return InputOldPhoneNumberFragment.newInstance(bundle)
    }

    override fun getComponent(): InactivePhoneComponent =
        inactivePhoneNumberComponent ?: initializeInactivePhoneNumber()

    private fun initializeInactivePhoneNumber(): InactivePhoneComponent {
        return InactivePhoneComponentBuilder.getComponent(application)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                analytics.trackPageInactivePhoneNumberClickBack()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}