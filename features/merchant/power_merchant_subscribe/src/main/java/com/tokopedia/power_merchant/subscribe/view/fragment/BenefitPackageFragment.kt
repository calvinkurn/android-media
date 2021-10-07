package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantErrorLogger
import com.tokopedia.power_merchant.subscribe.databinding.FragmentBenefitPackagePageBinding
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapter
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageDataListener
import com.tokopedia.power_merchant.subscribe.view.adapter.BenefitPackageErrorListener
import com.tokopedia.power_merchant.subscribe.view.model.BenefitPackageErrorUiModel
import com.tokopedia.power_merchant.subscribe.view.viewmodel.BenefitPackageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class BenefitPackageFragment : BaseDaggerFragment(), BenefitPackageDataListener,
    BenefitPackageErrorListener {

    @Inject
    lateinit var benefitPackageViewModel: BenefitPackageViewModel

    private val binding: FragmentBenefitPackagePageBinding? by viewBinding()

    private val benefitPackageAdapterTypeFactory by lazy {
        BenefitPackageAdapterFactoryImpl(
            this, this
        )
    }

    private val benefitPackageAdapter by lazy {
        BenefitPackageAdapter(
            benefitPackageAdapterTypeFactory
        )
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_benefit_package_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundColor()
        setupActionBar()
        setupAdapter()
        onSwipeRefreshBenefitPackage()
        observeBenefitPackagePage()
        loadData()
    }

    override fun onDestroy() {
        benefitPackageViewModel.benefitPackage.removeObservers(this)
        super.onDestroy()
    }

    private fun observeBenefitPackagePage() {
        observe(benefitPackageViewModel.benefitPackage) {
            hideLoading()
            when (it) {
                is Success -> {
                    benefitPackageAdapter.setBenefitPackageData(it.data)
                }
                is Fail -> {
                    benefitPackageAdapter.setBenefitPackageError(
                        BenefitPackageErrorUiModel(
                            it.throwable
                        )
                    )
                    PowerMerchantErrorLogger.logToCrashlytic(
                        PowerMerchantErrorLogger.PM_BENEFIT_PACKAGE_ERROR, it.throwable
                    )
                }
            }
        }
    }

    private fun setupAdapter() {
        binding?.rvBenefitPackagePage?.run {
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = benefitPackageAdapter
        }
    }

    private fun setBackgroundColor() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                it.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun onSwipeRefreshBenefitPackage() {
        binding?.benefitPackageSwipeRefresh?.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        benefitPackageAdapter.clearAllElements()
        showLoading()
        benefitPackageViewModel.getBenefitPackage()
    }

    private fun showLoading() {
        benefitPackageAdapter.showLoading()
        binding?.benefitPackageSwipeRefresh?.isRefreshing = false
    }

    private fun hideLoading() {
        benefitPackageAdapter.hideLoading()
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.benefitPackageToolbar)
            supportActionBar?.apply {
                title = getString(R.string.pm_benefit_package_title_activity)
            }
        }
    }


    /**
     * BenefitPackageDataListener
     */
    override fun onLearnMoreToShopScoreClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalMarketplace.SHOP_PERFORMANCE)
        }
    }

    /**
     * BenefitPackageErrorListener
     */
    override fun onErrorActionClicked() {
        loadData()
    }

    companion object {
        @JvmStatic
        fun newInstance(): BenefitPackageFragment {
            return BenefitPackageFragment()
        }
    }
}