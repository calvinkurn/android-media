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
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.analytics.SmartBillsAnalytics
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsOnboardingActivity
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapter
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.smartbills.presentation.widget.SmartBillsItemDetailBottomSheet
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_smart_bills.*
import java.util.*
import javax.inject.Inject

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsFragment : BaseListFragment<RechargeBills, SmartBillsAdapterFactory>(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<RechargeBills>,
        SmartBillsViewHolder.DetailListener,
        TopupBillsCheckoutWidget.ActionListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsViewModel
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var performanceMonitoring: PerformanceMonitoring

    lateinit var adapter: SmartBillsAdapter

    @Inject
    lateinit var smartBillsAnalytics: SmartBillsAnalytics

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
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.statementMonths.observe(this, Observer {
            when (it) {
                is Success -> {
                    val ongoingMonth = it.data.firstOrNull { monthItem -> monthItem.isOngoing }
                    if (ongoingMonth != null) {
                        viewModel.getStatementBills(
                                GraphqlHelper.loadRawString(resources, R.raw.query_recharge_statement_bills),
                                viewModel.createStatementBillsParams(ongoingMonth.month, ongoingMonth.year),
                                swipeToRefresh?.isRefreshing ?: false
                        )
                    } else {
                        showGetListError(getDataErrorException())
                    }
                }
                is Fail -> {
                    view_smart_bills_shimmering.hide()
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.STATEMENT_MONTHS_ERROR) {
                        throwable = getDataErrorException()
                    }
                    showGetListError(throwable)
                }
            }
        })

        viewModel.statementBills.observe(this, Observer {
            performanceMonitoring.stopTrace()

            view_smart_bills_shimmering.hide()
            when (it) {
                is Success -> {
                    val bills = it.data.bills
                    if (bills.isNotEmpty()) {
                        view_smart_bills_select_all_checkbox_container.show()
                        renderList(it.data.bills)
                        smartBillsAnalytics.impressionAllProducts(it.data.bills)

                        // Show coach mark
                        showOnboarding()

                        // Save maximum price
                        maximumPrice = 0
                        for (bill in it.data.bills) {
                            maximumPrice += bill.amount.toInt()
                        }
                    } else {
                        hideLoading()
                        // Show empty state
                        tv_smart_bills_title.hide()
                        smart_bills_checkout_view.setVisibilityLayout(false)
                        adapter.renderEmptyState()
                    }
                }
                is Fail -> {
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.STATEMENT_BILLS_ERROR) {
                        throwable = getDataErrorException()
                    }
                    showGetListError(throwable)
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
                        smartBillsAnalytics.clickPayFailed()

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
                    smartBillsAnalytics.clickPayFailed()
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.MULTI_CHECKOUT_EMPTY_REQUEST) {
                        throwable = MessageErrorException(getString(R.string.smart_bills_checkout_error))
                    }
                    NetworkErrorHelper.showRedSnackbar(activity, throwable.message)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle user not logging in from onboarding
        if (resultCode == Activity.RESULT_CANCELED) activity?.finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        // If user is not logged in, redirect to onboarding page;
        // Add sharedpref to make sure onboarding page is not visited more than once in each session
        // (support for phones with don't keep activities)
        if (!userSession.isLoggedIn && !sharedPrefs.getBoolean(SMART_BILLS_VISITED_ONBOARDING_PAGE, false)) {
            sharedPrefs.edit().putBoolean(SMART_BILLS_VISITED_ONBOARDING_PAGE, true).apply()
            startActivityForResult(Intent(context,
                    SmartBillsOnboardingActivity::class.java),
                    REQUEST_CODE_SMART_BILLS_ONBOARDING
            )
        } else {
            smartBillsAnalytics.userId = userSession.userId
            smartBillsAnalytics.eventOpenScreen()

            context?.let { context ->
                // Setup ticker
                ticker_smart_bills.setTextDescription(String.format(getString(R.string.smart_bills_ticker), LANGGANAN_URL))
                ticker_smart_bills.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        smartBillsAnalytics.clickSubscription()
                        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${linkUrl}")
                    }

                    override fun onDismiss() {

                    }
                })

                // Reset toggle all items checkbox
                cb_smart_bills_select_all.isChecked = false
                // Setup toggle all items listener
                cb_smart_bills_select_all.setOnClickListener {
                    toggleAllItems(cb_smart_bills_select_all.isChecked)
                }
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
                smart_bills_checkout_view.setBuyButtonLabel(getString(R.string.smart_bills_checkout_view_button_label))
                setTotalPrice()

                loadInitialData()
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

    override fun loadData(page: Int) {
        // Reset view states
        tv_smart_bills_title.show()
        view_smart_bills_select_all_checkbox_container.hide()
        view_smart_bills_shimmering.show()
        smart_bills_checkout_view.setVisibilityLayout(true)

        viewModel.getStatementMonths(
                GraphqlHelper.loadRawString(resources, R.raw.query_recharge_statement_months),
                viewModel.createStatementMonthsParams(1),
                swipeToRefresh?.isRefreshing ?: false
        )
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_smart_bills_items
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.smart_bills_swipe_refresh_layout
    }

    override fun createAdapterInstance(): BaseListAdapter<RechargeBills, SmartBillsAdapterFactory> {
        adapter = SmartBillsAdapter(adapterTypeFactory, this)
        return adapter
    }

    override fun getAdapterTypeFactory(): SmartBillsAdapterFactory {
        return SmartBillsAdapterFactory(this, this)
    }

    override fun showGetListError(throwable: Throwable?) {
        adapter.showErrorNetwork(ErrorHandler.getErrorMessage(context, throwable)) {
            showLoading()
            loadInitialData()
        }
    }

    override fun onItemClicked(t: RechargeBills?) {

    }

    override fun isChecked(position: Int): Boolean {
        return adapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        adapter.updateListByCheck(isChecked, position)
    }

    override fun onItemChecked(item: RechargeBills, isChecked: Boolean) {
        if (isChecked) {
            smartBillsAnalytics.clickTickBill(item, adapter.checkedDataList)
            totalPrice += item.amount.toInt()
        } else {
            smartBillsAnalytics.clickUntickBill(item)
            totalPrice -= item.amount.toInt()
        }
        setTotalPrice()

        cb_smart_bills_select_all.isChecked = adapter.totalChecked == adapter.dataSize
    }

    private fun toggleAllItems(value: Boolean) {
        smartBillsAnalytics.clickAllBills(value)
        adapter.toggleAllItems(value)

        totalPrice = if (value) maximumPrice else 0
        setTotalPrice()
    }

    private fun showOnboarding() {
        // Render onboarding coach mark if there are bills & if it's the first visit
        if (adapter.dataSize > 0 && !sharedPrefs.getBoolean(SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, false)) {
            sharedPrefs.edit().putBoolean(SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, true).apply()

            // Get first viewholder item for coach marks
            rv_smart_bills_items.post {
                val billItemView = (rv_smart_bills_items.findViewHolderForAdapterPosition(0) as? SmartBillsViewHolder)?.itemView
                val coachMarks = ArrayList<CoachMarkItem>()
                coachMarks.add(
                        CoachMarkItem(
                                view_smart_bills_select_all_checkbox_container,
                                getString(R.string.smart_bills_onboarding_title_1),
                                getString(R.string.smart_bills_onboarding_description_1)
                        )
                )
                billItemView?.run {
                    coachMarks.add(
                            CoachMarkItem(
                                    this,
                                    getString(R.string.smart_bills_onboarding_title_2),
                                    getString(R.string.smart_bills_onboarding_description_2)
                            )
                    )
                }
                coachMarks.add(
                        CoachMarkItem(
                                smart_bills_checkout_view.getCheckoutButton(),
                                getString(R.string.smart_bills_onboarding_title_3),
                                getString(R.string.smart_bills_onboarding_description_3)
                        )
                )

                val coachMark = CoachMarkBuilder().build()
                coachMark.enableSkip = true
                coachMark.setHighlightMargin(SMART_BILLS_COACH_MARK_HIGHLIGHT_MARGIN)
                coachMark.show(activity, "SmartBillsOnboardingCoachMark", coachMarks)
            }
        }
    }

    override fun onShowBillDetail(bill: RechargeBills, bottomSheet: SmartBillsItemDetailBottomSheet) {
        smartBillsAnalytics.clickBillDetail(bill)

        fragmentManager?.run {
            bottomSheet.setTitle(getString(R.string.smart_bills_item_detail_bottomsheet_title))
            bottomSheet.show(this)
        }
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

            smartBillsAnalytics.clickPay(adapter.checkedDataList)

            viewModel.runMultiCheckout(
                    viewModel.createMultiCheckoutParams(adapter.checkedDataList, userSession)
            )
        }
    }

    override fun onDestroy() {
        // Reset visited onboarding state after each session
        sharedPrefs.edit().remove(SMART_BILLS_VISITED_ONBOARDING_PAGE).apply()
        super.onDestroy()
    }

    private fun getDataErrorException(): Throwable {
        return MessageErrorException(getString(R.string.smart_bills_data_error))
    }

    companion object {
        const val RECHARGE_SMART_BILLS_PAGE_PERFORMANCE = "dg_smart_bills_pdp"

        const val SMART_BILLS_PREF = "SMART_BILLS"
        const val SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK = "SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK"
        const val SMART_BILLS_VISITED_ONBOARDING_PAGE = "SMART_BILLS_VISITED_ONBOARDING_PAGE"
        const val SMART_BILLS_COACH_MARK_HIGHLIGHT_MARGIN = 4

        const val REQUEST_CODE_SMART_BILLS_ONBOARDING = 1700

        const val LANGGANAN_URL = "https://www.tokopedia.com/langganan"
    }
}