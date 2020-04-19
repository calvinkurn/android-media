package com.tokopedia.vouchercreation.create.view.customview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import kotlinx.android.synthetic.main.mvc_voucher_target_item.view.*

class VoucherTargetCardItem @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_voucher_target_item,
        styleableResource: IntArray = R.styleable.VoucherTargetCardItem
) : BaseVoucherView(context, attrs, defStyleAttr, defStyleRes, layoutResource, styleableResource) {

    companion object {
        private const val TARGET_PUBLIC_TYPE = 0
        private const val TARGET_SPECIAL_TYPE = 1
    }

    var voucherTargetType: Int = TARGET_PUBLIC_TYPE
        set(value) {
            field = value
            setupView()
        }
    var isItemEnabled: Boolean = false
        set(value) {
            field = value
            setupView()
        }
    var isHavePromoCode: Boolean = false
        set(value) {
            field = value
            setupView()
        }
    var promoCode: String = ""
        set(value) {
            field = value
            setupView()
        }

    override fun setupAttributes() {
        attributes?.run {
            voucherTargetType = getInt(R.styleable.VoucherTargetCardItem_targetType, voucherTargetType)
            isItemEnabled = getBoolean(R.styleable.VoucherTargetCardItem_isEnabled, isItemEnabled)
            isHavePromoCode = getBoolean(R.styleable.VoucherTargetCardItem_isHavePromoCode, isHavePromoCode)
            promoCode = getString(R.styleable.VoucherTargetCardItem_promoCode).toBlankOrString()
        }
    }

    override fun setupView() {
        view?.run {
            setupIcon()
            setupTitle()
            setupDescription()
            setupItemEnability()
            setupPromoCode()
        }
    }

    private fun View.setupIcon() {
        val targetIconDrawableRes = when (voucherTargetType) {
            TARGET_PUBLIC_TYPE -> R.drawable.ic_im_umum
            TARGET_SPECIAL_TYPE -> R.drawable.ic_im_terbatas
            else -> R.drawable.ic_im_umum
        }
        voucherTargetItemIcon?.setImageDrawable(ContextCompat.getDrawable(context, targetIconDrawableRes))
    }

    private fun View.setupTitle() {
        val targetTitleRes = when(voucherTargetType) {
            TARGET_PUBLIC_TYPE -> R.string.mvc_create_target_public
            TARGET_SPECIAL_TYPE -> R.string.mvc_create_target_special
            else -> R.string.mvc_create_target_public
        }
        voucherTargetItemTitle.text = resources.getString(targetTitleRes)
    }

    private fun View.setupDescription() {
        val targetDescRes = when(voucherTargetType) {
            TARGET_PUBLIC_TYPE -> R.string.mvc_create_target_public_desc
            TARGET_SPECIAL_TYPE -> R.string.mvc_create_target_special_desc
            else -> R.string.mvc_create_target_public_desc
        }
        voucherTargetItemDescription.text = resources.getString(targetDescRes)
    }

    private fun View.setupItemEnability() {
        setupBorderColor()
        voucherTargetItemRadioButton?.isChecked = isItemEnabled
    }

    private fun View.setupBorderColor() {
        resources?.run {
            val borderColorRes = if (isItemEnabled) {
                R.color.mvc_card_border_green
            } else {
                R.color.mvc_color_grey
            }
            (background as GradientDrawable).setStroke(
                    getDimension(R.dimen.mvc_create_target_card_border_width).toInt().orZero(),
                    ContextCompat.getColor(context, borderColorRes))
        }
    }

    private fun View.setupPromoCode() {
        if (isHavePromoCode) {
            voucherTargetPromoCodeInfo?.run {
                visibility = View.VISIBLE
                isChangeEnabled = isItemEnabled
                promoCodeString = promoCode
            }
        }
    }

}