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

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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
        return ContextCompat.getColor(context, R.color.shadow_topchat_voucher_attachment)
    }
}