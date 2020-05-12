package com.tokopedia.topads.debit.autotopup.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.debit.autotopup.data.extensions.selectedPrice
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.data.model.Loading
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsAutoTopUpPriceTypeFactory
import com.tokopedia.topads.debit.autotopup.view.adapter.viewholder.TopAdsAutoTopUpPriceViewHolder
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.Toaster
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
        viewModel.getAutoTopUpStatus.observe(this, androidx.lifecycle.Observer { when (it) {
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

    private fun handleResponseSaving(r: ResponseSaving?) {
        if (r == null) return
        if (r.isSuccess){
            sendResultIntentOk()
        } else {
            view?.let {
                Toaster.make(it, ErrorHandler.getErrorMessage(context, r.throwable), Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                        getString(com.tokopedia.abstraction.R.string.close), View.OnClickListener { })
            }
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
        auto_topup_toggle.isChecked = data.status.toIntOrZero() != 0
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

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
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
        viewModel.flush()
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