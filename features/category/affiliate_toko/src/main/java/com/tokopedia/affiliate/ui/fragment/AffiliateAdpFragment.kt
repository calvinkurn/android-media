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
import com.tokopedia.affiliate.AFFILIATE_NC
import com.tokopedia.affiliate.AFFILIATE_PROMOTE_HOME
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.CLICK_TYPE
import com.tokopedia.affiliate.COMMISSION_TYPE
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_HOME
import com.tokopedia.affiliate.PAGE_ZERO
import com.tokopedia.affiliate.TIME_EIGHTEEN
import com.tokopedia.affiliate.TIME_ELEVEN
import com.tokopedia.affiliate.TIME_FIFTEEN
import com.tokopedia.affiliate.TIME_SIX
import com.tokopedia.affiliate.TIME_SIXTEEN
import com.tokopedia.affiliate.TIME_TEN
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
import com.tokopedia.affiliate.viewmodel.AffiliateAdpViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliateAdpFragmentLayoutBinding
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateAdpFragment :
    AffiliateBaseFragment<AffiliateAdpViewModel>(),
    ProductClickInterface,
    AffiliatePerformaClickInterfaces,
    AffiliateDatePickerRangeChangeInterface,
    AffiliatePerformanceChipClick {

    private var isSwipeRefresh = false
    private var listSize = 0

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var remoteConfig: RemoteConfigInstance? = null

    private var binding by autoClearedNullable<AffiliateAdpFragmentLayoutBinding>()

    private var bottomNavBarClickListener: AffiliateBottomNavBarInterface? = null
    private var affiliateActivityInterface: AffiliateActivityInterface? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private var affiliateAdpViewModel: AffiliateAdpViewModel? = null
    private val adapter: AffiliateAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(
                productClickInterface = this,
                onDateRangeClickInterface = this,
                onPerformaGridClick = this,
                bottomNavBarClickListener = bottomNavBarClickListener,
                affiliatePerformanceChipClick = this
            ),
            source = AffiliateAdapter.PageSource.SOURCE_HOME,
            userId = userSessionInterface?.userId.orEmpty()
        )
    }
    private var isUserBlackListed = false
    private var isNoPromoItem = false
    private var isNoMoreData = false

    private val loginRequest =
        registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setUserDetails()
                affiliateAdpViewModel?.getAffiliateValidateUser()
            } else {
                activity?.finish()
            }
        }

    private fun isAffiliateNCEnabled() =
        remoteConfig?.abTestPlatform?.getString(
            AFFILIATE_NC,
            ""
        ) == AFFILIATE_NC

    private fun isAffiliatePromoteHomeEnabled() =
        remoteConfig?.abTestPlatform?.getString(
            AFFILIATE_PROMOTE_HOME,
            ""
        ) == AFFILIATE_PROMOTE_HOME

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
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AffiliateAdpFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
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
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun afterViewCreated() {
        if (affiliateAdpViewModel?.isUserLoggedIn() == false) {
            loginRequest.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        } else {
            affiliateAdpViewModel?.getAffiliateValidateUser()
        }
        setAffiliateGreeting()
        binding?.navHeaderGroup?.isVisible = !isAffiliatePromoteHomeEnabled()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        binding?.productsRv?.layoutManager = layoutManager
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            isSwipeRefresh = true
            resetItems()
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        binding?.productsRv?.adapter = adapter
        loadMoreTriggerListener?.let { binding?.productsRv?.addOnScrollListener(it) }
        binding?.homeNavToolbar?.run {
            val iconBuilder = IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.AFFILIATE))
            if (isAffiliateNCEnabled()) {
                iconBuilder.addIcon(IconList.ID_NOTIFICATION, disableRouteManager = true) {
                    affiliateAdpViewModel?.resetNotificationCount()
                    sendNotificationClickEvent()
                    RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.AFFILIATE_NOTIFICATION
                    )
                }
            }
            if (!isAffiliatePromoteHomeEnabled()) {
                iconBuilder.addIcon(IconList.ID_BILL) { openHistoryActivity() }
            }
            iconBuilder
                .addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                if (isAffiliatePromoteHomeEnabled()) {
                    getString(R.string.performa_affiliate)
                } else {
                    getString(R.string.label_affiliate)
                }
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }

//        if (!CoachMarkPreference.hasShown(
//                requireContext(),
//                COACHMARK_TAG
//            )
//        ) {
//            affiliateActivityInterface?.showCoachMarker()
//        }
        setUserDetails()
        if (isAffiliateNCEnabled()) {
            affiliateAdpViewModel?.fetchUnreadNotificationCount()
        }
    }

    private fun sendNotificationClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_NOTIFICATION_ENTRY_POINT,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "",
            userSessionInterface?.userId.orEmpty()
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

    private fun setUserDetails() {
        binding?.userImage?.loadImageCircle(affiliateAdpViewModel?.getUserProfilePicture())
        binding?.userName?.text = affiliateAdpViewModel?.getUserName()
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliateAdpViewModel?.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
        if (isAffiliateNCEnabled()) {
            affiliateAdpViewModel?.fetchUnreadNotificationCount()
        }
    }

    private fun setAffiliateGreeting() {
        view?.findViewById<Typography>(R.id.affiliate_greeting)?.text =
            when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                in TIME_SIX..TIME_TEN -> getString(R.string.affiliate_morning)
                in TIME_ELEVEN..TIME_FIFTEEN -> getString(R.string.affiliate_noon)
                in TIME_SIXTEEN..TIME_EIGHTEEN -> getString(R.string.affiliate_afternoon)
                else -> getString(R.string.affiliate_night)
            }
    }

    private fun getEndlessRecyclerViewListener(
        recyclerViewLayoutManager: RecyclerView.LayoutManager
    ): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!isNoPromoItem && !isNoMoreData) {
                    affiliateAdpViewModel?.getAffiliatePerformance(page - 1)
                }
            }
        }
    }

    private fun setObservers() {
        affiliateAdpViewModel?.getShimmerVisibility()?.observe(this) { visibility ->
            setShimmerVisibility(visibility)
        }
        affiliateAdpViewModel?.getDataShimmerVisibility()?.observe(this) { visibility ->
            setDataShimmerVisibility(visibility)
        }
        affiliateAdpViewModel?.getRangeChanged()?.observe(this) { changed ->
            if (changed) resetItems()
        }
        affiliateAdpViewModel?.progressBar()?.observe(this) { visibility ->
            binding?.affiliateProgressBar?.isVisible = visibility.orFalse()
        }
        affiliateAdpViewModel?.getErrorMessage()?.observe(this) { error ->
            onGetError(error)
        }
        affiliateAdpViewModel?.getValidateUserdata()?.observe(this) { validateUserdata ->
            binding?.affiliateProgressBar?.gone()
            binding?.swipeRefreshLayout?.show()
            onGetValidateUserData(validateUserdata)
        }

        affiliateAdpViewModel?.getAffiliateDataItems()?.observe(this) { dataList ->
            isNoPromoItem = dataList.firstOrNull { it is AffiliateNoPromoItemFoundModel } != null

            onGetAffiliateDataItems(dataList)
        }

        affiliateAdpViewModel?.getAffiliateAnnouncement()?.observe(this) { announcementData ->
            if (announcementData.getAffiliateAnnouncementV2?.data?.subType == TICKER_BOTTOM_SHEET &&
                !isAffiliatePromoteHomeEnabled()
            ) {
                context?.getSharedPreferences(TICKER_SHARED_PREF, Context.MODE_PRIVATE)?.let {
                    if (it.getString(
                            USER_ID,
                            null
                        ) != userSessionInterface?.userId.orEmpty() || it.getLong(
                                TICKER_ID,
                                -1
                            ) != announcementData.getAffiliateAnnouncementV2?.data?.id
                    ) {
                        it.edit().apply {
                            putLong(
                                TICKER_ID,
                                announcementData.getAffiliateAnnouncementV2?.data?.id ?: 0
                            )
                            putString(USER_ID, userSessionInterface?.userId.orEmpty())
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
                binding?.affiliateAnnouncementTicker?.setAnnouncementData(
                    announcementData,
                    activity,
                    source = PAGE_ANNOUNCEMENT_HOME
                )
            }
        }
        affiliateAdpViewModel?.noMoreDataAvailable()?.observe(this) { noDataAvailable ->
            isNoMoreData = noDataAvailable
        }
        affiliateAdpViewModel?.getUnreadNotificationCount()?.observe(this) { count ->
            binding?.homeNavToolbar?.apply {
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
                userSessionInterface?.userId.orEmpty()
            )
        }
    }

    private fun onGetAffiliateDataItems(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        adapter.removeShimmer(listSize)
        if (isSwipeRefresh) {
            binding?.swipeRefreshLayout?.isRefreshing = false
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
        binding?.homeGlobalError?.run {
            when (error) {
                is UnknownHostException, is SocketTimeoutException -> setType(GlobalError.NO_CONNECTION)

                is IllegalStateException -> setType(GlobalError.PAGE_FULL)

                else -> setType(GlobalError.SERVER_ERROR)
            }
            binding?.swipeRefreshLayout?.hide()
            show()
            setActionClickListener {
                hide()
                affiliateAdpViewModel?.getAffiliateValidateUser()
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

    override fun getVMFactory(): ViewModelProvider.Factory? {
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

    override fun getViewModelType(): Class<AffiliateAdpViewModel> {
        return AffiliateAdpViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateAdpViewModel = viewModel as AffiliateAdpViewModel
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
        affiliateAdpViewModel?.onRangeChanged(range)
        if (range.value == RANGE_TODAY) {
            affiliateAdpViewModel?.startSSE()
        } else {
            affiliateAdpViewModel?.stopSSE()
        }
    }

    override fun onRangeSelectionButtonClicked() {
        sendHomeEvent(
            AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,
            "",
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE
        )
        AffiliateBottomDatePicker.newInstance(
            affiliateAdpViewModel?.getSelectedDate().orEmpty(),
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
            userSessionInterface?.userId.orEmpty()
        )
    }

    override fun onInfoClick(
        title: String?,
        desc: String?,
        metrics: List<
            AffiliateUserPerformaListItemData
            .GetAffiliatePerformance
            .Data
            .UserData
            .Metrics
            .Tooltip
            .SubMetrics?
            >?,
        type: String?,
        tickerInfo: String?
    ) {
        sendInfoClickEvent(type)
        AffiliateRecylerBottomSheet.newInstance(
            TYPE_HOME,
            title,
            desc,
            metrics,
            affiliateAdpViewModel?.getSelectedDate().orEmpty(),
            tickerInfo = tickerInfo
        ).show(childFragmentManager, "")
    }

    override fun onPromoHistoryClick() {
        openHistoryActivity()
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
                userSessionInterface?.userId.orEmpty()
            )
        }
    }

    override fun onSystemDown() {
        affiliateAdpViewModel?.getAnnouncementInformation(!isAffiliatePromoteHomeEnabled())
        binding?.swipeRefreshLayout?.hide()
    }

    override fun onReviewed() {
        affiliateAdpViewModel?.getAnnouncementInformation(!isAffiliatePromoteHomeEnabled())
        affiliateAdpViewModel?.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
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
        affiliateAdpViewModel?.getAnnouncementInformation(!isAffiliatePromoteHomeEnabled())
        affiliateAdpViewModel?.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
    }

    override fun onChipClick(type: ItemTypesItem?) {
        sendFilterClickEvent(type)
        affiliateAdpViewModel?.lastSelectedChip = type
        partialReset()
        affiliateAdpViewModel?.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun sendFilterClickEvent(type: ItemTypesItem?) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            AffiliateAnalytics.ActionKeys.CLICK_PROMOTED_PAGE_FILTER_TAB,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "${type?.name?.lowercase()}",
            userSessionInterface?.userId.orEmpty()
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
        listSize = affiliateAdpViewModel?.staticSize.orZero()
        isNoMoreData = false
    }
}
