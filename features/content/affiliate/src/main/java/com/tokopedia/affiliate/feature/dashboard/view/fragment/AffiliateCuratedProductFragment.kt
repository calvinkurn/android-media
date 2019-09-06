package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateProductBoughtPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.applink.RouteManager
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductFragment : BaseListFragment<DashboardItemViewModel, DashboardItemTypeFactory>(), AffiliateCuratedProductContract.View {

    companion object {
        private const val EXTRA_TYPE = "type"

        fun newInstance(type: Int?): AffiliateCuratedProductFragment {
            val fragment = AffiliateCuratedProductFragment()
            val args = Bundle()
            type?.let { args.putInt(EXTRA_TYPE, type) }
            fragment.arguments = args
            return fragment
        }
    }

    override val ctx: Context?
        get() = context

    private var cursor: String = ""

    private val type: Int? by lazy { if (arguments?.containsKey(EXTRA_TYPE) == true) arguments?.getInt(EXTRA_TYPE) else null }

    @Inject
    lateinit var presenter: AffiliateProductBoughtPresenter

    override fun getAdapterTypeFactory(): DashboardItemTypeFactory {
        return DashboardItemTypeFactoryImpl(object : DashboardItemTypeFactoryImpl.OnClickListener {
            override fun onDashboardItemClickedListener(item: DashboardItemViewModel) {
                onItemClicked(item)
            }

            override fun onBuyClick(appLink: String) {
                openApplink(appLink)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        return inflater.inflate(R.layout.fragment_af_product_bought, container, false)
    }

    override fun onItemClicked(t: DashboardItemViewModel?) {

    }

    override fun getScreenName(): String = "Product Bought"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun loadData(page: Int) {
        if (page == 0) cursor = ""
        presenter.loadProductBoughtByType(type, cursor)
    }

    override fun onErrorGetDashboardItem(error: String) {

    }

    override fun onSuccessLoadMoreDashboardItem(itemList: List<DashboardItemViewModel>, cursor: String) {
        if (this.cursor.isEmpty()) clearAllData()
        renderList(itemList)
        this.cursor = if (itemList.isEmpty()) "" else cursor
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun openApplink(applink: String) {
        if (RouteManager.isSupportApplink(context, applink)) {
            RouteManager.route(context, applink)
        }
    }
}