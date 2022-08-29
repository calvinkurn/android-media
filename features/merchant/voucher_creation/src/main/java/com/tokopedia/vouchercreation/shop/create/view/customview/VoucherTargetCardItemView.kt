package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.databinding.MvcVoucherTargetItemBinding
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherTargetCardType

class VoucherTargetCardItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        styleableResource: IntArray = R.styleable.VoucherTargetCardItemView
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, styleableResource) {

    companion object {
        const val TARGET_PUBLIC_TYPE = 0
        const val TARGET_PRIVATE_TYPE = 1
    }

    val binding: MvcVoucherTargetItemBinding = MvcVoucherTargetItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupLayout(binding)
    }

    private var voucherTargetCardType: VoucherTargetCardType = VoucherTargetCardType.PUBLIC

    private var voucherTargetType: Int = TARGET_PUBLIC_TYPE
        set(value) {
            field = value
            voucherTargetCardType =
                    when (value) {
                        TARGET_PUBLIC_TYPE -> {
                            VoucherTargetCardType.PUBLIC
                        }
                        TARGET_PRIVATE_TYPE -> {
                            VoucherTargetCardType.PRIVATE
                        }
                        else -> return
                    }
        }
    private var isItemEnabled: Boolean = false
    private var isHavePromoCode: Boolean = false
    private var promoCode: String = ""
    private var enablePromoCode: Boolean = true

    override fun setupAttributes() {
        attributes?.run {
            voucherTargetType = getInt(R.styleable.VoucherTargetCardItemView_targetType, voucherTargetType)
            isItemEnabled = getBoolean(R.styleable.VoucherTargetCardItemView_isEnabled, isItemEnabled)
            isHavePromoCode = getBoolean(R.styleable.VoucherTargetCardItemView_isHavePromoCode, isHavePromoCode)
            promoCode = getString(R.styleable.VoucherTargetCardItemView_promoCode).toBlankOrString()
        }
        setupView()
    }

    override fun setupView() {
        view?.run {
            setupIcon()
            setupTitle()
            setupDescription()
            setupItemEnabling()
            setupPromoCode()
        }
    }

    private fun View.setupIcon() {
        binding.voucherTargetItemIcon.setImageResource(voucherTargetCardType.iconDrawableRes)
    }

    private fun View.setupTitle() {
        binding.voucherTargetItemTitle.text = resources.getString(voucherTargetCardType.titleStringRes)
    }

    private fun View.setupDescription() {
        binding.voucherTargetItemDescription.text = resources.getText(voucherTargetCardType.descriptionStringRes)
    }

    private fun View.setupItemEnabling() {
        setupBorderColor()
        binding.voucherTargetItemRadioButton.isChecked = isItemEnabled
    }

    private fun View.setupBorderColor() {
        resources?.run {
            val borderColorRes = if (isItemEnabled) {
                com.tokopedia.unifyprinciples.R.color.Green_G400
            } else {
                com.tokopedia.unifyprinciples.R.color.Neutral_N75
            }
            (background as GradientDrawable).setStroke(
                    getDimension(R.dimen.mvc_create_target_card_border_width).toInt().orZero(),
                    ContextCompat.getColor(context, borderColorRes))
        }
    }

    private fun View.setupPromoCode() {
        binding.apply {
            if (isHavePromoCode) {
                voucherTargetPromoCodeInfo.run {
                    visibility = View.VISIBLE
                    isChangeEnabled = isItemEnabled && enablePromoCode
                    promoCodeString = promoCode.toBlankOrString()
                }
            } else {
                voucherTargetPromoCodeInfo.visibility = View.GONE
            }
        }
    }

    fun setupCurrentView(voucherTargetType: VoucherTargetCardType,
                         isItemEnabled: Boolean,
                         isHavePromoCode: Boolean,
                         promoCode: String,
                         enablePromoCode: Boolean) {
        this.voucherTargetCardType = voucherTargetType
        this.isItemEnabled = isItemEnabled
        this.isHavePromoCode = isHavePromoCode
        this.promoCode = promoCode
        this.enablePromoCode = enablePromoCode
        setupView()
    }

}