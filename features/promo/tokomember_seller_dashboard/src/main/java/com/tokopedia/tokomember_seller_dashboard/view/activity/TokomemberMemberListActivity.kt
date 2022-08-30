package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberMemberListFragment

class TokomemberMemberListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return TokomemberMemberListFragment.getInstance()
    }

}