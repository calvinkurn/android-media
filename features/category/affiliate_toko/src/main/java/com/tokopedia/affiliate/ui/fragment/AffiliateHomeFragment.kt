package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.tokopedia.affiliate.PAGE_TYPE_PDP
import com.tokopedia.affiliate.PAGE_TYPE_SHOP
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
import com.tokopedia.affiliate.interfaces.AffiliateHomeImpressionListener
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.interfaces.AffiliatePerformanceChipClick
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.response.ItemTypesItem
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateComponentActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker.Companion.IDENTIFIER_HOME
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
import java.util.*
import javax.inject.Inject

class AffiliateHomeFragment : AffiliateBaseFragment<AffiliateHomeViewModel>(),
    ProductClickInterface,
    AffiliatePerformaClickInterfaces, AffiliateDatePickerRangeChangeInterface,
    AffiliatePerformanceChipClick, AffiliateHomeImpressionListener {

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
        private const val PRODUCT_ACTIVE = 1
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
            )
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
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.label_affiliate)
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
            view?.findViewById<Ticker>(R.id.affiliate_announcement_ticker)
                ?.setAnnouncementData(announcementData, activity)
        }
        affiliateHomeViewModel.noMoreDataAvailable().observe(this) { noDataAvailable ->
            isNoMoreData = noDataAvailable
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
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE).show(childFragmentManager, "")
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

    override fun onUserRegistered() {
        affiliateHomeViewModel.getAnnouncementInformation()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, isFullLoad = true)
    }

    override fun onChipClick(type: ItemTypesItem?) {
        sendFilterClickEvent(type)
        affiliateHomeViewModel.lastSelectedChip = type
        partialReset()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO, type?.pageType.toIntOrZero())
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
    }
    override fun onItemImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int,
        type: String
    ) {
        if (type == PAGE_TYPE_PDP) {
            sendProductImpression(item, position)
        } else if (type == PAGE_TYPE_SHOP) {
           sendShopImpression(item, position)
        }
    }

    private fun sendProductImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int
    ) {
        val status = if (item.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUK_YANG_DIPROMOSIKAN,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            userSessionInterface.userId,
            item.itemID,
            position,
            item.itemTitle,
            "${item.itemID} - ${item.metrics?.findLast { it?.metricType == "orderCommissionPerItem" }?.metricValue} - ${item.metrics?.findLast { it?.metricType == "totalClickPerItem" }?.metricValue} - ${item.metrics?.findLast { it?.metricType == "orderPerItem" }?.metricValue} - $status",
        )
    }

    private fun sendShopImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int
    ) {
        val status = if (item.status == PRODUCT_ACTIVE) AffiliateAnalytics.LabelKeys.ACTIVE else AffiliateAnalytics.LabelKeys.INACTIVE
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_SHOP_LINK_DENGAN_PERFORMA,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            userSessionInterface.userId,
            item.itemID,
            position,
            item.itemTitle,
            "${item.itemID} - ${item.metrics?.findLast { it?.metricType == "shopOrderCommissionPerItem" }?.metricValue} - ${item.metrics?.findLast { it?.metricType == "shopTotalClickPerItem" }?.metricValue} - ${item.metrics?.findLast { it?.metricType == "shopOrderPerItem" }?.metricValue} - $status",
            AffiliateAnalytics.ItemKeys.AFFILAITE_HOME_SHOP_SELECT_CONTENT
        )
    }

}