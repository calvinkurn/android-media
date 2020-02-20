package com.tokopedia.shop.home.view.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

class ShopHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>() {

    companion object{
        fun createInstance(shopId: String): Fragment {
            val fragment = ShopHomeFragment()
            return fragment
        }
    }

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory()
    }

    override fun getAdapterTypeFactory() = shopHomeAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
    }

    override fun callInitialLoadAutomatically() = false

    override fun loadData(page: Int) {
    }
}