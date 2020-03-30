package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.*
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
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
class LogisticVoucherView : CustomVoucherView {

    var onMerchantVoucherViewListener: OnMerchantVoucherViewListener? = null

    interface OnMerchantVoucherViewListener {
        fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
        fun isOwner(): Boolean
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
        LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view,
                this, true)
        tvCode.visibility = View.GONE
        tvVoucherSubtitle.visibility = View.VISIBLE
        tvVoucherTitle.setSingleLine(false)
    }

    fun setData(title: String, subtitle: String, desc: String, isApplied: Boolean) {
        tvVoucherTitle.text = title
        tvVoucherSubtitle.text = subtitle
        tvVoucherDesc.text = desc
        ImageHandler.loadImage(
                context,
                iv_voucher_type,
                DELIVERY_VOUCHER_IMAGE_URL,
                R.drawable.ic_loading_image
        )
        btnUseVoucher.isEnabled = !isApplied
        btnUseVoucher.text = if (isApplied) context.getString(R.string.applied) else
            context.getString(R.string.use_promo)
    }

    fun setUseButtonClickListener(listener: View.OnClickListener) {
        btnUseVoucher.setOnClickListener(listener)
    }
}
