package com.tokopedia.tokopedianow.shoppinglist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.common.constant.RequestCode
import com.tokopedia.tokopedianow.shoppinglist.presentation.fragment.TokoNowShoppingListFragment
import com.tokopedia.user.session.UserSession

class TokoNowShoppingListActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment = TokoNowShoppingListFragment.newInstance()

    override fun loadLayout() {
        if (UserSession(this).isLoggedIn) {
            attachFragment()
        } else {
            finish()
            openLoginPage()
        }
    }

    private fun openLoginPage() {
        val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
        startActivityForResult(intent, RequestCode.REQUEST_CODE_LOGIN)
    }
}
