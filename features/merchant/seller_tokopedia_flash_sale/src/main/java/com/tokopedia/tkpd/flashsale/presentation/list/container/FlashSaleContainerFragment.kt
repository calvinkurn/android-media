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
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListFragment
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleContainerFragment : BaseDaggerFragment() {

    companion object {
        private const val DEFAULT_TOTAL_CAMPAIGN_COUNT = 0

        @JvmStatic
        fun newInstance() = FlashSaleContainerFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<StfsFragmentFlashSaleListContainerBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleContainerViewModel::class.java) }

    private val predefinedTabs by lazy {
        listOf(
            TabMetadata(TabConstant.TAB_ID_UPCOMING, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_upcoming)),
            TabMetadata(TabConstant.TAB_ID_REGISTERED, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_registered)),
            TabMetadata(TabConstant.TAB_ID_ONGOING, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_ongoing)),
            TabMetadata(TabConstant.TAB_ID_FINISHED, "", DEFAULT_TOTAL_CAMPAIGN_COUNT, getString(R.string.stfs_tab_name_finished))
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
        inflater: LayoutInflater, container: ViewGroup?,
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
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)
    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
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

    private fun handleEffect(effect: FlashSaleContainerViewModel.UiEffect) {
        when (effect) {
            is FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData -> {
                binding?.root.showToasterError(effect.throwable)

            }
        }
    }

    private fun handleUiState(uiState: FlashSaleContainerViewModel.UiState) {
        renderLoadingState(uiState.isLoading, uiState.error)
        renderTicker(uiState.showTicker, uiState.error)
        renderTabs(uiState.tabsMetadata, uiState.error)
        renderErrorState(uiState.error)
    }

    private fun renderErrorState(error: Throwable?) {
        val isError = error != null
        binding?.globalError?.isVisible = isError
        binding?.globalError?.setActionClickListener {
            viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)
        }
    }


    private fun renderLoadingState(isLoading: Boolean, error: Throwable?) {
        val isError = error != null
        binding?.shimmer?.content?.isVisible = isLoading && !isError
    }

    private fun renderTabs(tabs: List<TabMetadata>, error: Throwable?) {
        val isError = error != null
        binding?.tabsUnify?.isVisible = tabs.isNotEmpty() && !isError
        binding?.viewPager?.isVisible = tabs.isNotEmpty() && !isError

        if (tabs.isNotEmpty()) {
            displayTabs(predefinedTabs, tabs)
        }
    }

    private fun createFragments(predefinedTabs: List<TabMetadata>, tabs: List<TabMetadata>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        predefinedTabs.forEachIndexed { index, currentTab ->
            val tab = tabs.find { tab -> tab.tabId == currentTab.tabId }
            val totalFlashSaleCount = tab?.totalFlashSaleCount.orZero()
            val tabName = tab?.tabName.orEmpty()
            val tabId = tab?.tabId.orZero()

            val fragment = FlashSaleListFragment.newInstance(
                index,
                tabId,
                tabName,
                totalFlashSaleCount
            )

            val displayedTabName = "${currentTab.displayName} (${totalFlashSaleCount})"
            pages.add(Pair(displayedTabName, fragment))
        }

        return pages
    }

    private fun displayTabs(predefinedTabs: List<TabMetadata>, tabs: List<TabMetadata>) {
        val fragments = createFragments(predefinedTabs, tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)
            }
        }
    }

    private fun renderTicker(shouldDisplayTicker: Boolean, error: Throwable?) {
        binding?.run {
            val isError = error != null
            ticker.isVisible = shouldDisplayTicker && !isError
            ticker.setHtmlDescription(getString(R.string.stfs_multi_location_ticker))
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    routeToUrl(linkUrl.toString())
                }

                override fun onDismiss() {
                    viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.DismissMultiLocationTicker)
                }

            })
        }
    }
}