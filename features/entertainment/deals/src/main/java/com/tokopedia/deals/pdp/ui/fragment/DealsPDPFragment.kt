package com.tokopedia.deals.pdp.ui.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity.Companion.EXTRA_PRODUCT_ID

class DealsPDPFragment: BaseDaggerFragment() {

    var productId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = arguments?.getString(EXTRA_PRODUCT_ID, "") ?: ""
        super.onCreate(savedInstanceState)
    }

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    companion object {

        fun createInstance(productId: String?): DealsPDPFragment {
            val fragment = DealsPDPFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            fragment.arguments = bundle
            return fragment
        }
    }
}