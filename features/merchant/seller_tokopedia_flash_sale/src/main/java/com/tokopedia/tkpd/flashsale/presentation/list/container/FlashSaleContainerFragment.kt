package com.tokopedia.tkpd.flashsale.presentation.list.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.components.bottomsheet.rbac.IneligibleAccessWarningBottomSheet
import com.tokopedia.campaign.components.ineligibleaccessview.IneligibleAccessView
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.utils.extension.doOnDelayFinished
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailActivity
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListFragment
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.tkpd.flashsale.util.tracker.FlashSaleListPageTracker
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ERROR
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_INFORMATION
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleContainerFragment : BaseDaggerFragment() {

    companion object {
        private const val REDIRECTION_DELAY: Long = 500
        private const val DEFAULT_TOTAL_CAMPAIGN_COUNT = 0
        private const val FEATURE_LEARN_MORE_ARTICLE_URL = "https://seller.tokopedia.com/edu/fitur-admin-toko/"
        private const val SELLER_EDU_ARTICLE_URL = "https://seller.tokopedia.com/edu/cara-daftar-produk-flash-sale/"
        private const val INELIGIBLE_ACCESS_IMAGE_URL = TokopediaImageUrl.INELIGIBLE_ACCESS_IMAGE_URL

        @JvmStatic
        fun newInstance() = FlashSaleContainerFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracker: FlashSaleListPageTracker

    private lateinit var remoteConfig: RemoteConfigInstance
    private var binding by autoClearedNullable<StfsFragmentFlashSaleListContainerBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleContainerViewModel::class.java) }

    private val predefinedTabs by lazy {
        listOf(
            TabMetadata.Tab(TabConstant.TAB_ID_UPCOMING, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_upcoming)),
            TabMetadata.Tab(TabConstant.TAB_ID_REGISTERED, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_registered)),
            TabMetadata.Tab(TabConstant.TAB_ID_ONGOING, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_ongoing)),
            TabMetadata.Tab(TabConstant.TAB_ID_FINISHED, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_finished))
        )
    }

    override fun getScreenName(): String = FlashSaleContainerFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentFlashSaleListContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiEffect()
        observeUiState()
        applyUnifyBackgroundColor()
        val rollenceValues = getRollenceValues()
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValues
        )
    }

    override fun onResume() {
        super.onResume()
        getFlashSaleSubmissionProgress()
    }

    private fun setupView() {
        binding?.run {
            header.headerTitle = getString(R.string.fs_tkpd_title)
            header.setNavigationOnClickListener { activity?.finish() }
        }
        addToolbarIcon()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun getFlashSaleSubmissionProgress() {
        viewModel.getFlashSaleSubmissionProgress()
    }

    private fun handleEffect(effect: FlashSaleContainerViewModel.UiEffect) {
        when (effect) {
            is FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData -> {
                binding?.root.showToasterError(effect.throwable)
            }
            FlashSaleContainerViewModel.UiEffect.ShowIneligibleAccessWarning -> {
                showIneligibleAccessBottomSheet()
            }
            is FlashSaleContainerViewModel.UiEffect.FlashSaleSubmissionProgress -> {
                setupWidgetCampaignProductSubmissionProgress(effect.uiModel)
            }
        }
    }

    private fun setupWidgetCampaignProductSubmissionProgress(
        uiModel: List<FlashSaleProductSubmissionProgress.Campaign>
    ) {
        binding?.widgetCampaignProductSubmissionProgress?.run {
            if (uiModel.isNotEmpty()) {
                show()
                setData(uiModel) {
                    navigateToFlashSaleDetailPage(it.campaignId.toLongOrZero())
                }
            } else {
                hide()
            }
        }
    }

    private fun navigateToFlashSaleDetailPage(campaignId: Long) {
        CampaignDetailActivity.start(context ?: return, campaignId)
    }

    private fun getRollenceValues(): List<String> {
        val listOfFilteredRollenceKeys: List<String> = getFilteredRollenceKeys()
        val listOfFilteredRollenceValues: List<String> = getFilteredRollenceValues(listOfFilteredRollenceKeys)
        return listOfFilteredRollenceValues
//        viewModel.getTickers(listOfFilteredRollenceValues)
    }

    private fun handleUiState(uiState: FlashSaleContainerViewModel.UiState) {
        renderLoadingState(uiState.isLoading, uiState.error)
        renderTicker(
            showTicker = uiState.showTicker,
            tickerList = uiState.tickerList,
            error = uiState.error,
            isLoading = uiState.isLoading,
            isEligibleUsingFeature = uiState.isEligibleUsingFeature
        )
        renderTabs(uiState.tabs, uiState.error, findTargetTabDestination() ?: return, uiState.isEligibleUsingFeature)
        renderErrorState(uiState.error)
        renderIneligibleAccessWarning(uiState.isEligibleUsingFeature)
    }

    private fun renderIneligibleAccessWarning(isEligibleUsingFeature: Boolean) {
        if (!isEligibleUsingFeature) {
            val onButtonClick = { routeToUrl(SELLER_EDU_ARTICLE_URL) }
            val param = IneligibleAccessView.Param(
                INELIGIBLE_ACCESS_IMAGE_URL,
                getString(R.string.stfs_ineligible_reason_title),
                getString(R.string.stfs_ineligible_reason_description),
                getString(R.string.stfs_feature_learn_more),
                onButtonClick
            )
            binding?.ineligibleAccessView?.show(param)
        } else {
            binding?.ineligibleAccessView?.gone()
        }
    }

    private fun renderErrorState(error: Throwable?) {
        val isError = error != null
        val rollenceValues = getRollenceValues()
        binding?.globalError?.isVisible = isError
        binding?.globalError?.setActionClickListener {
            viewModel.processEvent(
                event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
                rollenceValueList = rollenceValues
            )
        }
    }

    private fun renderLoadingState(isLoading: Boolean, error: Throwable?) {
        val isError = error != null
        binding?.shimmer?.content?.isVisible = isLoading && !isError
    }

    private fun renderTabs(tabs: List<TabMetadata.Tab>, error: Throwable?, targetTabPosition: Int, isEligibleUsingFeature: Boolean) {
        val isError = error != null
        binding?.tabsUnify?.isVisible = tabs.isNotEmpty() && !isError && isEligibleUsingFeature
        binding?.viewPager?.isVisible = tabs.isNotEmpty() && !isError && isEligibleUsingFeature

        if (tabs.isNotEmpty()) {
            displayTabs(predefinedTabs, tabs, targetTabPosition)
        }
    }

    private fun createFragments(predefinedTabs: List<TabMetadata.Tab>, tabs: List<TabMetadata.Tab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        predefinedTabs.forEachIndexed { _, currentTab ->
            val tab = tabs.find { tab -> tab.tabId == currentTab.tabId }
            val totalFlashSaleCount = tab?.totalFlashSaleCount.orZero()
            val tabName = tab?.tabName.orEmpty()
            val tabId = tab?.tabId.orZero()

            val fragment = FlashSaleListFragment.newInstance(tabId, tabName)

            val displayedTabName = "${currentTab.displayName} ($totalFlashSaleCount)"
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

    private fun displayTabs(predefinedTabs: List<TabMetadata.Tab>, tabs: List<TabMetadata.Tab>, targetTabPosition: Int) {
        val fragments = createFragments(predefinedTabs, tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)
                handleAutoRedirectionToSpecificTab(currentPosition, targetTabPosition)
            }
        }
    }

    private fun renderTicker(
        showTicker: Boolean,
//        remoteTickerMessage: String,
        tickerList: List<RemoteTicker>,
        error: Throwable?,
        isLoading: Boolean,
        isEligibleUsingFeature: Boolean
    ) {
        val isError = error != null
        val shouldDisplayTicker = showTicker && !isError && !isLoading && isEligibleUsingFeature

        if (shouldDisplayTicker) {
            displayTicker(tickerList)
        }
    }

    private fun addToolbarIcon() {
        val shopIcon = IconUnify(requireContext(), IconUnify.LIGHT_BULB)
        binding?.run {
            header.addCustomRightContent(shopIcon)
            header.setOnClickListener { routeToUrl(SELLER_EDU_ARTICLE_URL) }
        }
    }

    private fun handleAutoRedirectionToSpecificTab(currentPosition: Int, targetTabPosition: Int) {
        if (currentPosition == targetTabPosition) {
            focusTo(targetTabPosition)
        }
    }

    private fun focusTo(tabPosition: Int) {
        // Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        doOnDelayFinished(REDIRECTION_DELAY) {
            val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
            val tab = tabLayout?.getTabAt(tabPosition)
            tab?.select()
        }
    }

    private fun displayTicker(tickerList: List<RemoteTicker>) {
        binding?.run {
            ticker.isVisible = true

//            val defaultTicker = TickerData(
//                title = "",
//                description = getString(R.string.stfs_multi_location_ticker),
//                isFromHtml = true,
//                type = Ticker.TYPE_ANNOUNCEMENT
//            )
//            val remoteTicker = TickerData(
//                title = "",
//                description = remoteTickerMessage,
//                isFromHtml = true,
//                type = Ticker.TYPE_ANNOUNCEMENT
//            )
            var tickerDataList: MutableList<TickerData> = mutableListOf()
            tickerList.map {
                tickerDataList.add(
                    TickerData(
                        description = it.description,
                        type = it.type.toInt()
                    )
                )
            }

//            val tickers = if (remoteTickerMessage.isEmpty()) listOf(defaultTicker) else listOf(remoteTicker, defaultTicker)

            val tickerAdapter = TickerPagerAdapter(activity ?: return, tickerDataList)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    sendTickerHyperlinkClickEvent(linkUrl.toString())
                    routeToUrl(linkUrl.toString())
                }
            })

            ticker.addPagerView(tickerAdapter, tickerDataList)
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    routeToUrl(linkUrl.toString())
                }

                override fun onDismiss() {
//                    val rollenceValues = getRollenceValues()
//                    viewModel.processEvent(
//                        event = FlashSaleContainerViewModel.UiEvent.DismissMultiLocationTicker,
//                        rollenceValueList = rollenceValues
//                    )
                }
            })
        }
    }

    private fun findTargetTabDestination(): Int? {
        if (activity == null) return null

        val appLinkData = RouteManager.getIntent(activity, activity?.intent?.data.toString()).data

        val lastPathSegment = appLinkData?.lastPathSegment.orEmpty()
        val tabPosition = when (lastPathSegment) {
            "upcoming" -> FlashSaleListPageTab.UPCOMING.position
            "registered" -> FlashSaleListPageTab.REGISTERED.position
            "ongoing" -> FlashSaleListPageTab.ONGOING.position
            "finished" -> FlashSaleListPageTab.FINISHED.position
            else -> FlashSaleListPageTab.UPCOMING.position
        }

        return tabPosition
    }

    private fun showIneligibleAccessBottomSheet() {
        val bottomSheet = IneligibleAccessWarningBottomSheet.newInstance()
        bottomSheet.setOnButtonClicked { routeToUrl(FEATURE_LEARN_MORE_ARTICLE_URL) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun sendTickerHyperlinkClickEvent(linkUrl: String) {
        val description = getString(R.string.stfs_multi_location_ticker_description)
        val eventLabel = "$description - $linkUrl"
        tracker.sendClickReadArticleEvent(eventLabel)
    }

    private fun getFilteredRollenceKeys(): List<String> {
        val prefixKey = "CT_"
        val filteredRollenceKeys = getAbTestPlatform()
            .getKeysByPrefix(prefix = prefixKey)
        return filteredRollenceKeys.toList()
    }

    private fun getFilteredRollenceValues(keyList: List<String>): List<String> {
        var valueList: MutableList<String> = mutableListOf()
        for (key in keyList) {
            val rollenceValue: String = getAbTestPlatform().getString(key)
            if (rollenceValue.isNotEmpty()) {
                valueList.add(rollenceValue)
            }
        }
        return valueList
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfig.isInitialized) {
            remoteConfig = RemoteConfigInstance(activity?.application)
        }
        return remoteConfig.abTestPlatform
    }
}
