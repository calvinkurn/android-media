package com.tokopedia.shop.flashsale.presentation.list.container

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
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.slideDown
import com.tokopedia.shop.flashsale.common.extension.slideUp
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
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
        private const val DELAY_IN_MILLIS: Long = 400
        private const val FIRST_TAB = 0
        private const val SECOND_TAB = 1
        private const val BUNDLE_KEY_AUTO_FOCUS_TAB_POSITION = "auto_focus_tab_position"
        private const val REFRESH_CAMPAIGN_DELAY_DURATION_IN_MILLIS : Long = 1_000

        @JvmStatic
        fun newInstance(autoFocusTabPosition: Int = FIRST_TAB): CampaignListContainerFragment {
            return CampaignListContainerFragment().apply {
                val bundle = Bundle()
                bundle.putInt(BUNDLE_KEY_AUTO_FOCUS_TAB_POSITION, autoFocusTabPosition)
                arguments = bundle
            }
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListContainerBinding>()
    private val autoFocusTabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_AUTO_FOCUS_TAB_POSITION).orZero()
    }

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

    private fun displayTabs(tabs: List<TabMeta>) {
        val fragments = createFragments(tabs)
        val pagerAdapter =
            TabPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, fragments)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_FIXED

            TabsUnifyMediator(tabsUnify, viewPager) { tab, currentPosition ->
                tab.setCustomText(fragments[currentPosition].first)

                val targetTabPosition = viewModel.getAutoFocusTabPosition()
                if (currentPosition == targetTabPosition) {
                    focusTo(targetTabPosition)
                }

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
            fragment.setOnNavigateToActiveCampaignListener { focusTo(FIRST_TAB) }
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
        //Add some spare time caused by Backend write operation delay
        doOnDelayFinished(REFRESH_CAMPAIGN_DELAY_DURATION_IN_MILLIS) {
            viewModel.setAutoFocusTabPosition(SECOND_TAB)
            viewModel.getTabsMeta()
        }
    }

    private fun focusTo(tabPosition : Int) {
        //Add some spare time to make sure tabs are successfully drawn before select and focusing to a tab
        doOnDelayFinished(DELAY_IN_MILLIS) {
            val tabLayout = binding?.tabsUnify?.getUnifyTabLayout()
            val tab = tabLayout?.getTabAt(tabPosition)
            tab?.select()
        }

    }
}