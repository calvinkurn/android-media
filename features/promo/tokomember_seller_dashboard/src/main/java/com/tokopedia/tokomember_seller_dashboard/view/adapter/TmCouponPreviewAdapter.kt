package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TmCouponListItemPreview
import kotlinx.android.synthetic.main.tm_coupon_list_preview_item.view.*

class TmCouponPreviewAdapter(var list: ArrayList<TmCouponListItemPreview>) :
    RecyclerView.Adapter<TmCouponPreviewAdapter.TmCouponPreviewViewHolder>() {

    inner class TmCouponPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(tmCouponListItemPreview: TmCouponListItemPreview) {
            itemView.ivPreview.loadImage(tmCouponListItemPreview.couponImage)
            itemView.tvMemberValue.text = tmCouponListItemPreview.level
            itemView.tvMQuotaValue.text = tmCouponListItemPreview.quota
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmCouponPreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tm_coupon_list_preview_item, parent, false)
        return TmCouponPreviewViewHolder((view))
    }

    override fun onBindViewHolder(holder: TmCouponPreviewViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}