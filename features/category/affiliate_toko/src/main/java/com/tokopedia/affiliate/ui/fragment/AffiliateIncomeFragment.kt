package com.tokopedia.affiliate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.affiliate.AFFILIATE_NC
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
import com.tokopedia.affiliate_toko.databinding.AffiliateIncomeFragmentLayoutBinding
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AffiliateIncomeFragment :
    AffiliateBaseFragment<AffiliateIncomeViewModel>(),
    AffiliateDatePickerRangeChangeInterface {

    private var binding by autoClearedNullable<AffiliateIncomeFragmentLayoutBinding>()

    private val userSession by lazy { context?.let { UserSession(it) } }

    private var affiliateIncomeViewModel: AffiliateIncomeViewModel? = null
    private var userName: String = ""
    private var profilePicture: String = ""
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var listSize = 0
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null
    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null
    private var isReviewed = false
    private var walletOnHold = true
    private fun isAffiliateNCEnabled() =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_NC,
            ""
        ) == AFFILIATE_NC

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
    ): View {
        return AffiliateIncomeFragmentLayoutBinding.inflate(inflater, container, false)
            .also { binding = it }.root
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
                userSession?.isLoggedIn.orFalse(),
                userSession?.userId.orEmpty()
            )
        }
    }

    private var lastItem: Visitable<AffiliateAdapterTypeFactory>? = null

    private fun setObservers() {
        affiliateIncomeViewModel?.getAffiliateBalanceData()?.observe(this) { balanceData ->
            onGetAffiliateBalance(balanceData)
        }

        affiliateIncomeViewModel?.getAffiliateDataItems()?.observe(this) { dataList ->
            onGetAffiliateDataItems(dataList)
        }
        affiliateIncomeViewModel?.getShimmerVisibility()?.observe(this) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    adapter.addShimmer()
                } else {
                    stopSwipeRefresh()
                    adapter.removeShimmer(listSize)
                }
            }
        }
        affiliateIncomeViewModel?.getErrorMessage()?.observe(this) { error ->
            onGetError(error)
        }
        affiliateIncomeViewModel?.getRangeChange()?.observe(this) { changed ->
            if (changed) {
                resetItems()
                binding?.dateRangeText?.text = affiliateIncomeViewModel?.getSelectedDate()
            }
        }
        affiliateIncomeViewModel?.getAffiliateKycData()?.observe(this) {
            onGetAffiliateKycData(it)
        }

        affiliateIncomeViewModel?.getAffiliateKycLoader()?.observe(this) { show ->
            if (show) {
                binding?.saldoButtonAffiliate?.invisible()
                binding?.tarikSaldoLoader?.show()
            } else {
                binding?.saldoButtonAffiliate?.show()
                binding?.tarikSaldoLoader?.hide()
            }
        }

        affiliateIncomeViewModel?.getKycErrorMessage()?.observe(this) {
            view?.let {
                Toaster.build(
                    it,
                    getString(R.string.affiliate_retry_message),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
        affiliateIncomeViewModel?.getValidateUserdata()?.observe(this) { validateUserdata ->
            onGetValidateUserData(validateUserdata)
        }
        affiliateIncomeViewModel?.getAffiliateAnnouncement()?.observe(this) { announcementData ->
            if (announcementData.getAffiliateAnnouncementV2?.announcementData?.subType != TICKER_BOTTOM_SHEET) {
                sendTickerImpression(
                    announcementData.getAffiliateAnnouncementV2?.announcementData?.type,
                    announcementData.getAffiliateAnnouncementV2?.announcementData?.id
                )
                binding?.affiliateAnnouncementTicker?.setAnnouncementData(
                    announcementData,
                    activity,
                    source = PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY
                )
            }
        }
        affiliateIncomeViewModel?.getUnreadNotificationCount()?.observe(this) { count ->
            binding?.withdrawalNavToolbar?.apply {
                setCentralizedBadgeCounter(IconList.ID_NOTIFICATION, count)
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
                tickerId.orZero(),
                AffiliateAnalytics.ItemKeys.AFFILIATE_PENDAPATAN_TICKER_COMMUNICATION,
                userSession?.userId.orEmpty()
            )
        }
    }

    private fun onGetError(error: Throwable?) {
        binding?.withdrawalGlobalError?.run {
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
                affiliateIncomeViewModel?.getAffiliateBalance()
                affiliateIncomeViewModel?.getAffiliateTransactionHistory(PAGE_ZERO)
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
        binding?.swipe?.isRefreshing = false
    }

    private fun hideGlobalErrorEmptyState() {
        binding?.affiliateNoTransactionIv?.hide()
        binding?.withdrawalGlobalError?.hide()
        binding?.affiliateNoDefaultTransactionIv?.hide()
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        hideGlobalErrorEmptyState()
        affiliateIncomeViewModel?.getAffiliateBalance()
        affiliateIncomeViewModel?.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    private fun showGlobalErrorEmptyState() {
        binding?.affiliateNoTransactionIv?.show()

        binding?.withdrawalGlobalError?.apply {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_empty_transaction)
            errorDescription.text = getString(R.string.affiliate_empty_transaction_description)
            errorSecondaryAction.gone()
            errorAction.text = getString(R.string.affiliate_choose_date)
            setActionClickListener {
                AffiliateBottomDatePicker.newInstance(
                    affiliateIncomeViewModel?.getSelectedDate()
                        ?: AffiliateBottomDatePicker.THIRTY_DAYS,
                    this@AffiliateIncomeFragment
                ).show(childFragmentManager, "")
            }
        }
    }

    private fun setErrorState(message: String?) {
        binding?.withdrawalGlobalError?.apply {
            show()
            errorTitle.text = message
            errorSecondaryAction.gone()
            setActionClickListener {
                hide()
                affiliateIncomeViewModel?.getAffiliateBalance()
                affiliateIncomeViewModel?.getAffiliateTransactionHistory(PAGE_ZERO)
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
                userSession?.userId.orEmpty()
            )
        }
    }

    private fun setAffiliateBalance(affiliateBalance: AffiliateBalance.AffiliateBalance.Data) {
        affiliateBalance.apply {
            binding?.saldoAmountAffiliate?.let {
                it.text = amountFormatted
                it.show()
            }
            binding?.affiliateSaldoProgressBar?.gone()
            walletOnHold = this.walletStatus == WALLET_STATUS_HOLD
            initTarikSaldo()
        }
    }

    private fun afterViewCreated() {
        setTarikSaldoButtonUI()
        binding?.withdrawalUserName?.text = userName
        binding?.swipe?.let {
            it.setOnRefreshListener {
                resetItems()
            }
            binding?.appbar

                ?.addOnOffsetChangedListener { _, verticalOffset ->
                    it.isEnabled = verticalOffset == 0
                }
        }
        binding?.saldoButtonAffiliate?.setOnClickListener {
            sendPendapatanEvent(
                AffiliateAnalytics.ActionKeys.CLICK_TARIK_SALDO,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                ""
            )
            openWithdrawalScreen()
        }
        binding?.withdrawalUserImage?.loadImageCircle(profilePicture)
        binding?.dateRangeText?.text =
            affiliateIncomeViewModel?.getSelectedDate()
        binding?.dateRange?.setOnClickListener {
            AffiliateBottomDatePicker.newInstance(AffiliateBottomDatePicker.TODAY, this)
                .show(childFragmentManager, "")
        }
        binding?.withdrawalTransactionsRv?.let {
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
        binding?.withdrawalNavToolbar?.run {
            val iconBuilder = IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.AFFILIATE))
            if (isAffiliateNCEnabled()) {
                iconBuilder.addIcon(IconList.ID_NOTIFICATION, disableRouteManager = true) {
                    affiliateIncomeViewModel?.resetNotificationCount()
                    sendNotificationClickEvent()
                    RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.AFFILIATE_NOTIFICATION
                    )
                }
            }
            iconBuilder.addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(
                    R.string.affiliate_withdrawal
                )
        }
        initDateRangeClickListener()
        affiliateIncomeViewModel?.getAffiliateValidateUser(userSession?.email.orEmpty())
        if (isAffiliateNCEnabled()) {
            affiliateIncomeViewModel?.fetchUnreadNotificationCount()
        }
    }

    private fun sendNotificationClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_NOTIFICATION_ENTRY_POINT,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "",
            userSession?.userId.orEmpty()
        )
    }

    private fun setTarikSaldoButtonUI() {
        binding?.saldoButtonAffiliate?.apply {
            isEnabled = affiliateIncomeViewModel?.getIsBlackListed() == false
        }
    }

    private fun openWithdrawalScreen() {
        affiliateIncomeViewModel?.getKycDetails()
    }

    private fun initDateRangeClickListener() {
        binding?.dateRange?.setOnClickListener {
            sendPendapatanEvent(
                AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE,
                ""
            )
            AffiliateBottomDatePicker.newInstance(
                affiliateIncomeViewModel?.getSelectedDate()
                    ?: AffiliateBottomDatePicker.THIRTY_DAYS,
                this
            )
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
                if (affiliateIncomeViewModel?.hasNext == true) {
                    affiliateIncomeViewModel?.getAffiliateTransactionHistory(page - 1)
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
                userSession?.userId.orEmpty()
            )
        }
    }

    override fun rangeChanged(range: AffiliateDatePickerData) {
        sendPendapatanEvent(
            AffiliateAnalytics.ActionKeys.CLICK_SIMPAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PENDAPATAN_PAGE_FILTER,
            range.value
        )
        affiliateIncomeViewModel?.onRangeChanged(range)
    }

    override fun onRangeSelectionButtonClicked() = Unit

    override fun onSystemDown() {
        affiliateIncomeViewModel?.getAnnouncementInformation()
        hideAllView()
        disableSwipeToRefresh()
    }

    private fun disableSwipeToRefresh() {
        binding?.swipe?.apply {
            setOnRefreshListener {
                isRefreshing = false
            }
        }
    }

    private fun hideAllView() {
        binding?.withdrawalHomeWidget?.hide()
        binding?.transactionHistoryAffiliate?.hide()
        binding?.filterCard?.hide()
    }

    override fun onReviewed() {
        isReviewed = false
        initTarikSaldo()
        affiliateIncomeViewModel?.getAnnouncementInformation()
        affiliateIncomeViewModel?.getAffiliateBalance()
        affiliateIncomeViewModel?.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    private fun initTarikSaldo() {
        binding?.saldoButtonAffiliate?.isEnabled =
            isReviewed && affiliateIncomeViewModel?.getIsBlackListed() == false && !walletOnHold
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
        affiliateIncomeViewModel?.getAnnouncementInformation()
        affiliateIncomeViewModel?.getAffiliateBalance()
        affiliateIncomeViewModel?.getAffiliateTransactionHistory(PAGE_ZERO)
    }

    override fun getViewModelType(): Class<AffiliateIncomeViewModel> {
        return AffiliateIncomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateIncomeViewModel = viewModel as AffiliateIncomeViewModel
    }
}
