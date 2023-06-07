package com.tokopedia.affiliate.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.AFFILIATE_DISCO_PROMO
import com.tokopedia.affiliate.AFFILIATE_GAMIFICATION_REDIRECTION
import com.tokopedia.affiliate.AFFILIATE_GAMIFICATION_REDIRECTION_APPLINK
import com.tokopedia.affiliate.AFFILIATE_GAMIFICATION_VISIBILITY
import com.tokopedia.affiliate.AFFILIATE_SSA_SHOP
import com.tokopedia.affiliate.AFFILIATE_TOKONOW_BANNER
import com.tokopedia.affiliate.AFFILIATE_TOKONOW_DATA
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.ON_REVIEWED
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMOSIKAN
import com.tokopedia.affiliate.SYSTEM_DOWN
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateRecommendedAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ProductClickInterface
import com.tokopedia.affiliate.interfaces.PromotionClickInterface
import com.tokopedia.affiliate.model.pojo.AffiliatePromotionBottomSheetParams
import com.tokopedia.affiliate.model.pojo.TokonowRemoteConfigData
import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.setAnnouncementData
import com.tokopedia.affiliate.ui.activity.AffiliateActivity
import com.tokopedia.affiliate.ui.activity.AffiliateDiscoPromoListActivity
import com.tokopedia.affiliate.ui.activity.AffiliatePromoSearchActivity
import com.tokopedia.affiliate.ui.activity.AffiliateRegistrationActivity
import com.tokopedia.affiliate.ui.activity.AffiliateSSAShopListActivity
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetPromoCopyPasteInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateHowToPromoteBottomSheet
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerUiModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliatePromoFragmentLayoutBinding
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class AffiliatePromoFragment :
    AffiliateBaseFragment<AffiliatePromoViewModel>(),
    AffiliatePromoInterface,
    PromotionClickInterface,
    ProductClickInterface {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel

    private val tabFragments = arrayListOf<Fragment>()
    private var remoteConfig: RemoteConfig? = null
    private var tokoNowData: TokonowRemoteConfigData? = null

    private var binding by autoClearedNullable<AffiliatePromoFragmentLayoutBinding>()

    private val ssaAdapter: AffiliateAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(productClickInterface = this)
        )
    }

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    private fun isAffiliateGamificationEnabled() =
        remoteConfig?.getBoolean(AFFILIATE_GAMIFICATION_VISIBILITY, false) ?: false

    private fun isAffiliateSSAShopEnabled() =
        RemoteConfigInstance.getInstance().abTestPlatform.getString(
            AFFILIATE_SSA_SHOP,
            ""
        ) == AFFILIATE_SSA_SHOP

    private fun affiliateRedirection() =
        remoteConfig?.getString(
            AFFILIATE_GAMIFICATION_REDIRECTION,
            AFFILIATE_GAMIFICATION_REDIRECTION_APPLINK
        )

    private fun affiliateTokoNowData(): TokonowRemoteConfigData? {
        val data = remoteConfig?.getString(AFFILIATE_TOKONOW_DATA, "")
        return Gson().fromJson(data, TokonowRemoteConfigData::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
        setObservers()
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
        binding?.promoNavToolbar?.run {
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

        binding?.promotionInfoCard?.setOnClickListener {
            AffiliateBottomSheetPromoCopyPasteInfo.newInstance().show(childFragmentManager, "")
        }

        isAffiliateSSAShopEnabled().let {
            affiliatePromoViewModel.fetchSSAShopList()
        }

        if (RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AFFILIATE_TOKONOW_BANNER,
                ""
            ) == AFFILIATE_TOKONOW_BANNER
        ) {
            binding?.tokonowContainer?.apply {
                show()
                setOnClickListener {
                    tokoNowData = affiliateTokoNowData()
                    affiliatePromoViewModel.getTokoNowBottomSheetInfo(getTokoNowPageId())
                }
            }
        }

        binding?.gamificationContainer?.isVisible = isAffiliateGamificationEnabled()

        binding?.gamificationEntryCardBanner?.setOnClickListener {
            val urlRedirectionAppLink = affiliateRedirection()
            if (urlRedirectionAppLink?.isNotEmpty() == true) {
                RouteManager.route(context, urlRedirectionAppLink)
            }
        }
        setupViewPager()
        showDefaultState()
        affiliatePromoViewModel.getAffiliateValidateUser()
        if (RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AFFILIATE_DISCO_PROMO,
                ""
            ) == AFFILIATE_DISCO_PROMO
        ) {
            affiliatePromoViewModel.getDiscoBanners(page = 0, limit = 7)
        }
    }

    private fun getTokoNowPageId(): String? {
        val isStaging = TokopediaUrl.getInstance().GQL.contains("staging")
        return if (isStaging) tokoNowData?.tokonowIdStaging else tokoNowData?.tokonowId
    }

    private fun sendClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SSA_SHOP_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface.userId
        )
    }

    fun handleBack() {
        if (binding?.recommendedLayout?.isVisible == true) {
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
        binding = AffiliatePromoFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setupViewPager() {
        activity?.let {
            tabFragments.add(
                AffiliateRecommendedProductFragment.getFragmentInstance(
                    AffiliateRecommendedProductFragment.LAST_VIEWED_IDENTIFIER,
                    this
                )
            )
            tabFragments.add(
                AffiliateRecommendedProductFragment.getFragmentInstance(
                    AffiliateRecommendedProductFragment.BOUGHT_IDENTIFIER,
                    this
                )
            )
            val adapter =
                AffiliateRecommendedAdapter(childFragmentManager, lifecycle, context, tabFragments)
            binding?.viewPagerRecommended?.adapter = adapter
            if (binding?.tabLayoutRecommended != null && binding?.viewPagerRecommended != null) {
                TabLayoutMediator(
                    binding?.tabLayoutRecommended!!,
                    binding?.viewPagerRecommended!!
                ) { _, _ ->
                }.attach()
            }
            binding?.tabLayoutRecommended?.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
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
                }
            )
        }
        setCustomTabText(context, binding?.tabLayoutRecommended)
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
                    binding?.promoAffiliateProgressView?.show()
                } else {
                    binding?.promoAffiliateProgressView?.gone()
                }
            }
        }

        affiliatePromoViewModel.getErrorMessage().observe(this) { _ ->
            binding?.root?.let {
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
                binding?.affiliateAnnouncementTicker?.setAnnouncementData(
                    it,
                    activity,
                    source = PAGE_ANNOUNCEMENT_PROMOSIKAN
                )
            }
        }
        affiliatePromoViewModel.getDiscoCampaignBanners().observe(this) {
            if (!it?.recommendedAffiliateDiscoveryCampaign?.data?.items.isNullOrEmpty()) {
                binding?.discoPromotionGroup?.show()
                binding?.discoInspirationLihatSemua?.setOnClickListener {
                    context?.let { ctx ->
                        startActivity(
                            Intent(ctx, AffiliateDiscoPromoListActivity::class.java)
                        )
                    }
                    sendDiscoLihatEvent()
                }
                binding?.rvDiscoPromotion?.apply {
                    val discoBannerAdapter =
                        AffiliateAdapter(
                            source = AffiliateAdapter.PageSource.SOURCE_PROMOSIKAN,
                            affiliateAdapterFactory = AffiliateAdapterFactory(
                                promotionClickInterface = this@AffiliatePromoFragment
                            ),
                            userId = userSessionInterface.userId
                        )
                    discoBannerAdapter.setVisitables(
                        it.recommendedAffiliateDiscoveryCampaign?.data?.items?.mapNotNull { campaign ->
                            AffiliateDiscoBannerUiModel(campaign)
                        }
                    )
                    this.adapter = discoBannerAdapter
                }
            }
        }
        affiliatePromoViewModel.getTokoNowBottomSheetData().observe(viewLifecycleOwner) { eligibility ->
            val pageId = getTokoNowPageId().toString()
            AffiliatePromotionBottomSheet.newInstance(
                AffiliatePromotionBottomSheetParams(
                    null,
                    pageId,
                    tokoNowData?.name.toString(),
                    tokoNowData?.imageURL.toString(),
                    tokoNowData?.weblink.toString(),
                    type = PageType.SHOP.value,
                    productIdentifier = pageId,
                    isLinkGenerationEnabled = true,
                    origin = AffiliatePromotionBottomSheet.ORIGIN_PROMO_TOKO_NOW,
                    ssaInfo = AffiliatePromotionBottomSheetParams.SSAInfo(
                        ssaStatus = true,
                        ssaMessage = "",
                        message = eligibility?.eligibleCommission?.message.toString(),
                        label = AffiliatePromotionBottomSheetParams.SSAInfo.Label(
                            labelType = "",
                            labelText = ""
                        )
                    )
                ),
                AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
                null
            ).show(childFragmentManager, "")
        }
        affiliatePromoViewModel.getSSAShopList().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding?.ssaPromotionGroup?.show()
                binding?.rvSsa?.adapter = ssaAdapter
                binding?.ssaLihatSemua?.setOnClickListener {
                    sendClickEvent()
                    context?.let { ctx ->
                        startActivity(
                            Intent(ctx, AffiliateSSAShopListActivity::class.java)
                        )
                    }
                }
                ssaAdapter.setVisitables(it)
            }
        }
    }

    private fun sendDiscoLihatEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_LIHAT_DISCO_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface.userId
        )
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
        binding?.recommendedLayout?.show()
    }

    private fun hideAllElements() {
        binding?.recommendedLayout?.hide()
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

    private fun setCustomTabText(context: Context?, tabLayout: TabLayout?) {
        if (context != null && tabLayout != null) {
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.affiliate_terakhir_dilihat)
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
                text = context.getString(R.string.affiliate_pernah_dibeli)
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

    override fun onPromotionClick(
        itemID: String,
        itemName: String,
        itemImage: String,
        itemURL: String,
        position: Int,
        commison: String,
        status: String,
        type: String?,
        appUrl: String?,
        ssaInfo: AffiliatePromotionBottomSheetParams.SSAInfo?
    ) {
        AffiliatePromotionBottomSheet.newInstance(
            AffiliatePromotionBottomSheetParams(
                null,
                itemID,
                itemName,
                itemImage,
                itemURL,
                itemID,
                appUrl = appUrl,
                commission = commison,
                type = type,
                isLinkGenerationEnabled = true,
                origin = AffiliatePromotionBottomSheet.ORIGIN_PROMO_DISCO_BANNER,
                ssaInfo = ssaInfo
            ),
            AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
            null
        ).show(childFragmentManager, "")
    }

    override fun onButtonClick(errorCta: AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?) =
        Unit

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
        AffiliatePromotionBottomSheet.newInstance(
            AffiliatePromotionBottomSheetParams(
                null, productId, productName, productImage, productUrl, productIdentifier,
                AffiliatePromotionBottomSheet.ORIGIN_SSA_SHOP, true, type = type,
                ssaInfo = ssaInfo
            ),
            AffiliatePromotionBottomSheet.Companion.SheetType.LINK_GENERATION,
            null
        ).show(childFragmentManager, "")
    }
}

interface AffiliatePromoInterface {
    fun enterLinkButtonClicked()
}
