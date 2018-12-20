package com.tokopedia.flashsale.management.view.fragment

import android.os.Bundle
import android.support.annotation.Px
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.DataCampaignList
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.ekstension.toCampaignViewModel
import com.tokopedia.flashsale.management.view.activity.CampaignDetailActivity
import com.tokopedia.flashsale.management.view.adapter.CampaignAdapterTypeFactory
import com.tokopedia.flashsale.management.view.presenter.CampaignPresenter
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_list_campaign.*
import javax.inject.Inject

abstract class BaseCampaignFragment : BaseSearchListFragment<CampaignViewModel, CampaignAdapterTypeFactory>() {

    @Inject
    lateinit var presenter: CampaignPresenter
    @Px
    private var tempTopPaddingRecycleView: Int = 0
    @Px
    private var tempBottomPaddingRecycleView: Int = 0
    private var scrollFlags: Int = 0
    private val appBarBehaviour = AppBarLayout.Behavior()

    override fun onCreate(savedInstanceState: Bundle?) {
        GraphqlClient.init(context!!)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_list_campaign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempTopPaddingRecycleView = recycler_view.paddingTop
        tempBottomPaddingRecycleView = recycler_view.paddingBottom
        scrollFlags = (searchInputView.layoutParams as AppBarLayout.LayoutParams).scrollFlags
        searchInputView.setResetListener {
            searchInputView.searchText = ""
            loadInitialData()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<CampaignViewModel, CampaignAdapterTypeFactory> {
        val adapter =  super.createAdapterInstance()
        adapter.errorNetworkModel = ErrorNetworkModel().apply {
            iconDrawableRes = R.drawable.ic_error_cloud_green
        }
        return adapter
    }

    override fun onItemClicked(t: CampaignViewModel) {
        activity?.let {
            startActivity(CampaignDetailActivity.createIntent(it, t.id, t.campaignUrl, t.campaignType, t.name))
        }
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): CampaignAdapterTypeFactory {
        return CampaignAdapterTypeFactory()
    }

    fun onSuccessGetCampaignList(data: DataCampaignList.ResponseData) {
        showSearchInputView()
        val list = data.data.list.map { it.toCampaignViewModel() }
        renderList(list, data.data.totalData > adapter.dataSize)
    }

    fun onErrorGetCampaignList(throwable: Throwable) {
        showGetListError(throwable)
    }

    open fun onSuccessGetCampaignLabel(data: DataCampaignLabel) {}

    fun onErrorGetCampaignLabel(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    protected fun showSearchView(show: Boolean) {
        @Px var topPadding = 0
        @Px var bottomPadding = 0
        try {
            if (show) {
                topPadding = tempTopPaddingRecycleView
                bottomPadding = tempBottomPaddingRecycleView
            }
            recycler_view.setPadding(0, topPadding, 0, bottomPadding)
            if (app_bar_layout != null) {
                val seacrhViewParams = searchInputView.layoutParams as AppBarLayout.LayoutParams
                seacrhViewParams.scrollFlags = if (show) scrollFlags else 0
                searchInputView.layoutParams = seacrhViewParams

                val appBarLayoutParams = app_bar_layout.layoutParams as CoordinatorLayout.LayoutParams
                appBarLayoutParams.behavior = if (show) appBarBehaviour else null
                app_bar_layout.layoutParams = appBarLayoutParams
            }
            if (searchInputView != null) {
                searchInputView.visibility = if (show) View.VISIBLE else View.GONE
            }
        } catch (e:Exception) {

        }
    }

    override fun showSearchInputView() {
        showSearchView(true)
    }

    override fun hideSearchInputView() {
        showSearchView(false)
    }

    companion object {
        // campaign info backend process to get campaign list is heavy, so default row is minimized
        const val DEFAULT_ROWS = 10
        const val CAMPAIGN_TYPE = 1
    }
}