package com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.viewholder

import android.text.InputFilter
import android.text.SpannableString
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutShopDiscountManageProductVariantItemBinding
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageVariantFragmentListener
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.set_period.presentation.bottomsheet.SetPeriodBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation
import com.tokopedia.shopdiscount.utils.constant.UrlConstant.SELLER_EDU_R2_ABUSIVE_URL
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.thousandFormattedWithoutCurrency
import com.tokopedia.shopdiscount.utils.formatter.NumberFormatter
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
import com.tokopedia.shopdiscount.utils.formatter.SpannableHelper
import com.tokopedia.shopdiscount.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.round

class ShopDiscountManageProductVariantItemViewHolder(
    itemView: View,
    private val listener: Listener,
    private val mode: String,
    private val fragmentListener: ShopDiscountManageVariantFragmentListener
) : AbstractViewHolder<ShopDiscountManageProductVariantItemUiModel>(itemView) {
    private var viewBinding: LayoutShopDiscountManageProductVariantItemBinding? by viewBinding()
    private var toggleEnableVariant: SwitchUnify? = null
    private var textErrorAbusive: Typography? = null
    private var imageErrorAbusive: ImageUnify? = null
    private var divider: View? = null
    private var layoutFieldContainer: View? = null
    private var textVariantName: Typography? = null
    private var textVariantOriginalPrice: Typography? = null
    private var textTotalStock: Typography? = null
    private var discountPeriodSection: View? = null
    private var textDiscountPeriodRange: Typography? = null
    private var textDiscountPeriodError: Typography? = null
    private var textFieldDiscountPrice: TextFieldUnify2? = null
    private var textFieldDiscountPercentage: TextFieldUnify2? = null
    private var tickerR2AbusiveError: Ticker? = null
    private var quantityEditorMaxOrder: QuantityEditorUnify? = null
    private var textFieldDiscountPercentageWatcher: NumberThousandSeparatorTextWatcher? = null
    private var textFieldDiscountPriceWatcher: NumberThousandSeparatorTextWatcher? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_discount_manage_product_variant_item
        private const val DISCOUNT_PERCENTAGE_MAX_DIGIT = 2
    }

    interface Listener {
        fun checkShouldEnableButtonSubmit()
        fun checkShouldEnableBulkApplyVariant()
    }

    init {
        initView()
    }

    private fun initView() {
        viewBinding?.let {
            textVariantName = it.textVariantName
            textVariantOriginalPrice = it.textVariantOriginalPrice
            textTotalStock = it.textTotalVariantStock
            toggleEnableVariant = viewBinding?.switcherToggleVariant
            textErrorAbusive = viewBinding?.textErrorAbusive
            imageErrorAbusive = viewBinding?.imageErrorAbusive
            divider = viewBinding?.divider
            layoutFieldContainer = viewBinding?.layoutFieldContainer?.fieldContainer
            it.layoutFieldContainer.let { layoutFieldContainer ->
                discountPeriodSection = layoutFieldContainer.discountPeriodSection
                textDiscountPeriodError = layoutFieldContainer.textDiscountPeriodError
                textDiscountPeriodRange = layoutFieldContainer.textDiscountPeriodRange
                textFieldDiscountPrice = layoutFieldContainer.textFieldPrice
                textFieldDiscountPercentage = layoutFieldContainer.textFieldDiscount
                tickerR2AbusiveError = layoutFieldContainer.tickerR2AbusiveError
                quantityEditorMaxOrder = layoutFieldContainer.quantityEditorMaxOrder
            }
        }
    }

    override fun bind(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        setVariantInfoData(uiModel)
        setVariantToggle(uiModel)
        setVariantFieldData(uiModel)
        checkErrorState(uiModel)
        listener.checkShouldEnableButtonSubmit()
        listener.checkShouldEnableBulkApplyVariant()
    }

    private fun setVariantFieldData(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        setupDiscountPeriodSection(uiModel)
        setProductPriceData(uiModel)
        setupQuantityEditorMaxOrder(uiModel)
    }

    private fun setupDiscountPeriodSection(
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        discountPeriodSection?.setOnClickListener {
            openSetPeriodBottomSheet(uiModel)
        }
        updateDiscountPeriodRangeText(uiModel)
        checkStartDateError(uiModel)
    }

    private fun checkStartDateError(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        if (uiModel.productErrorType == ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.START_DATE_ERROR) {
            if (isStartDateError(uiModel)) {
                uiModel.productErrorType =
                    ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.START_DATE_ERROR
            } else {
                uiModel.productErrorType =
                    ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.NO_ERROR
            }
        }
    }

    private fun updateDiscountPeriodRangeText(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textDiscountPeriodRange?.text = getFormattedDiscountPeriod(uiModel)
    }

    private fun isStartDateError(
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ): Boolean {
        return checkProductStartDateError(uiModel)
    }

    private fun checkProductStartDateError(
        setupProductUiModel: ShopDiscountManageProductVariantItemUiModel
    ): Boolean {
        return (setupProductUiModel.startDate.time - Date().time) < TimeUnit.MINUTES.toMillis(
            5
        )
    }

    private fun openSetPeriodBottomSheet(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        val bottomSheet = SetPeriodBottomSheet.newInstance(
            uiModel.startDate.time,
            uiModel.endDate.time,
            uiModel.slashPriceStatusId,
            uiModel.selectedPeriodChip,
            mode
        )
        bottomSheet.setOnApplyClickListener { setPeriodResultModel, selectedPeriodChip ->
            setSelectedPeriodChip(selectedPeriodChip, uiModel)
            setDiscountPeriodBasedOnBottomSheetResult(
                setPeriodResultModel,
                uiModel
            )
        }
        bottomSheet.show(fragmentListener.getFragmentChildManager(), bottomSheet.tag)
    }

    private fun setSelectedPeriodChip(
        selectedPeriodChip: Int,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        uiModel.selectedPeriodChip = selectedPeriodChip
    }

    private fun setDiscountPeriodBasedOnBottomSheetResult(
        setPeriodResultUiModel: SetPeriodResultUiModel,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        updateDiscountPeriodData(uiModel, setPeriodResultUiModel)
        updateDiscountPeriodRangeText(uiModel)
        checkStartDateError(uiModel)
        checkErrorState(uiModel)
        listener.checkShouldEnableButtonSubmit()
    }

    private fun updateDiscountPeriodData(
        uiModel: ShopDiscountManageProductVariantItemUiModel,
        periodResultUiModel: SetPeriodResultUiModel
    ) {
        val startDate = periodResultUiModel.startDate
        val endDate = periodResultUiModel.endDate
        uiModel.startDate = startDate
        uiModel.endDate = endDate
    }

    private fun setProductPriceData(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        setupTextFieldDiscountPrice(uiModel)
        setupTextFieldDiscountPercentage(uiModel)
        updateTextFieldDiscountPriceText(uiModel)
        updateTextFieldDiscountPercentageText(uiModel)
    }

    private fun setupQuantityEditorMaxOrder(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        quantityEditorMaxOrder?.apply {
            maxValue = uiModel.variantStock
            setValue(uiModel.maxOrder.toIntOrZero())
            setValueChangedListener { newValue, _, _ ->
                uiModel.maxOrder = newValue.toString()
            }
        }
    }

    private fun checkErrorState(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        val initialDiscountPrice = uiModel.discountedPrice
        when {
            uiModel.isAbusive -> {
                showAbusiveError()
            }
            initialDiscountPrice.isMoreThanZero() -> {
                hideAbusiveError()
                validateInput(uiModel)
            }
            else -> {
                hideAbusiveError()
            }
        }
    }

    private fun hideAbusiveError() {
        toggleEnableVariant?.isEnabled = true
        textErrorAbusive?.hide()
        imageErrorAbusive?.hide()
    }

    private fun showAbusiveError() {
        toggleEnableVariant?.isEnabled = false
        textErrorAbusive?.show()
        imageErrorAbusive?.show()
    }

    private fun updateTextFieldDiscountPercentageText(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textFieldDiscountPercentage?.editText?.apply {
            removeTextChangedListener(textFieldDiscountPercentageWatcher)
            var discountPercentage = uiModel.discountedPercentage
            discountPercentage = when {
                discountPercentage < 0 -> {
                    0
                }
                discountPercentage > 99 -> {
                    99
                }
                else -> {
                    discountPercentage
                }
            }
            setText(discountPercentage.toString())
            addTextChangedListener(textFieldDiscountPercentageWatcher)
        }
    }

    private fun updateTextFieldDiscountPriceText(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textFieldDiscountPrice?.editText?.apply {
            removeTextChangedListener(textFieldDiscountPriceWatcher)
            setText(uiModel.discountedPrice.thousandFormattedWithoutCurrency())
            addTextChangedListener(textFieldDiscountPriceWatcher)
        }
    }

    private fun setupTextFieldDiscountPercentage(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textFieldDiscountPercentage?.apply {
            appendText("%")
            textInputLayout.errorIconDrawable = null
            textInputLayout.editText?.let {
                it.removeTextChangedListener(textFieldDiscountPriceWatcher)
                it.filters = arrayOf(
                    InputFilter.LengthFilter(
                        DISCOUNT_PERCENTAGE_MAX_DIGIT
                    )
                )
                textFieldDiscountPercentageWatcher = NumberThousandSeparatorTextWatcher(
                    it,
                    NumberFormatter.decimalFormatterThousand
                ) { discountPercent, formattedNumber ->
                    it.setText(formattedNumber)
                    it.setSelection(it.text?.length.orZero())
                    updateDiscountPercent(discountPercent, uiModel)
                    validateInput(uiModel)
                    listener.checkShouldEnableButtonSubmit()
                }
                it.addTextChangedListener(textFieldDiscountPercentageWatcher)
            }
        }
    }

    private fun updateDiscountPercent(
        discountPercent: Int,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        val originalPrice = uiModel.variantMinOriginalPrice
        val discountPrice = ((100 - discountPercent).toDouble() / 100 * originalPrice).toInt()
        uiModel.discountedPrice = discountPrice
        uiModel.discountedPercentage = discountPercent
        updatePriceData(uiModel, discountPrice, discountPercent)
        updateTextFieldDiscountPriceText(uiModel)
    }

    private fun setupTextFieldDiscountPrice(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textFieldDiscountPrice?.apply {
            textInputLayout.errorIconDrawable = null
            textInputLayout.editText?.let {
                it.removeTextChangedListener(
                    textFieldDiscountPercentageWatcher
                )
                textFieldDiscountPriceWatcher = NumberThousandSeparatorTextWatcher(
                    it,
                    NumberFormatter.decimalFormatterThousand
                ) { price, formattedNumber ->
                    it.setText(formattedNumber)
                    it.setSelection(it.text?.length.orZero())
                    updateDiscountPrice(price, uiModel)
                    validateInput(uiModel)
                    listener.checkShouldEnableButtonSubmit()
                }
                it.addTextChangedListener(textFieldDiscountPriceWatcher)
            }
        }
    }

    private fun updateDiscountPrice(
        discountPrice: Int,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        val originalPrice = uiModel.variantMinOriginalPrice
        val discountedPrice = (originalPrice - discountPrice).toDouble()
        val discountPercentage = round((discountedPrice) / originalPrice * 100f).toInt()
        updatePriceData(uiModel, discountPrice, discountPercentage)
        updateTextFieldDiscountPercentageText(uiModel)
    }

    private fun updatePriceData(
        uiModel: ShopDiscountManageProductVariantItemUiModel,
        discountPrice: Int,
        discountPercentage: Int
    ) {
        uiModel.discountedPrice = discountPrice
        uiModel.discountedPercentage = discountPercentage
    }

    private fun validateInput(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        uiModel.let {
            val minDiscountPrice = getMinDiscountPrice(uiModel)
            val maxDiscountPrice = getMaxDiscountPrice(uiModel)
            val discountedPrice = it.discountedPrice
            val averageSoldPrice = it.averageSoldPrice
            val errorValidation = when {
                discountedPrice > maxDiscountPrice -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MAX
                }
                discountedPrice < minDiscountPrice -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MIN
                }
                discountedPrice > averageSoldPrice && averageSoldPrice.isMoreThanZero() -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_R2_ABUSIVE
                }
                uiModel.productErrorType == ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.START_DATE_ERROR -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_START_DATE
                }
                else -> {
                    ShopDiscountManageProductDiscountErrorValidation.NONE
                }
            }
            tickerR2AbusiveError?.hide()
            textFieldDiscountPrice?.textInputLayout?.setOnClickListener(null)
            textFieldDiscountPrice?.textInputLayout?.error = null
            textFieldDiscountPercentage?.textInputLayout?.error = null
            textDiscountPeriodError?.hide()
            when (errorValidation) {
                ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MAX -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        String.format(
                            getString(R.string.shop_discount_manage_product_error_max_price_format),
                            getMaxDiscountPrice(uiModel).getCurrencyFormatted()
                        )
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MIN -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        String.format(
                            getString(R.string.shop_discount_manage_product_error_min_price_format),
                            getMinDiscountPrice(uiModel).getCurrencyFormatted()
                        )
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ShopDiscountManageProductDiscountErrorValidation.ERROR_R2_ABUSIVE -> {
                    textFieldDiscountPrice?.textInputLayout?.error = getR2AbusiveErrorMessage()
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                    textFieldDiscountPrice?.textInputLayout?.setOnClickListener {
                        redirectToWebViewR2AbusiveSellerInformation()
                    }
                    tickerR2AbusiveError?.apply {
                        show()
                        val tickerDesc = String.format(
                            getString(R.string.shop_discount_manage_product_error_r2_abusive_ticker_desc_format),
                            averageSoldPrice.getCurrencyFormatted()
                        )
                        setHtmlDescription(tickerDesc)
                        setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                applyRecommendedSoldPriceToDiscountedPrice(averageSoldPrice)
                            }

                            override fun onDismiss() {
                            }
                        })
                    }
                }
                ShopDiscountManageProductDiscountErrorValidation.ERROR_START_DATE -> {
                    textDiscountPeriodError?.show()
                }
            }
            updateValueErrorTypeData(errorValidation, uiModel)
        }
    }

    private fun getR2AbusiveErrorMessage(): SpannableString {
        val errorMessageFormat =
            getString(R.string.shop_discount_manage_product_error_r2_abusive_format)
        val errorStringToBeColorSpanned =
            getString(R.string.shop_discount_manage_product_error_r2_abusive_spanned_text)
        val errorMessage = SpannableString(
            String.format(
                errorMessageFormat,
                errorStringToBeColorSpanned
            )
        )
        SpannableHelper.setSpannedColorString(
            itemView.context,
            errorMessage,
            errorStringToBeColorSpanned,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        return errorMessage
    }

    private fun applyRecommendedSoldPriceToDiscountedPrice(averageSoldPrice: Int) {
        textFieldDiscountPriceWatcher?.isForceTextChanged = true
        textFieldDiscountPrice?.editText?.setText(averageSoldPrice.toString())
    }

    private fun updateValueErrorTypeData(
        errorType: Int,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        uiModel.valueErrorType = errorType
    }

    private fun redirectToWebViewR2AbusiveSellerInformation() {
        RouteManager.route(
            itemView.context,
            String.format(
                "%s?url=%s",
                ApplinkConst.WEBVIEW,
                SELLER_EDU_R2_ABUSIVE_URL
            )
        )
    }

    private fun getMinDiscountPrice(uiModel: ShopDiscountManageProductVariantItemUiModel): Int {
        val originalPrice = uiModel.variantMinOriginalPrice.orZero()
        val minDiscountPrice = (originalPrice.toDouble() * 0.01).toInt()
        return if (minDiscountPrice < 100) {
            100
        } else {
            minDiscountPrice
        }
    }

    private fun getMaxDiscountPrice(uiModel: ShopDiscountManageProductVariantItemUiModel): Int {
        val originalPrice = uiModel.variantMaxOriginalPrice.orZero()
        return (originalPrice.toDouble() * 0.99).toInt()
    }

    private fun setVariantInfoData(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textVariantName?.text = uiModel.variantName
        textVariantOriginalPrice?.text = getOriginalPriceFormatted(
            uiModel.variantMinOriginalPrice,
            uiModel.variantMaxOriginalPrice
        )
        textTotalStock?.text = MethodChecker.fromHtml(getTotalStockAndLocationData(uiModel))
    }

    private fun getTotalStockAndLocationData(
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ): String {
        val totalStock = uiModel.variantStock
        val totalLocation = uiModel.variantTotalLocation
        return when {
            uiModel.isMultiLoc -> {
                String.format(
                    getString(R.string.sd_total_stock_multiple_location_plain),
                    totalStock,
                    totalLocation
                )
            }
            else -> {
                String.format(
                    getString(R.string.sd_total_stock_plain),
                    totalStock
                )
            }
        }
    }

    private fun getOriginalPriceFormatted(
        minOriginalPrice: Int,
        maxOriginalPrice: Int
    ): String {
        return RangeFormatterUtil.getFormattedRangeString(
            minOriginalPrice,
            maxOriginalPrice,
            {
                it.getCurrencyFormatted()
            },
            { min, max ->
                String.format(
                    getString(R.string.shop_discount_manage_product_discount_original_price_format),
                    min.getCurrencyFormatted(),
                    max.getCurrencyFormatted()
                )
            }
        )
    }

    private fun setVariantToggle(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        toggleEnableVariant?.apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    divider?.show()
                    layoutFieldContainer?.show()
                } else {
                    divider?.hide()
                    layoutFieldContainer?.hide()
                }
                updateVariantToggleData(isChecked, uiModel)
                listener.checkShouldEnableButtonSubmit()
                listener.checkShouldEnableBulkApplyVariant()
            }
            isChecked = uiModel.isEnabled
        }
    }

    private fun updateVariantToggleData(
        checked: Boolean,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        uiModel.isEnabled = checked
    }

    private fun getFormattedDiscountPeriod(
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ): String {
        val startDate = uiModel.startDate
        val endDate = uiModel.endDate
        val startDateFormatted = startDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
        val endDateFormatted = endDate.parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
        return String.format(
            getString(R.string.product_detail_start_date_end_date_format),
            startDateFormatted,
            endDateFormatted
        )
    }
}
