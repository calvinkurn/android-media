package com.tokopedia.entertainment.pdp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.entertainment.pdp.activity.EventRedeemActivity.Companion.EXTRA_URL_REDEEM
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import kotlinx.android.synthetic.main.fragment_event_redeem.*

class EventRedeemFragment : BaseDaggerFragment(){

    private var urlRedeem: String? = ""

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_redeem, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlRedeem = arguments?.getString(EXTRA_URL_REDEEM, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tg_url_redeem.text = urlRedeem
    }
    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    companion object{
        fun newInstance(urlRedeem: String) = EventRedeemFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_REDEEM, urlRedeem)
            }
        }
    }
}