package com.tokopedia.topads.debit.autotopup.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.extensions.selectedPrice
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsAutoTopUpPriceTypeFactory
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsAutoTopUpPriceViewHolder
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_top_ads_auto_topup.*
import javax.inject.Inject

class TopAdsAutoTopUpFragment:
        BaseListFragment<AutoTopUpItem, TopAdsAutoTopUpPriceTypeFactory>(),
        TopAdsAutoTopUpPriceViewHolder.ItemListener {
    private var selectedItem = AutoTopUpItem()
    lateinit var viewModel: TopAdsAutoTopUpViewModel
    private var isChanged: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(TopAdsAutoTopUpViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAutoTopUpStatus.observe(this, android.arch.lifecycle.Observer { when (it) {
            is Success -> onSuccessGetAutoTopUp(it.data)
            is Fail -> onErrorGetAutoTopUp(it.throwable)
        } })
        viewModel.statusSaveSelection.observe(this, Observer { when(it){
            is Loading -> progress_bar.visibility = View.VISIBLE
            is ResponseSaving -> {
                progress_bar.visibility = View.GONE
                handleResponseSaving(it)
            }
        } })
    }

    private fun handleResponseSaving(it: ResponseSaving?) {
        if (it == null) return
        if (it.isSuccess){
            sendResultIntentOk()
        } else {
            ToasterError.make(view, ErrorHandler.getErrorMessage(context, it.throwable))
                    .setAction(R.string.close){}.show()
        }
    }

    private fun sendResultIntentOk() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        }
    }

    private fun onErrorGetAutoTopUp(throwable: Throwable) {
        super.showGetListError(throwable)
        app_bar_layout.visibility = View.GONE
    }

    private fun onSuccessGetAutoTopUp(data: AutoTopUpStatus) {
        app_bar_layout.visibility = View.VISIBLE
        selectedItem = data.selectedPrice
        min_auto_topup_descr.text = getString(R.string.descr_min_autotopup, selectedItem.minCreditFmt)
        auto_topup_status.text = data.statusDesc
        auto_topup_toggle.isChecked = data.status.toInt() != 0
        addListenerToToggle()
        super.renderList(data.availableNominals)
    }

    private fun addListenerToToggle() {
        auto_topup_toggle.setOnCheckedChangeListener { _, isChecked ->
            isChanged = true
            notifyButtonSubmit()

            auto_topup_status.setText(if (isChecked) R.string.topads_active
            else R.string.topads_inactive)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_auto_topup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).addItemDecoration(DividerItemDecoration(activity))

        button_submit.setOnClickListener { viewModel.saveSelection(GraphqlHelper
                .loadRawString(resources, R.raw.gql_topads_save_auto_topup_selection),
                auto_topup_toggle.isChecked, selectedItem) }
        notifyButtonSubmit()
        loadInitialData()
    }

    private fun notifyButtonSubmit() {
        button_submit.isEnabled = isChanged
    }

    override fun onDestroyView() {
        viewModel.getAutoTopUpStatus.removeObservers(this)
        viewModel.statusSaveSelection.removeObservers(this)
        super.onDestroyView()
    }

    override fun callInitialLoadAutomatically() = false

    override fun getAdapterTypeFactory() =  TopAdsAutoTopUpPriceTypeFactory(this)

    override fun onItemClicked(t: AutoTopUpItem?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        viewModel.getAutoTopUpStatusFull(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_package_auto_topup))
    }

    override fun onDestroy() {
        viewModel.clear()
        super.onDestroy()
    }

    override fun isActive(): Boolean = auto_topup_toggle.isChecked

    override fun isSelected(id: Int): Boolean = id == selectedItem.id

    override fun onSelected(element: AutoTopUpItem) {
        if (isActive()) {
            if (selectedItem.id != element.id) {
                isChanged = true
                notifyButtonSubmit()

                selectedItem = element
                min_auto_topup_descr.text = getString(R.string.descr_min_autotopup, selectedItem.minCreditFmt)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {

        @JvmStatic
        fun createInstance(): Fragment = TopAdsAutoTopUpFragment()
    }
}