package com.tokopedia.digital.home.presentation.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.di.DigitalHomePageComponent
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageItemModel
import com.tokopedia.digital.home.presentation.viewmodel.DigitalHomePageViewModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import javax.inject.Inject

class DigitalHomePageFragment : BaseListFragment<DigitalHomePageItemModel, DigitalHomePageTypeFactory>(), OnItemBindListener {

    @Inject
    lateinit var viewModel : DigitalHomePageViewModel

    override fun onBannerItemDigitalBind() {
        viewModel.getBannerList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_banner))
    }

    override fun onCategoryItemDigitalBind() {
        viewModel.getCategoryList(GraphqlHelper.loadRawString(resources, R.raw.query_digital_home_category))
    }

    override fun onPromoItemDigitalBind() {
        //nothing to do, api not ready yet
    }

    override fun getAdapterTypeFactory(): DigitalHomePageTypeFactory {
        return DigitalHomePageTypeFactory()
    }

    override fun onItemClicked(t: DigitalHomePageItemModel?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalHomePageComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.digitalHomePageList.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })
    }

    override fun loadData(page: Int) {
        viewModel.getInitialList()
    }

    companion object{
        fun getInstance() = DigitalHomePageFragment()
    }
}
