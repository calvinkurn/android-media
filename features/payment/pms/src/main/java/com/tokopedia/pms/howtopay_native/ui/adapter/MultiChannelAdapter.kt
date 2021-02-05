package com.tokopedia.pms.howtopay_native.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.data.model.PaymentChannel
import com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder.MultiChannelViewHolder

class MultiChannelAdapter (private val paymentChannels: ArrayList<PaymentChannel>) :
        RecyclerView.Adapter<MultiChannelViewHolder>(){

    var expandedChannel : PaymentChannel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pms_hwp_item_multi_channel,
                parent, false)
        return MultiChannelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentChannels.size
    }

    override fun onBindViewHolder(holder: MultiChannelViewHolder, position: Int) {
       holder.bindView(paymentChannels[position],(position == itemCount-1) ,::onExpand)
    }

    private fun onExpand(paymentChannel: PaymentChannel){
        if(paymentChannel.isExpanded){
            expandedChannel?.isExpanded =  false
            expandedChannel = paymentChannel
            this.notifyDataSetChanged()
        }else{
            expandedChannel = null
            this.notifyDataSetChanged()
        }
    }

}