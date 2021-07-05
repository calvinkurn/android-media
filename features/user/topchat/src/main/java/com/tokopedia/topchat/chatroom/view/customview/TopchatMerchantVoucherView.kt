package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R

class TopchatMerchantVoucherView : MerchantVoucherView {

    private var voucherContainer: LinearLayout? = null

    private var useShadow = DEFAULT_USE_SHADOW

    @ColorInt
    private val defaultVoucherShadowColorInt = MethodChecker.getColor(context, DEFAULT_VOUCHER_SHADOW_COLOR)

    @ColorInt
    private var voucherShadowColor: Int = defaultVoucherShadowColorInt

    constructor(context: Context) : super(context) {
        initAttr(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TopchatMerchantVoucherView,
                0, 0).apply {
            try {
                useShadow = getBoolean(R.styleable.TopchatMerchantVoucherView_useShadow, DEFAULT_USE_SHADOW)
                voucherShadowColor = getColor(R.styleable.TopchatMerchantVoucherView_voucherShadowColor, defaultVoucherShadowColorInt)
            } finally {
                recycle()
            }
        }
    }

    override fun getVoucherCodeId() = R.id.tvCode
    override fun getVoucherDescId() = R.id.tvVoucherDesc
    override fun getVoucherTitleId() = R.id.tvVoucherTitle
    override fun getVoucherTypeId() = R.id.iv_voucher_type
    override fun getUseVoucherButtonId() = R.id.btnUseVoucher

    override fun getVoucherLayout(): Int {
        return R.layout.item_topchat_widget_merchant_voucher_view
    }

    override fun getShadowRadiusValue(): Float {
        return 2f.toPx()
    }

    override fun getShadowOffsetDx(): Float {
        return 0.5f.toPx()
    }

    override fun initView(view: View) {
        super.initView(view)
        voucherContainer = view.findViewById(R.id.vgVoucherView)
    }

    @ColorInt
    override fun getShadowColor(): Int {
        val color: Int = if (useShadow) {
            return voucherShadowColor
        } else {
            android.R.color.transparent
        }
        return ContextCompat.getColor(context, color)
    }

    companion object {
        private const val DEFAULT_USE_SHADOW = false
        private val DEFAULT_VOUCHER_SHADOW_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N150
    }
}