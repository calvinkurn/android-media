package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import kotlinx.android.synthetic.main.fragment_emoney_pdp.*

/**
 * @author by jessica on 29/03/21
 */

class EmoneyPdpFragment : BaseDaggerFragment() {
    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_emoney_pdp, container, false)
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //dummmy
        emoneyPdpHeaderView.titleText = "Cek saldo kamu di sini"
        emoneyPdpHeaderView.subtitleText = "Yuk, coba cek sisa saldo di kartu e-money kamu."
        emoneyPdpHeaderView.buttonCtaText = "Cek Saldo"

        emoneyPdpInputCardWidget.initView()
    }
}