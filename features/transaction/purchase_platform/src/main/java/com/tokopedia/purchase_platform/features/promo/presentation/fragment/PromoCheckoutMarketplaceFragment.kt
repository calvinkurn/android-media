package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapterTypeFactory

class PromoCheckoutMarketplaceFragment: BaseListFragment<Visitable<*>, PromoCheckoutAdapterTypeFactory>() {

    override fun getAdapterTypeFactory(): PromoCheckoutAdapterTypeFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(t: Visitable<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}