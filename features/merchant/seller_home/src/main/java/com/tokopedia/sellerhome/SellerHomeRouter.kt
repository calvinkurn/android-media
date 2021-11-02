package com.tokopedia.sellerhome

import android.content.Context
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

    fun getProductManageFragment(filterOptions: ArrayList<String>, searchKeyword: String): Fragment

    fun getChatListFragment(): Fragment
}