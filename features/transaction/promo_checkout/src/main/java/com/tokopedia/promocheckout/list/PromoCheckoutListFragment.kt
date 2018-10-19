package com.tokopedia.promocheckout.list

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.promocheckout.list.di.DaggerPromoCheckoutListComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.list.model.PromoCheckoutListModel
import javax.inject.Inject

class PromoCheckoutListFragment : BaseListFragment<PromoCheckoutListModel, PromoCheckoutListAdapterFactory>(), PromoCheckoutListContract.View {

    @Inject
    lateinit var promoCheckoutListPresenter: PromoCheckoutListPresenter

    override fun getAdapterTypeFactory(): PromoCheckoutListAdapterFactory {
        return PromoCheckoutListAdapterFactory()
    }

    override fun onItemClicked(t: PromoCheckoutListModel?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerPromoCheckoutListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutListModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutListPresenter.attachView(this)
    }

    override fun loadData(page: Int) {
        promoCheckoutListPresenter.getListPromo(page)
    }

    companion object {
        fun createInstance():PromoCheckoutListFragment{
            return PromoCheckoutListFragment()
        }
    }

}
