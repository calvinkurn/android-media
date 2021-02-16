package com.tokopedia.mvcwidget.views

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.promoui.common.PromoCouponView
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

/*
* 1. It has internal Padding of 6dp to render its shadows
* 2. isMainContainerSetFitsSystemWindows must be true if activity/fragment layout is setFitsSystemWindows(false) or setFitsSystemWindows = false
* */
class MvcView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var tvTitle: Typography
    lateinit var tvSubTitle: Typography
    lateinit var imageChevron: AppCompatImageView
    lateinit var imageCoupon: AppCompatImageView
    lateinit var promoCouponView: PromoCouponView
    var shopId: String = ""
    var isMainContainerSetFitsSystemWindows = false

    init {
        View.inflate(context, R.layout.mvc_entry_view, this)
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(64).toInt())
        this.layoutParams = lp
        initViews()

        val startPadding = 0
        val topPadding = dpToPx(8).toInt() - promoCouponView.topPadding
        setPadding(startPadding, topPadding, startPadding, 0)

        setClicks()
    }

    private fun initViews() {
        tvTitle = this.findViewById(R.id.tvTitle)
        promoCouponView = this.findViewById(R.id.promo_coupon_view)
        tvSubTitle = this.findViewById(R.id.tvSubTitle)
        imageChevron = this.findViewById(R.id.image_chevron)
        imageCoupon = this.findViewById(R.id.image_coupon)
    }

    private fun setClicks() {
        promoCouponView.setOnClickListener {
            val intent = Intent(context, TransParentActivity::class.java)
            intent.putExtra(TransParentActivity.SHOP_ID, shopId)
            context.startActivity(intent)
        }
    }

    fun setData(mvcData: MvcData, shopId: String, isMainContainerSetFitsSystemWindows: Boolean = false) {
        this.isMainContainerSetFitsSystemWindows = isMainContainerSetFitsSystemWindows
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTitle.text = HtmlUtil.fromHtml(mvcData.title).trim()
        } else {
            tvTitle.text = Html.fromHtml(mvcData.title).trim()
        }

        tvSubTitle.text = mvcData.subTitle
        this.shopId = shopId

        Glide.with(imageCoupon)
                .load(mvcData.imageUrl)
                .dontAnimate()
                .into(imageCoupon)
    }

}