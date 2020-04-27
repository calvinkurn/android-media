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
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsOnboardingActivity
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapter
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_smart_bills.*
import java.util.*
import javax.inject.Inject

class SmartBillsFragment : BaseDaggerFragment(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        SmartBillsAdapter.LoaderListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<RechargeBills>,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsViewModel
    private lateinit var sharedPrefs: SharedPreferences

    lateinit var adapter: SmartBillsAdapter

    private var totalPrice = 0
    private var maximumPrice = 0

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

                    // Save maximum price
                    maximumPrice = 0
                    for (bill in it.data.bills) {
                        maximumPrice += bill.amount.toInt()
                    }
                }
                is Fail -> {
                    adapter.showGetListError(it.throwable)
                }
            }
        })

        viewModel.multiCheckout.observe(this, Observer {
            when (it) {
                is Success -> {
                    // Check if all items' transaction succeeds; if they do, navigate to payment
                    if (it.data.attributes.allSuccess) {
                        val paymentPassData = PaymentPassData()
                        paymentPassData.convertToPaymenPassData(it.data)

                        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                    }
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

        // If user is not logged in, redirect to login page
        if (!userSession.isLoggedIn) {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, BaseTopupBillsFragment.REQUEST_CODE_LOGIN)
        } else {
            context?.let {
                // Navigate to onboarding if it's the first time
                val visitedOnboarding = sharedPrefs.getBoolean(SMART_BILLS_VISITED_ONBOARDING, false)
                if (!visitedOnboarding) {
                    //TODO: Put onboarding tracking here
                    if (::sharedPrefs.isInitialized) {
                        sharedPrefs.edit().putBoolean(SMART_BILLS_VISITED_ONBOARDING, true).apply()
                    }

                    startActivityForResult(Intent(it,
                            SmartBillsOnboardingActivity::class.java),
                            REQUEST_CODE_SMART_BILLS_ONBOARDING
                    )
                }
            }

            smart_bills_select_all_checkbox.setOnClickListener {
                toggleAllItems(smart_bills_select_all_checkbox.isChecked)
            }

            rv_smart_bills_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv_smart_bills_items.adapter = adapter

            smart_bills_checkout_view.listener = this

            loadData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> {

                }
            }
        }
    }

    override fun getScreenName(): String {
        return getString(R.string.app_name)
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
        viewModel.getStatementMonths(
                GraphqlHelper.loadRawString(resources, R.raw.query_recharge_statement_months),
                viewModel.createStatementMonthsParams(1)
        )
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
        smart_bills_checkout_view.setVisibilityLayout(adapter.checkedDataList.isNotEmpty())

        if (isChecked) {
            totalPrice += item.amount.toInt()
        } else {
            totalPrice -= item.amount.toInt()
        }
        smart_bills_checkout_view.setTotalPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice, true))

        smart_bills_select_all_checkbox.isChecked = adapter.totalChecked == adapter.dataSize
    }

    override fun onClickNextBuyButton() {
        viewModel.runMultiCheckout(viewModel.createMultiCheckoutParams(adapter.checkedDataList),
                userSession.userId)
    }

    private fun toggleAllItems(value: Boolean) {
        adapter.toggleAllItems(value)

        smart_bills_checkout_view.setVisibilityLayout(value)

        totalPrice = if (value) maximumPrice else 0
        smart_bills_checkout_view.setTotalPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice, true))
    }

    companion object {
        const val SMART_BILLS_PREF = "SMART_BILLS"
        const val SMART_BILLS_VISITED_ONBOARDING = "SMART_BILLS_VISITED_ONBOARDING"

        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_SMART_BILLS_ONBOARDING = 1700
    }
}