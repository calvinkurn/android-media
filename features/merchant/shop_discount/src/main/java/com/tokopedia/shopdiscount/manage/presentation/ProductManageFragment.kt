package com.tokopedia.shopdiscount.manage.presentation

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
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.databinding.FragmentDiscountProductManageBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.PageTab
import com.tokopedia.shopdiscount.utils.extension.applyUnifyBackgroundColor
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ProductManageFragment : BaseDaggerFragment() {

    companion object {
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

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductManageViewModel::class.java) }

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
        //displayBulkApplyBottomSheet()
        observeProductsMeta()
        viewModel.getSlashPriceProductsMeta()
    }

    private fun setupViews() {
        setupTicker()
        setupSearchBar()
        setupToolbar()
    }

    private fun setupTicker() {
        binding?.run {
            ticker.setHtmlDescription(getString(R.string.sd_ticker_announcement_wording))
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    displayBulkApplyBottomSheet()
                }

                override fun onDismiss() {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.isFocusable = false
            searchBar.setOnClickListener { displayBulkApplyBottomSheet() }
        }
    }

    private fun setupToolbar() {
        val shopIcon = IconUnify(requireContext(), IconUnify.SHOP_INFO)
        binding?.run {
            header.addCustomRightContent(shopIcon)
            header.setNavigationOnClickListener { activity?.finish() }
            header.setOnClickListener { displayBulkApplyBottomSheet() }
        }
    }

    private fun observeProductsMeta() {
        viewModel.productsMeta.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val formattedDiscountStatusMeta = viewModel.findDiscountStatusCount(it.data)
                    displayTabs(formattedDiscountStatusMeta)
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun displayTabs(tabs: List<PageTab>) {
        val pages = mutableListOf<Pair<String, Fragment>>()

        tabs.forEach { tab ->
            val fragment = ProductListFragment.newInstance(tab.discountStatusId)
            pages.add(Pair(tab.name, fragment))
        }

        val pagerAdapter = TabPagerAdapter(requireActivity(), pages)

        binding?.run {
            viewPager.adapter = pagerAdapter
            tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
            TabsUnifyMediator(tabsUnify, viewPager) { tab, position ->
                tab.setCustomText(pages[position].first)
            }
        }
    }

    private fun displayBulkApplyBottomSheet() {
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance()
        bottomSheet.setOnApplyClickListener { discountSettings ->

        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}