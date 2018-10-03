package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import kotlinx.android.synthetic.main.widget_merchant_voucher_view.view.*

/*
    Based on CustomVoucherView with predefined parameter and fixed layout.
    +----------------------+   +--------+
    |                      +-+-+        |
    |  [  ] VOUCHER 20RB     |   ABSDD  |
    |  *Max transaction 10Rb |   [USE]  |
    |                      +-+-+        |
    +----------------------+   +--------+
 */
class MerchantVoucherView : CustomVoucherView {

    var onMerchantVoucherViewListener: OnMerchantVoucherViewListener? = null
    interface OnMerchantVoucherViewListener {
        fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
    }

    var merchantVoucherViewModel: MerchantVoucherViewModel? = null
        set(value) {
            field = value
            onMerchantVoucherChanged(value)
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        clipToPadding = false
        cornerRadius = dpToPx(4f)
        mScallopRadius = dpToPx(8f)
        mScallopRelativePosition = 0.65f
        mShadowRadius = dpToPx(2f)
        mDashWidth = dpToPx(2f)
        mDashGap = dpToPx(4f)
        mDashColor = ContextCompat.getColor(this.context, R.color.colorGray)
        LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view,
                this, true)
        btnUseVoucher.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)
            }
        }
    }

    private fun onMerchantVoucherChanged(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        merchantVoucherViewModel?.run {
            tvVoucherTitle.text = "aaa" + Math.random()
            tvVoucherDesc.text = "aaa Desc " + Math.random()
            ivVoucherLogo.setImageResource(R.drawable.ic_store_logo)
            tvCode.text = "HIBFNJW"
        }
    }

}
