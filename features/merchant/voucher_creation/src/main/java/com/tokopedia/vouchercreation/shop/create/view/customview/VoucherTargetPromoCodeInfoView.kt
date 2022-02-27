package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import kotlinx.android.synthetic.main.mvc_promo_code_info.view.*

class VoucherTargetPromoCodeInfoView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_promo_code_info,
        styleableResource: IntArray = R.styleable.VoucherTargetPromoCodeInfoView
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource, styleableResource) {

    init {
        setupAttributes()
        setupLayout()
    }

    var promoCodeString = ""
        set(value) {
            field = value
            setupView()
        }
    var isChangeEnabled = false
        set(value) {
            field = value
            setupView()
        }

    override fun setupAttributes() {
        attributes?.run {
            promoCodeString = getString(R.styleable.VoucherTargetPromoCodeInfoView_code).toBlankOrString()
            isChangeEnabled = getBoolean(R.styleable.VoucherTargetPromoCodeInfoView_isChangeEnabled, isChangeEnabled)
        }
    }

    override fun setupView() {
        view?.run {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            voucherCreatePromoCodeValue?.text = promoCodeString
            voucherCreatePromoCodeChange?.setChangeTextColor(isChangeEnabled)
        }
    }

    private fun Typography.setChangeTextColor(isEnabled: Boolean) {
        val textColor =
                if (isEnabled) {
                    com.tokopedia.unifyprinciples.R.color.Green_G500
                } else {
                    com.tokopedia.unifyprinciples.R.color.Neutral_N700_32
                }
        setTextColor(ContextCompat.getColor(context, textColor))
    }

}