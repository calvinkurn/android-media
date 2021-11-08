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
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.model.AffiliateAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliateSharedProductCardsItemVH
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_home_fragment_layout.*
import java.util.*
import javax.inject.Inject

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>(), ProductClickInterface {

    private var totalDataItemsCount: Int = 0
    private var isSwipeRefresh = false

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface : UserSessionInterface
    private var bottomNavBarClickListener : AffiliateBottomNavBarInterface? = null
    private var loadMoreTriggerListener: EndlessRecyclerViewScrollListener? = null

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(productClickInterface = this))

    companion object {
        fun getFragmentInstance(affiliateBottomNavBarClickListener: AffiliateBottomNavBarInterface): Fragment {
            return AffiliateHomeFragment().apply {
                bottomNavBarClickListener = affiliateBottomNavBarClickListener
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_home_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        if (!affiliateHomeViewModel.isUserLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    AFFILIATE_LOGIN_REQUEST_CODE)
        } else {
            affiliateHomeViewModel.getAnnouncementInformation()
        }
        setAffiliateGreeting()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        products_rv.layoutManager = layoutManager
        swipe_refresh_layout.setOnRefreshListener {
            isSwipeRefresh = true
            loadMoreTriggerListener?.resetState()
            affiliateHomeViewModel.getAffiliatePerformance(PAGE_ZERO)
        }
        loadMoreTriggerListener = getEndlessRecyclerViewListener(layoutManager)
        products_rv.adapter = adapter
        loadMoreTriggerListener?.let { products_rv.addOnScrollListener(it) }
        user_name.text = affiliateHomeViewModel.getUserName()
        home_navToolbar.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_INFORMATION) {
                                AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_BETA_INFO).show(childFragmentManager, "")
                            }
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.label_affiliate)
            setOnBackButtonClickListener {
                (activity as? AffiliateActivity)?.handleBackButton()
            }
        }
        ImageHandler.loadImageCircle2(context, user_image, affiliateHomeViewModel.getUserProfilePicture())
        sendScreenEvent()
    }

    private fun setAffiliateGreeting() {
        affiliate_greeting.text = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 6..10 -> getString(R.string.affiliate_morning)
            in 11..15 -> getString(R.string.affiliate_noon)
            in 16..18 -> getString(R.string.affiliate_afternoon)
            else ->getString(R.string.affiliate_night)
        }
    }

    private fun showNoAffiliate() {
        affiliate_no_product_iv.show()
        home_global_error.run {
            show()
            errorIllustration.hide()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
            setActionClickListener {
                bottomNavBarClickListener?.selectItem(AffiliateActivity.PROMO_MENU,R.id.menu_promo_affiliate)
            }
        }
    }

    private fun getEndlessRecyclerViewListener(recyclerViewLayoutManager: RecyclerView.LayoutManager): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(recyclerViewLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if(totalItemsCount < totalDataItemsCount)
                    affiliateHomeViewModel.getAffiliatePerformance(page - 1)
            }
        }
    }

    private fun setObservers() {
        affiliateHomeViewModel.getShimmerVisibility().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility)
                    adapter.startShimmer()
                else
                    adapter.stopShimmer()
            }
        })
        affiliateHomeViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                    affiliate_progress_bar?.show()
                } else {
                    affiliate_progress_bar?.gone()
                }
            }
        })
        affiliateHomeViewModel.getErrorMessage().observe(this, { error ->
            home_global_error.run {
                show()
                errorTitle.text = error
                setActionClickListener {
                    affiliateHomeViewModel.getAnnouncementInformation()
                }
            }
        })
        affiliateHomeViewModel.getValidateUserdata().observe(this, { validateUserdata ->
            affiliate_progress_bar?.gone()
            affiliateHomeViewModel.getAffiliatePerformance(page = PAGE_ZERO)
        })

        affiliateHomeViewModel.getAffiliateDataItems().observe(this ,{ dataList ->
            if(isSwipeRefresh){
                swipe_refresh_layout.isRefreshing = false
                isSwipeRefresh = !isSwipeRefresh
            }
            if (dataList.isNotEmpty()) {
                adapter.addMoreData(dataList)
                loadMoreTriggerListener?.updateStateAfterGetData()
            } else {
                showNoAffiliate()
            }
        })

        affiliateHomeViewModel.getAffiliateItemCount().observe(this, { itemCount ->
            affiliate_products_count.text = getString(R.string.affiliate_product_count, itemCount.toString())
            totalDataItemsCount = itemCount
        })

        affiliateHomeViewModel.getAffiliateAnnouncement().observe(this,{ announcementData ->
            onGetAnnouncementData(announcementData)
        })

        affiliateHomeViewModel.getAffiliateErrorMessage().observe(this,{
            affiliate_progress_bar?.gone()
            onGetAnnouncementError()
        })
    }

    private fun onGetAnnouncementError() {
        product_list_group.hide()
        setupTickerView(
            getString(R.string.system_down_title),
            getString(R.string.system_down_description),
            Ticker.TYPE_ANNOUNCEMENT
        )
    }

    private fun onGetAnnouncementData(announcementData: AffiliateAnnouncementData?) {
        affiliate_progress_bar?.gone()
        if(announcementData?.getAffiliateAnnouncement?.data?.status== ANNOUNCEMENT__TYPE_SUCCESS) {
            when (announcementData.getAffiliateAnnouncement.data.type) {
                ANNOUNCEMENT__TYPE_CCA -> {
                    product_list_group.show()
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_INFORMATION
                    )
                }
                ANNOUNCEMENT__TYPE_USER_BLACKLIST -> {
                    product_list_group.show()
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_ERROR
                    )
                }
                ANNOUNCEMENT__TYPE_SERVICE_STATUS -> {
                    product_list_group.hide()
                    setupTickerView(
                        announcementData.getAffiliateAnnouncement.data.announcementTitle,
                        announcementData.getAffiliateAnnouncement.data.announcementDescription,
                        Ticker.TYPE_ANNOUNCEMENT
                    )
                }
                ANNOUNCEMENT__TYPE_NO_ANNOUNCEMENT -> {
                    product_list_group.show()
                    affiliateHomeViewModel.getAffiliateValidateUser()
                    affiliate_announcement_ticker_cv.hide()
                }
            }
        } else{
            product_list_group.hide()
            setupTickerView(
                getString(R.string.system_down_title),
                getString(R.string.system_down_description),
                Ticker.TYPE_ANNOUNCEMENT
            )
        }

    }
    private fun setupTickerView(title: String?,desc :String? ,tickerType: Int)
    {
        affiliate_announcement_ticker_cv.show()
        affiliate_announcement_ticker.tickerTitle = title
        desc?.let {
            affiliate_announcement_ticker.setTextDescription(
                it
            )
        }
        affiliate_announcement_ticker.tickerType = tickerType
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
                    affiliateHomeViewModel.getAnnouncementInformation()
                } else {
                    activity?.finish()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendScreenEvent() {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                AffiliateAnalytics.ActionKeys.IMPRESSION_HOME_PORTAL,
                AffiliateAnalytics.CategoryKeys.HOME_PORTAL,
                "",userSessionInterface.userId)
    }

    override fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, status : Int?) {
        if(status == AffiliateSharedProductCardsItemVH.PRODUCT_ACTIVE){
            AffiliatePromotionBottomSheet.newInstance(productId , productName , productImage, productUrl,productIdentifier,AffiliatePromotionBottomSheet.ORIGIN_HOME).show(childFragmentManager, "")
        }else {
            AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_PRODUCT_INACTIVE).show(childFragmentManager, "")
        }
    }
}
