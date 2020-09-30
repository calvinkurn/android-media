package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R

class TopchatMerchantVoucherView : MerchantVoucherView {

    private var voucherContainer: LinearLayout? = null

    private var useShadow = DEFAULT_USE_SHADOW

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
        if (context == null || attrs == null) return
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.TopchatMerchantVoucherView,
                0, 0).apply {
            try {
                useShadow = getBoolean(R.styleable.TopchatMerchantVoucherView_useShadow, DEFAULT_USE_SHADOW)
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

    override fun initView(view: View) {
        super.initView(view)
        voucherContainer = view.findViewById(R.id.vgVoucherView)
    }

    override fun getShadowColor(): Int {
        val color: Int = if (useShadow) {
            R.color.shadow_topchat_voucher_attachment
        } else {
            android.R.color.transparent
        }
        return ContextCompat.getColor(context, color)
    }

    companion object {
        private const val DEFAULT_USE_SHADOW = false
    }
}