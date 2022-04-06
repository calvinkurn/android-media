package com.tokopedia.shopdiscount.manage.presentation.container

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
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
        private const val ANIMATION_DURATION_IN_MILLIS : Long = 500
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
        viewModel.getSlashPriceProductsMeta()
    }

    private fun setupViews() {
        setupTicker()
        setupSearchBar()
        setupToolbar()
    }

    private fun setupTicker() {
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
            searchBar.searchBarTextField.setOnClickListener {
                SearchProductActivity.start(
                    requireActivity()
                )
            }
            searchBar.setOnClickListener { SearchProductActivity.start(requireActivity()) }
        }
    }

    private fun setupToolbar() {
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
                    binding?.ticker?.visible()
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

    private val onDiscountRemoved: (Int, Int) -> Unit = { discountStatusId: Int, totalProduct : Int ->
        val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
        val previouslySelectedPosition = tabLayout?.selectedTabPosition.orZero()

        val meta = tabs.find { it.discountStatusId == discountStatusId }
        val decreasedProductCount = totalProduct - 1
        val updatedTabName = "${meta?.name} ($decreasedProductCount)"

        val previouslySelectedTab = tabLayout?.getTabAt(previouslySelectedPosition)
        previouslySelectedTab?.setCustomText(updatedTabName)
        previouslySelectedTab?.select()
    }

    private val onRecyclerViewScrollDown: () -> Unit = {
        hideTickerWithAnimation()
        binding?.cardView?.visible()
    }

    private val onRecyclerViewScrollUp: () -> Unit = {
        val isPreviouslyDismissed = preferenceDataStore.isTickerDismissed()
        val shouldShowTicker = !isPreviouslyDismissed
        if (shouldShowTicker){
            showTickerWithAnimation()
        }
        binding?.cardView?.gone()
    }

    private fun displayTabs(tabs: List<PageTab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter = TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE

            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                tab.setCustomText(fragments[position].first)
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
            globalError.setActionClickListener { viewModel.getSlashPriceProductsMeta() }
            root showError throwable
        }

    }
    private fun showTickerWithAnimation() {
        binding?.run {
            ticker.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        ticker.visible()
                    }
                })
        }
    }

    private fun hideTickerWithAnimation() {
        binding?.run {
            ticker.animate()
                .translationY(ticker.height.toFloat())
                .alpha(0.0f)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        ticker.gone()
                    }
                })
        }
    }
}