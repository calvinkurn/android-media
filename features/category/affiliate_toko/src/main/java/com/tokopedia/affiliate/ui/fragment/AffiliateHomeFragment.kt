package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerRangeChangeInterface
import com.tokopedia.affiliate.interfaces.AffiliatePerformaClickInterfaces
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateComponentActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker.Companion.IDENTIFIER_HOME
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet.Companion.STATE_PERFORMA_INFO
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePerformaSharedProductCardsModel
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_URL
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>(), ProductClickInterface,
    AffiliatePerformaClickInterfaces, AffiliateDatePickerRangeChangeInterface {

    private var totalDataItemsCount: Int = 0
    private var isSwipeRefresh = false
    private var listSize = 0
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface
    private var bottomNavBarClickListener : AffiliateBottomNavBarInterface? = null
    private var affiliateActitvityInterface : AffiliateActivityInterface? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel
    lateinit var adapter: AffiliateAdapter
    private var isUserBlackListed = false

    companion object {
        fun getFragmentInstance(affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface,affiliateActitvity:AffiliateActivityInterface): Fragment {
            return AffiliateHomeFragment().apply {
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
                affiliateActitvityInterface = affiliateActitvity
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setObservers()
    }

    private fun initAdapter() {
        adapter = AffiliateAdapter(AffiliateAdapterFactory(productClickInterface = this,onDateRangeClickInterface = this,onPerformaGridClick = this,bottomNavBarClickListener = bottomNavBarClickListener))
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
            affiliateHomeViewModel.getAnnouncementInformation()
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
        if(!CoachMarkPreference.hasShown(requireContext(), COACHMARK_TAG)) affiliateActitvityInterface?.showCoachMarker()
        setUserDetails()
    }
    private fun openHistoryActivity() {
        sendHomeEvent(AffiliateAnalytics.ActionKeys.CLICK_GENERATED_LINK_HISTORY,"",AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE)
        val intent = Intent(context,AffiliateComponentActivity::class.java)
        intent.putExtra("isUserBlackListed",isUserBlackListed)
        startActivityForResult(intent,LINK_HISTORY_BUTTON_CLICKED)
    }

    private fun setUserDetails(){
        ImageHandler.loadImageCircle2(context, view?.findViewById(R.id.user_image), affiliateHomeViewModel.getUserProfilePicture())
        view?.findViewById<Typography>(R.id.user_name)?.text = affiliateHomeViewModel.getUserName()
    }
    private fun resetItems() {
        loadMoreTriggerListener?.resetState()
        listSize = 0
        adapter.resetList()
        affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO)
    }

    private fun setAffiliateGreeting() {
        view?.findViewById<Typography>(R.id.affiliate_greeting)?.text = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 6..10 -> getString(R.string.affiliate_morning)
            in 11..15 -> getString(R.string.affiliate_noon)
            in 16..18 -> getString(R.string.affiliate_afternoon)
            else ->getString(R.string.affiliate_night)
        }
    }

    private fun showNoAffiliate() {
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
        view?.findViewById<DeferredImageView>(R.id.affiliate_no_product_iv)?.show()
        view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
            setActionClickListener {
                bottomNavBarClickListener?.selectItem(AffiliateActivity.PROMO_MENU,R.id.menu_promo_affiliate,true)
            }
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(totalItemsCount < totalDataItemsCount) {
                    sendImpressionEvent()
                    affiliateHomeViewModel.getAffiliatePerformance(page - 1)
                }
            }
        }
    }

    private fun sendImpressionEvent() {
        lastItem?.let { item ->
            var itemID = ""
            item.product.itemID?.let {
                itemID = it
            }
            var itemName = ""
            item.product.itemTitle?.let {
                itemName = it
            }
            AffiliateAnalytics.trackEventImpression(AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUK_YANG_DIPROMOSIKAN,AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
                userSessionInterface.userId,itemID,listSize-2,itemName,itemID)
        }
    }

    private fun setObservers() {
        affiliateHomeViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addShimmer()
                else
                    adapter.removeShimmer(listSize)
            }
        })
        affiliateHomeViewModel.getDataShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.addDataPlatformShimmer()
                else
                    adapter.resetList()
            }
        })
        affiliateHomeViewModel.getRangeChanged().observe(this,{changed ->
            if(changed) resetItems()
        })
        affiliateHomeViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                    view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.show()
                } else {
                    view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
                }
            }
        })
        affiliateHomeViewModel.getErrorMessage().observe(this, { error ->
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
                    affiliateHomeViewModel.getAnnouncementInformation()
                }
            }
        })
        affiliateHomeViewModel.getValidateUserdata().observe(this, { validateUserdata ->
            view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.show()
            if (validateUserdata.validateAffiliateUserStatus.data?.isRegistered == true) {
                affiliateHomeViewModel.getAffiliatePerformance(page = PAGE_ZERO)
            }else {
                validateUserdata.validateAffiliateUserStatus.data?.error?.ctaLink?.androidUrl?.let {
                    try {
                        activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }catch (e : Exception){
                        activity?.startActivity(Intent(Intent(activity, BaseSimpleWebViewActivity::class.java)).putExtra(KEY_URL,it))
                    }
                }
                activity?.finish()
            }
        })

        affiliateHomeViewModel.getAffiliateDataItems().observe(this ,{ dataList ->
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
            } else if(totalDataItemsCount == 0) {
                showNoAffiliate()
            }
        })

        affiliateHomeViewModel.getAffiliateItemCount().observe(this, { itemCount ->
            totalDataItemsCount = itemCount
        })

        affiliateHomeViewModel.getAffiliateAnnouncement().observe(this,{ announcementData ->
            onGetAnnouncementData(announcementData)
        })

        affiliateHomeViewModel.getAffiliateErrorMessage().observe(this,{ error ->
            view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
            view?.findViewById<GlobalError>(R.id.home_global_error)?.run {
                setActionClickListener {
                    hide()
                    affiliateHomeViewModel.getAnnouncementInformation()
                }
                when(error) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        setType(GlobalError.NO_CONNECTION)
                        show()
                    }
                    is IllegalStateException -> {
                        setType(GlobalError.PAGE_FULL)
                        show()
                    }
                    else -> {
                        onGetAnnouncementError()
                    }
                }
            }
        })
    }
    var lastItem : AffiliatePerformaSharedProductCardsModel? = null
    private fun setLastDataForEvent(dataList: ArrayList<Visitable<AffiliateAdapterTypeFactory>>) {
        dataList[dataList.lastIndex].let {
            if(it is AffiliatePerformaSharedProductCardsModel){
                lastItem = it
            }
        }
    }

    private fun onGetAnnouncementError() {
        view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
        setupTickerView(
            getString(R.string.affiliate_system_down_title),
            getString(R.string.affiliate_system_down_description),
            Ticker.TYPE_ANNOUNCEMENT
        )
    }

    private fun onGetAnnouncementData(announcementData: AffiliateAnnouncementData?) {
        view?.findViewById<LoaderUnify>(R.id.affiliate_progress_bar)?.gone()
        if(announcementData?.getAffiliateAnnouncement?.data?.status== ANNOUNCEMENT__TYPE_SUCCESS) {
            when (announcementData.getAffiliateAnnouncement.data.type) {
                ANNOUNCEMENT__TYPE_CCA -> {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_INFORMATION,
                            announcementData.getAffiliateAnnouncement.data.ctaLink?.androidURL ?: ""
                    )
                }
                ANNOUNCEMENT__TYPE_USER_BLACKLIST -> {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    isUserBlackListed = true
                    (activity as? AffiliateActivity)?.setBlackListedStatus(isUserBlackListed)
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_ERROR,
                            announcementData.getAffiliateAnnouncement.data.ctaLink?.androidURL ?: ""
                    )
                }
                ANNOUNCEMENT__TYPE_SERVICE_STATUS -> {
                    view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_ANNOUNCEMENT,
                            announcementData.getAffiliateAnnouncement.data.ctaLink?.androidURL ?: ""
                    )
                }
                ANNOUNCEMENT__TYPE_NO_ANNOUNCEMENT -> {
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    view?.findViewById<CardView>(R.id.affiliate_announcement_ticker_cv)?.hide()
                }
            }
        } else{
            view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout)?.hide()
            setupTickerView(
                getString(R.string.affiliate_system_down_title),
                getString(R.string.affiliate_system_down_description),
                Ticker.TYPE_ANNOUNCEMENT
            )
        }

    }
    private fun setupTickerView(title: String?,desc :String? ,tickerTypeValue: Int, ctaLinkUrl  : String = "")
    {
        view?.findViewById<CardView>(R.id.affiliate_announcement_ticker_cv)?.show()
        view?.findViewById<Ticker>(R.id.affiliate_announcement_ticker)?.run {
            tickerTitle = title
            desc?.let {
                setHtmlDescription(
                    it
                )
            }
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    if(ctaLinkUrl.isNotBlank())
                        RouteManager.routeNoFallbackCheck(context,ctaLinkUrl,ctaLinkUrl)
                }
                override fun onDismiss() {}
            })
           tickerType = tickerTypeValue
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
                    affiliateHomeViewModel.getAnnouncementInformation()
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

    override fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, status : Int?) {
        if(status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE){
            AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                    null,null,productId , productName , productImage, productUrl,productIdentifier,
                    AffiliatePromotionBottomSheet.ORIGIN_HOME,!isUserBlackListed).show(childFragmentManager, "")
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

    override fun onInfoClick(title: String?, desc: String?) {
        AffiliateHowToPromoteBottomSheet.newInstance(STATE_PERFORMA_INFO,title,desc).show(childFragmentManager, "")
    }
}
