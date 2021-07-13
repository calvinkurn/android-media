package com.tokopedia.mvcwidget.views

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

class MvcTextContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var tvTitle: Typography
    lateinit var tvSubTitle: Typography
    lateinit var imageCoupon: SquareImageView

    init {
        View.inflate(context, R.layout.mvc_text_container, this)
        initViews()
    }

    private fun initViews() {
        tvTitle = this.findViewById(R.id.tvTitle)
        tvSubTitle = this.findViewById(R.id.tvSubTitle)
        imageCoupon = this.findViewById(R.id.image_coupon)
    }

    fun setData(animatedInfo: AnimatedInfos?) {
        animatedInfo?.let {
            setData(it.title ?: "", it.subTitle ?: "", it.iconURL ?: "")
        }
    }

    fun setData(title: String, subtitle: String, imageUrl: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.text = HtmlUtil.fromHtml(title).trim()
        } else {
            tvTitle.text = Html.fromHtml(title).trim()
        }
        tvSubTitle.text = subtitle
        if (imageUrl.isEmpty()) return

        if (!(context as Activity).isFinishing) {
            Glide.with(context)
                .load(imageUrl)
                .dontAnimate()
                .into(imageCoupon)

        }
    }
}