package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailFragment: BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>() {

    companion object {
        val PARAM_AFF_ID = "AFF_ID"
        fun newInstance(bundle: Bundle): CommissionDetailFragment {
            val fragment = CommissionDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory



    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
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