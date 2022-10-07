package com.tokopedia.deals.checkout.ui.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

class DealsCheckoutFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    companion object {
        fun createInstance(): DealsCheckoutFragment {
            val fragment = DealsCheckoutFragment()
            return fragment
        }
    }
}
