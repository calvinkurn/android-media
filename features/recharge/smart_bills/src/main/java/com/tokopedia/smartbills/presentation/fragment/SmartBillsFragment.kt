package com.tokopedia.smartbills.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.analytics.SmartBillsAnalytics
import com.tokopedia.smartbills.data.*
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsActivity
import com.tokopedia.smartbills.presentation.activity.SmartBillsOnboardingActivity
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapter
import com.tokopedia.smartbills.presentation.adapter.SmartBillsAdapterFactory
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsAccordionViewHolder
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsEmptyStateViewHolder
import com.tokopedia.smartbills.presentation.adapter.viewholder.SmartBillsViewHolder
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import com.tokopedia.smartbills.presentation.widget.SmartBillsCatalogBottomSheet
import com.tokopedia.smartbills.presentation.widget.SmartBillsDeleteBottomSheet
import com.tokopedia.smartbills.presentation.widget.SmartBillsItemDetailBottomSheet
import com.tokopedia.smartbills.presentation.widget.SmartBillsToolTipBottomSheet
import com.tokopedia.smartbills.util.DividerSBMItemDecoration
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.getAccordionSection
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.getNotAccordionSection
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.getUUIDAction
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_smart_bills.*
import java.util.*
import javax.inject.Inject

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsFragment : BaseListFragment<RechargeBillsModel, SmartBillsAdapterFactory>(),
    BaseCheckableViewHolder.CheckableInteractionListener,
    BaseListCheckableAdapter.OnCheckableAdapterListener<RechargeBills>,
    SmartBillsViewHolder.DetailListener,
    TopupBillsCheckoutWidget.ActionListener,
    SmartBillsActivity.SbmActivityListener,
    SmartBillsToolTipBottomSheet.Listener,
    SmartBillsAccordionViewHolder.SBMAccordionListener,
        SmartBillsEmptyStateViewHolder.EmptyStateSBMListener,
        SmartBillsCatalogBottomSheet.CatalogCallback
{

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsViewModel
    private lateinit var localCacheHandler: LocalCacheHandler
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private var containerCheckBox: RelativeLayout? = null

    lateinit var adapter: SmartBillsAdapter
    lateinit var adapterAccordion: SmartBillsAdapter

    @Inject
    lateinit var smartBillsAnalytics: SmartBillsAnalytics

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    private var source: String = ""

    private var autoTick = false
    private var totalPrice = 0
    private var maximumPrice = 0
    private var ongoingMonth: RechargeStatementMonths? = RechargeStatementMonths()
    private var listAccordion: List<Section> = listOf()
    private var listBills: List<RechargeBills> = listOf()
    private var rechargeStatement: RechargeListSmartBills = RechargeListSmartBills()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_smart_bills, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize performance monitoring
        performanceMonitoring = PerformanceMonitoring.start(RECHARGE_SMART_BILLS_PAGE_PERFORMANCE)

        activity?.let {
            (it as BaseSimpleActivity).updateTitle(getString(R.string.smart_bills_action_bar_title))

            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            viewModel = viewModelProvider.get(SmartBillsViewModel::class.java)
            localCacheHandler = LocalCacheHandler(context, SMART_BILLS_PREF)
        }

        arguments?.let {
            source = it.getString(EXTRA_SOURCE_TYPE, "")
        }
    }

    private fun observeData(){
        viewModel.statementMonths.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    ongoingMonth = it.data.firstOrNull { monthItem -> monthItem.isOngoing }
                    ongoingMonth?.let {
                        viewModel.getStatementBills(
                                viewModel.createStatementBillsParams(
                                        it.month,
                                        it.year,
                                        SOURCE
                                ),
                                swipeToRefresh?.isRefreshing ?: false
                        )
                    }
                    if (ongoingMonth == null) {
                        showGlobalError(getDataErrorException())
                    }
                }
                is Fail -> {
                    view_smart_bills_shimmering.hide()
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.STATEMENT_MONTHS_ERROR) {
                        throwable = getDataErrorException()
                    }
                    showGlobalError(throwable)
                }
            }
        })

        viewModel.statementBills.observe(viewLifecycleOwner, Observer {
            performanceMonitoring.stopTrace()

            view_smart_bills_shimmering.hide()
            when (it) {
                is Success -> {
                    adapter.data.clear()
                    rechargeStatement = it.data
                    val bills = getNotAccordionSection(it.data.sections)?.bills
                    if (!bills.isNullOrEmpty()) {
                        listBills = bills
                        containerCheckBox?.show()

                        if (!getNotAccordionSection(it.data.sections)?.title.isNullOrEmpty())
                            tv_smart_bills_title.text = getNotAccordionSection(it.data.sections)?.title

                        renderList(bills)
                        listAccordion = getAccordionSection(it.data.sections)
                        renderAccordionList(listAccordion)
                        smartBillsAnalytics.impressionAllProducts(bills)

                        // Auto select bills based on data
                        autoTick = true
                        bills.forEachIndexed { index, rechargeBills ->
                            if (rechargeBills.isChecked) adapter.updateListByCheck(true, index)
                        }
                        autoTick = false

                        // Show coach mark
                        showOnboarding()

                        // Save maximum price
                        maximumPrice = 0
                        for (bill in bills) {
                            maximumPrice += bill.amount.toInt()
                        }
                    } else {
                        hideLoading()
                        // Show empty state
                        tv_smart_bills_title.hide()
                        smart_bills_checkout_view.setVisibilityLayout(false)
                        adapter.renderEmptyState()
                    }
                    smartBillsAnalytics.eventOpenScreen((bills?.isEmpty()) ?: true, bills?.size ?: 0)
                }
                is Fail -> {
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.STATEMENT_BILLS_ERROR) {
                        throwable = getDataErrorException()
                    }
                    showGlobalError(throwable)
                }
            }
        })

        viewModel.multiCheckout.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    // Check if all items' transaction succeeds; if they do, navigate to payment
                    if (it.data.attributes.allSuccess) {
                        smartBillsAnalytics.clickPay(adapter.checkedDataList, listBills.size, totalPrice)

                        val paymentPassData = PaymentPassData()
                        paymentPassData.convertToPaymenPassData(it.data)

                        val intent = RouteManager.getIntent(context, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                    } else { // Else, show error message in affected items
                        smartBillsAnalytics.clickPayFailed(listBills.size, adapter.checkedDataList.size)

                        checkout_loading_view.hide()
                        view?.let { v ->
                            val throwable = MessageErrorException(getString(R.string.smart_bills_checkout_error))
                            Toaster.build(v, ErrorHandler.getErrorMessage(context, throwable),
                                    Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }

                        for (errorItem in it.data.attributes.errors) {
                            if (errorItem.index >= 0) {
                                val bill = adapter.data[errorItem.index]
                                bill.errorMessage = if (errorItem.errorMessage.isNotEmpty()) {
                                    errorItem.errorMessage
                                } else {
                                    getString(R.string.smart_bills_item_default_error)
                                }
                                adapter.data[errorItem.index] = bill
                                adapter.updateListByCheck(false, errorItem.index)
                            }
                        }
                    }
                }
                is Fail -> {
                    smartBillsAnalytics.clickPayFailed(listBills.size, adapter.checkedDataList.size)

                    checkout_loading_view.hide()
                    var throwable = it.throwable
                    if (throwable.message == SmartBillsViewModel.MULTI_CHECKOUT_EMPTY_REQUEST) {
                        throwable = MessageErrorException(getString(R.string.smart_bills_checkout_error))
                    }
                    view?.let { v ->
                        Toaster.build(v, ErrorHandler.getErrorMessage(context,throwable)
                                , Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }
                }
            }
        })

        viewModel.catalogList.observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            when (it) {
                is Success -> {
                    if (it.data.isNotEmpty()) {
                        showCatalogBottomSheet(it.data)
                    } else {
                        view?.let { view ->
                            Toaster.build(view, getString(R.string.smart_bills_add_bills_bottom_sheet_catalog_empty), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.build(view, ErrorHandler.getErrorMessage(context, it.throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }
                }
            }
        })

        observe(viewModel.deleteSBM){
            when(it){
                is Success -> {
                    val message = it.data.rechargeSBMDeleteBill.message
                    if (!message.isNullOrEmpty()) {
                        view?.let { view ->
                            smartBillsAnalytics.viewDeleteBillSuccess()
                            Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }
                        swipeToRefresh?.isRefreshing = true
                        showLoading()
                        loadInitialData()
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.build(view, ErrorHandler.getErrorMessage(context, it.throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Handle user not logging in from onboarding
        if(requestCode == REQUEST_CODE_SETTING) {
            showLoading()
            loadInitialData()
        } else if (requestCode == REQUEST_CODE_ADD_BILLS) {
            if(resultCode == Activity.RESULT_OK){
                swipeToRefresh?.isRefreshing = true
                showLoading()
                loadInitialData()
                val message = data?.getStringExtra(EXTRA_ADD_BILLS_MESSAGE)
                val category = data?.getStringExtra(EXTRA_ADD_BILLS_CATEGORY)
                if (message != null) {
                    view?.let { parentView ->
                        Toaster.build(
                            parentView,
                            message,
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_NORMAL,
                            getString(com.tokopedia.resources.common.R.string.general_label_ok)
                        ).show()
                    }
                }
                category?.let {
                    smartBillsAnalytics.clickViewShowToasterTelcoAddBills(category)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) activity?.finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        initView()
        observeData()
    }

    private fun setupUI(){
        view?.apply {
            containerCheckBox = findViewById(R.id.view_smart_bills_select_all_checkbox_container)
        }
    }

    private fun initView() {
        // If user is not logged in, redirect to onboarding page;
        // Add sharedpref to make sure onboarding page is not visited more than once in each session
        // (support for phones with don't keep activities)
        if (!userSession.isLoggedIn && !localCacheHandler.getBoolean(SMART_BILLS_VISITED_ONBOARDING_PAGE, false)) {
            localCacheHandler.apply {
                putBoolean(SMART_BILLS_VISITED_ONBOARDING_PAGE, true)
                applyEditor()
            }
            startActivityForResult(Intent(context,
                SmartBillsOnboardingActivity::class.java),
                REQUEST_CODE_SMART_BILLS_ONBOARDING
            )
        } else {
            smartBillsAnalytics.userId = userSession.userId

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


                tv_sbm_add_bills.apply {
                    show()
                    setOnClickListener {
                         smartBillsAnalytics.clickTambahTagihan()
                         getCatalogData()
                    }
                }

                // Setup toggle all items listener
                cb_smart_bills_select_all.setOnClickListener {
                    toggleAllItems(cb_smart_bills_select_all.isChecked, true)
                }
                tg_smart_bills_select_all.setOnClickListener {
                    cb_smart_bills_select_all.toggle()
                    toggleAllItems(cb_smart_bills_select_all.isChecked, true)
                }

                rv_smart_bills_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                rv_smart_bills_items.adapter = adapter
                rv_smart_bills_items.addItemDecoration(DividerSBMItemDecoration(context))
                initAccordion()
                smart_bills_checkout_view.listener = this
                smart_bills_checkout_view.setBuyButtonLabel(getString(R.string.smart_bills_checkout_view_button_label))
                updateCheckoutView()

                loadInitialData()
            }
        }
    }

    private fun initAccordion(){
        rv_smart_bills_accordion?.apply {
            resertAccordion()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterAccordion
            addItemDecoration(DividerSBMItemDecoration(context))
        }
    }

    private fun renderAccordionList(listSection: List<Section>){
        rv_smart_bills_accordion?.apply {
            show()
            adapterAccordion.clearAllElements()
            adapterAccordion.addElement(listSection)
            adapterAccordion.notifyDataSetChanged()
        }
    }

    private fun resertAccordion(){
        rv_smart_bills_accordion?.apply {
            hide()
            adapterAccordion.clearAllElements()
        }
    }

    private fun resetInitialState(){
        tv_smart_bills_title.show()
        containerCheckBox?.hide()
        resertAccordion()
        view_smart_bills_shimmering.show()
        smart_bills_checkout_view.setVisibilityLayout(true)
        toggleAllItems(false)
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    override fun onSwipeRefresh() {
        toggleAllItems(false)
        updateCheckAll()
        super.onSwipeRefresh()
    }

    override fun getScreenName(): String {
        return getString(R.string.app_name)
    }

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        // Reset to initial state
        resetInitialState()
        viewModel.getStatementMonths(
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

    override fun createAdapterInstance(): BaseListAdapter<RechargeBillsModel, SmartBillsAdapterFactory> {
        adapter = SmartBillsAdapter(adapterTypeFactory, this)
        adapterAccordion = SmartBillsAdapter(adapterTypeFactory, this)
        return adapter as BaseListAdapter<RechargeBillsModel, SmartBillsAdapterFactory>
    }

    override fun getAdapterTypeFactory(): SmartBillsAdapterFactory {
        return SmartBillsAdapterFactory(this, this,
                this, this)
    }

    override fun showGetListError(throwable: Throwable?) {
        adapter.showErrorNetwork(ErrorHandler.getErrorMessage(context, throwable)) {
            showLoading()
            loadInitialData()
        }
    }

    override fun onItemClicked(t: RechargeBillsModel?) {

    }

    override fun isChecked(position: Int): Boolean {
        return adapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        if (position >= 0)
            adapter.updateListByCheck(isChecked, position)
    }

    override fun onItemChecked(item: RechargeBills, isChecked: Boolean) {
        if (isChecked) {
            // Do not trigger event if bill is auto-ticked
            if (!autoTick) smartBillsAnalytics.clickTickBill(item, adapter.checkedDataList)
            totalPrice += item.amount.toInt()
        } else {
            smartBillsAnalytics.clickUntickBill(item)
            totalPrice -= item.amount.toInt()
        }
        updateCheckoutView()
        updateCheckAll()
    }

    override fun clickEmptyButton() {
        getCatalogData()
    }

    override fun onCloseCatalogBottomSheet() {
        smartBillsAnalytics.clickCloseBottomsheetCatalog()
    }

    private fun getCatalogData() {
        if (getRemoteConfigAddBillsEnabler()) {
            showProgressBar()
            viewModel.getCatalogAddBills(viewModel.createCatalogIDParam(PLATFORM_ID_SBM))
        } else {
            RouteManager.route(context, ApplinkConst.RECHARGE_SUBHOMEPAGE_HOME_NEW)
        }
    }

    private fun showCatalogBottomSheet(catalogList: List<SmartBillsCatalogMenu>) {
        smartBillsAnalytics.viewBottomsheetCatalog()
        val catalogBottomSheet = SmartBillsCatalogBottomSheet.newInstance(this)
        catalogBottomSheet.showSBMCatalog(catalogList)
        catalogBottomSheet.show(requireFragmentManager(), "")
    }

    private fun toggleAllItems(value: Boolean, triggerTracking: Boolean = false) {
        if (triggerTracking) smartBillsAnalytics.clickAllBills(value)
        adapter.toggleAllItems(value, listBills)

        totalPrice = if (value) maximumPrice else 0
        updateCheckoutView()
    }

    private fun showOnboarding() {
        context?.run {
            // Render onboarding coach mark if there are bills & if it's the first visit
            if (adapter.dataSize > 0 && !localCacheHandler.getBoolean(SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, false)) {
                localCacheHandler.apply {
                    putBoolean(SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, true)
                    applyEditor()
                }

                // Get first viewholder item for coach marks
                rv_smart_bills_items?.post {
                    val billItemView = (rv_smart_bills_items?.findViewHolderForAdapterPosition(0) as? SmartBillsViewHolder)?.itemView
                    val coachMarks = ArrayList<CoachMark2Item>()
                    containerCheckBox?.let {
                        coachMarks.add(
                                CoachMark2Item(
                                        it,
                                        getString(R.string.smart_bills_onboarding_title_1),
                                        getString(R.string.smart_bills_onboarding_description_1)
                                )
                        )
                    }
                    billItemView?.run {
                        coachMarks.add(
                            CoachMark2Item(
                                this,
                                getString(R.string.smart_bills_onboarding_title_2),
                                getString(R.string.smart_bills_onboarding_description_2)
                            )
                        )
                    }
                    coachMarks.add(
                        CoachMark2Item(
                            smart_bills_checkout_view.getCheckoutButton(),
                            getString(R.string.smart_bills_onboarding_title_3),
                            getString(R.string.smart_bills_onboarding_description_3)
                        )
                    )

                    val coachMark = CoachMark2(this)
//                    coachMark.enableSkip = true
//                    coachMark.setHighlightMargin(SMART_BILLS_COACH_MARK_HIGHLIGHT_MARGIN)
                    coachMark.showCoachMark(coachMarks)
                }
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

    override fun onDeleteClicked(bill: RechargeBills) {
        fragmentManager?.let {
            smartBillsAnalytics.clickKebab(bill.categoryName)
            val smartBillsDeleteBottomSheet = SmartBillsDeleteBottomSheet(object :
                    SmartBillsDeleteBottomSheet.DeleteProductSBMListener{
                override fun onDeleteProductClicked() {
                    smartBillsAnalytics.clickHapusTagihan(bill.categoryName)
                    showDeleteDialog(bill)
                }

                override fun onCloseBottomSheet() {
                    smartBillsAnalytics.viewCloseBottomSheet()
                }
            })

            smartBillsDeleteBottomSheet.show(it, "")
        }
    }

    private fun showDeleteDialog(bill: RechargeBills){
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(resources.getString(R.string.smart_bills_delete_dialog_title))
                setDescription(resources.getString(R.string.smart_bills_delete_dialog_desc))
                setPrimaryCTAText(resources.getString(R.string.smart_bills_delete_dialog_yes))
                setSecondaryCTAText(resources.getString(R.string.smart_bills_delete_dialog_no))

                setPrimaryCTAClickListener{
                    smartBillsAnalytics.clickConfirmHapusTagihan()
                    dismiss()
                    viewModel.deleteProductSBM(viewModel.createParamDeleteSBM(RechargeSBMDeleteBillRequest(bill.uuid, SOURCE)))
                }

                setSecondaryCTAClickListener{
                    smartBillsAnalytics.clickBatalHapusTagihan()
                    dismiss()
                }

            }
            dialog.show()
        }
    }

    private fun updateCheckoutView() {
        if (totalPrice >= 0) {
            val totalPriceString = if (totalPrice > 0) {
                CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice, false)
            } else {
                getString(R.string.smart_bills_no_item_price)
            }
            smart_bills_checkout_view.setTotalPrice(totalPriceString)
            smart_bills_checkout_view.getCheckoutButton().isEnabled = totalPrice > 0
        }
    }

    override fun onClickNextBuyButton() {
        // Prevent multiple checkout calls when one is already underway
        if (adapter.checkedDataList.isNotEmpty() && !checkout_loading_view.isVisible) {
            // Reset error in bill items
            for ((index, bill) in listBills.withIndex()) {
                if (bill.errorMessage.isNotEmpty()) {
                    bill.errorMessage = ""
                    adapter.data[index] = bill
                    adapter.notifyItemChanged(index)
                }
            }

            checkout_loading_view.show()
            viewModel.runMultiCheckout(
                viewModel.createMultiCheckoutParams(adapter.checkedDataList, userSession), userSession.userId
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkout_loading_view.isVisible) checkout_loading_view.hide()
    }

    override fun onDestroy() {
        // Reset visited onboarding state after each session
        localCacheHandler.apply {
            remove(SMART_BILLS_VISITED_ONBOARDING_PAGE)
            applyEditor()
        }
        super.onDestroy()
    }

    override fun clickToolTip() {
        context?.let {
            smartBillsAnalytics.clickToolTip()
            val tooltipBottomSheet = SmartBillsToolTipBottomSheet.newInstance(it, this)
            fragmentManager?.run {
                tooltipBottomSheet.show(this)
            }
        }
    }

    override fun onClickMoreLearn() {
        context?.let {
            smartBillsAnalytics.clickMoreLearn()
            RouteManager.route(context, HELP_SBM_URL)
        }
    }

    override fun onRefreshAccordion(titleAccordion: String) {
        resetInitialState()
        updateCheckAll()
        smartBillsAnalytics.clickRefreshAccordion(titleAccordion)
        ongoingMonth?.let {
            viewModel.getSBMWithAction(
                viewModel.createRefreshActionParams(
                    getUUIDAction(listAccordion),
                    it.month,
                    it.year,
                    SOURCE
                ),
                rechargeStatement
            )
        }
    }

    override fun onExpandAccordion(titleAccordion: String) {
        smartBillsAnalytics.clickExpandAccordion(titleAccordion)
    }

    override fun onCollapseAccordion(titleAccordion: String) {
        smartBillsAnalytics.clickCollapseAccordion(titleAccordion)
    }

    override fun onCatalogClickCallback(applink: String, category: String) {
        smartBillsAnalytics.clickCategoryBottomsheetCatalog(category)
        val intent = RouteManager.getIntent(context, applink)
        startActivityForResult(intent, REQUEST_CODE_ADD_BILLS)
    }

    private fun getDataErrorException(): Throwable {
        return MessageErrorException(getString(R.string.smart_bills_data_error))
    }

    private fun updateCheckAll(){
        cb_smart_bills_select_all.isChecked = adapter.totalChecked == listBills.size
    }

    private fun showGlobalError(throwable: Throwable){
        checkout_loading_view.show()
        smart_bills_checkout_view.setVisibilityLayout(false)
        sbm_global_error.apply {
            show()
            val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
                context, throwable, ErrorHandler.Builder().build())
            errorTitle.text = errMsg
            errorDescription.text = "${getString(com.tokopedia.globalerror.R.string.noConnectionDesc)} " +
                    "Kode Error: ($errCode)"
            sbm_loader_unify.hide()
            setActionClickListener {
                this.gone()
                checkout_loading_view.hide()
                smart_bills_checkout_view.setVisibilityLayout(true)
                showLoading()
                loadInitialData()
            }

            setSecondaryActionClickListener {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivityForResult(intent, REQUEST_CODE_SETTING)
            }
        }
    }

    private fun getRemoteConfigAddBillsEnabler(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_ADD_BILLS_SBM, true)
    }

    private fun showProgressBar(){
        sbm_progress_bar.show()
    }

    private fun hideProgressBar(){
        sbm_progress_bar.hide()
    }

    companion object {
        const val EXTRA_SOURCE_TYPE = "source"

        const val MAIN_TYPE = 2
        const val ACTION_TYPE = 3
        const val PAID_TYPE = 1

        const val SOURCE = 1

        const val PLATFORM_ID_SBM = 48

        const val RECHARGE_SMART_BILLS_PAGE_PERFORMANCE = "dg_smart_bills_pdp"

        const val EXPAND_ACCORDION_START_DELAY = 150L

        const val SMART_BILLS_PREF = "smart_bills_preference"
        const val SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK = "SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK"
        const val SMART_BILLS_VISITED_ONBOARDING_PAGE = "SMART_BILLS_VISITED_ONBOARDING_PAGE"
        const val SMART_BILLS_COACH_MARK_HIGHLIGHT_MARGIN = 4

        const val REQUEST_CODE_SMART_BILLS_ONBOARDING = 1700

        const val REQUEST_CODE_SETTING = 1669

        const val REQUEST_CODE_ADD_BILLS= 2030
        const val EXTRA_ADD_BILLS_MESSAGE = "MESSAGE"
        const val EXTRA_ADD_BILLS_CATEGORY = "CATEGORY"

        const val LANGGANAN_URL = "https://www.tokopedia.com/langganan"
        const val HELP_SBM_URL = "https://www.tokopedia.com/help/article/bayar-sekaligus"

        fun newInstance(sourceType: String = ""): SmartBillsFragment {
            val fragment = SmartBillsFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_SOURCE_TYPE, sourceType)
            fragment.arguments = bundle
            return fragment
        }
    }
}