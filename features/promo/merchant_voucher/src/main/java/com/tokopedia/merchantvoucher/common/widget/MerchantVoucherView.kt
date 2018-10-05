package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.model.getAmountString
import com.tokopedia.merchantvoucher.common.model.getMinSpendLongString
import com.tokopedia.merchantvoucher.common.model.getTypeString
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
        btnUseVoucher.visibility = View.GONE
        btnUseVoucher.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)
            }
        }
    }

    private fun onMerchantVoucherChanged(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        merchantVoucherViewModel?.run {
            ivVoucherLogo.setImageResource( when (merchantVoucherViewModel.ownerId) {
                MerchantVoucherOwnerTypeDef.TYPE_TOKOPEDIA -> R.drawable.ic_tokopedia_logo
                else -> R.drawable.ic_store_logo
            })
            tvVoucherTitle.text = context.getString(R.string.voucher_title_x_x,
                    merchantVoucherViewModel.getTypeString(context),
                    merchantVoucherViewModel.getAmountString(context))
            tvVoucherDesc.text = merchantVoucherViewModel.getMinSpendLongString(context)
            ivVoucherLogo.setImageResource(R.drawable.ic_store_logo)
            tvCode.text = merchantVoucherViewModel.voucherCode
            when (merchantVoucherViewModel.status){
                MerchantVoucherStatusTypeDef.TYPE_AVAILABLE -> {
                    btnUseVoucher.visibility = View.VISIBLE
                    tvVoucherStatus.visibility = View.GONE
                }
                MerchantVoucherStatusTypeDef.TYPE_OUT_OF_STOCK -> {
                    btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.out_of_stock)
                    tvVoucherStatus.visibility = View.VISIBLE
                }
                MerchantVoucherStatusTypeDef.TYPE_IN_USE -> {
                    btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.in_use)
                    tvVoucherStatus.visibility = View.VISIBLE
                }
            }
        }
    }

}
