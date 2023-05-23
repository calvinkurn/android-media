package com.tokopedia.shopdiscount.info.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountSellerInfoBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.info.data.uimodel.ShopDiscountSellerInfoUiModel
import com.tokopedia.shopdiscount.info.presentation.viewmodel.ShopDiscountSellerInfoBottomSheetViewModel
import com.tokopedia.shopdiscount.info.presentation.widget.ShopDiscountSellerInfoSectionView
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.unixToMs
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Date
import javax.inject.Inject

class ShopDiscountSellerInfoBottomSheet : BottomSheetUnify() {
    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountSellerInfoBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(
            ShopDiscountSellerInfoBottomSheetViewModel::class.java
        )
    }

    private var tickerInfo: Ticker? = null
    private var sourceQuotaSection: ShopDiscountSellerInfoSectionView? = null
    private var quotaLeftSection: ShopDiscountSellerInfoSectionView? = null
    private var expirySection: ShopDiscountSellerInfoSectionView? = null
    private var shimmeringLayout: View? = null
    private var globalErrorLayout: GlobalError? = null

    companion object {

        fun newInstance(): ShopDiscountSellerInfoBottomSheet {
            return ShopDiscountSellerInfoBottomSheet()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun observeLiveData() {
        observeSellerInfoBenefitLiveData()
        observeSlashPriceTickerData()
    }

    private fun observeSlashPriceTickerData() {
        viewModel.slashPriceTickerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val uiModel = it.data
                    if (uiModel.responseHeader.success) {
                        populateTickerData(uiModel.listTicker)
                    }
                }
                is Fail -> {
                }
            }
        }
    }

    private fun populateTickerData(listTicker: List<TickerData>) {
        context?.let {
            val tickerPagerAdapter = TickerPagerAdapter(it, listTicker)
            tickerPagerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    redirectLink(linkUrl)
                }
            })
            tickerInfo?.apply {
                show()
                addPagerView(tickerPagerAdapter, listTicker)
            }
        }
    }

    private fun redirectLink(linkUrl: CharSequence) {
        RouteManager.route(context, linkUrl.toString())
    }

    private fun observeSellerInfoBenefitLiveData() {
        viewModel.sellerInfoLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            when (it) {
                is Success -> {
                    if (!it.data.responseHeader.success) {
                        showErrorState(Throwable(""))
                    } else {
                        setSourceQuotaSection(it.data)
                        setQuotaLeftSection(it.data)
                        setExpirySection(it.data)
                    }
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        }
    }

    private fun setExpirySection(data: ShopDiscountSellerInfoUiModel) {
        expirySection?.apply {
            val title = getString(R.string.seller_info_bottom_sheet_expiry_title)
            val contentText: String
            val descriptionText: String
            if (data.isUseVps) {
                val vpsPackageData = getSellerVpsPackageData(data)
                val formattedExpiryDate = Date(
                    vpsPackageData?.expiredAtUnix.orZero().unixToMs()
                ).parseTo(DateUtil.DEFAULT_VIEW_FORMAT)
                contentText = String.format(
                    getString(R.string.seller_info_bottom_sheet_expiry_content_vps),
                    formattedExpiryDate
                )
                descriptionText = ""
            } else {
                contentText = getString(R.string.seller_info_bottom_sheet_expiry_content_non_vps)
                descriptionText =
                    getString(R.string.seller_info_bottom_sheet_expiry_description_non_vps)
            }
            setSectionData(
                title,
                contentText,
                descriptionText
            )
            hideDivider()
        }
    }

    private fun setQuotaLeftSection(data: ShopDiscountSellerInfoUiModel) {
        quotaLeftSection?.apply {
            val title = getString(R.string.seller_info_bottom_sheet_quota_left_title)
            val contentText: String
            val descriptionText: String
            if (data.isUseVps) {
                val vpsData = getSellerVpsPackageData(data)
                contentText = String.format(
                    getString(R.string.seller_info_bottom_sheet_quota_left_content_vps_format),
                    vpsData?.remainingQuota,
                    vpsData?.maxQuota
                )
                descriptionText =
                    getString(R.string.seller_info_bottom_sheet_quota_left_vps_description)
            } else {
                contentText =
                    getString(R.string.seller_info_bottom_sheet_quota_left_content_non_vps)
                descriptionText = ""
            }
            setSectionData(
                title,
                contentText,
                descriptionText
            )
        }
    }

    private fun setSourceQuotaSection(data: ShopDiscountSellerInfoUiModel) {
        sourceQuotaSection?.apply {
            val title = getString(R.string.seller_info_quota_source_quota_source_title)
            val sourceQuota = if (data.isUseVps) {
                getSellerVpsPackageData(data)
            } else {
                getSellerNonVpsPackageData(data)
            }
            val contentText = String.format(
                getString(R.string.seller_info_bottom_sheet_quota_source_content_format),
                sourceQuota?.packageName.orEmpty()
            )
            val descriptionText = if (data.listSlashPriceBenefitData.size > 1) {
                getString(R.string.seller_info_bottom_sheet_quota_source_description)
            } else {
                ""
            }
            setSectionData(
                title,
                contentText,
                descriptionText
            )
            setOptionData(data)
        }
    }

    private fun getSellerNonVpsPackageData(data: ShopDiscountSellerInfoUiModel): ShopDiscountSellerInfoUiModel.SlashPriceBenefitData? {
        return data.listSlashPriceBenefitData.firstOrNull {
            it.packageId.toIntOrZero() == -1
        }
    }

    private fun getSellerVpsPackageData(data: ShopDiscountSellerInfoUiModel): ShopDiscountSellerInfoUiModel.SlashPriceBenefitData? {
        return data.listSlashPriceBenefitData.firstOrNull {
            it.packageId.toIntOrZero() != -1
        }
    }

    private fun showErrorState(throwable: Throwable) {
        globalErrorLayout?.apply {
            errorSecondaryAction.hide()
            val message = ErrorHandler.getErrorMessage(context, throwable)
            errorTitle.text = message
            setActionClickListener {
                getShopDiscountSellerBenefitData()
            }
        }
        globalErrorLayout?.show()
    }

    private fun hideErrorState() {
        globalErrorLayout?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeLiveData()
        getSellerInfoTickerData()
        getShopDiscountSellerBenefitData()
    }

    private fun getSellerInfoTickerData() {
        viewModel.getTickerData()
    }

    private fun init() {
        tickerInfo = viewBinding?.tickerInfo
        sourceQuotaSection = viewBinding?.quotaSourceSection
        quotaLeftSection = viewBinding?.quotaLeftSection
        expirySection = viewBinding?.expirySection
        shimmeringLayout = viewBinding?.shimmeringLayout?.shimmeringContainer
        globalErrorLayout = viewBinding?.globalError
    }

    private fun getShopDiscountSellerBenefitData() {
        hideErrorState()
        showLoading()
        viewModel.getSellerInfoBenefitData()
    }

    private fun showLoading() {
        shimmeringLayout?.show()
    }

    private fun hideLoading() {
        shimmeringLayout?.hide()
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountSellerInfoBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            setTitle(getString(R.string.seller_info_bottom_sheet_title))
            setChild(this.root)
            setCloseClickListener {
                dismiss()
            }
        }
    }
}
