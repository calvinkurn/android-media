package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.affiliate.AFFILIATE_LOGIN_REQUEST_CODE
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.CLICK_TYPE
import com.tokopedia.affiliate.COACHMARK_TAG
import com.tokopedia.affiliate.COMMISSION_TYPE
import com.tokopedia.affiliate.LINK_HISTORY_BUTTON_CLICKED
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
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.ItemTypesItem
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateComponentActivity
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker.Companion.IDENTIFIER_HOME
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
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
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Calendar
import javax.inject.Inject

class AffiliateHomeFragment : AffiliateBaseFragment<AffiliateHomeViewModel>(),
    ProductClickInterface,
    AffiliatePerformaClickInterfaces, AffiliateDatePickerRangeChangeInterface,
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

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        private const val TICKER_SHARED_PREF = "tickerSharedPref"
        private const val USER_ID = "userId"
        private const val TICKER_ID = "tickerId"
        fun getFragmentInstance(
            affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface,
            affiliateActivity: AffiliateActivityInterface
        ): Fragment {
            return AffiliateHomeFragment().apply {
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
                affiliateActivityInterface = affiliateActivity
            }
        }
        private const val PARTIAL_RESET_LENGTH =3
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
            source = AffiliateAdapter.SOURCE_HOME,
            userId = userSessionInterface.userId
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_home_fragment_layout, container, false)
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
        val productRV = view?.findViewById<RecyclerView>(R.id.products_rv)
        if (!affiliateHomeViewModel.isUserLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    AFFILIATE_LOGIN_REQUEST_CODE)
        } else {
            affiliateHomeViewModel.getAffiliateValidateUser()
        }
        setAffiliateGreeting()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        productRV?.layoutManager = layoutManager
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.setOnRefreshListener {
            isSwipeRefresh = true
            resetItems()
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        productRV?.adapter = adapter
        loadMoreTriggerListener?.let { productRV?.addOnScrollListener(it) }
        view?.findViewById<NavToolbar>(R.id.home_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_BILL){
                                openHistoryActivity()
                            }
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
//            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.label_affiliate)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.gone()
            setOnBackButtonClickListener {
                (activity as? AffiliateActivity)?.handleBackButton(false)
            }
        }
        if(!CoachMarkPreference.hasShown(requireContext(), COACHMARK_TAG)) affiliateActivityInterface?.showCoachMarker()
        setUserDetails()
    }

    private fun openHistoryActivity() {
        sendHomeEvent(AffiliateAnalytics.ActionKeys.CLICK_GENERATED_LINK_HISTORY,"",AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE)
        val intent = Intent(context,AffiliateComponentActivity::class.java)
        intent.putExtra("isUserBlackListed",isUserBlackListed)
        startActivityForResult(intent,LINK_HISTORY_BUTTON_CLICKED)
    }

    private fun setUserDetails() {
        view?.findViewById<ImageUnify>(R.id.user_image)
            ?.loadImageCircle(affiliateHomeViewModel.getUserProfilePicture())
        view?.findViewById<Typography>(R.id.user_name)?.text = affiliateHomeViewModel.getUserName()
    }

    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
    }

    private fun setAffiliateGreeting() {
        view?.findViewById<Typography>(R.id.affiliate_greeting)?.text = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in TIME_SIX..TIME_TEN -> getString(R.string.affiliate_morning)
            in TIME_ELEVEN..TIME_FIFTEEN -> getString(R.string.affiliate_noon)
            in TIME_SIXTEEN..TIME_EIGHTEEN -> getString(R.string.affiliate_afternoon)
            else ->getString(R.string.affiliate_night)
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
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
            setProgressBar(visibility)
        }
        affiliateHomeViewModel.getErrorMessage().observe(this) { error ->
            onGetError(error)
        }
        affiliateHomeViewModel.getValidateUserdata().observe(this) { validateUserdata ->
            view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.show()
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
                    ?.setAnnouncementData(announcementData, activity, source = PAGE_ANNOUNCEMENT_HOME)
            }
        }
        affiliateHomeViewModel.noMoreDataAvailable().observe(this) { noDataAvailable ->
            isNoMoreData = noDataAvailable
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
        if(isSwipeRefresh){
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.isRefreshing = false
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
            when(error) {
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
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
            show()
            setActionClickListener {
                hide()
                affiliateHomeViewModel.getAffiliateValidateUser()
            }
        }
    }

    private fun setProgressBar(visibility: Boolean?) {
        if (visibility != null) {
            if (visibility) {
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
            } else {
                view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
            }
        }
    }

    private fun setDataShimmerVisibility(visibility: Boolean?) {
        if (visibility != null) {
            if (visibility)
                adapter.addDataPlatformShimmer()
            else
                adapter.resetList()
        }
    }

    private fun setShimmerVisibility(visibility: Boolean?) {
        if (visibility != null) {
            if (visibility)
                adapter.addShimmer()
            else
                adapter.removeShimmer(listSize)
        }
    }

    private var lastItem: AffiliatePerformaSharedProductCardsModel? = null
    private fun setLastDataForEvent(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        dataList[dataList.lastIndex].let {
            if(it is AffiliatePerformaSharedProductCardsModel){
                lastItem = it
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectHomeFragment(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AFFILIATE_LOGIN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    setUserDetails()
                    affiliateHomeViewModel.getAffiliateValidateUser()
                } else {
                    activity?.finish()
                }
            }
            LINK_HISTORY_BUTTON_CLICKED -> {
                if (resultCode == Activity.RESULT_OK) {
                    bottomNavBarClickListener?.selectItem(AffiliateActivity.PROMO_MENU,R.id.menu_promo_affiliate,true)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, status : Int?, type: String?) {
        if(status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE){
            AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                    null,null,productId , productName , productImage, productUrl,productIdentifier,
                    AffiliatePromotionBottomSheet.ORIGIN_HOME,!isUserBlackListed, type = type).show(childFragmentManager, "")
        }else {
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE, type= type).show(childFragmentManager, "")
        }
    }

   override fun rangeChanged(range: AffiliateDatePickerData) {
        sendHomeEvent(AffiliateAnalytics.ActionKeys.CLICK_SIMPAN,range.value,AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE_FILTER)
        affiliateHomeViewModel.onRangeChanged(range)
    }

    override fun onRangeSelectionButtonClicked() {
        sendHomeEvent(AffiliateAnalytics.ActionKeys.CLICK_FILTER_DATE,"",AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE)
        AffiliateBottomDatePicker.newInstance(affiliateHomeViewModel.getSelectedDate(),this,IDENTIFIER_HOME).show(childFragmentManager, "")
    }

    private fun sendHomeEvent(eventAction :String,eventLabel: String,eventCategory: String) {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            eventCategory,
            eventLabel,
            userSessionInterface.userId
        )
    }

    override fun onInfoClick(title: String?, desc: String?, metrics: List<AffiliateUserPerformaListItemData.GetAffiliatePerformance.Data.UserData.Metrics.Tooltip.SubMetrics?>?,type: String?,tickerInfo: String?) {
            sendInfoClickEvent(type)
            AffiliateRecylerBottomSheet.newInstance(TYPE_HOME,title,desc,metrics,affiliateHomeViewModel.getSelectedDate(),tickerInfo = tickerInfo).show(childFragmentManager, "")
    }

    private fun sendInfoClickEvent(type: String?) {
        var action = ""
        when(type){
            CLICK_TYPE -> action = AffiliateAnalytics.ActionKeys.CLICK_KLIK_CARD
            COMMISSION_TYPE -> action = AffiliateAnalytics.ActionKeys.CLICK_TOTAL_KOMISI_CARD
        }
        if(action.isNotEmpty()) AffiliateAnalytics.sendEvent(AffiliateAnalytics.EventKeys.CLICK_CONTENT,action,AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,"",userSessionInterface.userId)
    }

    override fun onSystemDown() {
        affiliateHomeViewModel.getAnnouncementInformation()
        hideViews()
    }

    private fun hideViews() {
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
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
        adapter.list.removeAll(adapter.list.subList(PARTIAL_RESET_LENGTH, adapter.list.size))
        adapter.notifyItemRangeRemoved(PARTIAL_RESET_LENGTH, listSize - PARTIAL_RESET_LENGTH)
        loadMoreTriggerListener?.resetState()
        listSize = affiliateHomeViewModel.staticSize
        isNoMoreData = false
    }

}
