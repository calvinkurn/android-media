package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.model.*
import kotlinx.android.synthetic.main.widget_merchant_voucher_view.view.*
import android.content.ClipData
import android.content.ClipboardManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DISCOUNT_OR_CASHBACK_VOUCHER_IMAGE_URL
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.*


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
        fun isOwner(): Boolean
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
        btnUseVoucher.visibility = View.GONE
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below comment for future release
        //btnUseVoucher.text = context.getString(R.string.use_voucher)
        //TOGGLE_MVC_OFF
        btnUseVoucher.text = context.getString(R.string.copy_to_clipboard)
        MethodChecker.setBackground(
                btnUseVoucher,
                MethodChecker.getDrawable(context, R.drawable.bg_voucher_button)
        )
        btnUseVoucher.setTextColor(
                context.resources.getColorStateList(R.color.text_color_voucher_button)
        )
        btnUseVoucher.setOnClickListener {
            merchantVoucherViewModel?.run {
                //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below comment for future release
                //onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)

                //TOGGLE_MVC_OFF
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(voucherCode, voucherCode)
                clipboard.setPrimaryClip(clip)
                onMerchantVoucherViewListener?.onMerchantUseVoucherClicked(this)
            }
        }
    }

    fun setData(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        this.merchantVoucherViewModel = merchantVoucherViewModel
        merchantVoucherViewModel?.run {
            var voucherImageUrl = ""
            when (merchantVoucherViewModel.merchantVoucherType) {
                TYPE_DISCOUNT, TYPE_CASHBACK -> {
                    voucherImageUrl = DISCOUNT_OR_CASHBACK_VOUCHER_IMAGE_URL
                }
                TYPE_FREE_ONGKIR -> {
                    voucherImageUrl = DELIVERY_VOUCHER_IMAGE_URL
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
                    //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below comment for future release
                    /*btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.in_use)
                    tvVoucherStatus.visibility = View.VISIBLE*/

                    //TOGGLE_MVC_OFF
                    MethodChecker.setBackground(
                            btnUseVoucher,
                            MethodChecker.getDrawable(context, R.drawable.bg_voucher_button_in_use)
                    )
                    btnUseVoucher.setTextColor(
                            MethodChecker.getColor(context, R.color.white)
                    )
                    btnUseVoucher.visibility = View.VISIBLE
                    btnUseVoucher.isEnabled = false
                    btnUseVoucher.text = context.getString(R.string.in_use)
                }
            }
        }
    }

}
