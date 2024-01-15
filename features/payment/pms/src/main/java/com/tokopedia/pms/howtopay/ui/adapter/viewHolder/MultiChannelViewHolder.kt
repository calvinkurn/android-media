package com.tokopedia.pms.howtopay.ui.adapter.viewHolder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.databinding.PmsHwpItemMultiChannelBinding
import com.tokopedia.pms.howtopay.data.model.HtpPaymentChannel
import com.tokopedia.pms.howtopay.ui.adapter.InstructionAdapter

class MultiChannelViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = PmsHwpItemMultiChannelBinding.bind(view)

    @SuppressLint("NotifyDataSetChanged")
    fun bindView(
        paymentChannel: HtpPaymentChannel,
        isLastItem: Boolean,
        onExpand: (HtpPaymentChannel, Int) -> Unit
    ) {
        binding.run {
            tvChannelName.text = paymentChannel.channelTitle
            if (paymentChannel.isExpanded) {
                iconExpand.setImage(IconUnify.CHEVRON_UP)
                rvInstructions.visible()
                rvInstructions.layoutManager = NonScrollLinerLayoutManager(itemView.context)
                rvInstructions.adapter = InstructionAdapter(paymentChannel.channelSteps, null)
                rvInstructions.post {
                    rvInstructions.adapter?.notifyDataSetChanged()
                }
            } else {
                iconExpand.setImage(IconUnify.CHEVRON_DOWN)
                rvInstructions.gone()
            }

            itemView.setOnClickListener {
                onExpand(paymentChannel, adapterPosition)
            }
            if (isLastItem) {
                divider.gone()
            } else {
                divider.visible()
            }
        }
    }
}

class NonScrollLinerLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}
