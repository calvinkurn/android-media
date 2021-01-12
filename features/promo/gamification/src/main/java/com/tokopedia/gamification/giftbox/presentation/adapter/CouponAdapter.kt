package com.tokopedia.gamification.giftbox.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.entities.CouponTapTap
import com.tokopedia.gamification.giftbox.data.entities.CouponType
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardSourceType.Companion.DAILY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.image.ImageUtils

class CouponAdapter(@RewardContainer.RewardSourceType val type: Int, val couponList: ArrayList<CouponType>, val isTablet: Boolean) : RecyclerView.Adapter<CouponListVHDaily>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListVHDaily {
        var vh: CouponListVHDaily
        when (type) {
            DAILY -> {
                vh = CouponListVHDaily(LayoutInflater.from(parent.context).inflate(CouponListVHDaily.LAYOUT_DAILY, parent, false))
            }
            else -> {
                vh = CouponListVHTapTap(LayoutInflater.from(parent.context).inflate(CouponListVHDaily.LAYOUT_TAP_TAP, parent, false))
            }
        }
        if (couponList.size > 1 && !isTablet) {
            vh.setDynamicWidth()
        }
        return vh
    }

    override fun getItemCount() = couponList.size

    override fun onBindViewHolder(vh: CouponListVHDaily, position: Int) {
        vh.setData(couponList[position])
    }
}

class CouponListVHTapTap(itemView: View) : CouponListVHDaily(itemView) {

    private val clTransaction: ConstraintLayout = itemView.findViewById(R.id.clTransaction)
    private val tvTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvTitle)
    private val tvSubTitle: com.tokopedia.unifyprinciples.Typography = itemView.findViewById(R.id.tvSubTitle)

    override fun setCouponTapTap(data: CouponTapTap) {
        clTransaction.gone()
        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
        super.setCouponTapTap(data)
    }

    override fun setGetCouponDetail(data: GetCouponDetail) {
        clTransaction.visible()
        tvTitle.text = data.minimumUsageLabel
        tvSubTitle.text = data.minimumUsage
        if (tvSubTitle.text.isNullOrEmpty()) {
            tvSubTitle.visibility = View.GONE
        } else {
            tvSubTitle.visibility = View.VISIBLE
        }
        super.setGetCouponDetail(data)
    }
}

open class CouponListVHDaily(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT_TAP_TAP = com.tokopedia.gamification.R.layout.list_item_coupons
        val LAYOUT_DAILY = com.tokopedia.gamification.R.layout.list_item_coupons_daily
        const val WIDTH_RATIO = 1.26
    }

    val imageView: AppCompatImageView = itemView.findViewById(R.id.appCompatImageView)

    fun setDynamicWidth() {
        val width = itemView.context.resources.displayMetrics.widthPixels
        val lp = itemView.layoutParams as RecyclerView.LayoutParams
        lp.width = (width / WIDTH_RATIO).toInt()
        itemView.layoutParams = lp
    }

    fun setData(data: CouponType) {
        when (data) {
            is CouponTapTap -> setCouponTapTap(data)
            is GetCouponDetail -> setGetCouponDetail(data)
        }
    }

    open fun setCouponTapTap(data: CouponTapTap) {
        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
    }

    open fun setGetCouponDetail(data: GetCouponDetail) {
        data.imageUrl?.let {
            ImageUtils.loadImage(imageView, it)
        }
    }
}