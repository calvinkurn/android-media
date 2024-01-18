package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BERANDA_CREATION_SHEET
import com.tokopedia.topads.dashboard.databinding.AutoPsBerandaCreationModalLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.topads.dashboard.R as topadsdashboardR

class TopadsBerandaCreationBottomSheet : BottomSheetUnify() {

    private var binding: AutoPsBerandaCreationModalLayoutBinding? = null
    private var autoAdsData: AutoAdsResponse.TopAdsGetAutoAds.Data? = null
    private var productCount: Int = Int.ZERO
    private var headlineCount: Int = Int.ZERO
    private val ROTATION_180 = 180

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout(inflater: LayoutInflater, container: ViewGroup?) {
        binding = AutoPsBerandaCreationModalLayoutBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(topadsdashboardR.string.topads_auto_ps_beranda_modal_title))
        showCloseIcon = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setClicks()
    }

    private fun initView() {
        autoAdsData?.let {
            if (it.status == AutoAdsStatus.STATUS_INACTIVE) {
                binding?.autoPsDisabledState?.show()
                binding?.autoPsStateEnabled?.root?.gone()
                setManualProductsView()
                setManualShopView()
            } else {
                binding?.autoPsDisabledState?.gone()
                binding?.autoPsStateEnabled?.root?.show()
                setAutoPsEnabledView(it)
            }
        }
    }

    private fun setAutoPsEnabledView(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        binding?.autoPsStateEnabled?.desc?.text = when (data.status) {
            AutoAdsStatus.STATUS_ACTIVE -> {
                getAutoPsSubtitle(
                    getString(topadsdashboardR.string.topads_active),
                    data.dailyBudget.toLong()
                )
            }

            AutoAdsStatus.STATUS_NOT_DELIVERED -> {
                getAutoPsSubtitle(
                    getString(topadsdashboardR.string.topads_not_delivered),
                    data.dailyBudget.toLong()
                )
            }

            AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE,
            AutoAdsStatus.STATUS_IN_PROGRESS_AUTOMANAGE,
            AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE -> {
                getAutoPsSubtitle(
                    getString(topadsdashboardR.string.topads_in_progress),
                    data.dailyBudget.toLong()
                )
            }

            else -> String.EMPTY
        }
    }

    private fun setManualProductsView() {
        if (productCount > 0) {
            binding?.createProduct?.productCount?.text = HtmlCompat.fromHtml(
                String.format(
                    getString(topadsdashboardR.string.topads_product_advertised_count),
                    productCount
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding?.createProduct?.accordianListTitle?.gone()
            binding?.createProduct?.accordianIcon?.gone()
            binding?.createProduct?.submitBtn?.buttonVariant = UnifyButton.Variant.GHOST
            binding?.createProduct?.submitBtn?.text = getString(topadsdashboardR.string.topads_auto_ps_add_ad_group)
        } else {
            binding?.createProduct?.productCount?.text =
                getString(topadsdashboardR.string.topads_doesnt_have_product_advertisements_yet)
            binding?.createProduct?.accordianListTitle?.show()
            binding?.createProduct?.accordianIcon?.show()
            binding?.createProduct?.accordianListTitle?.setOnClickListener {
                expandAccordian(
                    binding?.createProduct?.accordianGroup,
                    binding?.createProduct?.accordianIcon
                )
            }
            binding?.createProduct?.accordianIcon?.setOnClickListener {
                expandAccordian(
                    binding?.createProduct?.accordianGroup,
                    binding?.createProduct?.accordianIcon
                )
            }
            binding?.createProduct?.submitBtn?.buttonVariant = UnifyButton.Variant.FILLED
            binding?.createProduct?.submitBtn?.text = getString(topadsdashboardR.string.topads_dashboard_create_produk_advertisement)
        }
    }

    private fun setManualShopView() {
        if (headlineCount > 0) {
            binding?.createShopAd?.productCount?.text = HtmlCompat.fromHtml(
                String.format(
                    getString(topadsdashboardR.string.topads_product_advertised_count),
                    headlineCount
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding?.createShopAd?.accordianListTitle?.gone()
            binding?.createShopAd?.accordianIcon?.gone()
            binding?.createShopAd?.submitBtn?.buttonVariant = UnifyButton.Variant.GHOST
            binding?.createShopAd?.submitBtn?.text = getString(topadsdashboardR.string.topads_auto_ps_add_ad_group)
        } else {
            binding?.createShopAd?.productCount?.text =
                getString(topadsdashboardR.string.topads_doesnt_have_shop_advertisements_yet)
            binding?.createShopAd?.accordianListTitle?.show()
            binding?.createShopAd?.accordianIcon?.show()
            binding?.createShopAd?.accordianListTitle?.setOnClickListener {
                expandAccordian(
                    binding?.createShopAd?.accordianGroup,
                    binding?.createShopAd?.accordianIcon
                )
            }
            binding?.createShopAd?.accordianIcon?.setOnClickListener {
                expandAccordian(
                    binding?.createShopAd?.accordianGroup,
                    binding?.createShopAd?.accordianIcon
                )
            }
            binding?.createShopAd?.submitBtn?.buttonVariant = UnifyButton.Variant.FILLED
            binding?.createShopAd?.submitBtn?.text = getString(topadsdashboardR.string.topads_dashboard_create_shop_advertisement)
        }
    }

    private fun expandAccordian(accordianGroup: Group?, accordianIcon: ImageUnify?) {
        accordianGroup?.let {
            it.showWithCondition(!it.isVisible)
        }
        accordianIcon?.let {
            it.rotation = it.rotation + ROTATION_180
        }
    }

    private fun getAutoPsSubtitle(status: String, dailyBudget: Long): String {
        return String.format(
            getString(topadsdashboardR.string.topads_beranda_model_autops_subtitle_format), status,
            Utils.convertToCurrency(dailyBudget)
        )
    }

    private fun setClicks() {
        binding?.autoPsStateEnabled?.cta?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_EDIT_AUTOADS
            )
            startActivity(intent)
        }

        binding?.createProduct?.submitBtn?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE_MANUAL_ADS
            )
            startActivity(intent)
        }

        binding?.createShopAd?.submitBtn?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION
            )
            startActivity(intent)
        }

        binding?.enableAutoPs?.autoPsCta?.setOnClickListener {
            val intent = RouteManager.getIntent(
                context,
                ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE
            )
            startActivity(intent)
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        autoAdsData: AutoAdsResponse.TopAdsGetAutoAds.Data?,
        productCount: Int,
        headlineCount: Int
    ) {
        this.autoAdsData = autoAdsData
        this.productCount = productCount
        this.headlineCount = headlineCount
        show(fragmentManager, BERANDA_CREATION_SHEET)
    }
}
