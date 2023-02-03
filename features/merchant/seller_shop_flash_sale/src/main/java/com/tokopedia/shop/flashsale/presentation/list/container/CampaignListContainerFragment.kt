package com.tokopedia.shop.flashsale.presentation.list.container

import com.tokopedia.imageassets.ImageUrl

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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListContainerBinding
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.slideDown
import com.tokopedia.shop.flashsale.common.extension.slideUp
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignListTab
import com.tokopedia.shop.flashsale.presentation.list.container.adapter.TabPagerAdapter
import com.tokopedia.shop.flashsale.presentation.list.list.CampaignListFragment
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignListContainerFragment : BaseDaggerFragment() {

    companion object {
        private const val REDIRECTION_DELAY : Long = 500
        private const val EMPTY_STATE_IMAGE_URL = ImageUrl.EMPTY_STATE_IMAGE_URL
        private const val FEATURE_INTRODUCTION_URL = "https://seller.tokopedia.com/edu/flash-sale-toko/"

        @JvmStatic
        fun newInstance(): CampaignListContainerFragment {
            return CampaignListContainerFragment()
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListContainerBinding>()

    private var listener : ActiveCampaignListListener? = null

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
        observeSellerEligibility()
        val targetTabPosition = findTargetTabDestination()?.position ?: return
        viewModel.getPrerequisiteData(targetTabPosition)
    }

    private fun observeSellerEligibility() {
        viewModel.isEligible.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()

            when (result) {
                is Success -> {
                    handleSellerEligibility(result.data)
                }
                is Fail -> {
                    displayError(result.throwable)
                }
            }
        }

    }

    private fun observeTabsMeta() {
        viewModel.tabsMeta.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()

            when (result) {
                is Success -> {
                    binding?.groupContent?.visible()
                    displayTabs(result.data.tabMeta, result.data.targetTabPosition)
                }
                is Fail -> {
                    binding?.groupContent?.gone()
                    binding?.container showError result.throwable
                }
            }
        }

    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
            btnLearnMore.setOnClickListener { routeToFeatureIntro() }
        }
    }


    private fun setupTabs() {
        binding?.run {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    displayTabs()
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

    private fun reload() {
        binding?.loader?.visible()
        binding?.groupContent?.gone()
        binding?.globalError?.gone()
        viewModel.getPrerequisiteData(CampaignListTab.ACTIVE_CAMPAIGN.position)
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

    private fun displayTabs(tabs: List<TabMeta>, targetTabPosition: Int) {
        val fragments = createFragments(tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)
                handleAutoRedirectionToSpecificTab(currentPosition, targetTabPosition)
            }
        }
    }

    private fun createFragments(tabs: List<TabMeta>): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEachIndexed { index, tab ->
            val fragment = CampaignListFragment.newInstance(
                index,
                tab.status.toIntArray(),
                tab.totalCampaign
            )
            fragment.setOnScrollDownListener { onRecyclerViewScrollDown() }
            fragment.setOnScrollUpListener { onRecyclerViewScrollUp() }
            fragment.setOnNavigateToActiveCampaignListener { focusTo(tabPosition = CampaignListTab.ACTIVE_CAMPAIGN.position) }
            fragment.setOnCancelCampaignSuccess { handleCancelCampaignSuccess() }

            val tabName = "${tab.name} (${tab.totalCampaign})"
            pages.add(Pair(tabName, fragment))
        }

        return pages
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


    private fun handleCancelCampaignSuccess() {
        binding?.loader?.visible()
        viewModel.getTabsMeta(CampaignListTab.HISTORY_CAMPAIGN.position)
    }

    private fun focusTo(delay: Long = 0, tabPosition : Int) {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        doOnDelayFinished(delay) {
            val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
            val tab = tabLayout?.getTabAt(tabPosition)
            tab?.select()
        }
    }

    private fun displayTabs() {
        binding?.tabsUnify?.getUnifyTabLayout().slideUp()
        alignRecyclerViewToTabsBottom()
    }

    private fun handleAutoRedirectionToSpecificTab(currentPosition: Int, targetTabPosition: Int) {
        if (currentPosition == targetTabPosition) {
            focusTo(REDIRECTION_DELAY, targetTabPosition)
        }
    }

    fun setActiveCampaignListListener(listener: ActiveCampaignListListener) {
        this.listener = listener
    }

    fun showSaveDraftSuccessInActiveTab() {
        listener?.onSaveDraftSuccess()
    }

    fun showEditCampaignSuccessInActiveTab() {
        listener?.onEditCampaignSuccess()
    }

    interface ActiveCampaignListListener {
        fun onSaveDraftSuccess()
        fun onEditCampaignSuccess()
    }

    private fun handleSellerEligibility(isEligible: Boolean) {
        if (!isEligible) {
            showIneligibleAccessNotice()
        }
    }

    private fun showIneligibleAccessNotice() {
        binding?.run {
            groupIneligibleAccess.visible()
            emptyState.setImageUrl(EMPTY_STATE_IMAGE_URL)
            emptyState.setTitle(getString(R.string.sfs_forbidden_access_title))
            emptyState.setDescription(getString(R.string.sfs_forbidden_access_description))
        }
    }

    private fun routeToFeatureIntro() {
        if (!isAdded) return
        val encodedUrl = FEATURE_INTRODUCTION_URL.encodeToUtf8()
        val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
        RouteManager.route(activity ?: return, route)
    }

    private fun findTargetTabDestination(): CampaignListTab? {
        if (activity == null) return null

        val appLinkData = RouteManager.getIntent(activity, activity?.intent?.data.toString()).data
        val lastPathSegment = appLinkData?.lastPathSegment.orEmpty()

        return if (lastPathSegment == "history") {
            CampaignListTab.HISTORY_CAMPAIGN
        } else {
            CampaignListTab.ACTIVE_CAMPAIGN
        }
    }


}