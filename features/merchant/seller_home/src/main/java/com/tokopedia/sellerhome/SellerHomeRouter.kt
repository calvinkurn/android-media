package com.tokopedia.sellerhome

import androidx.fragment.app.Fragment

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

interface SellerHomeRouter {

    fun getSomListFragment(tabPage: String?): Fragment

    fun getProductManageFragment(filterOptions: ArrayList<String>, searchKeyword: String): Fragment

    fun getChatListFragment(): Fragment
}