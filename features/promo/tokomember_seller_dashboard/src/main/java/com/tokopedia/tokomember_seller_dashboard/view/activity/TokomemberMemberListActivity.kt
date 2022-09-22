package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberMemberListFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class TokomemberMemberListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar?.hide()
    }

    override fun getNewFragment(): Fragment {
        val session = UserSession(this)
        return TokomemberMemberListFragment.getInstance(session.shopId)
    }

}
