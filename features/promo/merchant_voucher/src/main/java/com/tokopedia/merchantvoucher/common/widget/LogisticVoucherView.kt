package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherConst.DELIVERY_VOUCHER_IMAGE_URL
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
class LogisticVoucherView : CustomVoucherView {

    private var tvCode: Typography? = null
    private var tvVoucherSubtitle: Typography? = null
    private var tvVoucherTitle: Typography? = null
    private var tvVoucherDesc: Typography? = null
    private var btnUseVoucher: Button? = null
    private var ivVoucherType: MerchantVoucherImageType? = null
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
        val view = LayoutInflater.from(context).inflate(R.layout.widget_merchant_voucher_view, this, true)

        tvCode = view.findViewById(R.id.tvCode)
        tvVoucherSubtitle = view.findViewById(R.id.tvVoucherSubtitle)
        tvVoucherTitle = view.findViewById(R.id.tvVoucherTitle)
        tvVoucherDesc = view.findViewById(R.id.tvVoucherDesc)
        btnUseVoucher = view.findViewById(R.id.btnUseVoucher)
        ivVoucherType = view.findViewById(R.id.iv_voucher_type)

        tvCode?.visibility = View.GONE
        tvVoucherSubtitle?.visibility = View.VISIBLE
        tvVoucherTitle?.isSingleLine = false
    }

    fun setData(title: String, subtitle: String, desc: String, isApplied: Boolean) {
        tvVoucherTitle?.text = title
        tvVoucherSubtitle?.text = subtitle
        tvVoucherDesc?.text = desc
        ImageHandler.loadImage(
                context,
                ivVoucherType,
                DELIVERY_VOUCHER_IMAGE_URL,
                R.drawable.ic_loading_image
        )
        btnUseVoucher?.isEnabled = !isApplied
        btnUseVoucher?.text = if (isApplied) context.getString(R.string.applied) else
            context.getString(R.string.use_promo)
    }

    fun setUseButtonClickListener(listener: View.OnClickListener) {
        btnUseVoucher?.setOnClickListener(listener)
    }
}
