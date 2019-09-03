package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.adapter.AffiliateDashboardAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.DashboardAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.AffiliateDashboardItemTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateDashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateDashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.presenter.DashboardPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-02.
 */
class AffiliateDashboardFragment : BaseDaggerFragment(), AffiliateDashboardContract.View {

    companion object {
        fun newInstance(bundle: Bundle): AffiliateDashboardFragment {
            return AffiliateDashboardFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var presenter: AffiliateDashboardPresenter

    private lateinit var rvContent: RecyclerView

    private lateinit var adapter: AffiliateDashboardAdapter

    override val ctx: Context?
        get() = context

    override fun getScreenName(): String = "Dashboard"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_af_affiliate_dashboard, container, false)
        presenter.attachView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        adapter = AffiliateDashboardAdapter(AffiliateDashboardItemTypeFactoryImpl(this), mutableListOf())
        rvContent = view.findViewById(R.id.rv_content)
        rvContent.adapter = adapter
        rvContent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        presenter.checkAffiliate()
    }

    override fun hideLoading() {
        adapter.hideLoading()
    }

    override fun onSuccessGetDashboardItem(header: DashboardHeaderViewModel) {
        adapter.apply {
            clearAllElements()
            addElement(header)
            notifyDataSetChanged()
        }
    }

    override fun onErrorCheckAffiliate(error: String) {
        NetworkErrorHelper.showEmptyState(activity,
                view,
                error
        ) { presenter.checkAffiliate() }
    }

    override fun onSuccessCheckAffiliate(isAffiliate: Boolean) {
        if (isAffiliate) presenter.loadDashboardDetail()
        else closePage()
    }

    override fun onUserNotLoggedIn() {
        closePage()
    }

    private fun closePage() = activity?.finish()
}