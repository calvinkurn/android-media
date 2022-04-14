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
import com.tokopedia.shopdiscount.search.presentation.SearchProductActivity
import com.tokopedia.shopdiscount.utils.animator.ViewAnimator
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.applyUnifyBackgroundColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class ProductManageFragment : BaseDaggerFragment() {

    companion object {
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance() = ProductManageFragment().apply {
            arguments = Bundle()
        }
    }

    private var binding by autoClearedNullable<FragmentDiscountProductManageBinding>()
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

    @Inject
    lateinit var viewAnimator: ViewAnimator

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
        setupSearchBar()
        setupHeader()
        setupTabs()
    }


    private fun setupTabs() {
        binding?.run {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.setSelectedTabPosition(position)

                    val currentTabPosition = viewModel.getSelectedTabPosition()
                    val tab = viewModel.getSelectedTab(currentTabPosition)
                    refreshSearchBarTitle(tab)
                    handleEmptyState(tab)
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

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.isFocusable = false
            searchBar.searchBarTextField.setOnClickListener { navigateToSearchProductPage() }
            searchBar.setOnClickListener { navigateToSearchProductPage() }
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
                    binding?.searchBar?.visible()
                    val discountStatusWithCounter = viewModel.findDiscountStatusCount(tabs, it.data)
                    displayTabs(discountStatusWithCounter)
                    viewModel.storeTabsData(discountStatusWithCounter)
                }
                is Fail -> {
                    binding?.ticker?.gone()
                    binding?.shimmer?.content?.gone()
                    binding?.groupContent?.gone()
                    binding?.globalError?.gone()
                    binding?.searchBar?.gone()

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

    private fun refreshSearchBarTitle(tab : PageTab) {
        binding?.searchBar?.isVisible = tab.count > ZERO
        binding?.searchBar?.searchBarPlaceholder = String.format(getString(R.string.sd_search_at), tab.name)
    }


    private fun handleEmptyState(tab: PageTab) {
        if (tab.count == ZERO) {
            binding?.emptyState?.visible()
            showEmptyState(tab.discountStatusId)
        } else {
            binding?.emptyState?.gone()
        }
    }

    private fun showEmptyState(discountStatusId : Int) {
        val title = if (discountStatusId == DiscountStatus.PAUSED) {
            getString(R.string.sd_no_paused_discount_title)
        } else {
            getString(R.string.sd_no_paused_discount_description)
        }

        val description = if (discountStatusId == DiscountStatus.PAUSED) {
            getString(R.string.sd_no_discount_title)
        } else {
            getString(R.string.sd_no_discount_description)
        }

        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(title)
        binding?.emptyState?.setDescription(description)
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
                if (previouslySelectedPosition == position) {
                    tab.select()
                }
            }
        }
    }

    private fun createFragments(tabs : List<PageTab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEach { tab ->
            val fragment = ProductListFragment.newInstance(tab.discountStatusId, onDiscountRemoved)
            fragment.setOnScrollDownListener { onRecyclerViewScrollDown() }
            fragment.setOnScrollUpListener { onRecyclerViewScrollUp() }

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

    private fun navigateToSearchProductPage() {
        val currentTabPosition = viewModel.getSelectedTabPosition()
        val tab = viewModel.getSelectedTab(currentTabPosition)
        SearchProductActivity.start(
            requireActivity(),
            tab.name,
            tab.discountStatusId
        )
    }

    private fun getTabsMetadata() {
        binding?.shimmer?.content?.visible()
        binding?.groupContent?.gone()
        binding?.searchBar?.gone()
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
            viewAnimator.hideWithAnimation(ticker)
            viewAnimator.hideWithAnimation(searchBar)
        }
    }

    private val onRecyclerViewScrollUp: () -> Unit = {
        binding?.run {
            val isPreviouslyDismissed = preferenceDataStore.isTickerDismissed()
            val shouldShowTicker = !isPreviouslyDismissed
            if (shouldShowTicker){
                viewAnimator.showWithAnimation(ticker)
            }
            viewAnimator.showWithAnimation(searchBar)
        }
    }
}