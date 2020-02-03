package com.tokopedia.sellerhomedrawer.presentation.view.dashboard

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

class SellerDashboardFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        val SCREEN_NAME = "/user/jual"

        @JvmStatic
        fun newInstance() = SellerDashboardFragment()
    }

    override fun getScreenName(): String {
        return "Seller Home"
    }

    override fun initInjector() {

    }


}