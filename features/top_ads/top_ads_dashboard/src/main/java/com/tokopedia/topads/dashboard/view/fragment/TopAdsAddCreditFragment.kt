package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditTypeFactory
import com.tokopedia.topads.dashboard.view.adapter.viewholder.DataCreditViewHolder
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddCreditPresenter
import kotlinx.android.synthetic.main.fragment_top_ads_add_credit.*

import javax.inject.Inject

class TopAdsAddCreditFragment : BaseListFragment<DataCredit, TopAdsCreditTypeFactory>(), TopAdsAddCreditView, DataCreditViewHolder.OnSelectedListener {

    private var selectedCreditPos = -1

    @Inject
    lateinit var presenter: TopAdsAddCreditPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_add_credit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_submit.setOnClickListener { chooseCredit() }
        initErrorNetworkViewModel()
    }

    private fun initErrorNetworkViewModel() {
        adapter.errorNetworkModel = ErrorNetworkModel().apply { iconDrawableRes = R.drawable.ic_error_network }
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
            activity?.let {
                it.setResult(Activity.RESULT_OK)
                val intent = Intent(activity, TopAdsPaymentCreditActivity::class.java).apply {
                    putExtra(TopAdsDashboardConstant.EXTRA_CREDIT, selected)
                }
                startActivity(intent)
                it.finish()
            }
        }
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