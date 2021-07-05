package com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.howtopay_native.data.model.PaymentChannel
import com.tokopedia.pms.howtopay_native.ui.adapter.InstructionAdapter
import kotlinx.android.synthetic.main.pms_hwp_item_multi_channel.view.*

class MultiChannelViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val tvChannelName: TextView = itemView.tvChannelName
    private val iconExpand: IconUnify = itemView.iconExpand
    private val recyclerView: RecyclerView = itemView.rvInstructions
    private val divider: View = itemView.divider

    fun bindView(paymentChannel: PaymentChannel, isLastItem: Boolean,
                 onExpand: (PaymentChannel) -> Unit) {

        tvChannelName.text = paymentChannel.channelTitle
        if (paymentChannel.isExpanded) {
            iconExpand.setImage(IconUnify.CHEVRON_UP)
            recyclerView.visible()
            recyclerView.layoutManager = NonScrollLinerLayoutManager(itemView.context)
            recyclerView.adapter = InstructionAdapter(paymentChannel.channelSteps,
                    paymentChannel.channelNotes)
            recyclerView.post {
                recyclerView.adapter?.notifyDataSetChanged()
            }
        } else {
            iconExpand.setImage(IconUnify.CHEVRON_DOWN)
            recyclerView.gone()
        }

        itemView.setOnClickListener {
            paymentChannel.isExpanded = !paymentChannel.isExpanded
            onExpand(paymentChannel)
        }
        if (isLastItem)
            divider.gone()
        else
            divider.visible()
    }
}

class NonScrollLinerLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}