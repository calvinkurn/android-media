package com.tokopedia.shopdiscount.manage.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.bulk.presentation.DiscountBulkApplyBottomSheet
import com.tokopedia.shopdiscount.databinding.FragmentDiscountProductManageBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.DiscountStatusMeta
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
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
        setupViews()
        //displayBulkApplyBottomSheet()
        observeProductsMeta()
        viewModel.getSlashPriceProductsMeta()
    }

    private fun setupViews() {
        binding?.run {
            tabs.getUnifyTabLayout().addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val position = tab.position
                    val fragment = ProductListFragment.newInstance(DiscountStatus.ONGOING)
                    router.replace(childFragmentManager, R.id.container, fragment)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    private fun observeProductsMeta() {
        viewModel.productsMeta.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    displayTabs(it.data)
                }
                is Fail -> {
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun displayTabs(tabs: List<DiscountStatusMeta>) {
        tabs.forEachIndexed { index, discountStatusMeta ->
            val tabName = String.format(
                getString(R.string.sd_tab_name),
                discountStatusMeta.name,
                discountStatusMeta.productCount
            )
            val isSelected = index == 0
            binding?.tabs?.addNewTab(tabName, isSelected)
            binding?.tabs?.customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    private fun displayBulkApplyBottomSheet() {
        val bottomSheet = DiscountBulkApplyBottomSheet.newInstance()
        bottomSheet.setOnApplyClickListener { discountSettings ->

        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}