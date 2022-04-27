package com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.viewholder

import android.text.InputFilter
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutShopDiscountManageProductVariantItemBinding
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageProductVariantDiscountFragmentListener
import com.tokopedia.shopdiscount.set_period.data.uimodel.SetPeriodResultUiModel
import com.tokopedia.shopdiscount.set_period.presentation.bottomsheet.SetPeriodBottomSheet
import com.tokopedia.shopdiscount.utils.formatter.RangeFormatterUtil
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.ShopDiscountManageProductDiscountErrorValidation
import com.tokopedia.shopdiscount.utils.formatter.NumberFormatter
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.thousandFormattedWithoutCurrency
import com.tokopedia.shopdiscount.utils.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.round

class ShopDiscountManageProductVariantItemViewHolder(
    itemView: View,
    private val listener: Listener,
    private val fragmentListener: ShopDiscountManageProductVariantDiscountFragmentListener
) : AbstractViewHolder<ShopDiscountManageProductVariantItemUiModel>(itemView) {
    private var viewBinding: LayoutShopDiscountManageProductVariantItemBinding? by viewBinding()
    private var toggleEnableVariant: SwitchUnify? = null
    private var divider: View? = null
    private var layoutFieldContainer: View? = null
    private var textVariantName: Typography? = null
    private var textVariantOriginalPrice: Typography? = null
    private var textTotalStock: Typography? = null
    private var discountPeriodSection: View? = null
    private var textDiscountPeriodRange: Typography? = null
    private var textFieldDiscountPrice: TextFieldUnify2? = null
    private var textFieldDiscountPercentage: TextFieldUnify2? = null
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
            divider = viewBinding?.divider
            layoutFieldContainer = viewBinding?.layoutFieldContainer?.fieldContainer
            it.layoutFieldContainer.let { layoutFieldContainer ->
                discountPeriodSection = layoutFieldContainer.discountPeriodSection
                textDiscountPeriodRange = layoutFieldContainer.textDiscountPeriodRange
                textFieldDiscountPrice = layoutFieldContainer.textFieldPrice
                textFieldDiscountPercentage = layoutFieldContainer.textFieldDiscount
                quantityEditorMaxOrder = layoutFieldContainer.quantityEditorMaxOrder
            }
        }
    }

    override fun bind(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        setVariantInfoData(uiModel)
        setVariantToggle(uiModel)
        setVariantFieldData(uiModel)
        checkShouldValidateInputAfterPopulateData(uiModel)
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
    }

    private fun updateDiscountPeriodRangeText(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        textDiscountPeriodRange?.text = getFormattedDiscountPeriod(uiModel)
    }

    private fun openSetPeriodBottomSheet(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        val bottomSheet = SetPeriodBottomSheet.newInstance(
            uiModel.startDate.time,
            uiModel.endDate.time,
            uiModel.slashPriceStatusId
        )
        bottomSheet.setOnApplyClickListener {
            setDiscountPeriodBasedOnBottomSheetResult(it, uiModel)
        }
        bottomSheet.show(fragmentListener.getFragmentChildManager(), bottomSheet.tag)
    }

    private fun setDiscountPeriodBasedOnBottomSheetResult(
        setPeriodResultUiModel: SetPeriodResultUiModel,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        updateDiscountPeriodData(uiModel, setPeriodResultUiModel)
        updateDiscountPeriodRangeText(uiModel)
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

    private fun checkShouldValidateInputAfterPopulateData(uiModel: ShopDiscountManageProductVariantItemUiModel) {
        val initialDiscountPrice = uiModel.discountedPrice
        if (initialDiscountPrice.isMoreThanZero()) {
            validateInput(uiModel)
        }
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
            appendText(getString(R.string.sd_percent))
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
            val errorValidation = when {
                discountedPrice > maxDiscountPrice -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MAX
                }
                discountedPrice < minDiscountPrice -> {
                    ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MIN
                }
                else -> {
                    ShopDiscountManageProductDiscountErrorValidation.NONE
                }
            }
            when (errorValidation) {
                ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MAX -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        "Maks. ${getMaxDiscountPrice(uiModel).getCurrencyFormatted()}"
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ShopDiscountManageProductDiscountErrorValidation.ERROR_PRICE_MIN -> {
                    textFieldDiscountPrice?.textInputLayout?.error =
                        "Min ${getMinDiscountPrice(uiModel).getCurrencyFormatted()}"
                    textFieldDiscountPercentage?.textInputLayout?.error = " "
                }
                ShopDiscountManageProductDiscountErrorValidation.NONE -> {
                    textFieldDiscountPrice?.textInputLayout?.error = null
                    textFieldDiscountPercentage?.textInputLayout?.error = null
                }
            }
            updateErrorTypeData(errorValidation, uiModel)
        }
    }

    private fun updateErrorTypeData(
        errorType: Int,
        uiModel: ShopDiscountManageProductVariantItemUiModel
    ) {
        uiModel.errorType = errorType
    }

    private fun getMinDiscountPrice(uiModel: ShopDiscountManageProductVariantItemUiModel): Int {
        val originalPrice = uiModel.variantMinOriginalPrice.orZero()
        return (originalPrice.toDouble() * 0.01).toInt()
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
            maxOriginalPrice, {
                it.getCurrencyFormatted()
            }, { min, max ->
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
        uiModel: ShopDiscountManageProductVariantItemUiModel,
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