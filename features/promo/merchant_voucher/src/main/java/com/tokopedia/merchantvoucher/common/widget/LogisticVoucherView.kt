package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.model.*
import kotlinx.android.synthetic.main.widget_merchant_voucher_view.view.*
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast


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
        val dp2 = dpToPx(2f);
        cornerRadius = 2 * dp2
        mScallopRadius = 4 * dp2
        mScallopRelativePosition = 0.59f
        mShadowRadius = dp2
        mDashWidth = dp2
        mDashGap = 2 * dp2
        mDashColor = ContextCompat.getColor(this.context, R.color.colorGray)
        LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view,
                this, true)
        ivVoucherLogo.visibility = View.GONE
        tvVoucherStatus.visibility = View.GONE
        tvCode.visibility = View.GONE
        tvVoucherSubtitle.visibility = View.VISIBLE
        tvVoucherTitle.setSingleLine(false)
    }

    fun setData(title: String, subtitle: String, desc: String) {
        tvVoucherTitle.text = title
        tvVoucherSubtitle.text = subtitle
        tvVoucherDesc.text = desc
    }

    fun setUseButtonClickListener(listener: View.OnClickListener) {
        btnUseVoucher.setOnClickListener(listener)
    }
}
