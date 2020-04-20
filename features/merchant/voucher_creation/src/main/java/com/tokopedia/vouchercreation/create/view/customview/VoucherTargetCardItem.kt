package com.tokopedia.vouchercreation.create.view.customview

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
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
        const val TARGET_PUBLIC_TYPE = 0
        const val TARGET_SPECIAL_TYPE = 1
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
                        TARGET_SPECIAL_TYPE -> {
                            VoucherTargetCardType.SPECIAL
                        }
                        else -> return
                    }
        }
    private var isItemEnabled: Boolean = false
    private var isHavePromoCode: Boolean = false
    private var promoCode: String = ""

    override fun setupAttributes() {
        attributes?.run {
            voucherTargetType = getInt(R.styleable.VoucherTargetCardItem_targetType, voucherTargetType)
            isItemEnabled = getBoolean(R.styleable.VoucherTargetCardItem_isEnabled, isItemEnabled)
            isHavePromoCode = getBoolean(R.styleable.VoucherTargetCardItem_isHavePromoCode, isHavePromoCode)
            promoCode = getString(R.styleable.VoucherTargetCardItem_promoCode).toBlankOrString()
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
        voucherTargetItemIcon?.setImageDrawable(ContextCompat.getDrawable(context, voucherTargetCardType.iconDrawableRes))
    }

    private fun View.setupTitle() {
        voucherTargetItemTitle.text = resources.getString(voucherTargetCardType.titleStringRes)
    }

    private fun View.setupDescription() {
        voucherTargetItemDescription.text = resources.getString(voucherTargetCardType.descriptionStringRes)
    }

    private fun View.setupItemEnabling() {
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
                promoCodeString = promoCode.toBlankOrString()
            }
        } else {
            voucherTargetPromoCodeInfo?.visibility = View.GONE
        }
    }

    fun setupCurrentView(voucherTargetType: Int,
                         isItemEnabled: Boolean,
                         isHavePromoCode: Boolean,
                         promoCode: String) {
        this.voucherTargetType = voucherTargetType
        this.isItemEnabled = isItemEnabled
        this.isHavePromoCode = isHavePromoCode
        this.promoCode = promoCode

        setupView()
    }

    enum class VoucherTargetCardType(@DrawableRes val iconDrawableRes: Int,
                                     @StringRes val titleStringRes: Int,
                                     @StringRes val descriptionStringRes: Int) {

        PUBLIC(R.drawable.ic_im_umum, R.string.mvc_create_target_public, R.string.mvc_create_target_public_desc),
        SPECIAL(R.drawable.ic_im_terbatas, R.string.mvc_create_target_special, R.string.mvc_create_target_special_desc)

    }

}