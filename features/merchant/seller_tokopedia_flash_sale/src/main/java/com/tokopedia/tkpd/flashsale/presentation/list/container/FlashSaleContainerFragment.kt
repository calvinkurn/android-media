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
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentLandingContainerBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListFragment
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleContainerFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FlashSaleContainerFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding by autoClearedNullable<StfsFragmentLandingContainerBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleContainerViewModel::class.java) }

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
        binding = StfsFragmentLandingContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiEvent()
        observeUiState()
        viewModel.getTabsMetaData()
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

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: FlashSaleContainerViewModel.UiEvent) {
        when (event) {
            is FlashSaleContainerViewModel.UiEvent.FetchTabMetaError -> {

            }
        }
    }

    private fun handleUiState(uiState: FlashSaleContainerViewModel.UiState) {
        renderLoadingState(uiState.isLoading)
        renderTabs(uiState.tabsMetadata)
    }


    private fun renderLoadingState(isLoading: Boolean) {

    }

    private fun renderTabs(tabs: List<TabMetadata>) {
        if (tabs.isEmpty()) return
        displayTabs(tabs)
    }

    private fun createFragments(tabs: List<TabMetadata>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { index, tab ->
            val fragment = FlashSaleListFragment.newInstance(
                index,
                tab.tabName,
                tab.totalCampaign
            )

            val tabName = "${tab.displayName} (${tab.totalCampaign})"
            pages.add(Pair(tabName, fragment))
        }

        return pages
    }

    private fun displayTabs(tabs: List<TabMetadata>) {
        val fragments = createFragments(tabs)
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
}