package com.tokopedia.pms.howtopay.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay.data.model.HtpPaymentChannel
import com.tokopedia.pms.howtopay.ui.adapter.viewHolder.MultiChannelViewHolder

class MultiChannelAdapter (private val paymentChannels: ArrayList<HtpPaymentChannel>) :
        RecyclerView.Adapter<MultiChannelViewHolder>(){

    private var lastExpandedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pms_hwp_item_multi_channel,
                parent, false)
        return MultiChannelViewHolder(view)
    }

    override fun getItemCount() = paymentChannels.size

    override fun onBindViewHolder(holder: MultiChannelViewHolder, position: Int) {
       holder.bindView(paymentChannels[position],(position == itemCount-1) ,::onExpand)
    }

    private fun onExpand(paymentChannel: HtpPaymentChannel, position: Int){
        if(paymentChannel.isExpanded) {
            paymentChannel.isExpanded = false
            notifyItemChanged(position)
        } else {
            paymentChannels[lastExpandedPosition].isExpanded = false
            notifyItemChanged(lastExpandedPosition)
            paymentChannel.isExpanded = true
            notifyItemChanged(position)
            lastExpandedPosition = position
        }
    }

}