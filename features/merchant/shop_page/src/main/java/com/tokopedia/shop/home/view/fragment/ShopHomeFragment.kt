package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.shop.home.HomeConstant
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

class ShopHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>() {

    companion object {
        fun createInstance(shopId: String): Fragment {
            val fragment = ShopHomeFragment()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitialData()
    }

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory> {
        return ShopHomeAdapter(context, HomeConstant.videoWidget, shopHomeAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory() = shopHomeAdapterTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadInitialData() {
        adapter.addElement(HomeConstant.tripleItemImage)
        adapter.addElement(HomeConstant.sliderSquareWidget)
        adapter.addElement(HomeConstant.sliderBannerWidget)
        adapter.addElement(HomeConstant.videoWidget)
    }

    override fun getScreenName() = ""

    override fun initInjector() {}

    override fun callInitialLoadAutomatically() = false

    override fun loadData(page: Int) {
    }

}