package com.tokopedia.tokopedianow.shoppinglist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.shoppinglist.presentation.fragment.TokoNowShoppingListFragment

class TokoNowShoppingListActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment = TokoNowShoppingListFragment.newInstance()
}
