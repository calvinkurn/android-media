package com.tokopedia.smartbills.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsOnboardingActivity
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapter
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_smart_bills.*
import java.util.*
import javax.inject.Inject

class SmartBillsFragment : BaseTopupBillsFragment(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        SmartBillsAdapter.LoaderListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<RechargeBills>,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsViewModel
    private lateinit var sharedPrefs: SharedPreferences

    lateinit var adapter: SmartBillsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_smart_bills, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SmartBillsViewModel::class.java)
            sharedPrefs = it.getSharedPreferences(SMART_BILLS_PREF, Context.MODE_PRIVATE)

            adapter = SmartBillsAdapter(it, SmartBillsAdapterFactory(this), this, this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.statementMonths.observe(this, Observer {

        })

        viewModel.statementBills.observe(this, Observer {
            when (it) {
                is Success -> {
                    adapter.renderList(it.data.bills)
                }
                is Fail -> {
                    adapter.showGetListError(it.throwable)
                }
            }
        })

        viewModel.multiCheckout.observe(this, Observer {
            when (it) {
                is Success -> {
                    // Check if all items' transaction succeeds
                    if (it.data.attributes.allSuccess) navigateToPayment(it.data)
                    else { //TODO: Handle partial success

                    }
                }
                is Fail -> {

                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            // Navigate to onboarding if it's the first time
            val visitedOnboarding = sharedPrefs.getBoolean(SMART_BILLS_VISITED_ONBOARDING, false)
            if (visitedOnboarding) {
                startActivityForResult(Intent(it,
                        SmartBillsOnboardingActivity::class.java),
                        REQUEST_CODE_SMART_BILLS_ONBOARDING
                )
            }
        }

        smart_bills_select_all_checkbox.setOnClickListener {
            adapter.toggleAllItems(smart_bills_select_all_checkbox.isChecked)
        }

        smart_bills_checkout_view.listener = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SMART_BILLS_ONBOARDING -> {
                    //TODO: Put onboarding tracking here
                    if (::sharedPrefs.isInitialized) {
                        sharedPrefs.edit().putBoolean(SMART_BILLS_VISITED_ONBOARDING, true).apply()
                    }
                }
            }
        }
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return smart_bills_checkout_view
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun isChecked(position: Int): Boolean {
        return adapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        adapter.updateListByCheck(isChecked, position)
    }

    override fun loadData() {
//        viewModel.getStatementMonths(
//                GraphqlHelper.loadRawString(resources, R.raw.query_recharge_statement_months),
//                viewModel.createStatementMonthsParams(1)
//        )
        val calendarInstance = Calendar.getInstance()
        viewModel.getStatementBills(
                GraphqlHelper.loadRawString(resources, R.raw.query_recharge_statement_bills),
                viewModel.createStatementBillsParams(
                        calendarInstance.get(Calendar.MONTH),
                        calendarInstance.get(Calendar.YEAR)
                ),
                true
        )
    }

    override fun onItemChecked(item: RechargeBills, isChecked: Boolean) {
        if (isChecked) {
            price += item.amount.toInt()
        } else {
            price -= item.amount.toInt()
        }

        smart_bills_select_all_checkbox.isChecked = adapter.totalChecked == adapter.dataSize
    }

    override fun onClickNextBuyButton() {
        processTransaction()
    }

    override fun processCheckout() {
        viewModel.runMultiCheckout(viewModel.createMultiCheckoutParams(adapter.checkedDataList),
                userSession.userId)
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        TODO("Not yet implemented")
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        TODO("Not yet implemented")
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        TODO("Not yet implemented")
    }

    override fun onEnquiryError(error: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onMenuDetailError(error: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onCatalogPluginDataError(error: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onFavoriteNumbersError(error: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onCheckVoucherError(error: Throwable) {
        TODO("Not yet implemented")
    }

    override fun onExpressCheckoutError(error: Throwable) {
        TODO("Not yet implemented")
    }

    companion object {
        const val SMART_BILLS_PREF = "SMART_BILLS"
        const val SMART_BILLS_VISITED_ONBOARDING = "SMART_BILLS_VISITED_ONBOARDING"

        const val REQUEST_CODE_SMART_BILLS_ONBOARDING = 1700
    }
}