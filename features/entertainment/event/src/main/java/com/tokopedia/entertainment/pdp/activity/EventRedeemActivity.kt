package com.tokopedia.entertainment.pdp.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.fragment.EventRedeemFragment

class EventRedeemActivity : BaseSimpleActivity(), HasComponent<EventPDPComponent> {

    private var urlRedeem: String = ""

    override fun getComponent(): EventPDPComponent = DaggerEventPDPComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (intent.extras != null) {
            urlRedeem = intent.getStringExtra(EXTRA_URL_REDEEM)
        } else if (savedInstanceState != null) {
            urlRedeem = savedInstanceState.getString(urlRedeem, "")
        }

        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? = EventRedeemFragment.newInstance(urlRedeem)

    companion object {
        const val EXTRA_URL_REDEEM = "EXTRA_URL_REDEEM"
    }
}