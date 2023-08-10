package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentQuotaMonitoringBinding
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListContainerFragment
import com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.adapter.QuotaMonitoringAdapter
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class QuotaMonitoringFragment : BaseDaggerFragment() {

    companion object {
        private const val FLASH_SALE_TOKO_PROMOTION_PACKAGE_ARTICLE_URL =
        "https://seller.tokopedia.com/edu/flash-sale-toko-paket-promosi/"

        @JvmStatic
        fun newInstance() = QuotaMonitoringFragment()
    }

    private var binding by autoClearedNullable<SsfsFragmentQuotaMonitoringBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(QuotaMonitoringViewModel::class.java) }

    private val quotaMonitoringAdapter by lazy {
        QuotaMonitoringAdapter()
    }

    override fun getScreenName(): String =
        CampaignListContainerFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentQuotaMonitoringBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        observeVpsPackages()
        viewModel.getVpsPackages()
    }

    private fun observeVpsPackages() {
        viewModel.vpsPackages.observe(viewLifecycleOwner) { results ->
            when (results) {
                is Success -> {
                    displayPackages(results.data)
                }
                is Fail -> {
                    binding?.btnLearnQuota showError results.throwable
                }
            }
        }
    }

    private fun displayPackages(vpsPackage: List<VpsPackage>) {
        val packageAvailability = viewModel.getPackageAvailability(vpsPackage)
        val remainingQuota = packageAvailability.remainingQuota
        val totalQuota = packageAvailability.totalQuota
        binding?.apply {
            header.apply {
                navigationIcon = getIconUnifyDrawable(context, IconUnify.CLOSE)
                setNavigationOnClickListener {
                    activity?.finish()
                }
            }
            cardQuotaMonitoring.cardType = CardUnify2.TYPE_BORDER
            tpRemainingQuota.text = getString(
                R.string.ssfs_remaining_quota_value_placeholder,
                remainingQuota,
                totalQuota
            )
            rvQuota.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            quotaMonitoringAdapter.clearAll()
            quotaMonitoringAdapter.submit(vpsPackage)
            rvQuota.adapter = quotaMonitoringAdapter
            btnLearnQuota.setOnClickListener {
                routeToPromotionPackageArticle()
            }
        }
    }

    private fun routeToPromotionPackageArticle() {
        if (!isAdded) return
        val encodedUrl = FLASH_SALE_TOKO_PROMOTION_PACKAGE_ARTICLE_URL.encodeToUtf8()
        val route = String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
        RouteManager.route(activity ?: return, route)
    }
}
