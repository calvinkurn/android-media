package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
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
        LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view,
                this, true)
        MethodChecker.setBackground(
                btnUseVoucher,
                MethodChecker.getDrawable(context, R.drawable.bg_voucher_button)
        )
        btnUseVoucher.setTextColor(
                context.resources.getColorStateList(R.color.text_color_voucher_button)
        )
    }

    fun setData(merchantVoucherViewModel: MerchantVoucherViewModel?) {
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
            var voucherImageUrl = ""
            when (merchantVoucherViewModel.merchantVoucherType) {
                MerchantVoucherTypeDef.TYPE_DISCOUNT, MerchantVoucherTypeDef.TYPE_CASHBACK -> {
                    voucherImageUrl = MerchantVoucherConst.DISCOUNT_OR_CASHBACK_VOUCHER_IMAGE_URL
                }
                MerchantVoucherTypeDef.TYPE_FREE_ONGKIR -> {
                    voucherImageUrl = MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
                }
            }
            ImageHandler.loadImage(
                    context,
                    iv_voucher_type,
                    voucherImageUrl,
                    R.drawable.ic_loading_image
            )
            val voucherTitle = context.getString(R.string.voucher_title_x_x,
                    merchantVoucherViewModel.getTypeString(context),
                    merchantVoucherViewModel.getAmountShortString())
            val spannedVoucherTitle = SpanText(
                    voucherTitle,
                    merchantVoucherViewModel.getAmountShortString()
            ).addBoldSpanWithFontFamily("sans-serif").changeTextSize(resources.getDimensionPixelSize(R.dimen.sp_20)).getCharSequence()
            tvVoucherTitle.text = spannedVoucherTitle
            val voucherDesc = merchantVoucherViewModel.getMinSpendLongString(context)
            tvVoucherDesc.text = SpanText(
                    voucherDesc,
                    merchantVoucherViewModel.getMinSpendAmountShortString()
            ).addBoldSpanWithFontFamily("").getCharSequence()
            tvCode.text = merchantVoucherViewModel.voucherCode
            var isOwner = false
            onMerchantVoucherViewListener?.run {
                isOwner = this.isOwner()
            }
            when {
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && !isOwner) -> {
                    btnUseVoucher.isEnabled = true
                    btnUseVoucher.visibility = View.VISIBLE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && isOwner) -> {
                    btnUseVoucher.visibility = View.GONE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_RUN_OUT) -> {
                    btnUseVoucher.text = context.getString(R.string.run_out)
                    btnUseVoucher.visibility = View.VISIBLE
                    btnUseVoucher.isEnabled = false
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_RESTRICTED) -> {
                    btnUseVoucher.text = context.getString(R.string.restricted)
                    btnUseVoucher.visibility = View.VISIBLE
                    btnUseVoucher.isEnabled = false
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_IN_USE) -> {
                    MethodChecker.setBackground(
                            btnUseVoucher,
                            MethodChecker.getDrawable(context, R.drawable.bg_used_voucher)
                    )
                    btnUseVoucher.setTextColor(
                            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                    )
                    btnUseVoucher.visibility = View.VISIBLE
                    btnUseVoucher.isEnabled = false
                    btnUseVoucher.text = context.getString(R.string.in_use)
                }
            }
        }
    }

}
