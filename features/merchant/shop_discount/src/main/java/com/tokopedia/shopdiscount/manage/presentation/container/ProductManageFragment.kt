package com.tokopedia.shopdiscount.manage.presentation.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentDiscountProductManageBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.presentation.bottomsheet.ShopDiscountSellerInfoBottomSheet
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.manage.presentation.list.ProductListFragment
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.extension.applyUnifyBackgroundColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProductManageFragment : BaseDaggerFragment() {

    companion object {
        private const val DELAY_IN_MILLIS : Long = 300
        private const val BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB = "focus_to_upcoming_status_tab"
        private const val UPCOMING_STATUS_TAB_POSITION = 1

        @JvmStatic
        fun newInstance(focusToUpcomingStatusTab : Boolean) = ProductManageFragment().apply {
            val bundle  = Bundle()
            bundle.putBoolean(BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB, focusToUpcomingStatusTab)
            arguments = bundle
        }
    }

    private var binding by autoClearedNullable<FragmentDiscountProductManageBinding>()
    private val focusToUpcomingStatusTab by lazy {
        arguments?.getBoolean(
            BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB
        ).orFalse()
    }

    override fun getScreenName(): String = ProductManageFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var router: FragmentRouter

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    private var listener : TabChangeListener? = null


    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductManageViewModel::class.java) }

    private val tabs = listOf(
        PageTab("Berlangsung", "ACTIVE", DiscountStatus.ONGOING, 0),
        PageTab("Akan Datang", "SCHEDULED", DiscountStatus.SCHEDULED, 0),
        PageTab("Dialihkan", "PAUSED", DiscountStatus.PAUSED, 0)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscountProductManageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupViews()
        observeProductsMeta()
    }

    private fun setupViews() {
        setupHeader()
        setupTabs()
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

    private fun displayTicker() {
        val isPreviouslyDismissed = preferenceDataStore.isTickerDismissed()

        binding?.run {
            ticker.isVisible = !isPreviouslyDismissed
            ticker.setHtmlDescription(getString(R.string.sd_ticker_announcement_wording))
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    showSellerInfoBottomSheet()
                }

                override fun onDismiss() {
                    preferenceDataStore.markTickerAsDismissed()
                }

            })
        }
    }

    private fun setupHeader() {
        val shopIcon = IconUnify(requireContext(), IconUnify.SHOP_INFO)
        binding?.run {
            header.addCustomRightContent(shopIcon)
            header.setNavigationOnClickListener { activity?.finish() }
            header.setOnClickListener { showSellerInfoBottomSheet() }
        }
    }

    private fun observeProductsMeta() {
        viewModel.productsMeta.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    displayTicker()
                    binding?.shimmer?.content?.gone()
                    binding?.groupContent?.visible()
                    binding?.globalError?.gone()

                    val discountStatusWithCounter = viewModel.findDiscountStatusCount(tabs, it.data)
                    displayTabs(discountStatusWithCounter)
                }
                is Fail -> {
                    binding?.ticker?.gone()
                    binding?.shimmer?.content?.gone()
                    binding?.groupContent?.gone()
                    binding?.globalError?.gone()

                    displayError(it.throwable)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSelectedTabPosition(getCurrentTabPosition())
        getTabsMetadata()
    }


    private fun displayTabs(tabs: List<PageTab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter = TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        val previouslySelectedPosition = viewModel.getSelectedTabPosition()

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE

            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                tab.setCustomText(fragments[position].first)
                if (focusToUpcomingStatusTab) {
                    focusToUpcomingStatusTab()
                    binding?.viewPager?.showToaster(getString(R.string.sd_discount_created_successfully))
                } else {
                    focusToPreviousTab(tab, previouslySelectedPosition, position)
                }
            }
        }
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

    private fun focusToUpcomingStatusTab() {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_IN_MILLIS)

            val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
            val upcomingStatusTab =
                tabLayout?.getTabAt(UPCOMING_STATUS_TAB_POSITION) ?: return@launch
            upcomingStatusTab.select()
        }
    }

    private fun createFragments(tabs : List<PageTab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEach { tab ->
            val fragment = ProductListFragment.newInstance(
                tab.name,
                tab.discountStatusId,
                tab.count,
                onDiscountRemoved
            )
            fragment.setOnScrollDownListener { onRecyclerViewScrollDown() }
            fragment.setOnScrollUpListener { onRecyclerViewScrollUp() }
            fragment.setOnSwipeRefresh { onSwipeRefreshed() }

            val tabName = "${tab.name} (${tab.count})"
            pages.add(Pair(tabName, fragment))
        }

        return pages
    }

    private fun showSellerInfoBottomSheet() {
        val bottomSheet = ShopDiscountSellerInfoBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayError(throwable: Throwable) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { getTabsMetadata() }
            root showError throwable
        }

    }

    private fun getCurrentTabPosition(): Int {
        val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
        return tabLayout?.selectedTabPosition.orZero()
    }


    private fun getTabsMetadata() {
        binding?.shimmer?.content?.visible()
        binding?.groupContent?.gone()
        binding?.globalError?.gone()
        viewModel.getSlashPriceProductsMeta()
    }

    private val onDiscountRemoved: (Int, Int) -> Unit = { discountStatusId: Int, newTotalProduct : Int ->
        val currentTab = tabs.find { it.discountStatusId == discountStatusId }
        val updatedTabName = "${currentTab?.name} ($newTotalProduct)"

        val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
        val previouslySelectedPosition = tabLayout?.selectedTabPosition.orZero()

        val previouslySelectedTab = tabLayout?.getTabAt(previouslySelectedPosition)
        previouslySelectedTab?.setCustomText(updatedTabName)
        previouslySelectedTab?.select()
    }

    private val onRecyclerViewScrollDown: () -> Unit = {
        binding?.run {
            binding?.ticker?.gone()
        }
    }

    private val onRecyclerViewScrollUp: () -> Unit = {
        binding?.run {
            val isPreviouslyDismissed = preferenceDataStore.isTickerDismissed()
            val shouldShowTicker = !isPreviouslyDismissed
            if (shouldShowTicker){
                binding?.ticker?.visible()
            }
        }
    }

    private val onSwipeRefreshed: () -> Unit = {
        viewModel.getSlashPriceProductsMeta()
    }

    fun setTabChangeListener(listener: TabChangeListener) {
        this.listener = listener
    }

    interface TabChangeListener {
        fun onTabChanged()
    }
}