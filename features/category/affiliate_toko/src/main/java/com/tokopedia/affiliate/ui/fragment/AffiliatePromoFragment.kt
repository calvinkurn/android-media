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
import com.tokopedia.affiliate.AffiliateAnalytics
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
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextFieldInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliatePromotionErrorCardItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionCardModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePromotionErrorCardModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
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

class AffiliatePromoFragment : BaseViewModelFragment<AffiliatePromoViewModel>(), PromotionClickInterface ,
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
    }

    fun handleBack() {
        if (recommended_layout.isVisible) (activity as? AffiliateActivity)?.handleBackButton()
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
                    tab?.position?.let {
                        adapter.setOnSelectView(tab)
                    }
                }

            })
        }
        setCustomTabText(requireContext(), tabLayout)
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
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_LINK_ERROR,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "", userSessionInterface.userId)
                } else {
                    affiliateSearchData.searchAffiliate?.data?.error?.let {
                        adapter.addElement(AffiliatePromotionErrorCardModel(it))
                    }
                    var errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_FOUND_ERROR
                    when (affiliateSearchData.searchAffiliate?.data?.error?.errorStatus) {
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_FOUND ->
                            errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_FOUND_ERROR
                        AffiliatePromotionErrorCardItemVH.ERROR_STATUS_NOT_ELIGIBLE ->
                            errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_ELIGIBLE
                        AffiliatePromotionErrorCardItemVH.ERROR_NON_PM_OS ->
                            errorAction = AffiliateAnalytics.ActionKeys.IMPRESSION_NOT_OS_PM_ERROR
                    }
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            errorAction,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "", userSessionInterface.userId)
                }
            } else {
                affiliateSearchData.searchAffiliate?.data?.cards?.firstOrNull()?.items?.let { items ->
                    showData(false)
                    items.forEach {
                        it?.let {
                            val isBlackListedUser = (activity as? AffiliateActivity)?.getBlackListedStatus() ?: false
                            if(isBlackListedUser){
                                it.status?.isLinkGenerationAllowed = !isBlackListedUser
                            }
                            adapter.addElement(AffiliatePromotionCardModel(it))
                        }
                    }
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_PROMOSIKAN_SRP,
                            AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
                            "", userSessionInterface.userId)
                }
            }
        })
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

    override fun onPromotionClick(productId: String, productName: String, productImage: String, productUrl: String, productIdentifier: String) {
        AffiliatePromotionBottomSheet.newInstance(AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null,null,productId, productName, productImage, productUrl,
                productIdentifier, AffiliatePromotionBottomSheet.ORIGIN_PROMOSIKAN).show(childFragmentManager, "")
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

    override fun onEditState(state: Boolean) {
        AffiliateAnalytics.sendEvent(
                AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                AffiliateAnalytics.ActionKeys.CLICK_SEARCH,
                AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP,
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
}

interface AffiliatePromoInterface {

    fun enterLinkButtonClicked()

}