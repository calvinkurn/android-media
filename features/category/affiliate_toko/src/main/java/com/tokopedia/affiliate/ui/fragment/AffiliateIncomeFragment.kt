package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.APP_LINK_KYC
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.KYC_DONE
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.PRODUCT_TYPE
import com.tokopedia.affiliate.TRANSACTION_TYPE_DEPOSIT
import com.tokopedia.affiliate.TRANSACTION_TYPE_WITHDRAWAL
import com.tokopedia.affiliate.WITHDRAWAL_APPLINK_PROD
import com.tokopedia.affiliate.WITHDRAWAL_APPLINK_STAGING
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTransactionHistoryItemModel
import com.tokopedia.affiliate.viewmodel.AffiliateIncomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AffiliateIncomeFragment :
    AffiliateBaseFragment<AffiliateIncomeViewModel>(),
    AffiliateDatePickerRangeChangeInterface {

    private val affiliateIncomeViewModel by lazy {
        ViewModelProvider(this)[AffiliateIncomeViewModel::class.java]
    }
    private var userName: String = ""
    private var profilePicture: String = ""
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var listSize = 0
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null
    private var isReviewed = false
    private var walletOnHold = true

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        private const val WALLET_STATUS_HOLD = "WALLET_STATUS_HOLD"
        fun getFragmentInstance(
            userNameParam: String,
            profilePictureParam: String,
            affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface
        ): Fragment {
            return AffiliateIncomeFragment().apply {
                userName = userNameParam
                profilePicture = profilePictureParam
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_income_fragment_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
        sendOpenScreenTracking()
    }

    private fun sendOpenScreenTracking() {
        context?.let {
            AffiliateAnalytics.sendOpenScreenEvent(
                AffiliateAnalytics.EventKeys.OPEN_SCREEN,
                AffiliateAnalytics.ScreenKeys.AFFILIATE_PENDAPATAN_PAGE,
                UserSession(it).isLoggedIn,
                UserSession(it).userId
            )
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    private var lastItem: Visitable<AffiliateAdapterTypeFactory>? = null

    private fun setObservers() {
        affiliateIncomeViewModel.getAffiliateBalanceData().observe(this) { balanceData ->
            onGetAffiliateBalance(balanceData)
        }

        affiliateIncomeViewModel.getAffiliateDataItems().observe(this) { dataList ->
            onGetAffiliateDataItems(dataList)
        }
        affiliateIncomeViewModel.getShimmerVisibility().observe(this) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    adapter.addShimmer()
                } else {
                    stopSwipeRefresh()
                    adapter.removeShimmer(listSize)
                }
            }
        }
        affiliateIncomeViewModel.getErrorMessage().observe(this) { error ->
            onGetError(error)
        }
        affiliateIncomeViewModel.getRangeChange().observe(this) { changed ->
            if (changed) {
                resetItems()
                view?.findViewById<Typography>(R.id.date_range_text)?.text =
                    affiliateIncomeViewModel.getSelectedDate()
            }
        }
        affiliateIncomeViewModel.getAffiliateKycData().observe(this) {
            onGetAffiliateKycData(it)
        }

        affiliateIncomeViewModel.getAffiliateKycLoader().observe(this) { show ->
            if (show) {
                view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.invisible()
                view?.findViewById<LoaderUnify>(R.id.tarik_saldo_loader)?.show()
            } else {
                view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.show()
                view?.findViewById<LoaderUnify>(R.id.tarik_saldo_loader)?.hide()
            }
        }

        affiliateIncomeViewModel.getKycErrorMessage().observe(this) {
            view?.let {
                Toaster.build(
                    it,
                    getString(R.string.affiliate_retry_message),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
        affiliateIncomeViewModel.getValidateUserdata().observe(this) { validateUserdata ->
            onGetValidateUserData(validateUserdata)
        }
        affiliateIncomeViewModel.getAffiliateAnnouncement().observe(this) { announcementData ->
            if (announcementData.getAffiliateAnnouncementV2?.data?.subType != TICKER_BOTTOM_SHEET) {
                sendTickerImpression(
                    announcementData.getAffiliateAnnouncementV2?.data?.type,
                    announcementData.getAffiliateAnnouncementV2?.data?.id
                )
                view?.findViewById<Ticker>(R.id.affiliate_announcement_ticker)?.setAnnouncementData(
                    announcementData,
                    activity,
                    source = PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
                )
            }
        }
    }

    private fun sendTickerImpression(tickerType: String?, tickerId: Long?) {
        if (tickerId.isMoreThanZero()) {
            AffiliateAnalytics.sendTickerEvent(
                AffiliateAnalytics.EventKeys.VIEW_ITEM,
                AffiliateAnalytics.ActionKeys.IMPRESSION_TICKER_COMMUNICATION,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                "$tickerType - $tickerId",
                PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY,
                tickerId!!,
                AffiliateAnalytics.ItemKeys.AFFILIATE_PENDAPATAN_TICKER_COMMUNICATION,
                UserSession(context).userId
            )
        }
    }

    private fun onGetError(error: Throwable?) {
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.run {
            when (error) {
                is UnknownHostException, is SocketTimeoutException -> {
                    setType(GlobalError.NO_CONNECTION)
                    show()
                }

                is IllegalStateException -> {
                    setType(GlobalError.PAGE_FULL)
                    show()
                }

                else -> {
                    setErrorState(error?.message)
                }
            }
            setActionClickListener {
                hide()
                affiliateIncomeViewModel.getAffiliateBalance()
                affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
            }
        }
    }

    private fun onGetAffiliateDataItems(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        adapter.removeShimmer(listSize)
        stopSwipeRefresh()
        if (dataList.isEmpty() && listSize == 0) {
            showGlobalErrorEmptyState()
        } else {
            lastItem = dataList[dataList.lastIndex]
            hideGlobalErrorEmptyState()
            listSize += dataList.size
            adapter.addMoreData(dataList)
            loadMoreTriggerListener?.updateStateAfterGetData()
        }
    }

    private fun onGetAffiliateBalance(balanceData: AffiliateBalance.AffiliateBalance.Data) {
        if (balanceData.status == 1) {
            setAffiliateBalance(balanceData)
        } else {
            balanceData.error?.message?.let { setErrorState(it) }
        }
    }

    private fun onGetAffiliateKycData(kycProjectInfo: AffiliateKycDetailsData.KycProjectInfo) {
        when (kycProjectInfo.status) {
            KYC_DONE -> {
                if (TokopediaUrl.getInstance().GQL.contains("staging")) {
                    RouteManager.route(context, WITHDRAWAL_APPLINK_STAGING)
                } else {
                    RouteManager.route(context, WITHDRAWAL_APPLINK_PROD)
                }
            }

            else -> RouteManager.route(context, APP_LINK_KYC)
        }
    }

    private fun stopSwipeRefresh() {
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe)?.isRefreshing = false
    }

    private fun hideGlobalErrorEmptyState() {
        view?.findViewById<DeferredImageView>(R.id.affiliate_no_transaction_iv)?.hide()
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.hide()
        view?.findViewById<DeferredImageView>(R.id.affiliate_no__default_transaction_iv)?.hide()
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        hideGlobalErrorEmptyState()
        affiliateIncomeViewModel.getAffiliateBalance()
        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    private fun showGlobalErrorEmptyState() {
        view?.findViewById<DeferredImageView>(R.id.affiliate_no_transaction_iv)?.show()

        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_empty_transaction)
            errorDescription.text = getString(R.string.affiliate_empty_transaction_description)
            errorSecondaryAction.gone()
            errorAction.text = getString(R.string.affiliate_choose_date)
            setActionClickListener {
                AffiliateBottomDatePicker.newInstance(
                    affiliateIncomeViewModel.getSelectedDate(),
                    this@AffiliateIncomeFragment
                ).show(childFragmentManager, "")
            }
        }
    }

    private fun setErrorState(message: String?) {
        view?.findViewById<GlobalError>(R.id.withdrawal_global_error)?.apply {
            show()
            errorTitle.text = message
            errorSecondaryAction.gone()
            setActionClickListener {
                hide()
                affiliateIncomeViewModel.getAffiliateBalance()
                affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
            }
        }
    }

    private fun sendPendapatanEvent(
        eventAction: String,
        eventCategory: String,
        eventLabel: String
    ) {
        context?.let {
            AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_PG,
                eventAction,
                eventCategory,
                eventLabel,
                UserSession(it).userId
            )
        }
    }

    private fun setAffiliateBalance(affiliateBalance: AffiliateBalance.AffiliateBalance.Data) {
        affiliateBalance.apply {
            view?.findViewById<Typography>(R.id.saldo_amount_affiliate)?.let {
                it.text = amountFormatted
                it.show()
            }
            view?.findViewById<LoaderUnify>(R.id.affiliate_saldo_progress_bar)?.gone()
            walletOnHold = this.walletStatus == WALLET_STATUS_HOLD
            initTarikSaldo()
        }
    }

    private fun afterViewCreated() {
        setTarikSaldoButtonUI()
        view?.findViewById<Typography>(R.id.withdrawal_user_name)?.text = userName
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe)?.let {
            it.setOnRefreshListener {
                resetItems()
            }
            view?.findViewById<AppBarLayout>(R.id.appbar)
                ?.addOnOffsetChangedListener { _, verticalOffset ->
                    it.isEnabled = verticalOffset == 0
                }
        }
        view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.setOnClickListener {
            sendPendapatanEvent(
                AffiliateAnalytics.ActionKeys.CLICK_TARIK_SALDO,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                ""
            )
            openWithdrawalScreen()
        }
        view?.findViewById<ImageUnify>(R.id.withdrawal_user_image)?.loadImageCircle(profilePicture)
        view?.findViewById<Typography>(R.id.date_range_text)?.text =
            affiliateIncomeViewModel.getSelectedDate()
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            AffiliateBottomDatePicker.newInstance(AffiliateBottomDatePicker.TODAY, this)
                .show(childFragmentManager, "")
        }
        view?.findViewById<RecyclerView>(R.id.withdrawal_transactions_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter

            loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
            it.adapter = adapter
            loadMoreTriggerListener?.let { listener ->
                it.addOnScrollListener(listener)
            }
        }
        view?.findViewById<NavToolbar>(R.id.withdrawal_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                IconBuilder()
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(
                    R.string.affiliate_withdrawal
                )
        }
        initDateRangeClickListener()
        affiliateIncomeViewModel.getAffiliateValidateUser(UserSession(context).email)
    }

    private fun setTarikSaldoButtonUI() {
        view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.apply {
            isEnabled = !affiliateIncomeViewModel.getIsBlackListed()
        }
    }

    private fun openWithdrawalScreen() {
        affiliateIncomeViewModel.getKycDetails()
    }

    private fun initDateRangeClickListener() {
        view?.findViewById<ConstraintLayout>(R.id.date_range)?.setOnClickListener {
            sendPendapatanEvent(
                AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                ""
            )
            AffiliateBottomDatePicker.newInstance(affiliateIncomeViewModel.getSelectedDate(), this)
                .show(
                    childFragmentManager,
                    ""
                )
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                sendImpressionTracker()
                if (affiliateIncomeViewModel.hasNext) {
                    affiliateIncomeViewModel.getAffiliateTransactionHistory(page - 1)
                }
            }
        }
    }

    private fun sendImpressionTracker() {
        val item = (lastItem as? AffiliateTransactionHistoryItemModel)?.transaction
        var transactionID = ""
        var label = ""
        item?.transactionID?.let {
            transactionID = it
        }
        item?.transactionType?.let {
            if (it == TRANSACTION_TYPE_DEPOSIT) {
                label = if (item.commissionType == PRODUCT_TYPE) {
                    AffiliateAnalytics.LabelKeys.DEPOSIT_ORDER
                } else {
                    AffiliateAnalytics.LabelKeys.DEPOSIT_TRAFFIC
                }
            } else if (it == TRANSACTION_TYPE_WITHDRAWAL) {
                label =
                    AffiliateAnalytics.LabelKeys.WITHDRAWAL
            }
        }

        context?.let {
            AffiliateAnalytics.sendIcomeTracker(
                AffiliateAnalytics.EventKeys.VIEW_ITEM,
                AffiliateAnalytics.ActionKeys.IMPRESSION_TRANSACTION_CARD,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                label,
                listSize,
                transactionID,
                UserSession(it).userId
            )
        }
    }

    override fun rangeChanged(range: AffiliateDatePickerData) {
        sendPendapatanEvent(
            AffiliateAnalytics.ActionKeys.CLICK_SIMPAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE_FILTER,
            range.value
        )
        affiliateIncomeViewModel.onRangeChanged(range)
    }

    override fun onRangeSelectionButtonClicked() = Unit

    override fun onSystemDown() {
        affiliateIncomeViewModel.getAnnouncementInformation()
        hideAllView()
        disableSwipeToRefresh()
    }

    private fun disableSwipeToRefresh() {
        view?.findViewById<SwipeRefreshLayout>(R.id.swipe)?.apply {
            setOnRefreshListener {
                isRefreshing = false
            }
        }
    }

    private fun hideAllView() {
        view?.findViewById<CardUnify>(R.id.withdrawal_home_widget)?.hide()
        view?.findViewById<Typography>(R.id.transaction_history_affiliate)?.hide()
        view?.findViewById<CardUnify>(R.id.filterCard)?.hide()
    }

    override fun onReviewed() {
        isReviewed = false
        initTarikSaldo()
        affiliateIncomeViewModel.getAnnouncementInformation()
        affiliateIncomeViewModel.getAffiliateBalance()
        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    private fun initTarikSaldo() {
        view?.findViewById<UnifyButton>(R.id.saldo_button_affiliate)?.isEnabled =
            isReviewed && !affiliateIncomeViewModel.getIsBlackListed() && !walletOnHold
    }

    override fun onUserNotRegistered() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onNotEligible() {
        activity?.let {
            AffiliateRegistrationActivity.newInstance(it)
            it.finish()
        }
    }

    override fun onUserValidated() {
        isReviewed = true
        initTarikSaldo()
        affiliateIncomeViewModel.getAnnouncementInformation()
        affiliateIncomeViewModel.getAffiliateBalance()
        affiliateIncomeViewModel.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    override fun getViewModelType(): Class<AffiliateIncomeViewModel> {
        return AffiliateIncomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) = Unit
}
