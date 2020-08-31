package com.tokopedia.gamification.giftbox.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.entities.CouponTapTap
import com.tokopedia.gamification.giftbox.data.entities.CouponType
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.image.ImageUtils

class CouponAdapter(val couponList: ArrayList<CouponType>, val isTablet: Boolean) : RecyclerView.Adapter<CouponListVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListVH {
        val vh = CouponListVH(LayoutInflater.from(parent.context).inflate(CouponListVH.LAYOUT, parent, false))
        if (couponList.size > 1 && !isTablet) {
            vh.setDynamicWidth()
        }
        return vh
    }

    override fun getItemCount() = couponList.size

    override fun onBindViewHolder(vh: CouponListVH, position: Int) {
        vh.setData(couponList[position])
    }
}

open class CouponListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = com.tokopedia.gamification.R.layout.list_item_coupons
        const val WIDTH_RATIO = 1.26
    }

    fun setDynamicWidth() {
        val width = itemView.context.resources.displayMetrics.widthPixels
        val lp = itemView.layoutParams as RecyclerView.LayoutParams
        lp.width = (width / WIDTH_RATIO).toInt()
        itemView.layoutParams = lp
    }

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.appCompatImageView)
    private val clTransaction: ConstraintLayout = itemView.findViewById(R.id.clTransaction)
    private val tvTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvTitle)
    private val tvSubTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvSubTitle)

    fun setData(data: CouponType) {
        when (data) {
            is CouponTapTap -> setCouponTapTap(data)
            is GetCouponDetail -> setGetCouponDetail(data)
        }
    }

    private fun setCouponTapTap(data: CouponTapTap) {
        clTransaction.gone()
        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
    }

    private fun setGetCouponDetail(data: GetCouponDetail) {
        clTransaction.visible()
        tvTitle.text = data.minimumUsageLabel
        tvSubTitle.text = data.minimumUsage
        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }

        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
    }
}