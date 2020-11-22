package com.tokopedia.merchantvoucher.common.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DISCOUNT_OR_CASHBACK_VOUCHER_IMAGE_URL
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.*
import com.tokopedia.merchantvoucher.common.model.*


/*
    Based on CustomVoucherView with predefined parameter and fixed layout.
    +----------------------+   +--------+
    |                      +-+-+        |
    |  [  ] VOUCHER 20RB     |   ABSDD  |
    |  *Max transaction 10Rb |   [USE]  |
    |                      +-+-+        |
    +----------------------+   +--------+
 */
open class MerchantVoucherView : CustomVoucherView {

    var onMerchantVoucherViewListener: OnMerchantVoucherViewListener? = null

    private var btnUseVoucher: Button? = null
    private var ivVoucherType: ImageView? = null
    private var tvVoucherTitle: TextView? = null
    private var tvVoucherDesc: TextView? = null
    private var tvCode: TextView? = null

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
        LayoutInflater.from(context).inflate(getVoucherLayout(), this, true).apply {
            initView(this)
        }
        btnUseVoucher?.visibility = View.GONE
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below comment for future release
        //btnUseVoucher.text = context.getString(R.string.use_voucher)
        //TOGGLE_MVC_OFF
        btnUseVoucher?.text = context.getString(R.string.copy_to_clipboard)
        MethodChecker.setBackground(
                btnUseVoucher,
                MethodChecker.getDrawable(context, R.drawable.bg_voucher_button)
        )
        btnUseVoucher?.setTextColor(
                context.resources.getColorStateList(R.color.text_color_voucher_button)
        )
        btnUseVoucher?.setOnClickListener {
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

    protected open fun initView(view: View) {
        btnUseVoucher = view.findViewById(getUseVoucherButtonId())
        ivVoucherType = view.findViewById(getVoucherTypeId())
        tvVoucherTitle = view.findViewById(getVoucherTitleId())
        tvVoucherDesc = view.findViewById(getVoucherDescId())
        tvCode = view.findViewById(getVoucherCodeId())
    }

    protected open fun getVoucherCodeId() = R.id.tvCode
    protected open fun getVoucherDescId() = R.id.tvVoucherDesc
    protected open fun getVoucherTitleId() = R.id.tvVoucherTitle
    protected open fun getVoucherTypeId() = R.id.iv_voucher_type
    protected open fun getUseVoucherButtonId() = R.id.btnUseVoucher

    @LayoutRes
    protected open fun getVoucherLayout(): Int {
        return R.layout.widget_merchant_voucher_view
    }

    fun setData(merchantVoucherViewModel: MerchantVoucherViewModel?, hasActionButton: Boolean = true) {
        setData(merchantVoucherViewModel)
        if (!hasActionButton) {
            btnUseVoucher?.visibility = View.GONE
            btnUseVoucher?.isEnabled = false
        } else {
            btnUseVoucher?.visibility = View.VISIBLE
            btnUseVoucher?.isEnabled = true
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
                    //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below comment for future release
                    /*btnUseVoucher.visibility = View.GONE
                    tvVoucherStatus.text = context.getString(R.string.in_use)
                    tvVoucherStatus.visibility = View.VISIBLE*/

                    //TOGGLE_MVC_OFF
                    MethodChecker.setBackground(
                            btnUseVoucher,
                            MethodChecker.getDrawable(context, R.drawable.bg_voucher_button_in_use)
                    )
                    btnUseVoucher?.setTextColor(
                            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                    )
                    btnUseVoucher?.visibility = View.VISIBLE
                    btnUseVoucher?.isEnabled = false
                    btnUseVoucher?.text = context.getString(R.string.in_use)
                }
            }
            tvCode?.hide()
            btnUseVoucher?.hide()
        }
    }

}
