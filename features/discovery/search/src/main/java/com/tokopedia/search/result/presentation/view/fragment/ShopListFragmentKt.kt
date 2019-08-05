package com.tokopedia.search.result.presentation.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter

class ShopListFragmentKt: BaseDaggerFragment() {

    companion object {
        private const val SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab"
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"

        @JvmStatic
        fun newInstance(searchParameter: SearchParameter): ShopListFragmentKt {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)

            val shopListFragment = ShopListFragmentKt()
            shopListFragment.arguments = bundle

            return shopListFragment
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getScreenName(): String {
        return SCREEN_SEARCH_PAGE_SHOP_TAB
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}