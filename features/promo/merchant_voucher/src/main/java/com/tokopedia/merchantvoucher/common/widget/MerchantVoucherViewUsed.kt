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

/*
    Based on CustomVoucherView with predefined parameter and fixed layout.
    +----------------------+   +--------+
    |                      +-+-+        |
    |  [  ] VOUCHER 20RB     |   ABSDD  |
    |  *Max transaction 10Rb |   [USE]  |
    |                      +-+-+        |
    +----------------------+   +--------+
 */
class MerchantVoucherViewUsed : CustomVoucherView {

    var onMerchantVoucherViewListener: OnMerchantVoucherViewListener? = null

    interface OnMerchantVoucherViewListener {
        fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
        fun isOwner(): Boolean
        fun onMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel)
    }

    var merchantVoucherViewModel: MerchantVoucherViewModel? = null
        get
        private set

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
    }

    fun setData(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        if (merchantVoucherViewModel?.enableButtonUse == true) {
            btnUseVoucher.visibility = View.VISIBLE
            tvCode.visibility = View.GONE
        } else {
            btnUseVoucher.visibility = View.GONE
            tvCode.visibility = View.VISIBLE
        }
        this.merchantVoucherViewModel = merchantVoucherViewModel
        btnUseVoucher.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)
            }
        }

        vgVoucherView.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantVoucherClicked(this)
            }
        }
        merchantVoucherViewModel?.run {
            ivVoucherLogo.setImageResource(when (merchantVoucherViewModel.ownerId) {
                MerchantVoucherOwnerTypeDef.TYPE_TOKOPEDIA -> R.drawable.ic_big_notif_customerapp
                else -> R.drawable.ic_store_logo
            })
            tvVoucherTitle.text = context.getString(R.string.voucher_title_x_x,
                    merchantVoucherViewModel.getTypeString(context),
                    merchantVoucherViewModel.getAmountShortString())
            tvVoucherDesc.text = merchantVoucherViewModel.getMinSpendLongString(context)
            tvCode.text = merchantVoucherViewModel.voucherCode
            var isOwner = false
            onMerchantVoucherViewListener?.run {
                isOwner = this.isOwner()
            }
            when {
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && !isOwner) -> {
                    btnUseVoucher.visibility = View.VISIBLE
                    tvVoucherStatus.visibility = View.GONE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && isOwner) -> {
                    btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.visibility = View.GONE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_RUN_OUT) -> {
                    btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.run_out)
                    tvVoucherStatus.visibility = View.VISIBLE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_IN_USE) -> {
                    btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.in_use)
                    tvVoucherStatus.visibility = View.VISIBLE
                }
            }
        }
    }

}
