package com.tokopedia.promotionstarget.presentation.ui.viewholders

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetail
import com.tokopedia.promotionstarget.presentation.loadImageGlide

open class CouponListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    protected val imageView: AppCompatImageView = itemView.findViewById(R.id.appCompatImageView)
    protected val tvTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvTitle)
    protected val tvSubTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvSubTitle)

    fun setData(data: GetCouponDetail) {
        tvTitle.text = data.minimumUsageLabel
        tvSubTitle.text = data.minimumUsage
        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }
        imageView.loadImageGlide(data.imageUrl)
    }
}