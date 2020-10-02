package com.tokopedia.promotionstarget.presentation.ui.viewholders

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.promotionstarget.R
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetail
import com.tokopedia.promotionstarget.presentation.loadImageGlide

class CouponListTokopointsVH(itemView: View) : CouponListVH(itemView) {
    companion object {
        fun getLayout() = R.layout.t_promo_item_coupons_tp
    }

    private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    private val tvTitleRight: TextView = itemView.findViewById(R.id.tvTitle1)
    private val tvSubTitleRight: TextView = itemView.findViewById(R.id.tvSubTitle1)

    fun setTpCouponData(data: TokopointsCouponDetail) {
        tvStatus.text = data.statusStr
        imageView.loadImageGlide(data.imageUrl)
        tvTitle.text = data.usage?.dateKey
        tvSubTitle.text = data.usage?.dateValue

        tvTitleRight.text = data.minimumUsageLabel
        tvSubTitleRight.text = data.minimumUsage

        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }

    }
}