package com.tokopedia.affiliate.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateRecommendedAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextField
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextFieldInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionErrorCardItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.affiliate_promo_fragment_layout.*
import java.util.*
import javax.inject.Inject

class AffiliatePromoFragment : AffiliateBaseFragment<AffiliatePromoViewModel>(), PromotionClickInterface ,
        AffiliateLinkTextFieldInterface, AffiliatePromoInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(null, null, this))

    private val tabFragments = arrayListOf<Fragment>()

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoFragment()
        }
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
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            AffiliateAnalytics.ScreenKeys.AFFILIATE_PROMOSIKAN_PAGE,
            userSessionInterface.isLoggedIn,
            userSessionInterface.userId
        )
    }

    private fun afterViewCreated() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.setVisitables(ArrayList())
        promotion_recycler_view.layoutManager = layoutManager
        promotion_recycler_view.adapter = adapter
        product_link_et.run {
            setRelatedView(dim_layer)
            setDoneAction {
                affiliatePromoViewModel.getSearch(editText.text.toString())
            }
            setEventListener(this@AffiliatePromoFragment)
        }
        promo_navToolbar.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_INFORMATION) {
                                AffiliateHowToPromoteBottomSheet.newInstance(AffiliateHowToPromoteBottomSheet.STATE_HOW_TO_PROMOTE).show(childFragmentManager, "")
                            }
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text = getString(R.string.affiliate_promo)
            setOnBackButtonClickListener {
                handleBack()
            }
        }
        setupViewPager()
        showDefaultState()
        affiliatePromoViewModel.getAffiliateValidateUser()
    }

    fun handleBack() {
        if (recommended_layout.isVisible) (activity as? AffiliateActivity)?.handleBackButton(false)
        else showDefaultState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_promo_fragment_layout, container, false)
    }

    private fun setupViewPager() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout_recommended)
        val viewPager = view?.findViewById<ViewPager2>(R.id.view_pager_recommended)
        activity?.let {
            tabFragments.add(AffiliateRecommendedProductFragment.getFragmentInstance(AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER,this))
            tabFragments.add(AffiliateRecommendedProductFragment.getFragmentInstance(AffiliateRecommendedProductFragment.LAST_VIEWED_IDENTIFIER,this))
            val adapter = AffiliateRecommendedAdapter(childFragmentManager, lifecycle, context, tabFragments)
            viewPager?.adapter = adapter
            if (tabLayout != null && viewPager != null) {
                TabLayoutMediator(tabLayout, viewPager) { _, _ ->

                }.attach()
            }
            tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.position?.let { adapter.setUnSelectView(tab) }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    sendTabSelectedEvent(tab?.position)
                    tab?.position?.let {
                        adapter.setOnSelectView(tab)
                    }
                }

            })
        }
        setCustomTabText(requireContext(), tabLayout)
    }

    private fun sendTabSelectedEvent(position: Int?) {
        var eventAction = ""
        when(position){
            0 -> eventAction = AffiliateAnalytics.ActionKeys.CLICK_PERNAH_DIBELI_TAB
            1 -> eventAction = AffiliateAnalytics.ActionKeys.CLICK_PERNAH_DILIHAT_TAB
        }
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface.userId
        )
    }

    private fun setObservers() {
        affiliatePromoViewModel.progressBar().observe(this, { visibility ->
            if (visibility != null) {
                if (visibility) {
                    hideAllElements()
                    promo_affiliate_progress_view?.show()
                } else
                    promo_affiliate_progress_view?.gone()
            }
        })

        affiliatePromoViewModel.getErrorMessage().observe(this, { error ->
            view?.rootView?.let {
                Toaster.build(it, getString(R.string.affiliate_product_link_invalid),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
            product_link_et.editingState(true)
        })

        affiliatePromoViewModel.getAffiliateSearchData().observe(this, { affiliateSearchData ->
            adapter.clearAllElements()
            if (affiliateSearchData.searchAffiliate?.data?.status == 0) {
                showData(true)
                if (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus == 0) {
                    view?.rootView?.let {
                        Toaster.build(it, getString(R.string.affiliate_product_link_invalid),
                                Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                    }
                    showDefaultState()
                    sendSearchEvent(AffiliateAnalytics.LabelKeys.NOT_URL)
                } else {
                    affiliateSearchData.searchAffiliate?.data?.error?.let {
                        adapter.addElement(AffiliatePromotionErrorCardModel(it))
                    }
                    val errorLabel = when (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus) {
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_FOUND ->
                            AffiliateAnalytics.LabelKeys.PRDOUCT_URL_NOT_FOUND
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_ELIGIBLE ->
                            AffiliateAnalytics.LabelKeys.NON_WHITELISTED_CATEGORIES
                        AffiliatePromotionErrorCardItemVH.ERROR_NON_PM_OS ->
                            AffiliateAnalytics.LabelKeys.NON_PM_OS_SHOP
                        else -> AffiliateAnalytics.LabelKeys.NOT_URL
                    }
                    sendSearchEvent(errorLabel)
                }
            } else {
                affiliateSearchData.searchAffiliate?.data?.cards?.firstOrNull()?.items?.let { items ->
                    showData(false)
                    items.forEach {
                        it?.let {
                            val isBlackListedUser = affiliatePromoViewModel.getSoftBan()
                            if(isBlackListedUser){
                                it.status?.isLinkGenerationAllowed = !isBlackListedUser
                            }
                            adapter.addElement(AffiliatePromotionCardModel(it))
                        }
                    }
                    if(items.isNotEmpty()) {
                        items.first()?.let {
                            sendEnhancedTracker(it)
                        }
                    }

                }
            }
        })

        affiliatePromoViewModel.getValidateUserdata().observe(this, { validateUserdata ->
            onGetValidateUserData(validateUserdata)
        })
    }

    private fun sendEnhancedTracker(it: AffiliateSearchData.SearchAffiliate.Data.Card.Item) {
        var status = ""
        if(it.status?.messages?.isNotEmpty() == true) {
            when (it.status?.messages?.first()?.messageType) {
                AVAILABLE -> status = AffiliateAnalytics.LabelKeys.AVAILABLE
                ALMOST_OOS -> status = AffiliateAnalytics.LabelKeys.ALMOST_OOS
                EMPTY_STOCK -> status = AffiliateAnalytics.LabelKeys.EMPTY_STOCK
                PRODUCT_INACTIVE -> status = AffiliateAnalytics.LabelKeys.PRODUCT_INACTIVE
                SHOP_INACTIVE -> status = AffiliateAnalytics.LabelKeys.SHOP_INACTIVE
            }
        }
        AffiliateAnalytics.trackEventImpression(
            AffiliateAnalytics.EventKeys.VIEW_ITEM_LIST,
            AffiliateAnalytics.ActionKeys.IMPRESSION_PRODUCT_SEARCH_RESULT_PAGE,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            userSessionInterface.userId,
            it.productID,
            1,
            it.title,
            "${it.productID} - ${it.commission?.amount} - $status"
        )
    }

    private fun sendSearchEvent(eventLabel: String){
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            AffiliateAnalytics.ActionKeys.CLICK_SEARCH,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            eventLabel, userSessionInterface.userId)
    }

    private fun showData(isErrorData: Boolean) {
        if (isErrorData) promotion_card_title.hide() else promotion_card_title.show()
        promotion_recycler_view.show()
    }

    private fun showDefaultState() {
        promotion_card_title.hide()
        recommended_layout.show()
        promotion_recycler_view.hide()
    }

    private fun hideAllElements() {
        promotion_card_title.hide()
        recommended_layout.hide()
        promotion_recycler_view.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectPromoFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromoViewModel = viewModel as AffiliatePromoViewModel
    }

    override fun onPromotionClick(productId: String, shopId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String, position: Int, commison: String, status: String) {
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null,null,productId, productName, productImage, productUrl,
                productIdentifier, AffiliatePromotionBottomSheet.ORIGIN_PROMOSIKAN,commission = commison,status = status).show(childFragmentManager, "")
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) {
        if (errorCta?.ctaAction == AffiliatePromotionErrorCardItemVH.ACTION_REDIRECT) {
            errorCta.ctaLink?.androidUrl?.let {
                RouteManager.routeNoFallbackCheck(context, it, it)
            }
        } else {
            showDefaultState()
            product_link_et.editingState(true)
        }
    }

    private fun disableSearchButton() {
        view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.isEnabled = false
    }

    override fun onEditState(state: Boolean) {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.CLICK_PG,
                AffiliateAnalytics.ActionKeys.CLICK_SEARCH_BOX,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
                "", userSessionInterface.userId)
    }

    private fun setCustomTabText(context: Context?, tabLayout: TabLayout?) {
        if (context != null && tabLayout != null) {
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.affiliate_pernah_dibeli)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.affiliate_terakhir_dilihat)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
        }
    }

    override fun enterLinkButtonClicked() {
        product_link_et.editingState(true)
    }

    override fun onSystemDown() {
        disableSearchButton()
        affiliatePromoViewModel.setValidateUserType(SYSTEM_DOWN)
    }

    override fun onReviewed() {
        affiliatePromoViewModel.setSoftBan(true)
        affiliatePromoViewModel.setValidateUserType(ON_REVIEWED)
    }

    override fun onUserNotEligible() {

    }

    override fun onUserRegistered() {
        affiliatePromoViewModel.setValidateUserType(ON_REGISTERED)
    }
}

interface AffiliatePromoInterface {

    fun enterLinkButtonClicked()

}