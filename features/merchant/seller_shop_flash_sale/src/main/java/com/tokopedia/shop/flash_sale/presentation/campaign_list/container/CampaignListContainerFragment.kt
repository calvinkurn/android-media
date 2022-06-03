package com.tokopedia.shop.flash_sale.presentation.campaign_list.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListContainerBinding
import com.tokopedia.shop.flash_sale.common.extension.showError
import com.tokopedia.shop.flash_sale.common.extension.slideDown
import com.tokopedia.shop.flash_sale.common.extension.slideUp
import com.tokopedia.shop.flash_sale.common.util.DateManager
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.TabMeta
import com.tokopedia.shop.flash_sale.presentation.campaign_list.list.CampaignListFragment
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CampaignListContainerFragment : BaseDaggerFragment() {

    companion object {
        private const val NOT_SET = 0
        private const val DELAY_IN_MILLIS: Long = 300
        private const val TAB_POSITION_FIRST = 0
        private const val TAB_POSITION_SECOND = 1
        private const val BUNDLE_KEY_PREVIOUS_CAMPAIGN_STATUS_ID = "previous_discount_status_id"

        @JvmStatic
        fun newInstance(
            previouslySelectedDiscountStatusId: Int = 0
        ): CampaignListContainerFragment {
            return CampaignListContainerFragment().apply {
                val bundle = Bundle()
                bundle.putInt(
                    BUNDLE_KEY_PREVIOUS_CAMPAIGN_STATUS_ID,
                    previouslySelectedDiscountStatusId
                )
                arguments = bundle
            }
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListContainerBinding>()
    private var previouslySelectedCampaignStatusId = 0
    private val currentCampaignStatusId by lazy {
        arguments?.getInt(
            BUNDLE_KEY_PREVIOUS_CAMPAIGN_STATUS_ID
        ) ?: NOT_SET
    }
    private var listener: TabChangeListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListContainerViewModel::class.java) }

    override fun getScreenName(): String =
        CampaignListContainerFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignListContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupTabs()
        observeTabsMeta()
        viewModel.getTabsMeta()

    }

    private fun observeTabsMeta() {
        viewModel.tabsMeta.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    binding?.loader?.gone()
                    binding?.groupContent?.visible()
                    binding?.globalError?.gone()

                    displayTabs(result.data)
                }
                is Fail -> {
                    binding?.loader?.gone()
                    binding?.groupContent?.gone()
                    binding?.globalError?.gone()
                    displayError(result.throwable)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.setSelectedTabPosition(getCurrentTabPosition())
    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
    }


    private fun setupTabs() {
        binding?.run {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setSelectedTabPosition(position)
                    listener?.onTabChanged()
                }
            })
        }
    }



    private fun displayError(throwable: Throwable) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { reload() }
            root showError throwable
        }

    }

    private fun getCurrentTabPosition(): Int {
        val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
        return tabLayout?.selectedTabPosition.orZero()
    }


    private fun reload() {
        binding?.loader?.visible()
        binding?.groupContent?.gone()
        binding?.globalError?.gone()
        viewModel.getTabsMeta()
    }

    private val onRecyclerViewScrollDown: () -> Unit = {
        binding?.run {
            tabsUnify.getUnifyTabLayout().slideDown()
            alignRecyclerViewToToolbarBottom()
        }
    }

    private val onRecyclerViewScrollUp: () -> Unit = {
        binding?.run {
            tabsUnify.getUnifyTabLayout().slideUp()
            alignRecyclerViewToTabsBottom()
        }
    }


    fun setTabChangeListener(listener: TabChangeListener) {
        this.listener = listener
    }

    interface TabChangeListener {
        fun onTabChanged()
    }

    private fun isRedirectionFromAnotherPage(): Boolean {
        return previouslySelectedCampaignStatusId != NOT_SET
    }

    private fun displayTabs(tabs: List<TabMeta>) {
        val fragments = createFragments(tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        val previouslySelectedPosition = viewModel.getSelectedTabPosition()

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)

                if (isRedirectionFromAnotherPage()) {
                    focusTo(currentCampaignStatusId)
                } else {
                    focusToPreviousTab(tab, previouslySelectedPosition, currentPosition)
                }
            }
        }
    }

    private fun createFragments(tabs: List<TabMeta>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()


        tabs.forEachIndexed { index, tab ->
            val fragment = CampaignListFragment.newInstance(
                index,
                tab.name,
                tab.status.toIntArray(),
                tab.totalCampaign
            )
            fragment.setOnScrollDownListener { onRecyclerViewScrollDown() }
            fragment.setOnScrollUpListener { onRecyclerViewScrollUp() }

            val tabName = "${tab.name} (${tab.totalCampaign})"
            pages.add(Pair(tabName, fragment))
        }

        return pages
    }

    private fun focusToPreviousTab(
        tab: TabLayout.Tab,
        previouslySelectedPosition: Int,
        currentlyRenderedTabPosition: Int
    ) {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_IN_MILLIS)
            if (previouslySelectedPosition == currentlyRenderedTabPosition) {
                tab.select()
            }
        }
    }

    private fun alignRecyclerViewToToolbarBottom() {
        val set = ConstraintSet()
        set.clone(binding?.container)
        set.connect(
            binding?.viewPager?.id ?: return,
            ConstraintSet.TOP,
            binding?.header?.id ?: return,
            ConstraintSet.BOTTOM
        )

        set.applyTo(binding?.container)
    }


    private fun alignRecyclerViewToTabsBottom() {
        val set = ConstraintSet()
        set.clone(binding?.container)
        set.connect(
            binding?.viewPager?.id ?: return,
            ConstraintSet.TOP,
            binding?.tabsUnify?.id ?: return,
            ConstraintSet.BOTTOM
        )

        set.applyTo(binding?.container)
    }


    private fun focusTo(discountStatusId: Int) {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_IN_MILLIS)

            /*val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()

            val matchedTab = tabs.find { tab -> tab.discountStatusId == discountStatusId }
            val tabPosition = matchedTab?.tabPosition.orZero()
            val upcomingStatusTab =
                tabLayout?.getTabAt(tabPosition) ?: return@launch
            upcomingStatusTab.select()*/
        }
    }

}