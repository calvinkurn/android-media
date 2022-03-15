package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohPmsButtonItemBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter

class UohPmsButtonViewHolder(private val binding: UohPmsButtonItemBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData) {
        if (item.dataObject is PmsNotification) {
            actionListener?.onImpressionPmsButton()
            val counter = item.dataObject.notifications.buyerOrderStatus.paymentStatus
            if (counter > 0) {
                binding.labelCounter.setLabel(counter.toString())
                binding.labelCounter.show()
            } else {
                binding.labelCounter.gone()
            }
            itemView.setOnClickListener {
                actionListener?.onPmsButtonClicked()
            }
        }
    }

}