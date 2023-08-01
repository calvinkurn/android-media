package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef
import com.tokopedia.merchantvoucher.common.model.*
import com.tokopedia.unifyprinciples.Typography

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


    private var tvCode: Typography? = null
    private var tvVoucherSubtitle: Typography? = null
    private var tvVoucherTitle: Typography? = null
    private var tvVoucherDesc: Typography? = null
    private var btnUseVoucher: Button? = null
    private var ivVoucherType: MerchantVoucherImageType? = null
    private var vgVoucherView: View? = null

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
        var view = LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view,
            this, true)
        tvCode = view.findViewById(R.id.tvCode)
        tvVoucherSubtitle = view.findViewById(R.id.tvVoucherSubtitle)
        tvVoucherTitle = view.findViewById(R.id.tvVoucherTitle)
        tvVoucherDesc = view.findViewById(R.id.tvVoucherDesc)
        btnUseVoucher = view.findViewById(R.id.btnUseVoucher)
        ivVoucherType = view.findViewById(R.id.iv_voucher_type)
        vgVoucherView = view.findViewById(R.id.vgVoucherView)

        clipToPadding = false

        MethodChecker.setBackground(
                btnUseVoucher,
                MethodChecker.getDrawable(context, R.drawable.bg_voucher_button)
        )
        btnUseVoucher?.setTextColor(
                context.resources.getColorStateList(com.tokopedia.merchantvoucher.R.color.merchant_voucher_dms_voucher_button)
        )
    }

    fun setData(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        this.merchantVoucherViewModel = merchantVoucherViewModel
        btnUseVoucher?.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)
            }
        }

        vgVoucherView?.setOnClickListener {
            merchantVoucherViewModel?.run {
                onMerchantVoucherViewListener?.onMerchantVoucherClicked(this)
            }
        }
        merchantVoucherViewModel?.run {
            var voucherImageUrl = ""
            when (merchantVoucherViewModel.merchantVoucherType) {
                MerchantVoucherTypeDef.TYPE_DISCOUNT -> {
                    voucherImageUrl = MerchantVoucherConst.DISCOUNT_VOUCHER_IMAGE_URL
                }
                MerchantVoucherTypeDef.TYPE_CASHBACK -> {
                    voucherImageUrl = MerchantVoucherConst.DISCOUNT_OR_CASHBACK_VOUCHER_IMAGE_URL
                }
                MerchantVoucherTypeDef.TYPE_FREE_ONGKIR -> {
                    voucherImageUrl = MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
                }
            }
            ImageHandler.loadImage(
                    context,
                    ivVoucherType,
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
            tvVoucherTitle?.text = spannedVoucherTitle
            val voucherDesc = merchantVoucherViewModel.getMinSpendLongString(context)
            tvVoucherDesc?.text = SpanText(
                    voucherDesc,
                    merchantVoucherViewModel.getMinSpendAmountShortString()
            ).addBoldSpanWithFontFamily("").getCharSequence()
            tvCode?.text = merchantVoucherViewModel.voucherCode
            var isOwner = false
            onMerchantVoucherViewListener?.run {
                isOwner = this.isOwner()
            }
            when {
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && !isOwner) -> {
                    btnUseVoucher?.isEnabled = true
                    btnUseVoucher?.visibility = View.VISIBLE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE && isOwner) -> {
                    btnUseVoucher?.visibility = View.GONE
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_RUN_OUT) -> {
                    btnUseVoucher?.text = context.getString(R.string.run_out)
                    btnUseVoucher?.visibility = View.VISIBLE
                    btnUseVoucher?.isEnabled = false
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_RESTRICTED) -> {
                    btnUseVoucher?.text = context.getString(R.string.restricted)
                    btnUseVoucher?.visibility = View.VISIBLE
                    btnUseVoucher?.isEnabled = false
                }
                (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_IN_USE) -> {
                    MethodChecker.setBackground(
                            btnUseVoucher,
                            MethodChecker.getDrawable(context, R.drawable.bg_used_voucher)
                    )
                    btnUseVoucher?.setTextColor(
                            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
                    )
                    btnUseVoucher?.visibility = View.VISIBLE
                    btnUseVoucher?.isEnabled = false
                    btnUseVoucher?.text = context.getString(R.string.in_use)
                }
            }
        }
    }

}
