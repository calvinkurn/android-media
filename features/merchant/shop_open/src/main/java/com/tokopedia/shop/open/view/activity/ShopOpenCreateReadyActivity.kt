package com.tokopedia.shop.open.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.open.view.fragment.ShopOpenCreateReadyFragment

class ShopOpenCreateReadyActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, ShopOpenCreateReadyActivity::class.java)
        }
    }

    override fun getNewFragment(): Fragment {
        return ShopOpenCreateReadyFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.elevation = 0f
        }
        toolbar.visibility = View.GONE
    }


}