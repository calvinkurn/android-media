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
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsOnboardingActivity
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapter
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
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
    private lateinit var performanceMonitoring: PerformanceMonitoring

    lateinit var adapter: SmartBillsAdapter

    private var totalPrice = 0
    private var maximumPrice = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_smart_bills, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize performance monitoring
        performanceMonitoring = PerformanceMonitoring.start(RECHARGE_SMART_BILLS_PAGE_PERFORMANCE)

        activity?.let {
            (it as BaseSimpleActivity).updateTitle(getString(R.string.smart_bills_action_bar_title))

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SmartBillsViewModel::class.java)
            sharedPrefs = it.getSharedPreferences(SMART_BILLS_PREF, Context.MODE_PRIVATE)

            adapter = SmartBillsAdapter(it, SmartBillsAdapterFactory(this), this, this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.statementMonths.observe(this, Observer {
            when (it) {
                is Success -> {
                    // TODO: Run statement bills query with info from statement months
                }
                is Fail -> {
                    view_smart_bills_shimmering.hide()
                    adapter.showGetListError(it.throwable)
                }
            }
        })

        viewModel.statementBills.observe(this, Observer {
            performanceMonitoring.stopTrace()

            view_smart_bills_shimmering.hide()
            when (it) {
                is Success -> {
                    if (it.data.bills.isNotEmpty()) {
                        view_smart_bills_select_all_checkbox_container.show()
                        adapter.renderList(it.data.bills)

                        // Save maximum price
                        maximumPrice = 0
                        for (bill in it.data.bills) {
                            maximumPrice += bill.amount.toInt()
                        }
                    } else {
                        adapter.renderEmptyState(
                                getString(R.string.smart_bills_empty_state_title),
                                getString(R.string.smart_bills_empty_state_description))
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
                    } else { // Else, show error message in affected items
                        NetworkErrorHelper.showRedSnackbar(activity, getString(R.string.smart_bills_checkout_error))

                        for (errorItem in it.data.attributes.errors) {
                            if (errorItem.index >= 0) {
                                val bill = adapter.data[errorItem.index]
                                bill.errorMessage = if (errorItem.errorMessage.isNotEmpty()) {
                                    errorItem.errorMessage
                                } else {
                                    getString(R.string.smart_bills_item_default_error)
                                }
                                adapter.data[errorItem.index] = bill
                                adapter.notifyItemChanged(errorItem.index)
                            }
                        }
                    }
                }
                is Fail -> {
                    NetworkErrorHelper.showRedSnackbar(activity, it.throwable.message)
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
            context?.let { context ->
                if (::sharedPrefs.isInitialized) {
                    // Navigate to onboarding if it's the first time
                    val visitedOnboarding = sharedPrefs.getBoolean(SMART_BILLS_VISITED_ONBOARDING, false)
                    if (!visitedOnboarding) {
                        //TODO: Put onboarding tracking here
                        sharedPrefs.edit().putBoolean(SMART_BILLS_VISITED_ONBOARDING, true).apply()

                        startActivityForResult(Intent(context,
                                SmartBillsOnboardingActivity::class.java),
                                REQUEST_CODE_SMART_BILLS_ONBOARDING
                        )
                    }
                }

                // Setup ticker
                ticker_smart_bills.setTextDescription(String.format(getString(R.string.smart_bills_ticker), LANGGANAN_URL))
                ticker_smart_bills.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                    }

                    override fun onDismiss() {

                    }
                })

                view_smart_bills_select_all_checkbox_container.setOnClickListener {
                    cb_smart_bills_select_all.toggle()
                    toggleAllItems(cb_smart_bills_select_all.isChecked)
                }

                rv_smart_bills_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                rv_smart_bills_items.adapter = adapter
                rv_smart_bills_items.addItemDecoration(object : DividerItemDecoration(context) {
                    override fun getDimenPaddingLeft(): Int {
                        return R.dimen.smart_bills_divider_left_padding
                    }
                })

                smart_bills_checkout_view.listener = this
                smart_bills_checkout_view.setVisibilityLayout(true)
                smart_bills_checkout_view.setBuyButtonLabel(getString(R.string.smart_bills_checkout_view_button_label))
                setTotalPrice()

                loadData()
            }
        }
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
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
        view_smart_bills_select_all_checkbox_container.hide()
        view_smart_bills_shimmering.show()

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
        if (isChecked) {
            totalPrice += item.amount.toInt()
        } else {
            totalPrice -= item.amount.toInt()
        }
        setTotalPrice()

        cb_smart_bills_select_all.isChecked = adapter.totalChecked == adapter.dataSize
    }

    private fun toggleAllItems(value: Boolean) {
        adapter.toggleAllItems(value)

        totalPrice = if (value) maximumPrice else 0
        setTotalPrice()
    }

    private fun setTotalPrice() {
        if (totalPrice >= 0) {
            val totalPriceString = if (totalPrice > 0) {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice, true)
            } else {
                getString(R.string.smart_bills_no_item_price)
            }
            smart_bills_checkout_view.setTotalPrice(totalPriceString)
        }
    }

    override fun onClickNextBuyButton() {
        if (adapter.checkedDataList.isNotEmpty()) {
            // Reset error in bill items
            for ((index, bill) in adapter.data.withIndex()) {
                if (bill.errorMessage.isNotEmpty()) {
                    bill.errorMessage = ""
                    adapter.data[index] = bill
                    adapter.notifyItemChanged(index)
                }
            }

            viewModel.runMultiCheckout(
                    viewModel.createMultiCheckoutParams(adapter.checkedDataList, userSession)
            )
        }
    }

    companion object {
        const val RECHARGE_SMART_BILLS_PAGE_PERFORMANCE = "dg_smart_bills_pdp"

        const val SMART_BILLS_PREF = "SMART_BILLS"
        const val SMART_BILLS_VISITED_ONBOARDING = "SMART_BILLS_VISITED_ONBOARDING"

        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_SMART_BILLS_ONBOARDING = 1700

        const val LANGGANAN_URL = "https://www.tokopedia.com/langganan"
    }
}