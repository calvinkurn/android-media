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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.FragmentDiscountedProductManageBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.presentation.bottomsheet.ShopDiscountSellerInfoBottomSheet
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.manage.presentation.list.DiscountedProductListFragment
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.UrlConstant
import com.tokopedia.shopdiscount.utils.extension.setFragmentToUnifyBgColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
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


class DiscountedProductManageFragment : BaseDaggerFragment() {

    companion object {
        private const val NOT_SET = 0
        private const val DELAY_IN_MILLIS: Long = 300
        private const val TAB_POSITION_FIRST = 0
        private const val TAB_POSITION_SECOND = 1
        private const val TAB_POSITION_THIRD = 2
        private const val BUNDLE_KEY_TOASTER_WORDING = "toaster_wording"
        private const val BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID = "previous_discount_status_id"
        private const val SELLER_ADMIN_ARTICLE = "https://seller.tokopedia.com/edu/fitur-admin-toko/"

        @JvmStatic
        fun newInstance(
            previouslySelectedDiscountStatusId: Int,
            toasterWording: String
        ): DiscountedProductManageFragment {
            return DiscountedProductManageFragment().apply {
                val bundle = Bundle()
                bundle.putInt(
                    BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID,
                    previouslySelectedDiscountStatusId
                )
                bundle.putString(BUNDLE_KEY_TOASTER_WORDING, toasterWording)
                arguments = bundle
            }
        }
    }

    private var binding by autoClearedNullable<FragmentDiscountedProductManageBinding>()
    private val toasterWording by lazy {
        arguments?.getString(
            BUNDLE_KEY_TOASTER_WORDING
        ).orEmpty()
    }
    private var previouslySelectedDiscountStatusId = 0
    private val currentDiscountStatusId by lazy {
        arguments?.getInt(
            BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID
        ) ?: NOT_SET
    }
    override fun getScreenName(): String = DiscountedProductManageFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    private var listener: TabChangeListener? = null
    private var remoteConfig: RemoteConfig? = null

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DiscountedProductManageViewModel::class.java) }

    private val tabs by lazy {
        listOf(
            PageTab(getString(R.string.sd_ongoing), "ACTIVE", DiscountStatus.ONGOING, 0, TAB_POSITION_FIRST),
            PageTab(getString(R.string.sd_upcoming), "SCHEDULED", DiscountStatus.SCHEDULED, 0, TAB_POSITION_SECOND),
            PageTab(getString(R.string.sd_paused), "PAUSED", DiscountStatus.PAUSED, 0, TAB_POSITION_THIRD)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscountedProductManageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteConfig = FirebaseRemoteConfigImpl(context)
        handleArguments()
        setFragmentToUnifyBgColor()
        setupViews()
        observeProductsMeta()
        observeSellerEligibility()
        checkSellerEligibility()
    }

    private fun setupViews() {
        setupHeader()
        setupTabs()
    }

    private fun handleArguments() {
        this.previouslySelectedDiscountStatusId = arguments?.getInt(
            BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID
        ) ?: NOT_SET
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
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
        }
    }

    private fun addShopInfoIcon() {
        val shopIcon = IconUnify(requireContext(), IconUnify.SHOP_INFO)
        binding?.run {
            header.addCustomRightContent(shopIcon)
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

                    displayError(it.throwable){
                        getTabsMetadata()
                    }
                }
            }
        }
    }

    private fun observeSellerEligibility() {
        viewModel.sellerEligibility.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if(it.data.hasBenefitPackage) {
                        hideErrorEligibleView()
                        addShopInfoIcon()
                        getTabsMetadata()
                    }
                    else {
                        binding?.ticker?.gone()
                        binding?.shimmer?.content?.gone()
                        binding?.groupContent?.gone()
                        binding?.globalError?.gone()
                        if(!it.data.hasBenefitPackage && !it.data.isAuthorize) {
                            showRbacBottomSheet()
                            showErrorEligibleView()
                        } else if(!it.data.hasBenefitPackage){
                            showErrorEligibleView()
                        }
                    }
                }
                is Fail -> {
                    binding?.ticker?.gone()
                    binding?.shimmer?.content?.gone()
                    binding?.groupContent?.gone()
                    binding?.globalError?.gone()
                    hideErrorEligibleView()
                    displayError(it.throwable){
                        checkSellerEligibility()
                    }
                }
            }
        }
    }

    private fun showErrorEligibleView() {
        binding?.layoutErrorNotEligible?.apply {
            containerLayoutErrorNotEligible.show()
            imageErrorNotEligible.loadImage(UrlConstant.URL_IMAGE_NON_ELIGIBLE_SELLER_EDU)
            buttonNonEligible.setOnClickListener {
                redirectToNonEligibleSellerEdu()
            }
        }
    }

    private fun redirectToNonEligibleSellerEdu() {
        RouteManager.route(
            context, String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW, UrlConstant.SELLER_NON_ELIGIBLE_EDU_URL
            )
        )
    }

    private fun hideErrorEligibleView() {
        binding?.layoutErrorNotEligible?.containerLayoutErrorNotEligible?.hide()
    }

    private fun showRbacBottomSheet() {
        RouteManager.route(context, getSlashPriceApplink())
    }

    private fun getSlashPriceApplink(): String {
        return UriUtil.buildUriAppendParam(
            uri = ApplinkConstInternalSellerapp.ADMIN_RESTRICTION,
            queryParameters = mapOf(
                ApplinkConstInternalSellerapp.PARAM_ARTICLE_URL to SELLER_ADMIN_ARTICLE
            )
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSelectedTabPosition(getCurrentTabPosition())
        (viewModel.sellerEligibility.value as? Success)?.data?.let {
            if(it.hasBenefitPackage)
                getTabsMetadata()
        }
    }

    private fun displayTabs(tabs: List<PageTab>) {
        val fragments = createFragments(tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)
        val previouslySelectedPosition = viewModel.getSelectedTabPosition()

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE

            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                tab.setCustomText(fragments[position].first)
                if (isRedirectionFromAnotherPage()) {
                    focusTo(currentDiscountStatusId)
                    displayToaster(toasterWording)
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

    private fun focusTo(discountStatusId: Int) {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        CoroutineScope(Dispatchers.Main).launch {
            delay(DELAY_IN_MILLIS)

            val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()

            val matchedTab = tabs.find { tab -> tab.discountStatusId == discountStatusId }
            val tabPosition = matchedTab?.tabPosition.orZero()
            val upcomingStatusTab =
                tabLayout?.getTabAt(tabPosition) ?: return@launch
            upcomingStatusTab.select()
        }
    }

    private fun displayToaster(wording: String) {
        if (wording.isNotEmpty()) {
            binding?.viewPager?.showToaster(toasterWording)
        }
    }

    private fun createFragments(tabs: List<PageTab>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEach { tab ->
            val fragment = DiscountedProductListFragment.newInstance(
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

    private fun displayError(throwable: Throwable, onClickRetry: () -> Unit) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener {
                onClickRetry.invoke()
            }
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


    private fun checkSellerEligibility() {
        if (isEnableShopDiscount()) {
            binding?.shimmer?.content?.visible()
            binding?.groupContent?.gone()
            binding?.globalError?.gone()
            hideErrorNoAccess()
            viewModel.checkSellerEligibility()
        } else {
            binding?.shimmer?.content?.gone()
            showErrorNoAccess()
        }
    }

    private fun isEnableShopDiscount(): Boolean {
        return remoteConfig?.getBoolean(RemoteConfigKey.ENABLE_SHOP_DISCOUNT, true).orFalse()
    }

    private fun showErrorNoAccess() {
        binding?.layoutErrorNoAccess?.apply {
            imageErrorNoAccess.loadImage(UrlConstant.URL_IMAGE_NO_ACCESS_SLASH_PRICE)
            containerLayoutErrorNoAccess.show()
        }
    }

    private fun hideErrorNoAccess() {
        binding?.layoutErrorNoAccess?.containerLayoutErrorNoAccess?.hide()
    }

    private val onDiscountRemoved: (Int, Int) -> Unit =
        { discountStatusId: Int, newTotalProduct: Int ->
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
            if (shouldShowTicker) {
                binding?.ticker?.visible()
            }
        }
    }

    private val onSwipeRefreshed: () -> Unit = {
        viewModel.getSlashPriceProductsMeta()
        if (isRedirectionFromAnotherPage()) {
            previouslySelectedDiscountStatusId = NOT_SET
        }
    }

    fun setTabChangeListener(listener: TabChangeListener) {
        this.listener = listener
    }

    interface TabChangeListener {
        fun onTabChanged()
    }

    private fun isRedirectionFromAnotherPage(): Boolean {
        return previouslySelectedDiscountStatusId != NOT_SET
    }
}