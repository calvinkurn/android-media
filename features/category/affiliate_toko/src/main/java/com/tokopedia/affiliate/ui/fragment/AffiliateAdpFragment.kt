package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.affiliate.AFFILIATE_NC
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.CLICK_TYPE
import com.tokopedia.affiliate.COACHMARK_TAG
import com.tokopedia.affiliate.COMMISSION_TYPE
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.interfaces.AffiliatePerformanceChipClick
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.ItemTypesItem
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateComponentActivity
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker.Companion.IDENTIFIER_HOME
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateRecylerBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateRecylerBottomSheet.Companion.TYPE_HOME
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateNoPromoItemFoundModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateAdpFragment :
    AffiliateBaseFragment<AffiliateHomeViewModel>(),
    ProductClickInterface,
    AffiliatePerformaClickInterfaces,
    AffiliateDatePickerRangeChangeInterface,
    AffiliatePerformanceChipClick {

    private var isSwipeRefresh = false
    private var listSize = 0

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface
    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null
    private var affiliateActivityInterface: AffiliateActivityInterface? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel
    lateinit var adapter: AffiliateAdapter
    private var isUserBlackListed = false
    private var isNoPromoItem = false
    private var isNoMoreData = false
    private var productRV: RecyclerView? = null
    private var swipeRefresh: SwipeToRefresh? = null
    private var navToolbar: NavToolbar? = null
    private var loader: LoaderUnify? = null

    private val loginRequest =
        registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                affiliateHomeViewModel.getAffiliateValidateUser()
            } else {
                activity?.finish()
            }
        }
    private fun isAffiliateNCEnabled() =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_NC,
            ""
        ) == AFFILIATE_NC

    private val linkHistory = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            bottomNavBarClickListener?.selectItem(
                AffiliateActivity.PROMO_MENU,
                R.id.menu_promo_affiliate,
                true
            )
        }
    }

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        private const val TICKER_SHARED_PREF = "tickerSharedPref"
        private const val USER_ID = "userId"
        private const val TICKER_ID = "tickerId"
        private const val RANGE_TODAY = "0"
        fun getFragmentInstance(
            affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface,
            affiliateActivity: AffiliateActivityInterface
        ): Fragment {
            return AffiliateAdpFragment().apply {
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
                affiliateActivityInterface = affiliateActivity
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setObservers()
    }

    private fun initAdapter() {
        adapter = AffiliateAdapter(
            AffiliateAdapterFactory(
                productClickInterface = this,
                onDateRangeClickInterface = this,
                onPerformaGridClick = this,
                bottomNavBarClickListener = bottomNavBarClickListener,
                affiliatePerformanceChipClick = this
            ),
            source = AffiliateAdapter.PageSource.SOURCE_HOME,
            userId = userSessionInterface.userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_adp_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
        sendOpenScreenTracking()
    }

    private fun sendOpenScreenTracking() {
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            "${AffiliateAnalytics.ScreenKeys.AFFILIATE_HOME_FRAGMENT}register",
            userSessionInterface.isLoggedIn,
            userSessionInterface.userId
        )
    }

    private fun afterViewCreated() {
        productRV = view?.findViewById(R.id.products_rv)
        swipeRefresh = view?.findViewById(R.id.swipe_refresh_layout)
        navToolbar = view?.findViewById(R.id.home_navToolbar)
        loader = view?.findViewById(R.id.affiliate_progress_bar)
        if (!affiliateHomeViewModel.isUserLoggedIn()) {
            loginRequest.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        } else {
            affiliateHomeViewModel.getAffiliateValidateUser()
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        productRV?.layoutManager = layoutManager
        swipeRefresh?.setOnRefreshListener {
            isSwipeRefresh = true
            resetItems()
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        productRV?.adapter = adapter
        loadMoreTriggerListener?.let { productRV?.addOnScrollListener(it) }
        navToolbar?.run {
            val iconBuilder = IconBuilder()
            if (isAffiliateNCEnabled()) {
                iconBuilder.addIcon(IconList.ID_NOTIFICATION, disableRouteManager = true) {
                    affiliateHomeViewModel.resetNotificationCount()
                    sendNotificationClickEvent()
                    RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.AFFILIATE_NOTIFICATION
                    )
                }
            }
            iconBuilder
                .addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(
                IconBuilder()
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(R.string.performa_affiliate)
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }

        if (!CoachMarkPreference.hasShown(
                requireContext(),
                COACHMARK_TAG
            )
        ) {
            affiliateActivityInterface?.showCoachMarker()
        }
        if (isAffiliateNCEnabled()) {
            affiliateHomeViewModel.fetchUnreadNotificationCount()
        }
    }

    private fun sendNotificationClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_NOTIFICATION_ENTRY_POINT,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "",
            userSessionInterface.userId
        )
    }

    private fun openHistoryActivity() {
        sendHomeEvent(
            AffiliateAnalytics.ActionKeys.CLICK_GENERATED_LINK_HISTORY,
            "",
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE
        )
        val intent = Intent(context, AffiliateComponentActivity::class.java)
        intent.putExtra("isUserBlackListed", isUserBlackListed)
        linkHistory.launch(intent)
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
        if (isAffiliateNCEnabled()) {
            affiliateHomeViewModel.fetchUnreadNotificationCount()
        }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isNoPromoItem && !isNoMoreData) {
                    affiliateHomeViewModel.getAffiliatePerformance(page - 1)
                }
            }
        }
    }

    private fun setObservers() {
        affiliateHomeViewModel.getShimmerVisibility().observe(this) { visibility ->
            setShimmerVisibility(visibility)
        }
        affiliateHomeViewModel.getDataShimmerVisibility().observe(this) { visibility ->
            setDataShimmerVisibility(visibility)
        }
        affiliateHomeViewModel.getRangeChanged().observe(this) { changed ->
            if (changed) resetItems()
        }
        affiliateHomeViewModel.progressBar().observe(this) { visibility ->
            loader?.isVisible = visibility.orFalse()
        }
        affiliateHomeViewModel.getErrorMessage().observe(this) { error ->
            onGetError(error)
        }
        affiliateHomeViewModel.getValidateUserdata().observe(this) { validateUserdata ->
            loader?.gone()
            swipeRefresh?.show()
            onGetValidateUserData(validateUserdata)
        }

        affiliateHomeViewModel.getAffiliateDataItems().observe(this) { dataList ->
            isNoPromoItem = dataList.firstOrNull { it is AffiliateNoPromoItemFoundModel } != null

            onGetAffiliateDataItems(dataList)
        }

        affiliateHomeViewModel.getAffiliateAnnouncement().observe(this) { announcementData ->
            if (announcementData.getAffiliateAnnouncementV2?.data?.subType == TICKER_BOTTOM_SHEET) {
                context?.getSharedPreferences(TICKER_SHARED_PREF, Context.MODE_PRIVATE)?.let {
                    if (it.getString(USER_ID, null) != userSessionInterface.userId || it.getLong(
                            TICKER_ID,
                            -1
                        ) != announcementData.getAffiliateAnnouncementV2?.data?.id
                    ) {
                        it.edit().apply {
                            putLong(
                                TICKER_ID,
                                announcementData.getAffiliateAnnouncementV2?.data?.id ?: 0
                            )
                            putString(USER_ID, userSessionInterface.userId)
                            apply()
                        }

                        AffiliateBottomSheetInfo.newInstance(
                            announcementData.getAffiliateAnnouncementV2?.data?.id ?: 0,
                            announcementData.getAffiliateAnnouncementV2?.data?.tickerData?.first()
                        ).show(childFragmentManager, "")
                    }
                }
            } else {
                sendTickerImpression(
                    announcementData.getAffiliateAnnouncementV2?.data?.type,
                    announcementData.getAffiliateAnnouncementV2?.data?.id
                )
                view?.findViewById<Ticker>(R.id.affiliate_announcement_ticker)
                    ?.setAnnouncementData(
                        announcementData,
                        activity,
                        source = PAGE_ANNOUNCEMENT_HOME
                    )
            }
        }
        affiliateHomeViewModel.noMoreDataAvailable().observe(this) { noDataAvailable ->
            isNoMoreData = noDataAvailable
        }
        affiliateHomeViewModel.getUnreadNotificationCount().observe(this) { count ->
            navToolbar?.apply {
                setCentralizedBadgeCounter(IconList.ID_NOTIFICATION, count)
            }
        }
    }

    private fun sendTickerImpression(tickerType: String?, tickerId: Long?) {
        if (tickerId.isMoreThanZero()) {
            AffiliateAnalytics.sendTickerEvent(
                AffiliateAnalytics.EventKeys.VIEW_ITEM,
                AffiliateAnalytics.ActionKeys.IMPRESSION_TICKER_COMMUNICATION,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
                "$tickerType - $tickerId",
                PAGE_ANNOUNCEMENT_HOME,
                tickerId!!,
                AffiliateAnalytics.ItemKeys.AFFILIATE_HOME_TICKER_COMMUNICATION,
                userSessionInterface.userId
            )
        }
    }

    private fun onGetAffiliateDataItems(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        adapter.removeShimmer(listSize)
        if (isSwipeRefresh) {
            swipeRefresh?.isRefreshing = false
            isSwipeRefresh = !isSwipeRefresh
        }
        if (dataList.isNotEmpty()) {
            setLastDataForEvent(dataList)
            listSize += dataList.size
            adapter.addMoreData(dataList)
            loadMoreTriggerListener?.updateStateAfterGetData()
        }
    }

    private fun onGetError(error: Throwable?) {
        view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
            when (error) {
                is UnknownHostException, is SocketTimeoutException -> {
                    setType(GlobalError.NO_CONNECTION)
                }

                is IllegalStateException -> {
                    setType(GlobalError.PAGE_FULL)
                }

                else -> {
                    setType(GlobalError.SERVER_ERROR)
                }
            }
            swipeRefresh?.hide()
            show()
            setActionClickListener {
                hide()
                affiliateHomeViewModel.getAffiliateValidateUser()
            }
        }
    }

    private fun setDataShimmerVisibility(visibility: Boolean?) {
        if (visibility != null) {
            if (visibility) {
                adapter.addDataPlatformShimmer()
            } else {
                adapter.resetList()
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean?) {
        if (visibility != null) {
            if (visibility) {
                adapter.addShimmer()
            } else {
                adapter.removeShimmer(listSize)
            }
        }
    }

    private var lastItem: AffiliatePerformaSharedProductCardsModel? = null
    private fun setLastDataForEvent(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        dataList[dataList.lastIndex].let {
            if (it is AffiliatePerformaSharedProductCardsModel) {
                lastItem = it
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectAdpFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getViewModelType(): Class<AffiliateHomeViewModel> {
        return AffiliateHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateHomeViewModel = viewModel as AffiliateHomeViewModel
    }

    override fun onProductClick(
        productId: String,
        productName: String,
        productImage: String,
        productUrl: String,
        productIdentifier: String,
        status: Int?,
        type: String?,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo?
    ) {
        if (status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE) {
            AffiliatePromotionBottomSheet.newInstance(
                AffiliatePromotionBottomSheetParams(
                    null, productId, productName, productImage, productUrl, productIdentifier,
                    AffiliatePromotionBottomSheet.ORIGIN_HOME, !isUserBlackListed, type = type,
                    ssaInfo = ssaInfo
                ),
                AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null
            ).show(childFragmentManager, "")
        }
    }

    override fun rangeChanged(range: AffiliateDatePickerData) {
        sendHomeEvent(
            AffiliateAnalytics.ActionKeys.CLICK_SIMPAN,
            range.value,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_FILTER
        )
        affiliateHomeViewModel.onRangeChanged(range)
        if (range.value == RANGE_TODAY) {
            affiliateHomeViewModel.startSSE()
        } else {
            affiliateHomeViewModel.stopSSE()
        }
    }

    override fun onRangeSelectionButtonClicked() {
        sendHomeEvent(
            AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,
            "",
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE
        )
        AffiliateBottomDatePicker.newInstance(
            affiliateHomeViewModel.getSelectedDate(),
            this,
            IDENTIFIER_HOME
        ).show(childFragmentManager, "")
    }

    private fun sendHomeEvent(eventAction: String, eventLabel: String, eventCategory: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            eventCategory,
            eventLabel,
            userSessionInterface.userId
        )
    }

    override fun onInfoClick(
        title: String?,
        desc: String?,
        metrics: List<AffiliateUserPerformaListItemData
            .GetAffiliatePerformance
            .Data
            .UserData
            .Metrics
            .Tooltip
            .SubMetrics?>?,
        type: String?,
        tickerInfo: String?
    ) {
        sendInfoClickEvent(type)
        AffiliateRecylerBottomSheet.newInstance(
            TYPE_HOME,
            title,
            desc,
            metrics,
            affiliateHomeViewModel.getSelectedDate(),
            tickerInfo = tickerInfo
        ).show(childFragmentManager, "")
    }

    private fun sendInfoClickEvent(type: String?) {
        var action = ""
        when (type) {
            CLICK_TYPE -> action = AffiliateAnalytics.ActionKeys.CLICK_KLIK_CARD
            COMMISSION_TYPE -> action = AffiliateAnalytics.ActionKeys.CLICK_TOTAL_KOMISI_CARD
        }
        if (action.isNotEmpty()) {
            AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_CONTENT,
                action,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
                "",
                userSessionInterface.userId
            )
        }
    }

    override fun onSystemDown() {
        affiliateHomeViewModel.getAnnouncementInformation()
        swipeRefresh?.hide()
    }

    override fun onReviewed() {
        affiliateHomeViewModel.getAnnouncementInformation()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
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
        affiliateHomeViewModel.getAnnouncementInformation()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
    }

    override fun onChipClick(type: ItemTypesItem?) {
        sendFilterClickEvent(type)
        affiliateHomeViewModel.lastSelectedChip = type
        partialReset()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun sendFilterClickEvent(type: ItemTypesItem?) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            AffiliateAnalytics.ActionKeys.CLICK_PROMOTED_PAGE_FILTER_TAB,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "${type?.name?.lowercase()}",
            userSessionInterface.userId
        )
    }

    private fun partialReset() {
        val index = adapter.list.indexOfFirst {
            it is AffiliatePerformaSharedProductCardsModel || it is AffiliateNoPromoItemFoundModel
        }
        adapter.list.removeAll {
            it is AffiliatePerformaSharedProductCardsModel || it is AffiliateNoPromoItemFoundModel
        }
        if (index >= 0) {
            adapter.notifyItemRangeRemoved(index, listSize - index)
        }
        loadMoreTriggerListener?.resetState()
        listSize = affiliateHomeViewModel.staticSize
        isNoMoreData = false
    }
}
