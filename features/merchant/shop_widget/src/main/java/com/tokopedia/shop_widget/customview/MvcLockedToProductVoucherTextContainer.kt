package com.tokopedia.shop_widget.customview

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop_widget.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil
import java.lang.reflect.Method

class MvcLockedToProductVoucherTextContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var textVoucherTitle: Typography? = null
    private var textLeftSubtitle: Typography? = null
    private var textRightSubtitle: Typography? = null
    private var textDot: Typography? = null
    private var imageCoupon: ImageUnify? = null

    init {
        View.inflate(context, R.layout.mvc_locked_to_product_voucher_text_container, this)
        initViews()
    }

    private fun initViews() {
        textVoucherTitle = this.findViewById(R.id.text_voucher_title)
        textLeftSubtitle = this.findViewById(R.id.text_left_subtitle)
        textRightSubtitle = this.findViewById(R.id.text_right_subtitle)
        textDot = this.findViewById(R.id.text_dot)
        imageCoupon = this.findViewById(R.id.image_coupon)
    }

    fun setData(
        title: String,
        leftSubtitle: String,
        rightSubtitle: String,
        imageUrl: String
    ) {
        textVoucherTitle?.text = getHtmlText(title)
        textLeftSubtitle?.text = getHtmlText(leftSubtitle)
        if(rightSubtitle.isEmpty()){
            textDot?.gone()
            textRightSubtitle?.text = ""
        } else {
            textDot?.visible()
            textRightSubtitle?.text = rightSubtitle
        }
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

    private fun getHtmlText(text: String): CharSequence {
        return MethodChecker.fromHtml(text)
    }
}