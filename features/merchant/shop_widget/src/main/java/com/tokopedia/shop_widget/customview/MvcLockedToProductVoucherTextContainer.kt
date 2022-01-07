package com.tokopedia.shop_widget.customview

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.tokopedia.shop_widget.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

class MvcLockedToProductVoucherTextContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var textVoucherTitle: Typography? = null
    private var textExpiredTime: Typography? = null
    private var textVoucherStock: Typography? = null
    private var imageCoupon: ImageUnify? = null

    init {
        View.inflate(context, R.layout.mvc_locked_to_product_voucher_text_container, this)
        initViews()
    }

    private fun initViews() {
        textVoucherTitle = this.findViewById(R.id.text_voucher_title)
        textExpiredTime = this.findViewById(R.id.text_expired_time)
        textVoucherStock = this.findViewById(R.id.text_voucher_stock)
        imageCoupon = this.findViewById(R.id.image_coupon)
    }

    fun setData(
        title: String,
        expiredTime: String,
        voucherStock: String,
        imageUrl: String
    ) {
        textVoucherTitle?.apply {
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                HtmlUtil.fromHtml(title).trim()
            } else {
                Html.fromHtml(title).trim()
            }
        }
        textExpiredTime?.text = expiredTime
        textVoucherStock?.text = voucherStock
        if (imageUrl.isEmpty()) return

        if (!(context as Activity).isFinishing) {
            imageCoupon?.let {
                Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    .into(it)
            }
        }
    }
}