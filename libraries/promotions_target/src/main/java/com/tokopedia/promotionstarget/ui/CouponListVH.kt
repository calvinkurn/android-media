package com.tokopedia.promotionstarget.ui

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.loadImageGlide
import com.tokopedia.promotionstarget.ui.views.CouponView

class CouponListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons
        const val WIDTH_RATIO = 1.26
    }

    fun setDynamicWidth() {
        val width = itemView.context.resources.displayMetrics.widthPixels
        val lp = itemView.layoutParams as RecyclerView.LayoutParams
        lp.width = (width / WIDTH_RATIO).toInt()
        itemView.layoutParams = lp
    }

    private val couponView: CouponView = itemView.findViewById(R.id.couponView)
    private val imageView: AppCompatImageView = itemView.findViewById(R.id.appCompatImageView)
    private val imageIcon: AppCompatImageView = itemView.findViewById(R.id.icon)
    private val tvTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvTitle)
    private val tvSubTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvSubTitle)

    fun setData(data: GetCouponDetail) {
        tvTitle.text = data.minimumUsageLabel
        tvSubTitle.text = data.minimumUsage
        imageIcon.loadImageGlide(data.icon)
        imageView.loadImageGlide(data.imageUrl)
    }
}