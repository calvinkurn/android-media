package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashCreateCardFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashIntroFragment

class TokomemberDashCreateCardActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return TokomemberDashCreateCardFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

}