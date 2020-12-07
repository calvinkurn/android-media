package com.tokopedia.promotionstarget.presentation.ui.viewholders

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.CouponStatusType
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetail
import com.tokopedia.promotionstarget.presentation.dim
import com.tokopedia.promotionstarget.presentation.loadImageGlide
import com.tokopedia.promotionstarget.presentation.unDim

class CouponListTokopointsVH(itemView: View) : CouponListVH(itemView) {
    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons_tp
    }

    private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    private val tvTitleRight: TextView = itemView.findViewById(R.id.tvTitle1)
    private val tvSubTitleRight: TextView = itemView.findViewById(R.id.tvSubTitle1)
    private val icon: AppCompatImageView = itemView.findViewById(R.id.icon)
    private val iconRight: AppCompatImageView = itemView.findViewById(R.id.icon1)

    fun setTpCouponData(data: TokopointsCouponDetail) {
        tvStatus.text = data.statusStr

        if (tvStatus.text.isNullOrEmpty()) {
            tvStatus.visibility = View.GONE
        } else {
            tvStatus.visibility = View.VISIBLE
        }

        imageView.loadImageGlide(data.imageUrl)
        tvTitle.text = data.usage?.dateKey
        tvSubTitle.text = data.usage?.dateValue?.trim()

        tvTitleRight.text = data.minimumUsageLabel
        tvSubTitleRight.text = data.minimumUsage

        if (tvSubTitleRight.text.isNullOrEmpty()) {
            tvSubTitleRight.visibility = View.GONE
        } else {
            tvSubTitleRight.visibility = View.VISIBLE
        }

        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }

        //Because we only have 1 viewholder - so no need to undim, its by default undim
        when (data.couponStatus) {
            CouponStatusType.ACTIVE -> {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(ContextCompat.getColor(icon.context, R.color.t_promo_active_green)))
                ImageViewCompat.setImageTintList(iconRight, ColorStateList.valueOf(ContextCompat.getColor(icon.context, R.color.t_promo_active_green)))
            }
            else -> {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(ContextCompat.getColor(icon.context, R.color.t_promo_inactive_green)))
                ImageViewCompat.setImageTintList(iconRight, ColorStateList.valueOf(ContextCompat.getColor(icon.context, R.color.t_promo_inactive_green)))
                imageView.dim()
            }
        }
    }
}