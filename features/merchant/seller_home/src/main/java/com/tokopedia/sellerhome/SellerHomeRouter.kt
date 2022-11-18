package com.tokopedia.sellerhome

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

interface SellerHomeRouter {

    fun getSomListFragment(
        context: Context,
        tabPage: String?,
        orderType: String,
        searchKeyword: String,
        orderId: String
    ): Fragment

    fun getProductManageFragment(
        filterOptions: ArrayList<String>,
        searchKeyword: String,
        tab: String,
        navigationMenu: View?
    ): Fragment

    fun getChatListFragment(): Fragment
}