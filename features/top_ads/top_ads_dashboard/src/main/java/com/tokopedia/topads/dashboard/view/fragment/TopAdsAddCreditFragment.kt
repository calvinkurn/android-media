package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditTypeFactory
import com.tokopedia.topads.dashboard.view.adapter.viewholder.DataCreditViewHolder
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddCreditPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_top_ads_add_credit.*

import javax.inject.Inject

class TopAdsAddCreditFragment : BaseListFragment<DataCredit, TopAdsCreditTypeFactory>(), TopAdsAddCreditView, DataCreditViewHolder.OnSelectedListener {

    private var selectedCreditPos = -1

    @Inject
    lateinit var presenter: TopAdsAddCreditPresenter
    private var userSession: UserSessionInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_add_credit, container, false)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_submit.setOnClickListener { chooseCredit() }
        initErrorNetworkViewModel()
    }

    private fun initErrorNetworkViewModel() {
        adapter.errorNetworkModel = ErrorNetworkModel().apply { iconDrawableRes = com.tokopedia.abstraction.R.drawable.unify_globalerrors_connection }
    }

    override fun loadData(page: Int) {
        populateData()
    }

    override fun getAdapterTypeFactory(): TopAdsCreditTypeFactory {
        return TopAdsCreditTypeFactory(this)
    }


    private fun populateData() {
        button_submit.visibility = View.GONE
        presenter.populateCreditList()
    }

    override fun onCreditListLoaded(creditList: List<DataCredit>) {
        super.renderList(creditList)
        if (!creditList.isEmpty() && selectedCreditPos < 0) {
            select(getDefaultSelection(creditList))
        }

        if (creditList.isEmpty()) {
            button_submit.visibility = View.GONE
            button_submit.isEnabled = false
        } else {
            button_submit.visibility = View.VISIBLE
            button_submit.isEnabled = true
        }
    }

    override fun onLoadCreditListError() {
        super.showGetListError(null)
    }

    private fun getDefaultSelection(creditList: List<DataCredit>): Int {
        return creditList.withIndex().filter { it.value.selected > 0 }.map { it.index }.firstOrNull() ?: 0
    }

    private fun chooseCredit() {
        if (selectedCreditPos > -1) {
            val selected = adapter.data[selectedCreditPos]
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, resources.getString(R.string.title_top_ads_add_credit), getUrl(selected))
        }
    }

    fun getUrl(selected: DataCredit): String {
        userSession = UserSession(activity)
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(selected.productUrl),
                userSession?.deviceId,
                userSession?.userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_SELECTION_POSITION, selectedCreditPos)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState == null) {
            return
        }
        select(savedInstanceState.getInt(EXTRA_SELECTION_POSITION))
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String? = null

    override fun onItemClicked(dataCredit: DataCredit) {}

    override fun isPositionChecked(pos: Int): Boolean = (pos == selectedCreditPos)

    override fun select(pos: Int) {
        selectedCreditPos = pos
        adapter.notifyDataSetChanged()
    }

    companion object {

        private val EXTRA_SELECTION_POSITION = "EXTRA_SELECTION_POSITION"

        @JvmStatic
        fun createInstance(): Fragment = TopAdsAddCreditFragment()
    }
}
