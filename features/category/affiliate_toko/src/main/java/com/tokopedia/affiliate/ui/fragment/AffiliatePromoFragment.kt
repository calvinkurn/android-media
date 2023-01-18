package com.tokopedia.affiliate.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.ON_REVIEWED
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMOSIKAN
import com.tokopedia.affiliate.SYSTEM_DOWN
import com.tokopedia.affiliate.adapter.AffiliateRecommendedAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliatePromoSearchActivity
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetPromoCopyPasteInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.custom.AffiliateLinkTextField
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromoFragment :
    AffiliateBaseFragment<AffiliatePromoViewModel>(),
    AffiliatePromoInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel

    private val tabFragments = arrayListOf<Fragment>()

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"

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
        view?.findViewById<NavToolbar>(R.id.promo_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                IconBuilder()
                    .addIcon(IconList.ID_INFORMATION) {
                        AffiliateHowToPromoteBottomSheet.newInstance(
                            AffiliateHowToPromoteBottomSheet.STATE_HOW_TO_PROMOTE
                        ).show(childFragmentManager, "")
                    }
                    .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                getString(R.string.affiliate_promo)
            setOnBackButtonClickListener {
                handleBack()
            }
        }

        view?.findViewById<ImageUnify>(R.id.icon_more)?.setOnClickListener {
            AffiliateBottomSheetPromoCopyPasteInfo.newInstance().show(childFragmentManager, "")
        }
        setupViewPager()
        showDefaultState()
        affiliatePromoViewModel.getAffiliateValidateUser()
    }

    fun handleBack() {
        if (view?.findViewById<RelativeLayout>(R.id.recommended_layout)?.isVisible == true) {
            (activity as? AffiliateActivity)?.handleBackButton(false)
        } else {
            showDefaultState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_promo_fragment_layout, container, false)
    }

    private fun setupViewPager() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout_recommended)
        val viewPager = view?.findViewById<ViewPager2>(R.id.view_pager_recommended)
        activity?.let {
            tabFragments.add(
                AffiliateRecommendedProductFragment.getFragmentInstance(
                    AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER,
                    this
                )
            )
            tabFragments.add(
                AffiliateRecommendedProductFragment.getFragmentInstance(
                    AffiliateRecommendedProductFragment.LAST_VIEWED_IDENTIFIER,
                    this
                )
            )
            val adapter =
                AffiliateRecommendedAdapter(childFragmentManager, lifecycle, context, tabFragments)
            viewPager?.adapter = adapter
            if (tabLayout != null && viewPager != null) {
                TabLayoutMediator(tabLayout, viewPager) { _, _ ->
                }.attach()
            }
            tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) = Unit

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
        when (position) {
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
        affiliatePromoViewModel.progressBar().observe(this) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    hideAllElements()
                    view?.findViewById<Group>(R.id.promo_affiliate_progress_view)?.show()
                } else {
                    view?.findViewById<Group>(R.id.promo_affiliate_progress_view)?.gone()
                }
            }
        }

        affiliatePromoViewModel.getErrorMessage().observe(this) { _ ->
            view?.rootView?.let {
                Toaster.build(
                    it,
                    getString(R.string.affiliate_product_link_invalid),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }

        affiliatePromoViewModel.getValidateUserdata().observe(this) { validateUserdata ->
            onGetValidateUserData(validateUserdata)
        }
        affiliatePromoViewModel.getAffiliateAnnouncement().observe(this) {
            if (it.getAffiliateAnnouncementV2?.data?.subType != TICKER_BOTTOM_SHEET) {
                sendTickerImpression(
                    it.getAffiliateAnnouncementV2?.data?.type,
                    it.getAffiliateAnnouncementV2?.data?.id
                )
                view?.findViewById<Ticker>(R.id.affiliate_announcement_ticker)?.setAnnouncementData(
                    it,
                    activity,
                    source = PAGE_ANNOUNCEMENT_PROMOSIKAN
                )
            }
        }
    }

    private fun sendTickerImpression(tickerType: String?, tickerId: Long?) {
        if (tickerId.isMoreThanZero()) {
            AffiliateAnalytics.sendTickerEvent(
                AffiliateAnalytics.EventKeys.VIEW_ITEM,
                AffiliateAnalytics.ActionKeys.IMPRESSION_TICKER_COMMUNICATION,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
                "$tickerType - $tickerId",
                PAGE_ANNOUNCEMENT_PROMOSIKAN,
                tickerId!!,
                AffiliateAnalytics.ItemKeys.AFFILIATE_PROMOSIKAN_TICKER_COMMUNICATION,
                userSessionInterface.userId
            )
        }
    }

    private fun showDefaultState() {
        view?.findViewById<RelativeLayout>(R.id.recommended_layout)?.show()
    }

    private fun hideAllElements() {
        view?.findViewById<RelativeLayout>(R.id.recommended_layout)?.hide()
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

    private fun disableSearchButton() {
        view?.findViewById<AffiliateLinkTextField>(R.id.product_link_et)?.isEnabled = false
    }

    private fun setCustomTabText(context: Context?, tabLayout: TabLayout?) {
        if (context != null && tabLayout != null) {
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.affiliate_pernah_dibeli)
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.affiliate_terakhir_dilihat)
                setType(Typography.DISPLAY_2)
                setWeight(Typography.BOLD)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )
            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
        }
    }

    override fun onSystemDown() {
        disableSearchButton()
        affiliatePromoViewModel.setValidateUserType(SYSTEM_DOWN)
        affiliatePromoViewModel.getAnnouncementInformation()
    }

    override fun onReviewed() {
        affiliatePromoViewModel.setValidateUserType(ON_REVIEWED)
        affiliatePromoViewModel.getAnnouncementInformation()
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
        affiliatePromoViewModel.getAnnouncementInformation()
        affiliatePromoViewModel.setValidateUserType(ON_REGISTERED)
    }

    override fun enterLinkButtonClicked() {
        startActivity(Intent(context, AffiliatePromoSearchActivity::class.java))
    }
}

interface AffiliatePromoInterface {
    fun enterLinkButtonClicked()
}
