package com.tokopedia.flashsale.management.product.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.flashsale.management.R
import kotlinx.android.synthetic.main.item_campaign_status.view.*

class FlashSaleSubmitLabelAdapter(var selectedIndex: Int = -1,
                                  var submittedCount: Int = 0,
                                  val onSellerStatusListAdapterListener: OnSellerStatusListAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnSellerStatusListAdapterListener {
        fun onStatusSelected(position: Int)
        fun onStatusCleared()
        fun isLoading(): Boolean
    }

    companion object {
        const val TYPE_SUBMIT = 0
        const val TYPE_NOT_SUBMIT = 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.text.text = when (getItemViewType(position)) {
            TYPE_SUBMIT -> holder.itemView.context.getString(R.string.flash_sale_registered) +
                    if (submittedCount > 0) {
                        " ($submittedCount)"
                    } else ""
            TYPE_NOT_SUBMIT -> holder.itemView.context.getString(R.string.flash_sale_not_registered)
            else -> ""
        }
        holder.itemView.text.isSelected = selectedIndex.equals(position)
    }

    override fun getItemCount(): Int {
        // if it has at least 1 submit count, it will show "Terdaftar" and "Belum Terdaftar"
        return (if (submittedCount > 0) {
            2
        } else {
            0
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (submittedCount > 0 && position == 0) {
            TYPE_SUBMIT
        } else {
            TYPE_NOT_SUBMIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_seller_status, parent, false)

        return CampaignStatusViewHolder(itemLayoutView)
    }

    fun setData(submittedCount: Int) {
        this.submittedCount = submittedCount
        notifyDataSetChanged()
    }

    inner class CampaignStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.text.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position < 0 || position >= itemCount) {
                return
            }
            if (onSellerStatusListAdapterListener.isLoading()) {
                return
            }
            if (selectedIndex > -1 && position == selectedIndex) {
                selectedIndex = -1
                onSellerStatusListAdapterListener.onStatusCleared()
            } else {
                selectedIndex = position
                onSellerStatusListAdapterListener.onStatusSelected(position)
            }
            notifyDataSetChanged()
        }
    }
}