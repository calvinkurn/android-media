package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.fragment_shipping_duration.*

class ShippingDurationFragment : BaseDaggerFragment(){

    val adapter = ShippingDurationItemAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_duration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shipping_duration_rv.adapter = adapter
    }

}