package com.tokopedia.digital_checkout.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartFragment: BaseDaggerFragment() {

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(DigitalCheckoutComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_checkout, container, false)
    }
}