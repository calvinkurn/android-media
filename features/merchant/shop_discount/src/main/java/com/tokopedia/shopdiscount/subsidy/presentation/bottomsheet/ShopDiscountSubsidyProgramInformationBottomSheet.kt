package com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getPercentFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountSubsidyProgramInformationBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProgramInformationDetailUiModel
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.math.min

class ShopDiscountSubsidyProgramInformationBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var tracker: ShopDiscountTracker

    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountSubsidyProgramInformationBinding>()
    private val textSubsidyInfo: Typography?
        get() = viewBinding?.textSubsidyStatus
    private val priceInfoSection: View?
        get() = viewBinding?.priceInfoSection
    private val originalPriceRow: View?
        get() = viewBinding?.originalPriceRow
    private val textOriginalPriceValue: Typography?
        get() = viewBinding?.textOriginalPriceValue
    private val sellerDiscountRow: View?
        get() = viewBinding?.sellerDiscountRow
    private val textSellerDiscountValue: Typography?
        get() = viewBinding?.textSellerDiscountValue
    private val sellerDiscountPercentageRow: View?
        get() = viewBinding?.sellerDiscountPercentageRow
    private val textSellerDiscountPercentageValue: Typography?
        get() = viewBinding?.textSellerDiscountPercentageValue
    private val tokopediaSubsidyRow: View?
        get() = viewBinding?.tokopediaSubsidyRow
    private val textTokopediaSubsidyValue: Typography?
        get() = viewBinding?.textTokopediaSubsidyValue
    private val tokopediaSubsidyPercentageRow: View?
        get() = viewBinding?.tokopediaSubsidyPercentageRow
    private val textTokopediaSubsidyPercentageValue: Typography?
        get() = viewBinding?.textTokopediaSubsidyPercentageValue
    private val finalPriceRow: View?
        get() = viewBinding?.finalPriceRow
    private val textFinalPriceValue: Typography?
        get() = viewBinding?.textFinalPriceValue
    private val finalPricePercentageRow: View?
        get() = viewBinding?.finalPricePercentageRow
    private val textFinalPricePercentageValue: Typography?
        get() = viewBinding?.textFinalPricePercentageValue
    private val stockInfoSection: View?
        get() = viewBinding?.stockInfoSection
    private val mainStockRow: View?
        get() = viewBinding?.mainStockRow
    private val textMainStockValue: Typography?
        get() = viewBinding?.textMainStockValue
    private val subsidyStockRow: View?
        get() = viewBinding?.subsidyStockRow
    private val textSubsidyStockValue: Typography?
        get() = viewBinding?.textSubsidyStockValue
    private val remainingSubsidyStockRow: View?
        get() = viewBinding?.remainingSubsidyStockRow
    private val textRemainingSubsidyStockValue: Typography?
        get() = viewBinding?.textRemainingSubsidyStockValue
    private val maxOrderInfoSection: View?
        get() = viewBinding?.maxOrderInfoSection
    private val maxOrderRow: View?
        get() = viewBinding?.maxOrderRow
    private val textMaxOrderValue: Typography?
        get() = viewBinding?.textMaxOrderValue
    private val subsidyPeriodSection: View?
        get() = viewBinding?.subsidyPeriodSection
    private val textSubsidyPeriodValue: Typography?
        get() = viewBinding?.textSubsidyPeriodValue
    private var programInformationDetailUiModel: ShopDiscountProgramInformationDetailUiModel? = null

    companion object {
        private const val PROGRAM_INFORMATION_DETAIL = "programInformationDetail"
        fun newInstance(
            programInformationDetailUiModel: ShopDiscountProgramInformationDetailUiModel?
        ): ShopDiscountSubsidyProgramInformationBottomSheet {
            return ShopDiscountSubsidyProgramInformationBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(PROGRAM_INFORMATION_DETAIL, programInformationDetailUiModel)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        getArgumentData()
    }

    private fun getArgumentData() {
        arguments?.let {
            programInformationDetailUiModel = it.getParcelable(PROGRAM_INFORMATION_DETAIL)
        }
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
        setContentData()
        sendImpressionSubsidyProgramInformationBottomSheetTracker()
    }

    private fun sendImpressionSubsidyProgramInformationBottomSheetTracker() {
        tracker.sendImpressionSubsidyProgramInformationBottomSheetEvent(
            programInformationDetailUiModel?.isBottomSheet.orFalse(),
            programInformationDetailUiModel?.productId.orEmpty()
        )
    }

    private fun setContentData() {
        setDescriptionSection()
        setDetailSection()
    }

    private fun setDetailSection() {
        val isVariant = programInformationDetailUiModel?.isVariant.orFalse()
        if (!isVariant) {
            setPriceInfoSection()
            setStockInfoSection()
            setMaxOrderInfoSection()
            setSubsidyPeriodSection()
        }
    }

    private fun setSubsidyPeriodSection() {
        val subsidyPeriodStart =
            programInformationDetailUiModel?.subsidyInfo?.subsidyDateStart.orEmpty()
        val subsidyPeriodEnd =
            programInformationDetailUiModel?.subsidyInfo?.subsidyDateEnd.orEmpty()
        if (subsidyPeriodStart.isNotEmpty() && subsidyPeriodEnd.isNotEmpty()) {
            subsidyPeriodSection?.show()
            textSubsidyPeriodValue?.text = getString(
                R.string.sd_subsidy_period_format,
                subsidyPeriodStart,
                subsidyPeriodEnd
            )
        } else {
            subsidyPeriodSection?.hide()
        }
    }

    private fun setMaxOrderInfoSection() {
        val maxOrderSubsidy = programInformationDetailUiModel?.subsidyInfo?.maxOrder.orZero()
        val maxOrderProduct = programInformationDetailUiModel?.maxOrder.orZero()
        val finalMaxOrder = min(maxOrderSubsidy, maxOrderProduct)
        if (finalMaxOrder > Int.ZERO) {
            maxOrderInfoSection?.show()
            textMaxOrderValue?.text = finalMaxOrder.toString()
        } else {
            maxOrderInfoSection?.hide()
        }
    }

    private fun setStockInfoSection() {
        stockInfoSection?.show()
        setMainStockData()
        setSubsidyStockData()
        setRemainingSubsidyStockData()
    }

    private fun setRemainingSubsidyStockData() {
        val remainingSubsidyStock =
            programInformationDetailUiModel?.subsidyInfo?.remainingQuota.orZero()
        if (remainingSubsidyStock > Int.ZERO) {
            remainingSubsidyStockRow?.show()
            textRemainingSubsidyStockValue?.text = remainingSubsidyStock.toString()
        } else {
            remainingSubsidyStockRow?.hide()
        }
    }

    private fun setSubsidyStockData() {
        val subsidyStock = programInformationDetailUiModel?.subsidyInfo?.quotaSubsidy.orZero()
        if (subsidyStock > Int.ZERO) {
            subsidyStockRow?.show()
            textSubsidyStockValue?.text = subsidyStock.toString()
        } else {
            subsidyStockRow?.hide()
        }
    }

    private fun setMainStockData() {
        val mainStock = programInformationDetailUiModel?.mainStock.orEmpty()
        if (mainStock.isNotEmpty()) {
            mainStockRow?.show()
            textMainStockValue?.text = mainStock
        } else {
            mainStockRow?.hide()
        }
    }

    private fun setPriceInfoSection() {
        priceInfoSection?.show()
        setOriginalPriceData()
        setSellerDiscountData()
        setSellerDiscountPercentageData()
        setTokopediaSubsidyData()
        setTokopediaSubsidyPercentageData()
        setFinalPriceData()
        setFinalPricePercentageData()
    }

    private fun setFinalPricePercentageData() {
        val finalPricePercentage =
            programInformationDetailUiModel?.formattedFinalDiscountedPercentage.orEmpty()
        if (finalPricePercentage.isNotEmpty()) {
            finalPricePercentageRow?.show()
            textFinalPricePercentageValue?.text = getString(
                R.string.sd_subsidy_discount_percentage_format,
                finalPricePercentage
            )
        } else {
            finalPricePercentageRow?.hide()
        }
    }

    private fun setFinalPriceData() {
        val finalPrice = programInformationDetailUiModel?.formattedFinalDiscountedPrice.orEmpty()
        if (finalPrice.isNotEmpty()) {
            finalPriceRow?.show()
            textFinalPriceValue?.text = finalPrice
        } else {
            finalPriceRow?.hide()
        }
    }

    private fun setTokopediaSubsidyPercentageData() {
        val tokopediaSubsidyPercentage =
            programInformationDetailUiModel?.subsidyInfo?.discountedPercentage.orZero()
        if (tokopediaSubsidyPercentage > Int.ZERO) {
            tokopediaSubsidyPercentageRow?.show()
            textTokopediaSubsidyPercentageValue?.text = getString(
                R.string.sd_subsidy_discount_percentage_format,
                tokopediaSubsidyPercentage.getPercentFormatted()
            )
        } else {
            tokopediaSubsidyPercentageRow?.hide()
        }
    }

    private fun setTokopediaSubsidyData() {
        val tokopediaSubsidy =
            programInformationDetailUiModel?.subsidyInfo?.discountedPrice?.getCurrencyFormatted()
                .orEmpty()
        if (tokopediaSubsidy.isNotEmpty()) {
            tokopediaSubsidyRow?.show()
            textTokopediaSubsidyValue?.text = getString(
                R.string.sd_subsidy_minus_price_format,
                tokopediaSubsidy
            )
        } else {
            tokopediaSubsidyRow?.hide()
        }
    }

    private fun setSellerDiscountPercentageData() {
        val sellerDiscountPercentage =
            programInformationDetailUiModel?.subsidyInfo?.sellerDiscountPercentage.orZero()
        if (sellerDiscountPercentage > Int.ZERO) {
            sellerDiscountPercentageRow?.show()
            textSellerDiscountPercentageValue?.text = getString(
                R.string.sd_subsidy_discount_percentage_format,
                sellerDiscountPercentage.getPercentFormatted()
            )
        } else {
            sellerDiscountPercentageRow?.hide()
        }
    }

    private fun setSellerDiscountData() {
        val sellerDiscount =
            programInformationDetailUiModel?.subsidyInfo?.sellerDiscountPrice.orZero()
        if (sellerDiscount > Int.ZERO) {
            sellerDiscountRow?.show()
            textSellerDiscountValue?.text = getString(
                R.string.sd_subsidy_minus_price_format,
                sellerDiscount.getCurrencyFormatted()
            )
        } else {
            sellerDiscountRow?.hide()
        }
    }

    private fun setOriginalPriceData() {
        val originalPrice = programInformationDetailUiModel?.formattedOriginalPrice.orEmpty()
        if (originalPrice.isNotEmpty()) {
            originalPriceRow?.show()
            textOriginalPriceValue?.text = originalPrice
        } else {
            originalPriceRow?.hide()
        }
    }

    private fun setDescriptionSection() {
        val isVariant = programInformationDetailUiModel?.isVariant.orFalse()
        val sellerEduUrl = programInformationDetailUiModel?.subsidyInfo?.ctaProgramLink.orEmpty()
        val textSubsidyProgramInfoDescription = if (isVariant) {
            getProgramInformationDescription(
                getString(R.string.sd_subsidy_program_information_description_variant),
                sellerEduUrl
            )
        } else {
            getProgramInformationDescription(
                getString(R.string.sd_subsidy_program_information_description_non_variant),
                sellerEduUrl
            )
        }
        textSubsidyInfo?.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = textSubsidyProgramInfoDescription?.spannedString
            textSubsidyProgramInfoDescription?.urlList?.firstOrNull()?.setOnClickListener {
                sendClickEduArticleTracker()
                redirectToWebView(textSubsidyProgramInfoDescription.urlList.firstOrNull()?.linkUrl.orEmpty())
            }
        }
    }

    private fun sendClickEduArticleTracker() {
        tracker.sendClickEduArticleEvent(
            programInformationDetailUiModel?.isBottomSheet.orFalse(),
            programInformationDetailUiModel?.productId.orEmpty()
        )
    }

    private fun getProgramInformationDescription(
        textDescription: String,
        sellerEduUrl: String
    ): HtmlLinkHelper? {
        return context?.let {
            HtmlLinkHelper(it, String.format(textDescription, sellerEduUrl))
        }
    }

    private fun redirectToWebView(linkUrl: CharSequence) {
        RouteManager.route(
            context,
            String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                linkUrl.toString()
            )
        )
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountSubsidyProgramInformationBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            setTitle(getString(R.string.sd_subsidy_program_information_bottomsheet_title))
            setChild(this.root)
            setCloseClickListener {
                dismiss()
            }
        }
    }

}
