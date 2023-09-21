package com.tokopedia.affiliate.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
import com.tokopedia.affiliate.AFFILIATE_NC
import com.tokopedia.affiliate.AFFILIATE_PROMOTE_HOME
import com.tokopedia.affiliate.AFFILIATE_SSA_SHOP
import com.tokopedia.affiliate.AFFILIATE_TOKONOW_BANNER
import com.tokopedia.affiliate.AFFILIATE_TOKONOW_DATA
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.ON_REGISTERED
import com.tokopedia.affiliate.ON_REVIEWED
import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_PROMO_PERFORMA
import com.tokopedia.affiliate.SYSTEM_DOWN
import com.tokopedia.affiliate.TIME_EIGHTEEN
import com.tokopedia.affiliate.TIME_ELEVEN
import com.tokopedia.affiliate.TIME_FIFTEEN
import com.tokopedia.affiliate.TIME_SIX
import com.tokopedia.affiliate.TIME_SIXTEEN
import com.tokopedia.affiliate.TIME_TEN
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
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomSheetPromoCopyPasteInfo
import com.tokopedia.affiliate.ui.bottomsheet.AffiliatePromotionBottomSheet
import com.tokopedia.affiliate.ui.custom.AffiliateBaseFragment
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDiscoBannerUiModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.affiliate_toko.databinding.AffiliatePromoFragmentLayoutBinding
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class AffiliatePromoFragment :
    AffiliateBaseFragment<AffiliatePromoViewModel>(),
    AffiliatePromoInterface,
    PromotionClickInterface,
    ProductClickInterface {

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var remoteConfigInstance: RemoteConfigInstance? = null

    private var affiliatePromoViewModel: AffiliatePromoViewModel? = null

    private val tabFragments = arrayListOf<Fragment>()
    private var remoteConfig: RemoteConfig? = null
    private var tokoNowData: TokonowRemoteConfigData? = null

    private var binding by autoClearedNullable<AffiliatePromoFragmentLayoutBinding>()

    private val ssaAdapter: AffiliateAdapter by lazy {
        AffiliateAdapter(
            AffiliateAdapterFactory(productClickInterface = this)
        )
    }
    private val loginRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setUserDetails()
                affiliatePromoViewModel?.getAffiliateValidateUser()
            } else {
                activity?.finish()
            }
        }

    private fun isAffiliateNCEnabled() =
        remoteConfigInstance?.abTestPlatform?.getString(
            AFFILIATE_NC,
            ""
        ) == AFFILIATE_NC

    private fun isAffiliatePromoteHomeEnabled() =
        remoteConfigInstance?.abTestPlatform?.getString(
            AFFILIATE_PROMOTE_HOME,
            ""
        ) == AFFILIATE_PROMOTE_HOME

    companion object {
        private const val TICKER_BOTTOM_SHEET = "bottomSheet"
        private const val TICKER_SHARED_PREF = "tickerSharedPref"
        private const val USER_ID = "userId"
        private const val TICKER_ID = "tickerId"
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
        remoteConfigInstance?.abTestPlatform?.getString(
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
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
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

    private fun afterViewCreated() {
        isAffiliatePromoteHomeEnabled().let { promoteHomeEnabled ->
            binding?.navHeaderGroup?.isVisible = promoteHomeEnabled
            binding?.promotionInfoTitle?.isVisible = !promoteHomeEnabled
            binding?.promotionInfoDesc?.isVisible = !promoteHomeEnabled
        }

        binding?.promoNavToolbar?.run {
            val iconBuilder = IconBuilder(builderFlags = IconBuilderFlag(pageSource = NavSource.AFFILIATE))
            if (isAffiliateNCEnabled()) {
                iconBuilder.addIcon(IconList.ID_NOTIFICATION, disableRouteManager = true) {
                    affiliatePromoViewModel?.resetNotificationCount()
                    sendNotificationClickEvent()
                    RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.AFFILIATE_NOTIFICATION
                    )
                }
            }
            iconBuilder
                .addIcon(IconList.ID_NAV_GLOBAL) {}
            setIcon(iconBuilder)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                if (isAffiliatePromoteHomeEnabled()) {
                    getString(R.string.label_affiliate)
                } else {
                    getString(R.string.affiliate_promo)
                }
            setOnBackButtonClickListener {
                (activity as? AffiliateActivity)?.handleBackButton(false)
            }
        }

        binding?.promotionInfoCard?.setOnClickListener {
            AffiliateBottomSheetPromoCopyPasteInfo.newInstance().show(childFragmentManager, "")
        }

        isAffiliateSSAShopEnabled().let {
            affiliatePromoViewModel?.fetchSSAShopList()
        }

        if (remoteConfigInstance?.abTestPlatform?.getString(
                AFFILIATE_TOKONOW_BANNER,
                ""
            ) == AFFILIATE_TOKONOW_BANNER
        ) {
            binding?.tokonowContainer?.apply {
                show()
                setOnClickListener {
                    tokoNowData = affiliateTokoNowData()
                    affiliatePromoViewModel?.getTokoNowBottomSheetInfo(getTokoNowPageId())
                }
            }
        }

        binding?.gamificationContainer?.isVisible = isAffiliateGamificationEnabled()

        binding?.buttonGamification?.setOnClickListener {
            val urlRedirectionAppLink = affiliateRedirection()
            if (urlRedirectionAppLink?.isNotEmpty() == true) {
                RouteManager.route(context, urlRedirectionAppLink)
            }
        }
        setupViewPager()
        showDefaultState()
        setAffiliateGreeting()
        setUserDetails()
        if (affiliatePromoViewModel?.isUserLoggedIn() == false) {
            loginRequest.launch(RouteManager.getIntent(context, ApplinkConst.LOGIN))
        } else {
            affiliatePromoViewModel?.getAffiliateValidateUser()
        }
        if (remoteConfigInstance?.abTestPlatform?.getString(
                AFFILIATE_DISCO_PROMO,
                ""
            ) == AFFILIATE_DISCO_PROMO
        ) {
            affiliatePromoViewModel?.getDiscoBanners(page = 0, limit = 7)
        }
        if (isAffiliateNCEnabled()) {
            affiliatePromoViewModel?.fetchUnreadNotificationCount()
        }
    }

    private fun getTokoNowPageId(): String? {
        val isStaging = TokopediaUrl.getInstance().TYPE == Env.STAGING
        return if (isStaging) tokoNowData?.tokonowIdStaging else tokoNowData?.tokonowId
    }

    private fun sendClickEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_SSA_SHOP_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface?.userId.orEmpty()
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

    private fun setUserDetails() {
        view?.findViewById<ImageUnify>(R.id.user_image)
            ?.loadImageCircle(affiliatePromoViewModel?.getUserProfilePicture())
        view?.findViewById<Typography>(R.id.user_name)?.text =
            affiliatePromoViewModel?.getUserName()
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
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun setObservers() {
        affiliatePromoViewModel?.progressBar()?.observe(this) { visibility ->
            if (visibility != null) {
                if (visibility) {
                    hideAllElements()
                    binding?.promoAffiliateProgressView?.show()
                } else {
                    binding?.promoAffiliateProgressView?.gone()
                }
            }
        }

        affiliatePromoViewModel?.getErrorMessage()?.observe(this) { _ ->
            binding?.root?.let {
                Toaster.build(
                    it,
                    getString(R.string.affiliate_product_link_invalid),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }

        affiliatePromoViewModel?.getValidateUserdata()?.observe(this) { validateUserdata ->
            onGetValidateUserData(validateUserdata)
        }
        affiliatePromoViewModel?.getAffiliateAnnouncement()?.observe(this) { announcementData ->
            if (announcementData.getAffiliateAnnouncementV2?.announcementData?.subType != TICKER_BOTTOM_SHEET) {
                sendTickerImpression(
                    announcementData.getAffiliateAnnouncementV2?.announcementData?.type,
                    announcementData.getAffiliateAnnouncementV2?.announcementData?.id
                )
                binding?.affiliateAnnouncementTicker?.setAnnouncementData(
                    announcementData,
                    activity,
                    source = PAGE_ANNOUNCEMENT_PROMO_PERFORMA
                )
            } else if (announcementData.getAffiliateAnnouncementV2?.announcementData?.subType == TICKER_BOTTOM_SHEET && isAffiliatePromoteHomeEnabled()) {
                context?.getSharedPreferences(TICKER_SHARED_PREF, Context.MODE_PRIVATE)?.let {
                    if (it.getString(
                            USER_ID,
                            null
                        ) != userSessionInterface?.userId.orEmpty() || it.getLong(
                                TICKER_ID,
                                -1
                            ) != announcementData.getAffiliateAnnouncementV2?.announcementData?.id
                    ) {
                        it.edit().apply {
                            putLong(
                                TICKER_ID,
                                announcementData.getAffiliateAnnouncementV2?.announcementData?.id ?: 0
                            )
                            putString(
                                USER_ID,
                                userSessionInterface?.userId.orEmpty()
                            )
                            apply()
                        }

                        AffiliateBottomSheetInfo.newInstance(
                            announcementData.getAffiliateAnnouncementV2?.announcementData?.id ?: 0,
                            announcementData.getAffiliateAnnouncementV2?.announcementData?.tickerData?.first()
                        ).show(childFragmentManager, "")
                    }
                }
            }
        }
        affiliatePromoViewModel?.getDiscoCampaignBanners()?.observe(this) {
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
                            userId = userSessionInterface?.userId.orEmpty()
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
        affiliatePromoViewModel?.getTokoNowBottomSheetData()
            ?.observe(viewLifecycleOwner) { eligibility ->
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
        affiliatePromoViewModel?.getSSAShopList()?.observe(viewLifecycleOwner) {
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
        affiliatePromoViewModel?.getUnreadNotificationCount()?.observe(this) { count ->
            binding?.promoNavToolbar?.apply {
                setCentralizedBadgeCounter(IconList.ID_NOTIFICATION, count)
            }
        }
    }

    private fun sendDiscoLihatEvent() {
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_CONTENT,
            AffiliateAnalytics.ActionKeys.CLICK_LIHAT_DISCO_BANNER,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
            "",
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun sendTickerImpression(tickerType: String?, tickerId: Long?) {
        if (tickerId.isMoreThanZero()) {
            AffiliateAnalytics.sendTickerEvent(
                AffiliateAnalytics.EventKeys.VIEW_ITEM,
                AffiliateAnalytics.ActionKeys.IMPRESSION_TICKER_COMMUNICATION,
                AffiliateAnalytics.CategoryKeys.AFFILIATE_PROMOSIKAN_PAGE,
                "$tickerType - $tickerId",
                PAGE_ANNOUNCEMENT_PROMO_PERFORMA,
                tickerId!!,
                AffiliateAnalytics.ItemKeys.AFFILIATE_PROMOSIKAN_TICKER_COMMUNICATION,
                userSessionInterface?.userId.orEmpty()
            )
        }
    }

    private fun showDefaultState() {
        binding?.recommendedLayout?.show()
    }

    private fun hideAllElements() {
        binding?.recommendedLayout?.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
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
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
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
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                    )
                )
            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
        }
    }

    override fun onSystemDown() {
        affiliatePromoViewModel?.setValidateUserType(SYSTEM_DOWN)
        affiliatePromoViewModel?.getAnnouncementInformation(isAffiliatePromoteHomeEnabled())
    }

    override fun onReviewed() {
        affiliatePromoViewModel?.setValidateUserType(ON_REVIEWED)
        affiliatePromoViewModel?.getAnnouncementInformation(isAffiliatePromoteHomeEnabled())
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
        affiliatePromoViewModel?.getAnnouncementInformation(isAffiliatePromoteHomeEnabled())
        affiliatePromoViewModel?.setValidateUserType(ON_REGISTERED)
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
