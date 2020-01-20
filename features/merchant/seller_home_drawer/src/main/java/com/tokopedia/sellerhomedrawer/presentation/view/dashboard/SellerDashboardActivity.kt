package com.tokopedia.sellerhomedrawer.presentation.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.drawer.SellerDrawerPresenterActivity

class SellerDashboardActivity<T>: SellerDrawerPresenterActivity<T>() {

    companion object {
        @JvmStatic
        val TAG = SellerDashboardActivity::class.java.simpleName

        @JvmStatic
        fun createInstance(context: Context) = Intent(context, SellerDashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateView(R.layout.sh_activity_simple_fragment)
        if (savedInstanceState != null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, )

        }

    }
}